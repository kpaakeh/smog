package io.github.kpaakeh.smog.copper;

import java.util.*;
import com.google.gson.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.complex.ComplexFormationData;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.*;

/** Copper-era thermal and manual processes. No FE, arbitrary recipe multiplication or random losses. */
public final class CopperWorkshops {
    public record Pottery(String input,int count,String output,int outputCount){}
    private static final int U=MoltenMetalStorage.QUARTER;
    private static final List<Pottery> POTTERY=new ArrayList<>(),FIRING=new ArrayList<>();
    public record RecipePreview(String machine,List<ItemStack> inputs,ItemStack tool,ItemStack result,String description){}
    public static List<RecipePreview> recipes(){
        List<RecipePreview> result=new ArrayList<>();
        for(var recipe:POTTERY)result.add(new RecipePreview("potter_table",List.of(new ItemStack(item(recipe.input),recipe.count)),ItemStack.EMPTY,new ItemStack(item(recipe.output),recipe.outputCount),"jei.smog.copper.pottery"));
        for(int metal=0;metal<3;metal++)for(int mold=0;mold<4;mold++){
            var cast=castResult(metal,mold);if(cast.isEmpty())continue;
            result.add(new RecipePreview("casting_table",List.of(new ItemStack(item((metal==0?"minecraft:":"smog:")+METALS[metal]+"_ingot"),mold==3?2:1)),new ItemStack(item("smog:"+new String[]{"ingot_mold","plate_mold","fitting_mold","pipe_mold"}[mold])),cast,"jei.smog.copper.casting"));
        }
        for(var firing:FIRING)result.add(new RecipePreview("ceramic_kiln",List.of(new ItemStack(item(firing.input),8)),ItemStack.EMPTY,new ItemStack(item(firing.output),8),"jei.smog.copper.firing"));
        for(var candidate:BuiltInRegistries.ITEM){var stack=candidate.getDefaultInstance();var amount=melt(stack);if(amount!=null)result.add(new RecipePreview("copper_hearth",List.of(stack),ItemStack.EMPTY,new ItemStack(item("smog:molten_"+METALS[amount[0]]+"_bucket")),"jei.smog.copper.melting"));}
        result.add(new RecipePreview("copper_hearth",List.of(new ItemStack(Items.COPPER_INGOT,3),new ItemStack(item("smog:tin_ingot"))),ItemStack.EMPTY,new ItemStack(item("smog:bronze_ingot"),4),"jei.smog.copper.alloy"));return List.copyOf(result);
    }
    private static final String[] METALS={"copper","tin","bronze"};
    public static void initialize(){
        try(var stream=CopperWorkshops.class.getResourceAsStream("/smog.content.json")){
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            if(root.has("firing"))for(var entry:root.getAsJsonArray("firing")){var o=entry.getAsJsonObject();FIRING.add(new Pottery(o.get("input").getAsString(),1,o.get("output").getAsString(),1));}
            if(root.has("pottery"))for(var entry:root.getAsJsonArray("pottery")){var o=entry.getAsJsonObject();POTTERY.add(new Pottery(o.get("input").getAsString(),o.get("count").getAsInt(),o.get("output").getAsString(),o.has("output_count")?o.get("output_count").getAsInt():1));}
        }catch(IOException e){throw new IllegalStateException(e);}
    }
    private static Item item(String id){return BuiltInRegistries.ITEM.getValue(Identifier.parse(id));}
    public static boolean is(ItemStack stack,String id){return !stack.isEmpty()&&BuiltInRegistries.ITEM.containsKey(Identifier.parse(id))&&stack.is(item(id));}
    public static boolean workshop(MachineEntity m,String name){return m.machineId().getPath().equals(name);}
    private static void output(MachineEntity m,ItemStack result){var old=m.getItem(3);if(old.isEmpty())m.setItem(3,result.copy());else {old.grow(result.getCount());m.setChanged();}}
    public static boolean accepts(MachineEntity m,int slot,ItemStack stack){
        if(slot==3)return false;
        if(slot==2)return (workshop(m,"copper_hearth")||workshop(m,"ceramic_kiln"))&&stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL);
        if(slot==1)return workshop(m,"casting_table")&&mold(stack)>=0;
        if(slot!=0)return false;
        if(workshop(m,"copper_hearth"))return melt(stack)!=null;
        if(workshop(m,"potter_table"))return POTTERY.stream().anyMatch(r->is(stack,r.input));
        if(workshop(m,"ceramic_kiln"))return tagged(stack,"c:unfired_ceramics");
        return false;
    }
    public static int capacity(MachineEntity m){
        if(!workshop(m,"copper_hearth"))return 0;
        var shape=m.complexShape();return shape==null?0:shape.internalVolume(false)*8*MoltenMetalStorage.INGOT;
    }
    public static int threshold(MachineEntity m){return workshop(m,"ceramic_kiln")?400:capacity(m)>64*U?800:600;}
    public static int data(MachineEntity m,int index){var s=m.copper;return switch(index){case 0->s.work;case 1->workshop(m,"casting_table")?100:workshop(m,"ceramic_kiln")?200:80;case 2->s.heat;case 3->threshold(m);case 4->s.total()/U;case 5->capacity(m)/U;case 6->s.selection;case 7->m.multiblockController()&&!m.assembled()?1:s.status;case 8->m.enabled()?1:0;case 9->s.copper/U;case 10->s.tin/U;case 11->s.bronze/U;case 12->workshop(m,"potter_table")&&!POTTERY.isEmpty()?Item.getId(item(POTTERY.get(Math.floorMod(s.selection,POTTERY.size())).output)):0;case 14->workshop(m,"casting_table")?1:workshop(m,"potter_table")?2:workshop(m,"ceramic_kiln")?4:0;case 15->m.complexShape()==null?0:m.complexShape().width();case 16->m.complexShape()==null?0:m.complexShape().depth();case 17->m.complexShape()==null?0:m.complexShape().height();case 18->s.air;default->0;};}
    public static void tick(ServerLevel level,MachineEntity m){
        if(workshop(m,"copper_hearth"))hearth(level,m);
        else if(workshop(m,"casting_table"))casting(level,m);
        else if(workshop(m,"ceramic_kiln"))kiln(level,m);
        if(level.getGameTime()%10==0)m.sync();
        if(level.getGameTime()%20==0&&m.copper.burn>0)level.sendParticles(net.minecraft.core.particles.ParticleTypes.SMOKE,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+(m.complexShape()==null?1:m.complexShape().height()-.5),m.getBlockPos().getZ()+.5,2,.15,.1,.15,.01);
    }
    private static void heat(ServerLevel level,MachineEntity m,boolean demand){var s=m.copper;
        if(s.burn==0&&demand&&s.heat<threshold(m)+80)s.burn=m.takeFuel();
        if(s.burn>0){s.burn--;s.heat=Math.min(1100,s.heat+1+(s.air>0?1:0));}
        else if(level.getGameTime()%(20+Math.min(40,m.complexRoles().getOrDefault("lining",0)*2))==0)s.heat=Math.max(0,s.heat-1);
        m.setChanged();
    }
    // Metal identity and quarter-ingot amount of one input item. No remelting bonus.
    public static int[] melt(ItemStack stack){
        if(stack.isEmpty())return null;
        if(is(stack,"smog:metal_residue")){var data=stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.EMPTY).copyTag();int kind=data.getIntOr("metal",-1),amount=data.getIntOr("units",0);return kind>=0&&kind<3&&amount>0&&amount<MoltenMetalStorage.INGOT?new int[]{kind,amount}:null;}
        for(int k=0;k<3;k++){
            String n=METALS[k],ns=k==0?"minecraft:":"smog:";
            if(tagged(stack,"c:nuggets/"+n)||is(stack,ns+n+"_nugget"))return new int[]{k,1000};
            if(tagged(stack,"c:dusts/"+n))return new int[]{k,4*U};
            if(tagged(stack,"c:ingots/"+n)||is(stack,ns+n+"_ingot"))return new int[]{k,4*U};
            if(tagged(stack,"c:storage_blocks/"+n)||is(stack,ns+n+"_block"))return new int[]{k,36*U};
            if(tagged(stack,"c:plates/"+n)||is(stack,"smog:"+n+"_plate"))return new int[]{k,4*U};
            if(is(stack,"smog:"+n+"_fitting"))return new int[]{k,1*U};
            if(is(stack,"smog:"+n+"_pipe_part"))return new int[]{k,2*U};
            if(k<2){
                if(tagged(stack,"c:crushed_ores/"+n)||tagged(stack,"c:ore_shards/"+n))return new int[]{k,4*U};
                if(tagged(stack,"c:raw_materials/"+n)||is(stack,ns+"raw_"+n))return new int[]{k,4*U};
                if(tagged(stack,"c:storage_blocks/raw_"+n)||is(stack,ns+"raw_"+n+"_block"))return new int[]{k,36*U};
            }
        }
        return null;
    }
    private static boolean tagged(ItemStack stack,String tag){return stack.is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,Identifier.parse(tag)));}
    public static void alloy(MachineEntity m){var s=m.copper;if(!m.assembled()||s.heat<threshold(m))return;int batches=Math.min(s.copper/3,s.tin);if(batches>0){s.copper-=batches*3;s.tin-=batches;s.bronze+=batches*4;m.setChanged();}}
    private static boolean working(MachineEntity m,String key,int ticks,int step){var s=m.copper;if(!s.workKey.equals(key)){s.work=0;s.workKey=key;}s.work+=step;m.setChanged();return s.work>=ticks;}
    private static void hearth(ServerLevel level,MachineEntity m){var s=m.copper;
        net.minecraft.world.entity.item.ItemEntity worldInput=null;
        if(m.getItem(0).isEmpty()&&m.complexShape()!=null){var box=new net.minecraft.world.phys.AABB(m.getBlockPos()).inflate(12,11,12);for(var entity:level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,box)){
            var local=entity.blockPosition().subtract(m.getBlockPos()).rotate(m.rotation()==Rotation.CLOCKWISE_90?Rotation.COUNTERCLOCKWISE_90:m.rotation()==Rotation.COUNTERCLOCKWISE_90?Rotation.CLOCKWISE_90:m.rotation());var bounds=m.complexShape().bounds();
            if(local.getX()>bounds.minX()&&local.getX()<bounds.maxX()&&local.getZ()>0&&local.getZ()<bounds.maxZ()&&local.getY()>=0&&local.getY()<=bounds.maxY()+1&&melt(entity.getItem())!=null){worldInput=entity;break;}}
        }
        var input=worldInput==null?m.getItem(0):worldInput.getItem();var recipe=melt(input);
        heat(level,m,recipe!=null||s.total()>0);alloy(m);
        if(recipe==null){s.work=0;s.status=2;return;}
        if(s.total()+recipe[1]>capacity(m)){s.status=5;return;}
        if(s.heat<threshold(m)){s.status=s.burn==0?3:4;return;}
        s.status=0;
        if(working(m,BuiltInRegistries.ITEM.getKey(input.getItem()).toString(),80,s.air>0?2:1)){
            input.shrink(1);if(worldInput!=null&&input.isEmpty())worldInput.discard();s.metal(recipe[0],s.metal(recipe[0])+recipe[1]);s.work=0;if(s.air>0)s.air=Math.max(0,s.air-(recipe[1]+4*U-1)/(4*U));m.setChanged();
        }
    }
    public static int mold(ItemStack stack){for(int i=0;i<4;i++)if(is(stack,"smog:"+new String[]{"ingot_mold","plate_mold","fitting_mold","pipe_mold"}[i]))return i;return -1;}
    private static ItemStack castResult(int metal,int mold){String n=METALS[metal];if(metal==1&&mold>=2)return ItemStack.EMPTY;return switch(mold){case 0->new ItemStack(item((metal==0?"minecraft:":"smog:")+n+"_ingot"));case 1->new ItemStack(item("smog:"+n+"_plate"));case 2->new ItemStack(item("smog:"+n+"_fitting"),4);case 3->new ItemStack(item("smog:"+n+"_pipe_part"),4);default->ItemStack.EMPTY;};}
    private static MachineEntity source(ServerLevel level,BlockPos table){
        record Node(BlockPos pos,int distance){}var queue=new ArrayDeque<Node>();Set<BlockPos> visited=new HashSet<>();queue.add(new Node(table,0));
        while(!queue.isEmpty()&&visited.size()<128){var node=queue.removeFirst();if(node.distance>8)continue;
            for(var side:Direction.values()){if(side==Direction.DOWN)continue;var pos=node.pos.relative(side);if(!visited.add(pos)||!level.hasChunkAt(pos))continue;var state=level.getBlockState(pos);
                if(state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_spout")))){
                    var owner=ComplexFormationData.get(level).owner(pos);
                    if(owner!=null&&level.hasChunkAt(owner)&&level.getBlockEntity(owner) instanceof MachineEntity m&&workshop(m,"copper_hearth")&&m.assembled())return m;
                }else if(state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_channel"))))queue.add(new Node(pos,node.distance+1));
            }
        }return null;
    }
    private static void casting(ServerLevel level,MachineEntity m){var s=m.copper;int mold=mold(m.getItem(1));if(mold<0){s.work=0;s.status=6;return;}
        int metal=Math.floorMod(s.selection,3),cost=(mold==3?8:4)*U;var source=source(level,m.getBlockPos());
        if(source==null){s.status=8;return;}
        if(source.copper.metal(metal)<cost||castResult(metal,mold).isEmpty()){boolean available=false;for(int k:new int[]{2,0,1})if(source.copper.metal(k)>=cost&&!castResult(k,mold).isEmpty()){metal=k;available=true;break;}if(!available){s.status=8;return;}}
        var result=castResult(metal,mold);if(!m.canOutput(result)){s.status=5;return;}s.selection=metal;
        if(source.copper.heat<threshold(source)){s.status=4;return;}
        s.status=7;
        if(working(m,metal+":"+mold,100,1)&&level.getGameTime()-source.copper.lastPour>=10){
            source.copper.metal(metal,source.copper.metal(metal)-cost);source.copper.lastPour=level.getGameTime();source.sync();output(m,result);s.work=0;s.status=0;m.sync();
        }
    }
    private static void kiln(ServerLevel level,MachineEntity m){var s=m.copper;var input=m.getItem(0);
        var match=level.recipeAccess().getRecipeFor(RecipeType.SMELTING,new SingleRecipeInput(input),level);
        // A ceramic kiln is deliberately not a universal ore smelter.
        boolean ceramic=tagged(input,"c:unfired_ceramics");
        heat(level,m,ceramic&&match.isPresent());if(!ceramic||match.isEmpty()){s.work=0;s.status=2;return;}
        int batch=Math.min(input.getCount(),Math.max(8,m.complexShape().internalVolume(true)*8));var result=match.get().value().assemble(new SingleRecipeInput(input));
        batch=Math.min(batch,result.getMaxStackSize()/Math.max(1,result.getCount()));result.setCount(result.getCount()*batch);
        if(!m.canOutput(result)){s.status=5;return;}if(s.heat<threshold(m)){s.status=s.burn==0?3:4;return;}s.status=0;
        if(working(m,BuiltInRegistries.ITEM.getKey(input.getItem()).toString(),200,1)){input.shrink(batch);output(m,result);s.work=0;}
    }
    public static boolean button(MachineEntity m,int id){var s=m.copper;
        if(id==20||id==21){int max=workshop(m,"potter_table")?POTTERY.size():3;s.selection=Math.floorMod(s.selection+(id==20?-1:1),Math.max(1,max));s.work=0;m.setChanged();return true;}
        if(id==22&&workshop(m,"potter_table")&&!POTTERY.isEmpty()){
            var recipe=POTTERY.get(Math.floorMod(s.selection,POTTERY.size()));var input=m.getItem(0);
            if(!is(input,recipe.input)||input.getCount()<recipe.count){s.status=2;return true;}
            int batch=Math.min(8,input.getCount()/recipe.count);var result=new ItemStack(item(recipe.output),recipe.outputCount*batch);
            if(!m.canOutput(result)){s.status=5;return true;}input.shrink(recipe.count*batch);output(m,result);s.status=0;return true;
        }
        return false;
    }
    public static boolean pump(ServerLevel level,MachineEntity bellows){
        for(var side:Direction.values()){
            var pos=bellows.getBlockPos().relative(side);if(!level.hasChunkAt(pos)||!level.getBlockState(pos).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("air_intake"))))continue;
            var owner=ComplexFormationData.get(level).owner(pos);
            if(owner!=null&&level.hasChunkAt(owner)&&level.getBlockEntity(owner) instanceof MachineEntity m&&m.assembled()){m.copper.air=8;m.sync();return true;}
        }return false;
    }
    public static void dropMetal(ServerLevel level,MachineEntity m){if(!workshop(m,"copper_hearth"))return;
        for(int kind=0;kind<3;kind++){int units=m.copper.metal(kind);while(units>=MoltenMetalStorage.INGOT){int ingots=Math.min(64,units/MoltenMetalStorage.INGOT);Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,new ItemStack(item((kind==0?"minecraft:":"smog:")+METALS[kind]+"_ingot"),ingots));units-=ingots*MoltenMetalStorage.INGOT;}
            if(units>0){var residue=new ItemStack(item("smog:metal_residue"));var data=new net.minecraft.nbt.CompoundTag();data.putInt("metal",kind);data.putInt("units",units);residue.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,residue);}m.copper.metal(kind,0);}
    }
}
