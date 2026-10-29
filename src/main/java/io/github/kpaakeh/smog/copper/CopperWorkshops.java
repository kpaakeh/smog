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
    public record RecipePreview(String machine,List<ItemStack> inputs,ItemStack tool,ItemStack result,String description,long fluidUnits){public RecipePreview(String machine,List<ItemStack> inputs,ItemStack tool,ItemStack result,String description){this(machine,inputs,tool,result,description,-1);}}
    public static List<RecipePreview> recipes(){
        List<RecipePreview> result=new ArrayList<>();
        result.add(new RecipePreview("washing_trough",List.of(new ItemStack(Items.RAW_COPPER,8),new ItemStack(Items.WATER_BUCKET)),ItemStack.EMPTY,new ItemStack(item("smog:copper_concentrate"),5),"jei.smog.copper.washing"));
        for(var seed:BuiltInRegistries.ITEM)if(seed.getDefaultInstance().is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,ModBlocks.id("oil_seeds"))))result.add(new RecipePreview("lever_press",List.of(new ItemStack(seed,8)),ItemStack.EMPTY,new ItemStack(item("smog:seed_cake")),"jei.smog.copper.pressing"));
        for(var part:ToolDefinitions.parts())if(part.cast()){
            var input=new ItemStack(item(part.item().toString()));result.add(new RecipePreview("tool_workbench",List.of(input),new ItemStack(item("smog:stone_work_hammer")),ToolParts.finish(input),"jei.smog.copper.tools"));
        }
        for(var schema:ToolDefinitions.templates())for(String material:ToolDefinitions.parts().stream().filter(p->p.form().category().equals("head")).map(p->p.material().id()).distinct().toList()){
            var assembled=ToolParts.example(schema,material);if(assembled.isEmpty())continue;var inputs=ToolParts.components(assembled).stream().filter(s->!s.isEmpty()).toList();result.add(new RecipePreview("tool_workbench",inputs,new ItemStack(item("smog:stone_work_hammer")),assembled,"jei.smog.copper.tools"));
        }
        for(var candidate:BuiltInRegistries.ITEM){var input=candidate.getDefaultInstance();if(ToolParts.nativeSource(input)!=null)result.add(new RecipePreview("tool_workbench",List.of(input),new ItemStack(item("smog:stone_work_hammer")),ToolParts.finish(input),"jei.smog.copper.tools"));}
        for(var candidate:ToolParts.repairIngredients()){var input=new ItemStack(candidate,64);for(var part:ToolParts.shapingChoices(input)){var shaped=ToolParts.shaping(input,part);result.add(new RecipePreview("tool_workbench",List.of(new ItemStack(candidate,shaped.count())),new ItemStack(item("smog:stone_work_hammer")),shaped.result(),"jei.smog.copper.shaping"));}}
        for(var source:BuiltInRegistries.ITEM)if(source.getDefaultInstance().is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,ModBlocks.id("fiber_sources"))))result.add(new RecipePreview("lever_press",List.of(new ItemStack(source,8)),new ItemStack(item("smog:fiber_press_basket")),new ItemStack(item("smog:plant_fiber"),4),"jei.smog.copper.fiber_pressing"));
        for(var recipe:POTTERY)result.add(new RecipePreview("potter_table",List.of(new ItemStack(item(recipe.input),recipe.count)),ItemStack.EMPTY,new ItemStack(item(recipe.output),recipe.outputCount),"jei.smog.copper.pottery"));
        for(int metal=0;metal<MoltenMetalStorage.NAMES.length;metal++)for(int mold=0;mold<MOLDS.length;mold++){
            var cast=castResult(metal,mold);if(cast.isEmpty())continue;
            result.add(new RecipePreview("casting_table",List.of(new ItemStack(item(namespace(metal)+MoltenMetalStorage.NAMES[metal]+"_ingot"),Math.max(1,(castCost(metal,mold)+8999)/9000))),new ItemStack(item("smog:"+MOLDS[mold])),cast,"jei.smog.copper.casting",castCost(metal,mold)));
            if(extraForm(mold)<0)for(String style:List.of("sand_","metal_")){String variant=style+MOLDS[mold];if(BuiltInRegistries.ITEM.containsKey(ModBlocks.id(variant)))result.add(new RecipePreview("casting_table",List.of(new ItemStack(item(namespace(metal)+MoltenMetalStorage.NAMES[metal]+"_ingot"))),new ItemStack(item("smog:"+variant)),cast,"jei.smog.copper.casting",castCost(metal,mold)));}
        }
        for(var firing:FIRING)result.add(new RecipePreview("ceramic_kiln",List.of(new ItemStack(item(firing.input),8)),ItemStack.EMPTY,new ItemStack(item(firing.output),8),"jei.smog.copper.firing"));
        for(var candidate:BuiltInRegistries.ITEM){var stack=candidate.getDefaultInstance();var amount=melt(stack);if(amount!=null)result.add(new RecipePreview("copper_hearth",List.of(stack),ItemStack.EMPTY,new ItemStack(item("smog:molten_"+MoltenMetalStorage.NAMES[amount[0]]+"_bucket")),"jei.smog.copper.melting"));}
        for(var alloy:Alloying.recipes()){var inputs=new ArrayList<ItemStack>();for(var input:alloy.ingredients().entrySet()){int metal=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(input.getKey());inputs.add(new ItemStack(item(namespace(metal)+input.getKey()+"_ingot"),input.getValue()));}int metal=Arrays.asList(MoltenMetalStorage.NAMES).indexOf(alloy.output());result.add(new RecipePreview("copper_hearth",inputs,alloy.closed()?new ItemStack(item("smog:closed_crucible_insert")):ItemStack.EMPTY,new ItemStack(item(namespace(metal)+alloy.output()+"_ingot"),alloy.total()),"jei.smog.copper.alloy"));}
        for(int metal=0;metal<MoltenMetalStorage.NAMES.length;metal++){String name=MoltenMetalStorage.NAMES[metal];var block=item(namespace(metal)+name+"_block");if(block!=Items.AIR)result.add(new RecipePreview("casting_basin",List.of(new ItemStack(item(namespace(metal)+name+"_ingot"),9)),ItemStack.EMPTY,new ItemStack(block),"jei.smog.copper.casting"));}
        var patterns=new LinkedHashMap<String,ItemStack>();patterns.put("ingot",new ItemStack(Items.COPPER_INGOT));patterns.put("plate",new ItemStack(item("smog:copper_plate")));patterns.put("fitting",new ItemStack(item("smog:copper_fitting")));patterns.put("pipe",new ItemStack(item("smog:copper_pipe_part")));for(var form:ToolDefinitions.headForms()){var head=ToolDefinitions.find("copper",form.id(),false);if(head!=null)patterns.put(form.id().replaceFirst("_head$",""),new ItemStack(item(head.item().toString())));}
        patterns.forEach((shape,sample)->{result.add(new RecipePreview("tool_workbench",List.of(new ItemStack(item("smog:molding_sand")),sample),new ItemStack(item("smog:stone_work_hammer")),MoldMaking.sandMold(shape),"jei.smog.copper.imprint"));result.add(new RecipePreview("casting_table",List.of(new ItemStack(Items.GOLD_INGOT,2)),sample,MoldMaking.metalMold(shape),"jei.smog.copper.mold_making"));});
        return List.copyOf(result);
    }
    public static int meltingHeat(int metal){return MetalProcessing.conditions(metal).heat();}
    private static String namespace(int metal){String n=MoltenMetalStorage.NAMES[metal];return java.util.Set.of("copper","iron","gold","netherite").contains(n)?"minecraft:":"smog:";}
    public static boolean closed(MachineEntity m){return m.complexRoles().getOrDefault("closed_insert",0)>0;}
    public static boolean compatible(MachineEntity m,int metal){return MetalProcessing.compatible(m,metal);}
    private record ExtraForm(String item,int units,boolean singleUse,java.util.Map<String,String> outputs){}
    private static final List<ExtraForm> EXTRA_FORMS=new ArrayList<>();
    private static final Map<String,String> MOLD_VARIANTS=new LinkedHashMap<>();
    public static void initialize(){
        try(var stream=CopperWorkshops.class.getResourceAsStream("/smog.content.json")){
            var root=JsonParser.parseReader(new InputStreamReader(stream,StandardCharsets.UTF_8)).getAsJsonObject();
            Smeltery.initialize(root);Alloying.initialize(root);
            if(root.has("casting_forms"))for(var value:root.getAsJsonArray("casting_forms")){var o=value.getAsJsonObject();var outputs=new java.util.LinkedHashMap<String,String>();o.getAsJsonObject("outputs").entrySet().forEach(e->outputs.put(e.getKey(),e.getValue().getAsString()));EXTRA_FORMS.add(new ExtraForm(o.get("item").getAsString(),o.get("units").getAsInt(),o.get("single_use").getAsBoolean(),outputs));}
            if(root.has("items"))for(var value:root.getAsJsonArray("items")){var o=value.getAsJsonObject();if(o.has("kind")&&o.get("kind").getAsString().equals("mold_variant"))MOLD_VARIANTS.put(o.get("name").getAsString(),o.get("base_mold").getAsString());}
            MOLDS=java.util.stream.Stream.concat(java.util.Arrays.stream(MOLDS),EXTRA_FORMS.stream().map(f->f.item().replace("smog:",""))).toArray(String[]::new);
            if(root.has("firing"))for(var entry:root.getAsJsonArray("firing")){var o=entry.getAsJsonObject();FIRING.add(new Pottery(o.get("input").getAsString(),1,o.get("output").getAsString(),1));}
            if(root.has("pottery"))for(var entry:root.getAsJsonArray("pottery")){var o=entry.getAsJsonObject();POTTERY.add(new Pottery(o.get("input").getAsString(),o.get("count").getAsInt(),o.get("output").getAsString(),o.has("output_count")?o.get("output_count").getAsInt():1));}
        }catch(IOException e){throw new IllegalStateException(e);}
    }
    private static Item item(String id){return BuiltInRegistries.ITEM.getValue(Identifier.parse(id));}
    public static boolean is(ItemStack stack,String id){return !stack.isEmpty()&&BuiltInRegistries.ITEM.containsKey(Identifier.parse(id))&&stack.is(item(id));}
    public static boolean workshop(MachineEntity m,String name){return m.machineId().getPath().equals(name);}
    private static void output(MachineEntity m,ItemStack result){var old=m.getItem(3);if(old.isEmpty())m.setItem(3,result.copy());else {old.grow(result.getCount());m.setChanged();}}
    public static boolean accepts(MachineEntity m,int slot,ItemStack stack){
        if(Smeltery.fuelTank(m))return false;
        if(Smeltery.vessel(m))return slot==1&&!CastingProcesses.locked(m)&&!workshop(m,"casting_basin")&&(mold(stack)>=0||MoldMaking.pattern(stack)!=null);
        if(Smeltery.handles(m)){int worker=Smeltery.worker(slot);return worker>=0?worker<Smeltery.workers(m)&&melt(stack)!=null:slot==2?stack.has(net.minecraft.core.component.DataComponents.COOKING_FUEL):false;}

        if(Ropeway.handles(m))return slot==0;
        if(LoadingCrane.handles(m))return false;
        if(WaterSupply.handles(m))return false;
        if(ManualWorkshops.handles(m))return slot==0&&ManualWorkshops.accepts(m,stack);
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
    public static int data(MachineEntity m,int index){var s=m.copper;if(Smeltery.handles(m)&&index>=20)return Smeltery.data(m,index);return switch(index){case 0->s.work;case 1->ManualWorkshops.handles(m)?100:workshop(m,"casting_table")?100:workshop(m,"ceramic_kiln")?200:80;case 2->s.heat;case 3->threshold(m);case 4->s.total()/U;case 5->capacity(m)/U;case 6->s.selection;case 7->m.multiblockController()&&!m.assembled()?1:s.status;case 8->m.enabled()?1:0;case 9->s.copper/U;case 10->s.tin/U;case 11->s.bronze/U;case 12->workshop(m,"potter_table")&&!POTTERY.isEmpty()?Item.getId(item(POTTERY.get(Math.floorMod(s.selection,POTTERY.size())).output)):0;case 13->s.queued;case 14->workshop(m,"casting_basin")?1:ToolAssembly.handles(m)?7:WaterSupply.handles(m)?6:ManualWorkshops.handles(m)?5:workshop(m,"casting_table")?1:workshop(m,"potter_table")?2:workshop(m,"ceramic_kiln")?4:0;case 15->ToolAssembly.handles(m)?ToolAssembly.activePart(m):m.complexShape()==null?0:m.complexShape().width();case 16->m.complexShape()==null?0:m.complexShape().depth();case 17->ToolAssembly.handles(m)&&ToolAssembly.shapingMode(m)?ToolAssembly.shapingPlan(m).count():m.complexShape()==null?0:m.complexShape().height();case 18->s.air;case 19->s.processFluid/81;default->0;};}
    public static void tick(ServerLevel level,MachineEntity m){
        if(workshop(m,"hand_bellows")&&level.getGameTime()%40==0&&BronzeDrive.connected(m))pump(level,m);
        if(Ropeway.handles(m))Ropeway.tick(level,m);
        else if(LoadingCrane.handles(m))LoadingCrane.tick(level,m);
        else if(ToolAssembly.handles(m))ToolAssembly.tick(level,m);
        else if(WaterSupply.handles(m))WaterSupply.tick(level,m);
        else if(ManualWorkshops.handles(m))ManualWorkshops.tick(level,m);
        else if(Smeltery.handles(m))Smeltery.tick(level,m);
        else if(Smeltery.vessel(m))CastingProcesses.tick(level,m);
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
        if(stack.getItem() instanceof ModularToolItem||ToolDefinitions.part(stack)!=null)return ToolParts.melt(stack);
        if(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("smog")&&MOLD_VARIANTS.containsKey(BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath())&&BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().startsWith("metal_"))return new int[]{Arrays.asList(MoltenMetalStorage.NAMES).indexOf("gold"),2*MoltenMetalStorage.INGOT};
        if(is(stack,"smog:copper_concentrate"))return new int[]{0,2*MoltenMetalStorage.INGOT};
        if(is(stack,"smog:metal_residue")){var data=stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.EMPTY).copyTag();int kind=data.contains("metal_name")?java.util.Arrays.asList(MoltenMetalStorage.NAMES).indexOf(data.getStringOr("metal_name","")):data.getIntOr("metal",-1),amount=data.getIntOr("units",0);return kind>=0&&kind<MoltenMetalStorage.NAMES.length&&amount>0&&amount<MoltenMetalStorage.INGOT?new int[]{kind,amount}:null;}
        for(int k=0;k<MoltenMetalStorage.NAMES.length;k++){
            String n=MoltenMetalStorage.NAMES[k],ns=namespace(k);
            if(is(stack,"smog:"+n+"_relief"))return new int[]{k,MoltenMetalStorage.INGOT};
            if(tagged(stack,"c:nuggets/"+n)||is(stack,ns+n+"_nugget"))return new int[]{k,1000};
            if(tagged(stack,"c:dusts/"+n))return new int[]{k,4*U};
            if(tagged(stack,"c:ingots/"+n)||is(stack,ns+n+"_ingot"))return new int[]{k,4*U};
            if(tagged(stack,"c:storage_blocks/"+n)||is(stack,ns+n+"_block"))return new int[]{k,36*U};
            if(tagged(stack,"c:plates/"+n)||is(stack,"smog:"+n+"_plate"))return new int[]{k,4*U};
            if(is(stack,"smog:"+n+"_fitting"))return new int[]{k,1*U};
            if(is(stack,"smog:"+n+"_pipe_part"))return new int[]{k,2*U};
            if(k<2||k==3){
                if(tagged(stack,"c:crushed_ores/"+n)||tagged(stack,"c:ore_shards/"+n))return new int[]{k,4*U};
                if(tagged(stack,"c:raw_materials/"+n)||is(stack,ns+"raw_"+n))return new int[]{k,4*U};
                if(tagged(stack,"c:storage_blocks/raw_"+n)||is(stack,ns+"raw_"+n+"_block"))return new int[]{k,36*U};
            }
        }
        return null;
    }
    private static boolean tagged(ItemStack stack,String tag){return stack.is(net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,Identifier.parse(tag)));}
    public static boolean canAlloy(MachineEntity m){return Alloying.possible(m);}
    public static void alloy(MachineEntity m){Alloying.tick(m);}
    private static boolean working(MachineEntity m,String key,int ticks,int step){var s=m.copper;if(!s.workKey.equals(key)){s.work=0;s.workKey=key;}s.work+=step;m.setChanged();return s.work>=ticks;}
    public static String[] MOLDS=java.util.stream.Stream.concat(java.util.stream.Stream.of("ingot_mold","plate_mold","fitting_mold","pipe_mold"),ToolDefinitions.headForms().stream().map(f->f.id().replaceFirst("_head$","_mold"))).toArray(String[]::new);
    private static int extraForm(int mold){return mold-4-ToolDefinitions.headForms().size();}
    public static int castCost(int mold){if(extraForm(mold)>=0)return EXTRA_FORMS.get(extraForm(mold)).units();return mold>=4?ToolDefinitions.headForms().get(mold-4).metalUnits():(mold==3?8:4)*U;}
    public static int castCost(int metal,int mold){if(mold>=4&&extraForm(mold)<0){var part=ToolDefinitions.find(MoltenMetalStorage.NAMES[metal],ToolDefinitions.headForms().get(mold-4).id(),true);if(part!=null)return ToolParts.units(part);}return castCost(mold);}
    public static boolean singleUse(int mold){return extraForm(mold)>=0&&EXTRA_FORMS.get(extraForm(mold)).singleUse();}
    public static boolean singleUse(ItemStack mold){return BuiltInRegistries.ITEM.getKey(mold.getItem()).getPath().startsWith("sand_")||singleUse(mold(mold));}
    public static int mold(ItemStack stack){String name=BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();if(BuiltInRegistries.ITEM.getKey(stack.getItem()).getNamespace().equals("smog")&&MOLD_VARIANTS.containsKey(name)){String base=MOLD_VARIANTS.get(name);for(int i=0;i<MOLDS.length;i++)if(MOLDS[i].equals(base))return i;}for(int i=0;i<MOLDS.length;i++)if(is(stack,"smog:"+MOLDS[i]))return i;return -1;}
    public static ItemStack castResult(int metal,int mold){String n=MoltenMetalStorage.NAMES[metal];if(extraForm(mold)>=0){String output=EXTRA_FORMS.get(extraForm(mold)).outputs().get(n);return output==null?ItemStack.EMPTY:new ItemStack(item(output));}if(mold>=4){var part=ToolDefinitions.find(n,ToolDefinitions.headForms().get(mold-4).id(),true);return part==null?ItemStack.EMPTY:new ItemStack(item(part.item().toString()));}if(metal>2&&mold>0)return ItemStack.EMPTY;if(metal==1&&mold>=2)return ItemStack.EMPTY;return switch(mold){case 0->new ItemStack(item(namespace(metal)+n+"_ingot"));case 1->new ItemStack(item("smog:"+n+"_plate"));case 2->new ItemStack(item("smog:"+n+"_fitting"),4);case 3->new ItemStack(item("smog:"+n+"_pipe_part"),4);default->ItemStack.EMPTY;};}
    public static MachineEntity source(ServerLevel level,BlockPos table){
        record Node(BlockPos pos,int distance){}var queue=new ArrayDeque<Node>();Set<BlockPos> visited=new HashSet<>();queue.add(new Node(table,0));
        while(!queue.isEmpty()&&visited.size()<128){var node=queue.removeFirst();if(node.distance>8)continue;
            for(var side:Direction.values()){if(side==Direction.DOWN)continue;var pos=node.pos.relative(side);if(!visited.add(pos)||!level.hasChunkAt(pos))continue;var state=level.getBlockState(pos);
                if(side==Direction.UP&&state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_faucet")))&&state.getValue(WorkshopPartBlock.OPEN)){
                    var port=pos.relative(state.getValue(WorkshopPartBlock.FACING).getOpposite());if(!level.hasChunkAt(port)||!level.getBlockState(port).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_spout"))))continue;
                    var owner=ComplexFormationData.get(level).owner(port);
                    if(owner!=null&&level.hasChunkAt(owner)&&level.getBlockEntity(owner) instanceof MachineEntity m&&workshop(m,"copper_hearth")&&m.assembled())return m;
                }else if(state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("casting_channel"))))queue.add(new Node(pos,node.distance+1));
            }
        }return null;
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
    public static boolean button(MachineEntity m,int id){if(Smeltery.vessel(m))return false;if(ToolAssembly.handles(m))return ToolAssembly.button(m,id);var s=m.copper;if(ManualWorkshops.handles(m)){if(id==22&&m.assembled()){ManualWorkshops.queue(m);return true;}return false;}
        if(id==20||id==21){int max=workshop(m,"potter_table")?POTTERY.size():MoltenMetalStorage.NAMES.length;s.selection=Math.floorMod(s.selection+(id==20?-1:1),Math.max(1,max));s.work=0;m.setChanged();return true;}
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
            var nozzle=bellows.getBlockPos().relative(side);if(!level.hasChunkAt(nozzle))continue;var state=level.getBlockState(nozzle);
            if(!state.is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("air_nozzle")))||!state.getValue(WorkshopPartBlock.OPEN))continue;
            var port=nozzle.relative(state.getValue(WorkshopPartBlock.FACING).getOpposite());if(!level.hasChunkAt(port)||!level.getBlockState(port).is(BuiltInRegistries.BLOCK.getValue(ModBlocks.id("air_intake"))))continue;
            var owner=ComplexFormationData.get(level).owner(port);
            if(owner!=null&&level.hasChunkAt(owner)&&level.getBlockEntity(owner) instanceof MachineEntity m&&m.assembled()){m.copper.air=8;m.sync();level.sendParticles(net.minecraft.core.particles.ParticleTypes.CLOUD,nozzle.getX()+.5,nozzle.getY()+.5,nozzle.getZ()+.5,3,.1,.1,.1,.02);return true;}
        }return false;
    }
    public static void dropMetal(ServerLevel level,MachineEntity m){if(!workshop(m,"copper_hearth"))return;
        for(int kind=0;kind<MoltenMetalStorage.NAMES.length;kind++){int units=m.copper.metal(kind);while(units>=MoltenMetalStorage.INGOT){int ingots=Math.min(64,units/MoltenMetalStorage.INGOT);Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,new ItemStack(item(namespace(kind)+MoltenMetalStorage.NAMES[kind]+"_ingot"),ingots));units-=ingots*MoltenMetalStorage.INGOT;}
            if(units>0){var residue=new ItemStack(item("smog:metal_residue"));var data=new net.minecraft.nbt.CompoundTag();data.putString("metal_name",MoltenMetalStorage.NAMES[kind]);data.putInt("units",units);residue.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+.5,m.getBlockPos().getZ()+.5,residue);}m.copper.metal(kind,0);}
    }
}
