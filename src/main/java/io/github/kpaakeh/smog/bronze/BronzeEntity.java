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
    private float phase,speed;
    private long phaseTick;
    private boolean simulationAttached;
    private static final java.util.Map<net.minecraft.world.level.Level,java.util.Map<BlockPos,BlockPos>> CLAIMS=new java.util.WeakHashMap<>();
    public BronzeEntity(BlockPos pos,BlockState state){super(BronzeContent.ENTITY_TYPE,pos,state);}
    public String id(){return ((BronzeBlock)getBlockState().getBlock()).id();}
    public String blueprint(){return blueprint;}
    public float angle(float partial){return level==null||!level.isClientSide()&&!simulationAttached?phase:(float)RotationTimeline.wrap(phase+speed*(level.getGameTime()-phaseTick+partial)*RotationTimeline.DEGREES_PER_TICK);}
    public boolean cranking(){return level!=null&&level.getGameTime()<motionEnd;}
    public boolean moving(){return cranking()||level!=null&&(level.isClientSide()||simulationAttached)&&speed!=0;}
    private void motion(BronzeNetwork.Result result){
        float next=(float)result.rate().value();float at=(float)result.phase();
        float error=(float)Math.abs(Math.IEEEremainder(angle(0)-at,360));
        if(next==speed&&error<.001&&Math.floorMod(level.getGameTime()+worldPosition.asLong(),20)!=0)return;
        phase=at;phaseTick=level.getGameTime();speed=next;sync();
    }
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
        if(BronzeNetwork.node(id())){var result=BronzeNetwork.query(level,worldPosition);var message=Component.translatable("message.smog.bronze_network."+result.status());String speedText=result.rate().denominator().equals(java.math.BigInteger.ONE)?result.rate().numerator().abs().toString():result.rate().numerator().abs()+"/"+result.rate().denominator();if(player.isShiftKeyDown())message.append(" ").append(Component.translatable("message.smog.bronze_network.details",result.required(),result.available(),result.rate().numerator().signum()==0?"—":speedText,Component.translatable("message.smog.bronze_network."+(result.rate().numerator().signum()==0?"stopped":result.rate().numerator().signum()<0?"reverse":"forward"))));player.sendOverlayMessage(message);return;}
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
        motionStart=level.getGameTime();motionEnd=motionStart+80;BronzeNetwork.invalidate(level);sync();
    }
    public void tick(){
        if(!simulationAttached){phaseTick=level.getGameTime();speed=0;simulationAttached=true;}
        if(BronzeNetwork.node(id()))motion(BronzeNetwork.query(level,worldPosition));
        if(level.getGameTime()%20!=Math.floorMod(worldPosition.asLong(),20)||blueprint.isEmpty())return;
        var definition=BronzeBlueprints.get(blueprint);if(definition==null){disassemble();return;}
        var result=BronzeBlueprints.inspect(level,worldPosition,getBlockState().getValue(BronzeBlock.FACING),definition);
        if(result.loaded()&&(!result.complete()||!claim(definition)))disassemble();
    }
    @Override protected void saveAdditional(ValueOutput output){super.saveAdditional(output);output.putString("blueprint",blueprint);output.putLong("motion_start",motionStart);output.putLong("motion_end",motionEnd);output.putFloat("phase",angle(0));output.putFloat("speed",speed);output.putLong("phase_tick",level==null?phaseTick:level.getGameTime());}
    @Override protected void loadAdditional(ValueInput input){super.loadAdditional(input);blueprint=input.getStringOr("blueprint","");motionStart=input.getLongOr("motion_start",0);motionEnd=input.getLongOr("motion_end",0);phase=input.getFloatOr("phase",0);speed=input.getFloatOr("speed",0);phaseTick=input.getLongOr("phase_tick",0);simulationAttached=false;}
    @Override public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket(){return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);}
    @Override public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries){return saveCustomOnly(registries);}
}
