package io.github.kpaakeh.smog.bronze;

import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.*;

/** Assembly and finite visual commissioning only; no fabricated production or FE. */
public final class BronzeEntity extends BlockEntity {
    private String blueprint="";
    private long motionStart,motionEnd;
    private static final java.util.Map<net.minecraft.world.level.Level,java.util.Map<BlockPos,BlockPos>> CLAIMS=new java.util.WeakHashMap<>();
    public BronzeEntity(BlockPos pos,BlockState state){super(BronzeContent.ENTITY_TYPE,pos,state);}
    public String id(){return ((BronzeBlock)getBlockState().getBlock()).id();}
    public String blueprint(){return blueprint;}
    public float angle(float partial){if(level==null)return 0;double now=level.getGameTime()+partial;return (float)(Math.max(0,Math.min(now,motionEnd)-motionStart)*9%360);}
    public boolean moving(){return level!=null&&level.getGameTime()<motionEnd;}
    private void sync(){setChanged();if(level!=null)level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),Block.UPDATE_CLIENTS);}
    private java.util.Map<BlockPos,BlockPos> claims(){return CLAIMS.computeIfAbsent(level,l->new java.util.HashMap<>());}
    private boolean claim(BronzeBlueprints.Blueprint definition){
        var positions=new java.util.ArrayList<BlockPos>();positions.add(worldPosition);
        var rotation=BronzeBlueprints.rotation(getBlockState().getValue(BronzeBlock.FACING));
        for(var cell:definition.cells())if(!cell.floor()){
            var p=worldPosition.offset(cell.offset().rotate(rotation));if(!level.getBlockState(p).isAir())positions.add(p);
        }
        var claims=claims();
        for(var p:positions){var owner=claims.get(p);if(owner==null||owner.equals(worldPosition))continue;
            if(level.hasChunkAt(owner)&&(!(level.getBlockEntity(owner) instanceof BronzeEntity other)||other.blueprint.isEmpty()))claims.entrySet().removeIf(e->e.getValue().equals(owner));
            else return false;
        }
        claims.entrySet().removeIf(e->e.getValue().equals(worldPosition));positions.forEach(p->claims.put(p,worldPosition));return true;
    }
    public void disassemble(){
        blueprint="";motionEnd=motionStart=0;
        if(level!=null){claims().entrySet().removeIf(e->e.getValue().equals(worldPosition));if(getBlockState().getValue(BronzeBlock.ASSEMBLED))level.setBlock(worldPosition,getBlockState().setValue(BronzeBlock.ASSEMBLED,false),Block.UPDATE_CLIENTS);sync();}
    }
    public void inspect(net.minecraft.world.entity.player.Player player){
        var variants=BronzeBlueprints.forController(id());
        if(id().equals("bronze_mine_hoist"))variants=BronzeBlueprints.hoistCandidates(level,worldPosition,getBlockState().getValue(BronzeBlock.FACING));
        if(variants.isEmpty()){player.sendOverlayMessage(Component.translatable("message.smog.bronze_registered"));return;}
        BronzeBlueprints.Inspection closest=null;int best=-1;
        for(var candidate:variants){
            var result=BronzeBlueprints.inspect(level,worldPosition,getBlockState().getValue(BronzeBlock.FACING),candidate);
            if(result.complete()){
                if(!claim(candidate)){player.sendOverlayMessage(Component.translatable("message.smog.assembly_overlap"));return;}
                blueprint=candidate.id();level.setBlock(worldPosition,getBlockState().setValue(BronzeBlock.ASSEMBLED,true),Block.UPDATE_CLIENTS);sync();player.sendOverlayMessage(Component.translatable("message.smog.bronze_assembled"));return;
            }
            // Prefer a loaded drawing whose first mismatch is farther into its ordered cells.
            int score=result.loaded()?candidate.cells().stream().map(Cell->worldPosition.offset(Cell.offset().rotate(BronzeBlueprints.rotation(getBlockState().getValue(BronzeBlock.FACING))))).toList().indexOf(result.problem()):-1;
            if(closest==null||score>best){closest=result;best=score;}
        }
        disassemble();
        player.sendOverlayMessage(closest!=null&&closest.loaded()?Component.translatable("message.smog.bronze_missing",closest.problem().getX(),closest.problem().getY(),closest.problem().getZ(),expectedName(closest.expected())):Component.translatable("message.smog.bronze_unloaded"));
    }
    private static Component expectedName(String expected){
        if(expected.equals("sturdy floor"))return Component.translatable("message.smog.bronze_floor");
        if(expected.equals("dimension bounds"))return Component.translatable("message.smog.bronze_bounds");
        var text=Component.empty();for(String id:expected.split(" / ")){if(!text.getSiblings().isEmpty())text.append(" / ");text.append(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(net.minecraft.resources.Identifier.parse(id)).getName());}return text;
    }
    public void crank(){
        if(level==null)return;
        // Only the crank itself is commissioned here. Connected power is a later stage.
        motionStart=level.getGameTime();motionEnd=motionStart+80;sync();
    }
    public void tick(){
        if(level.getGameTime()%20!=Math.floorMod(worldPosition.asLong(),20)||blueprint.isEmpty())return;
        var definition=BronzeBlueprints.get(blueprint);if(definition==null){disassemble();return;}
        var result=BronzeBlueprints.inspect(level,worldPosition,getBlockState().getValue(BronzeBlock.FACING),definition);
        if(result.loaded()&&(!result.complete()||!claim(definition)))disassemble();
    }
    @Override protected void saveAdditional(ValueOutput output){super.saveAdditional(output);output.putString("blueprint",blueprint);output.putLong("motion_start",motionStart);output.putLong("motion_end",motionEnd);}
    @Override protected void loadAdditional(ValueInput input){super.loadAdditional(input);blueprint=input.getStringOr("blueprint","");motionStart=input.getLongOr("motion_start",0);motionEnd=input.getLongOr("motion_end",0);}
    @Override public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket(){return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);}
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries){return saveCustomOnly(registries);}
}
