package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class BronzeManufacturingTests {
    @GameTest public void everyNewNativeRecipeMatchesItsPaidIngredients(GameTestHelper h) throws Exception {
        try(var stream=BronzeManufacturingTests.class.getResourceAsStream("/smog.bronze.recipes.json")){
            var root=com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            for(var value:root.getAsJsonArray("crafting")){
                var r=value.getAsJsonObject();String output=r.get("output").getAsString();
                java.util.function.Function<String,ItemStack> ingredient=id->{if(!id.startsWith("#"))return new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(id)));var tag=net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,net.minecraft.resources.Identifier.parse(id.substring(1)));return BuiltInRegistries.ITEM.getTagOrEmpty(tag).iterator().next().value().getDefaultInstance();};
                if(r.get("station").getAsString().equals("crafting")){
                    var stacks=new java.util.ArrayList<ItemStack>();for(var cell:r.getAsJsonArray("cells"))stacks.add(cell.getAsString().isEmpty()?ItemStack.EMPTY:ingredient.apply(cell.getAsString()));
                    var input=net.minecraft.world.item.crafting.CraftingInput.of(3,3,stacks);var recipe=h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,input,h.getLevel()).orElseThrow(()->new IllegalStateException("No native recipe: "+output));var result=recipe.value().assemble(input);
                    h.assertTrue(BuiltInRegistries.ITEM.getKey(result.getItem()).toString().equals(output),"Native matching must not select another recipe: "+output);h.assertValueEqual(result.getCount(),r.get("count").getAsInt(),"Exact specified batch output");
                }else{
                    var input=new net.minecraft.world.item.crafting.SingleRecipeInput(ingredient.apply(r.getAsJsonArray("inputs").get(0).getAsString()));
                    h.assertTrue(h.getLevel().getServer().getRecipeManager().getRecipes().stream().anyMatch(holder->holder.value() instanceof net.minecraft.world.item.crafting.StonecutterRecipe cut&&cut.matches(input,h.getLevel())&&BuiltInRegistries.ITEM.getKey(cut.assemble(input).getItem()).toString().equals(output)),"Selectable single-ingredient recipe exists: "+output);
                }
            }
        }h.succeed();
    }
    private static MachineEntity machine(GameTestHelper h,String id){var p=h.absolutePos(new BlockPos(3,3,3));h.getLevel().setBlockAndUpdate(p,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(id)).defaultBlockState());return (MachineEntity)h.getLevel().getBlockEntity(p);}
    @GameTest public void actualCastingAndFinishingConserveAllSixMechanicalParts(GameTestHelper h){
        int bronze=java.util.Arrays.asList(MoltenMetalStorage.NAMES).indexOf("bronze"),lead=java.util.Arrays.asList(MoltenMetalStorage.NAMES).indexOf("lead");
        for(var entry:BronzeManufacturing.SHAPES.entrySet())for(String style:java.util.List.of("sand_","metal_")){
            var table=machine(h,"casting_table");table.setItem(1,BronzeManufacturing.stack(style+entry.getKey()+"_mold"));
            h.assertTrue(CastingProcesses.recipe(table,lead)==null,"A lead pour cannot manufacture bronze");
            try(var tx=Transaction.openOuter()){h.assertValueEqual(table.castingFluid.insert(MoltenMetalStorage.variant(bronze),entry.getValue(),tx),entry.getValue().longValue(),"Exact fluid volume including a half ingot");tx.commit();}
            for(int i=0;i<200;i++)CastingProcesses.tick(h.getLevel(),table);
            h.assertTrue(table.getItem(3).is(BronzeManufacturing.stack("cast_bronze_"+entry.getKey()).getItem()),"Physical casting output is delivered");h.assertValueEqual(table.getItem(1).isEmpty(),style.equals("sand_"),"Only sand molds are consumed");
            var cast=table.getItem(3).copy();var bench=machine(h,"tool_workbench");bench.setItem(0,cast);
            var player=h.makeMockServerPlayerInLevel();var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");player.setItemInHand(InteractionHand.MAIN_HAND,hammer);ToolAssembly.use(h.getLevel(),bench,player,InteractionHand.MAIN_HAND);
            for(int i=0;i<20;i++)ToolAssembly.tick(h.getLevel(),bench);
            h.assertTrue(bench.getItem(3).is(BronzeManufacturing.stack("bronze_"+entry.getKey()).getItem()),"The real workbench finishes its casting");h.assertTrue(bench.getItem(0).isEmpty(),"Finishing consumes its paid casting");h.assertValueEqual(CopperWorkshops.melt(bench.getItem(3))[1],entry.getValue().intValue(),"No metal is created by finishing");
        }h.succeed();
    }
    @GameTest public void patternsAndTemplatesRetainTheirSamples(GameTestHelper h){
        var bench=machine(h,"tool_workbench");bench.setItem(0,new ItemStack(Items.OAK_PLANKS));bench.setItem(1,BronzeManufacturing.stack("hub_pattern"));h.assertFalse(ToolAssembly.requiresHammer(bench),"No metal requires no hammer");h.assertTrue(ToolAssembly.button(bench,22),"Native GUI action starts the wooden template");for(int i=0;i<20;i++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(bench.getItem(0).isEmpty()&&!bench.getItem(1).isEmpty(),"Only the plank is consumed");h.assertTrue(bench.getItem(3).is(BronzeManufacturing.stack("hub_template").getItem()),"A real reusable template is made");
        for(String shape:BronzeManufacturing.SHAPES.keySet())h.assertTrue(CopperWorkshops.potteryRecipes().stream().anyMatch(r->r.output().equals("smog:"+shape+"_pattern")&&r.count()==1),"Every mold has an obtainable nonmetal initial pattern");
        bench.setItem(3,ItemStack.EMPTY);bench.setItem(0,new ItemStack(Items.OAK_PLANKS));h.assertTrue(ToolAssembly.button(bench,22),"Another nonmetal job can start");bench.setItem(0,BronzeManufacturing.stack("cast_bronze_gear"));bench.setItem(1,ItemStack.EMPTY);for(int i=0;i<30;i++)ToolAssembly.tick(h.getLevel(),bench);h.assertTrue(bench.getItem(3).isEmpty()&&!bench.getItem(0).isEmpty(),"Swapping a running nonmetal job cannot bypass the hammer requirement");h.succeed();
    }
    @GameTest public void modularHoistDiscoversArbitraryHeightInsteadOfEnumeratedBlueprints(GameTestHelper h){
        var level=h.getLevel();var origin=h.absolutePos(new BlockPos(70,150,70));
        for(int height:new int[]{1,2,25,64,128}){
            var drawing=BronzeBlueprints.hoist(height);BronzeRegistrationTests.place(level,origin,Direction.EAST,drawing);
            h.assertTrue(BronzeBlueprints.inspect(level,origin,Direction.EAST,drawing).complete(),"Repeated modules form height "+height);
            h.assertTrue(BronzeBlueprints.hoistCandidates(level,origin,Direction.EAST).stream().anyMatch(b->b.id().equals(drawing.id())),"Lower landing determines the real height "+height);
            for(var cell:drawing.cells())level.setBlock(origin.offset(cell.offset().rotate(BronzeBlueprints.rotation(Direction.EAST))),Blocks.AIR.defaultBlockState(),Block.UPDATE_CLIENTS);level.setBlock(origin,Blocks.AIR.defaultBlockState(),Block.UPDATE_CLIENTS);
        }h.succeed();
    }
}
