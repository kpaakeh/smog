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
    private static final List<Pottery> POTTERY=new ArrayList<>();
    public record RecipePreview(String machine,List<ItemStack> inputs,ItemStack tool,ItemStack result,String description){}
    public static List<RecipePreview> recipes(){
        List<RecipePreview> result=new ArrayList<>();
        for(var recipe:POTTERY)result.add(new RecipePreview("potter_table",List.of(new ItemStack(item(recipe.input),recipe.count)),ItemStack.EMPTY,new ItemStack(item(recipe.output),recipe.outputCount),"jei.smog.copper.pottery"));
        for(int metal=0;metal<3;metal++)for(int mold=0;mold<4;mold++){
            var cast=castResult(metal,mold);if(cast.isEmpty())continue;
            result.add(new RecipePreview("casting_table",List.of(new ItemStack(item((metal==0?"minecraft:":"smog:")+METALS[metal]+"_ingot"),mold==3?2:1)),new ItemStack(item("smog:"+new String[]{"ingot_mold","plate_mold","fitting_mold","pipe_mold"}[mold])),cast,"jei.smog.copper.casting"));
        }
        result.add(new RecipePreview("copper_hearth",List.of(new ItemStack(Items.COPPER_INGOT,3),new ItemStack(item("smog:tin_ingot"))),ItemStack.EMPTY,new ItemStack(item("smog:bronze_ingot"),4),"jei.smog.copper.alloy"));return List.copyOf(result);
    }
    private static final String[] METALS={"copper","tin","bronze"};
    public static void initialize(){
        try(var stream=CopperWorkshops.class.getResourceAsStream("/smog.content.json")){
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            if(root.has("pottery"))for(var entry:root.getAsJsonArray("pottery")){var o=entry.getAsJsonObject();POTTERY.add(new Pottery(o.get("input").getAsString(),o.get("count").getAsInt(),o.get("output").getAsString(),o.has("output_count")?o.get("output_count").getAsInt():1));}
        }catch(IOException e){throw new IllegalStateException(e);}
    }
    private static Item item(String id){return BuiltInRegistries.ITEM.getValue(Identifier.parse(id));}
    private static boolean is(ItemStack stack,String id){return stack.is(item(id));}
    public static boolean workshop(MachineEntity m,String name){return m.machineId().getPath().equals(name);}
    private static void output(MachineEntity m,ItemStack result){var old=m.getItem(3);if(old.isEmpty())m.setItem(3,result.copy());else {old.grow(result.getCount());m.setChanged();}}
    public static boolean accepts(MachineEntity m,int slot,ItemStack stack){
        if(slot==3)return false;
        if(slot==2)return (workshop(m,"copper_hearth")||workshop(m,"ceramic_kiln"))&&stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL);
        if(slot==1)return workshop(m,"casting_table")&&mold(stack)>=0;
        return slot==0&&!workshop(m,"casting_table")&&!workshop(m,"hand_bellows");
    }
    public static int capacity(MachineEntity m){
        if(!workshop(m,"copper_hearth"))return 0;
        int basic=m.complexRoles().getOrDefault("crucible",0),large=m.complexRoles().getOrDefault("large_crucible",0);
        return basic*64+large*128;
    }
    private static int threshold(MachineEntity m){return workshop(m,"ceramic_kiln")?400:capacity(m)>64?800:600;}
    public static int data(MachineEntity m,int index){var s=m.copper;return switch(index){case 0->s.work;case 1->workshop(m,"casting_table")?100:workshop(m,"ceramic_kiln")?200:80;case 2->s.heat;case 3->threshold(m);case 4->s.total();case 5->capacity(m);case 6->s.selection;case 7->m.multiblockController()&&!m.assembled()?1:s.status;case 8->m.enabled()?1:0;case 9->s.copper;case 10->s.tin;case 11->s.bronze;case 12->workshop(m,"potter_table")&&!POTTERY.isEmpty()?Item.getId(item(POTTERY.get(Math.floorMod(s.selection,POTTERY.size())).output)):0;case 14->workshop(m,"casting_table")?1:workshop(m,"potter_table")?2:workshop(m,"ceramic_kiln")?4:0;case 15->m.complexShape()==null?0:m.complexShape().width();case 16->m.complexShape()==null?0:m.complexShape().depth();case 17->m.complexShape()==null?0:m.complexShape().height();case 18->s.air;default->0;};}
    public static void tick(ServerLevel level,MachineEntity m){
        if(workshop(m,"copper_hearth"))hearth(level,m);
        else if(workshop(m,"casting_table"))casting(level,m);
        else if(workshop(m,"ceramic_kiln"))kiln(level,m);
    }
    private static void heat(ServerLevel level,MachineEntity m,boolean demand){var s=m.copper;
        if(s.burn==0&&demand&&s.heat<threshold(m)+80)s.burn=m.takeFuel();
        if(s.burn>0){s.burn--;s.heat=Math.min(1100,s.heat+1+(s.air>0?1:0));}
        else if(level.getGameTime()%(20+Math.min(40,m.complexRoles().getOrDefault("lining",0)*2))==0)s.heat=Math.max(0,s.heat-1);
        m.setChanged();
    }
    // Metal identity and quarter-ingot amount of one input item. No remelting bonus.
    private static int[] melt(ItemStack stack){
        for(int k=0;k<3;k++){
            String n=METALS[k],ns=k==0?"minecraft:":"smog:";
            if(is(stack,ns+n+"_ingot"))return new int[]{k,4};
            if(is(stack,ns+n+"_block"))return new int[]{k,36};
            if(is(stack,"smog:"+n+"_plate"))return new int[]{k,4};
            if(is(stack,"smog:"+n+"_fitting"))return new int[]{k,1};
            if(is(stack,"smog:"+n+"_pipe_part"))return new int[]{k,2};
            if(k<2){
                if(is(stack,ns+"raw_"+n))return new int[]{k,4};
                if(is(stack,ns+"raw_"+n+"_block"))return new int[]{k,36};
            }
        }
        return null;
    }
    private static boolean working(MachineEntity m,String key,int ticks,int step){var s=m.copper;if(!s.workKey.equals(key)){s.work=0;s.workKey=key;}s.work+=step;m.setChanged();return s.work>=ticks;}
    private static void hearth(ServerLevel level,MachineEntity m){var s=m.copper;var recipe=melt(m.getItem(0));
        heat(level,m,recipe!=null||s.total()>0);
        if(recipe==null){s.work=0;s.status=2;return;}
        if(s.total()+recipe[1]>capacity(m)){s.status=5;return;}
        if(s.heat<threshold(m)){s.status=s.burn==0?3:4;return;}
        s.status=0;
        if(working(m,BuiltInRegistries.ITEM.getKey(m.getItem(0).getItem()).toString(),80,s.air>0?2:1)){
            m.getItem(0).shrink(1);s.metal(recipe[0],s.metal(recipe[0])+recipe[1]);s.work=0;if(s.air>0)s.air=Math.max(0,s.air-(recipe[1]+3)/4);m.setChanged();
        }
    }
    private static int mold(ItemStack stack){for(int i=0;i<4;i++)if(is(stack,"smog:"+new String[]{"ingot_mold","plate_mold","fitting_mold","pipe_mold"}[i]))return i;return -1;}
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
        int metal=Math.floorMod(s.selection,3),cost=mold==3?8:4;var result=castResult(metal,mold);
        if(result.isEmpty()){s.status=6;return;}if(!m.canOutput(result)){s.status=5;return;}var source=source(level,m.getBlockPos());
        if(source==null||source.copper.metal(metal)<cost){s.status=8;return;}
        if(source.copper.heat<threshold(source)){s.status=4;return;}
        s.status=7;
        if(working(m,metal+":"+mold,100,1)&&level.getGameTime()-source.copper.lastPour>=10){
            source.copper.metal(metal,source.copper.metal(metal)-cost);source.copper.lastPour=level.getGameTime();source.setChanged();output(m,result);s.work=0;s.status=0;
        }
    }
    private static void kiln(ServerLevel level,MachineEntity m){var s=m.copper;var input=m.getItem(0);
        var match=level.recipeAccess().getRecipeFor(RecipeType.SMELTING,new SingleRecipeInput(input),level);
        // A ceramic kiln is deliberately not a universal ore smelter.
        boolean ceramic=BuiltInRegistries.ITEM.getKey(input.getItem()).getNamespace().equals("smog")&&BuiltInRegistries.ITEM.getKey(input.getItem()).getPath().startsWith("unfired_");
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
        if(id==23&&workshop(m,"copper_hearth")){
            if(!m.assembled()){s.status=1;return true;}
            if(s.heat<threshold(m)){s.status=4;return true;}
            int batches=Math.min(s.copper/12,s.tin/4);if(batches==0){s.status=8;return true;}s.copper-=batches*12;s.tin-=batches*4;s.bronze+=batches*16;m.setChanged();return true;
        }return false;
    }
    public static void pump(ServerLevel level,MachineEntity bellows){
        for(var side:Direction.values()){
            var pos=bellows.getBlockPos().relative(side);if(!level.hasChunkAt(pos)||!level.getBlockState(pos).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("air_intake"))))continue;
            var owner=ComplexFormationData.get(level).owner(pos);
            if(owner!=null&&level.hasChunkAt(owner)&&level.getBlockEntity(owner) instanceof MachineEntity m&&m.assembled()){m.copper.air=8;m.setChanged();return;}
        }
    }
    public static void dropMetal(ServerLevel level,MachineEntity m){if(!workshop(m,"copper_hearth"))return;
        for(int kind=0;kind<3;kind++){int units=m.copper.metal(kind);while(units>=4){int ingots=Math.min(64,units/4);Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,new ItemStack(item((kind==0?"minecraft:":"smog:")+METALS[kind]+"_ingot"),ingots));units-=ingots*4;}
            if(units>0)Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,new ItemStack(item("smog:"+METALS[kind]+"_fitting"),units));m.copper.metal(kind,0);}
    }
}
