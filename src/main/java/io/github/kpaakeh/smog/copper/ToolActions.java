package io.github.kpaakeh.smog.copper;

import java.util.*;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.*;
import net.minecraft.server.level.*;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

/** Extra work uses normal player destruction; re-entry never expands another area. */
public final class ToolActions {
    private static final Set<UUID> ACTIVE=new HashSet<>();
    private record Face(BlockPos pos,Direction face,Set<BlockPos> tree){}
    private static final Map<UUID,Face> FACES=new HashMap<>();
    private ToolActions(){}
    public static void initialize(){
        PlayerBlockBreakEvents.AFTER.register((level,player,pos,state,entity)->{if(!level.isClientSide()&&state.is(BlockTags.LOGS))NaturalTreeLogs.placed(level,pos);});
        PlayerBlockBreakEvents.BEFORE.register((level,player,pos,state,entity)->{
            if(player instanceof ServerPlayer server&&!ACTIVE.contains(server.getUUID())){
                var hit=server.pick(6,0,false);var face=hit instanceof BlockHitResult block?block.getDirection():Direction.getApproximateNearest(server.getLookAngle()).getOpposite();
                var held=server.getMainHandItem();boolean saw=ModularToolItem.configured(held)&&ToolParts.schema(held).behavior().equals("saw");
                FACES.put(server.getUUID(),new Face(pos.immutable(),face,saw&&state.is(BlockTags.LOGS)?NaturalTreeLogs.tree(level,pos):Set.of()));
            }
            return true;
        });
    }
    public static void afterMined(ItemStack stack,ServerLevel level,BlockState state,BlockPos pos,ServerPlayer player){
        if(player.isShiftKeyDown()||ModularToolItem.broken(stack)||ACTIVE.contains(player.getUUID()))return;
        String behavior=ToolParts.schema(stack).behavior();if(!behavior.equals("hammer")&&!behavior.equals("saw"))return;
        if(!ACTIVE.add(player.getUUID()))return;
        try{
            if(behavior.equals("hammer")&&state.is(BlockTags.MINEABLE_WITH_PICKAXE)){
                var recorded=FACES.get(player.getUUID());var face=recorded!=null&&recorded.pos.equals(pos)?recorded.face:Direction.getApproximateNearest(player.getLookAngle()).getOpposite();
                for(int a=-1;a<=1;a++)for(int b=-1;b<=1;b++){
                    if(a==0&&b==0)continue;BlockPos target=switch(face.getAxis()){case X->pos.offset(0,a,b);case Y->pos.offset(a,0,b);case Z->pos.offset(a,b,0);};
                    if(ModularToolItem.broken(stack))return;var other=level.getBlockState(target);
                    if(other.is(BlockTags.MINEABLE_WITH_PICKAXE)&&other.getDestroySpeed(level,target)<=Math.max(0,state.getDestroySpeed(level,pos))*3+1)destroy(player,stack,target);
                }
            }else if(behavior.equals("saw")&&state.is(BlockTags.LOGS)){
                var before=FACES.get(player.getUUID());var logs=before!=null&&before.pos.equals(pos)?before.tree:Set.<BlockPos>of();
                for(var target:logs){if(target.equals(pos))continue;if(ModularToolItem.broken(stack))break;
                    if(level.hasChunkAt(target)&&level.getBlockState(target).is(state.getBlock())&&!NaturalTreeLogs.playerPlaced(level,target))destroy(player,stack,target);
                }
                NaturalTreeLogs.placed(level,pos);
            }
        }finally{ACTIVE.remove(player.getUUID());FACES.remove(player.getUUID());}
    }
    private static boolean destroy(ServerPlayer player,ItemStack tool,BlockPos pos){
        var level=player.level();if(!level.hasChunkAt(pos)||!level.mayInteract(player,pos)||player.isSpectator()||ModularToolItem.broken(tool))return false;
        var state=level.getBlockState(pos);if(state.isAir()||state.getDestroySpeed(level,pos)<0||level.getBlockEntity(pos)!=null||!tool.isCorrectToolForDrops(state))return false;
        return player.gameMode.destroyBlock(pos);
    }
    public static InteractionResult harvest(UseOnContext context){
        var player=context.getPlayer();if(player==null)return InteractionResult.PASS;var level=context.getLevel();var tool=context.getItemInHand();
        if(level.isClientSide())return InteractionResult.SUCCESS;
        if(!(player instanceof ServerPlayer server)||server.isSpectator())return InteractionResult.PASS;
        var origin=context.getClickedPos();int harvested=0;
        for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){
            if(ModularToolItem.broken(tool))return harvested>0?InteractionResult.SUCCESS:InteractionResult.PASS;
            var pos=origin.offset(x,0,z);if(!level.hasChunkAt(pos)||!level.mayInteract(server,pos)||server.blockActionRestricted(level,pos,server.gameMode.getGameModeForPlayer()))continue;
            var state=level.getBlockState(pos);BlockState replanted;Item seed;
            if(state.getBlock() instanceof CropBlock crop&&crop.isMaxAge(state)){
                replanted=crop.getStateForAge(0);seed=((io.github.kpaakeh.smog.mixin.CropSeedAccessor)crop).smog$seed().asItem();
            }else if(state.is(Blocks.NETHER_WART)&&state.getValue(NetherWartBlock.AGE)>=3){replanted=state.setValue(NetherWartBlock.AGE,0);seed=Items.NETHER_WART;}
            else if(state.is(Blocks.COCOA)&&state.getValue(CocoaBlock.AGE)>=2){replanted=state.setValue(CocoaBlock.AGE,0);seed=Items.COCOA_BEANS;}
            else continue;
            var drops=Block.getDrops(state,server.level(),pos,level.getBlockEntity(pos),server,tool);boolean reserve=false;
            for(var drop:drops)if(drop.is(seed)&&!drop.isEmpty()){drop.shrink(1);reserve=true;break;}
            if(!reserve||!PlayerBlockBreakEvents.BEFORE.invoker().beforeBlockBreak(level,server,pos,state,level.getBlockEntity(pos)))continue;
            if(!level.setBlock(pos,replanted,Block.UPDATE_ALL))continue;
            for(var drop:drops)if(!drop.isEmpty())Block.popResource(level,pos,drop);
            PlayerBlockBreakEvents.AFTER.invoker().afterBlockBreak(level,server,pos,state,null);
            if(!server.isCreative())ModularToolItem.wear(tool,level,ToolTraits.useWear(tool));harvested++;
        }
        return harvested>0?InteractionResult.SUCCESS:InteractionResult.PASS;
    }
}
