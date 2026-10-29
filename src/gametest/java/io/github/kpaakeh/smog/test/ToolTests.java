package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class ToolTests {
    private static ItemStack item(String name){return new ItemStack(ToolParts.item(name));}
    private static ItemStack build(String handle){return ToolParts.assemble(item("copper_pickaxe_head"),item(handle),item("cord_binding"),ItemStack.EMPTY);}
    private static MachineEntity table(GameTestHelper h){var pos=h.absolutePos(new BlockPos(3,2,3));h.getLevel().setBlock(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id("tool_workbench")).defaultBlockState(),Block.UPDATE_ALL);return (MachineEntity)h.getLevel().getBlockEntity(pos);}
    @GameTest public void partSwapsDoNotChangeWearOrHarvestTier(GameTestHelper h){
        var tool=build("wooden_handle");tool.setDamageValue(1);var initial=ToolParts.extract(tool,0);int units=ToolParts.melt(initial)[1];
        for(int i=0;i<50;i++)tool=ToolParts.assemble(ToolParts.extract(tool,0),item(i%2==0?"reinforced_handle":"wooden_handle"),item("copper_clamp"),i%2==0?item("fiber_wrap"):ItemStack.EMPTY);
        h.assertValueEqual(ToolParts.melt(ToolParts.extract(tool,0))[1],units,"Swapping parts fifty times does not heal or wear head");
        h.assertTrue(ToolParts.wear(initial).equals(ToolParts.wear(ToolParts.extract(tool,0))),"Exact head life fraction retained");
        h.assertFalse(tool.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Reinforced handle cannot upgrade copper mining tier");
        var bronze=ToolParts.assemble(item("bronze_pickaxe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);
        h.assertTrue(bronze.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Bronze head has iron harvest tier");h.assertFalse(bronze.isCorrectToolForDrops(Blocks.OBSIDIAN.defaultBlockState()),"Bronze is not diamond");h.succeed();
    }
    @GameTest public void wornHeadSurvivesAndLosesCombatBonus(GameTestHelper h){
        var tool=build("wooden_handle");tool.setDamageValue(tool.getMaxDamage()-1);ModularToolItem.wear(tool,h.getLevel(),1);
        h.assertFalse(tool.isEmpty(),"Worn assembly must not disappear");h.assertTrue(ModularToolItem.broken(tool),"Head spent");h.assertFalse(tool.has(DataComponents.ATTRIBUTE_MODIFIERS),"No permanent weapon bonus on broken head");
        h.assertTrue(ToolParts.melt(ToolParts.extract(tool,0))==null,"Spent head cannot return a full metal charge");
        tool.setDamageValue(tool.getMaxDamage()-5);((ModularToolItem)tool.getItem()).inventoryTick(tool,h.getLevel(),h.makeMockServerPlayerInLevel(),net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        h.assertFalse(ModularToolItem.broken(tool),"Native damage repair re-enables head");h.assertTrue(tool.has(DataComponents.ATTRIBUTE_MODIFIERS),"Native repair restores proper tool attributes");h.succeed();
    }
    @GameTest public void nativeAttacksAndEnchantingUseRealToolComponents(GameTestHelper h){
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);h.assertFalse(player.isCreative(),"Actual survival fixture");var tool=build("wooden_handle");player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        var target=BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("cow")).create(h.getLevel(),net.minecraft.world.entity.EntitySpawnReason.COMMAND);target.setPos(player.getX()+1,player.getY(),player.getZ());h.getLevel().addFreshEntity(target);player.attack(target);
        h.assertValueEqual(tool.getDamageValue(),2,"Real Player attack wears the head once");
        for(var key:java.util.List.of(net.minecraft.world.item.enchantment.Enchantments.EFFICIENCY,net.minecraft.world.item.enchantment.Enchantments.FORTUNE,net.minecraft.world.item.enchantment.Enchantments.UNBREAKING,net.minecraft.world.item.enchantment.Enchantments.MENDING)){
            var enchantment=h.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);h.assertTrue(enchantment.value().isSupportedItem(tool),"Native supported_items tags include composed tools");
        }
        h.succeed();
    }
    @GameTest public void nativeCombiningCannotLoseModularParts(GameTestHelper h){
        var first=build("wooden_handle");first.setDamageValue(100);var second=build("reinforced_handle");second.setDamageValue(100);
        var input=net.minecraft.world.item.crafting.CraftingInput.of(2,1,java.util.List.of(first,second));
        h.assertFalse(net.minecraft.world.item.crafting.RepairItemRecipe.INSTANCE.matches(input,h.getLevel()),"Crafting cannot erase both part sets");
        var vanillaA=new ItemStack(Items.IRON_PICKAXE);vanillaA.setDamageValue(100);var vanillaB=vanillaA.copy();
        h.assertTrue(net.minecraft.world.item.crafting.RepairItemRecipe.INSTANCE.matches(net.minecraft.world.item.crafting.CraftingInput.of(2,1,java.util.List.of(vanillaA,vanillaB)),h.getLevel()),"Vanilla repair unchanged");
        var player=h.makeMockServerPlayerInLevel();var grind=new net.minecraft.world.inventory.GrindstoneMenu(1,player.getInventory());grind.getSlot(0).set(first.copy());grind.getSlot(1).set(second.copy());h.assertTrue(grind.getSlot(2).getItem().isEmpty(),"No two-tool grindstone merge");
        var anvil=new net.minecraft.world.inventory.AnvilMenu(2,player.getInventory());anvil.getSlot(0).set(first.copy());anvil.getSlot(1).set(second.copy());h.assertTrue(anvil.getSlot(2).getItem().isEmpty(),"No donor assembly lost in anvil");h.succeed();
    }
    @GameTest public void jsonMaterialCreatesPartsAndRolesAreNotFixed(GameTestHelper h){
        var iron=ToolDefinitions.find("iron","pickaxe_head",false);h.assertTrue(iron!=null&&ToolParts.item(iron.item().toString()) instanceof ToolPartItem,"Iron head registered from its own JSON profile");
        var ironTool=ToolParts.assemble(item("iron_pickaxe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);h.assertValueEqual(ironTool.getMaxDamage(),250,"Material JSON controls life");
        var saved=new ItemStack[]{ItemStack.EMPTY};var original=ToolDefinitions.template("mattock");var json=com.google.gson.JsonParser.parseString("{\"id\":\"mattock\",\"behavior\":\"pickaxe\",\"parts\":[{\"id\":\"pick\",\"category\":\"head\",\"forms\":[\"pickaxe_head\"],\"primary\":true},{\"id\":\"axe\",\"category\":\"head\",\"forms\":[\"axe_head\"]},{\"id\":\"spade\",\"category\":\"head\",\"forms\":[\"shovel_head\"]},{\"id\":\"handle\",\"category\":\"handle\"},{\"id\":\"binding\",\"category\":\"binding\"},{\"id\":\"wrap\",\"category\":\"wrap\",\"optional\":true}]}").getAsJsonObject();
        try{
            var schema=ToolDefinitions.parseTemplate(json);ToolDefinitions.registerTemplate(schema);
            var composed=ToolParts.assemble(schema,java.util.List.of(item("copper_pickaxe_head"),item("iron_axe_head"),item("bronze_shovel_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY));
            saved[0]=composed;h.assertTrue(ModularToolItem.configured(composed),"Three different heads compose without a new Java item implementation");h.assertValueEqual(ToolParts.components(composed).size(),6,"All JSON roles retained");
            h.assertFalse(composed.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Iron axe cannot upgrade copper pick head");h.assertTrue(composed.getDestroySpeed(Blocks.DIRT.defaultBlockState())>1,"Secondary shovel works");
            var ops=h.getLevel().registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE);var encoded=ItemStack.CODEC.encodeStart(ops,composed).getOrThrow();var restored=ItemStack.CODEC.parse(ops,encoded).getOrThrow();h.assertTrue(ItemStack.isSameItemSameComponents(composed,restored),"Arbitrary composition round-trips native serialization");
            composed.setDamageValue(60);var before=ToolParts.components(composed);var repaired=ToolParts.repair(composed,new ItemStack(Items.COPPER_INGOT));var after=ToolParts.components(repaired);
            h.assertTrue(ToolParts.wear(after.get(1)).equals(ToolParts.wear(before.get(1))),"Repairing copper does not heal iron head");h.assertTrue(ToolParts.wear(after.get(2)).equals(ToolParts.wear(before.get(2))),"Repairing copper does not heal bronze head");
        }finally{ToolDefinitions.registerTemplate(original);}h.assertValueEqual(ToolParts.components(saved[0]).size(),6,"Saved schema cannot truncate parts after the current definition changes");h.succeed();
    }
    @GameTest public void profileShapingAndCountedRolesWork(GameTestHelper h){
        var schema=ToolDefinitions.template("hammer");var plan=ToolParts.shaping(new ItemStack(Items.IRON_INGOT,4),schema);h.assertTrue(plan!=null,"Iron custom head has a reachable profile-driven shaping route");h.assertValueEqual(plan.count(),4,"Three material units plus one unit shaping loss");
        var table=table(h);table.copper.selection=ToolDefinitions.index("hammer");table.setItem(0,new ItemStack(Items.IRON_INGOT,4));table.copper.queued=1;for(int i=0;i<40;i++)ToolAssembly.tick(h.getLevel(),table);h.assertTrue(table.getItem(0).isEmpty(),"Actual shaping consumes the full declared cost");h.assertTrue(ToolParts.metal(table.getItem(3)).equals("iron"),"No copper hardcoding in production");
        var raw=com.google.gson.JsonParser.parseString("{\"id\":\"mattock\",\"parts\":[{\"id\":\"heads\",\"category\":\"head\",\"forms\":[\"pickaxe_head\"],\"count\":5,\"primary\":true},{\"id\":\"handle\",\"category\":\"handle\"},{\"id\":\"binding\",\"category\":\"binding\"}]}").getAsJsonObject();var counted=ToolDefinitions.parseTemplate(raw);h.assertValueEqual(counted.roles().size(),7,"Count is read from JSON, not fixed slot names");
        var parts=new java.util.ArrayList<ItemStack>();for(int i=0;i<5;i++)parts.add(item("copper_pickaxe_head"));parts.add(item("wooden_handle"));parts.add(item("cord_binding"));var tool=ToolParts.assemble(counted,parts);h.assertValueEqual(ToolParts.components(tool).size(),7,"Five-head composition preserved without global schema replacement");h.succeed();
    }
    @GameTest public void finishingAssemblyAndRepairConsumeRealParts(GameTestHelper h){
        var m=table(h);h.assertValueEqual(m.getContainerSize(),io.github.kpaakeh.smog.copper.ToolDefinitions.inventorySize(),"Dedicated part slots persist");m.setItem(0,item("copper_cast_pickaxe_head"));m.copper.queued=1;
        for(int i=0;i<40;i++)ToolAssembly.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(0).isEmpty(),"Finishing consumes casting");h.assertTrue(CopperWorkshops.is(m.getItem(3),"smog:copper_pickaxe_head"),"Finished head produced");
        m.setItem(0,m.removeItemNoUpdate(3));m.setItem(1,item("wooden_handle"));m.setItem(2,item("cord_binding"));m.copper.queued=1;
        for(int i=0;i<40;i++)ToolAssembly.tick(h.getLevel(),m);
        h.assertTrue(ModularToolItem.configured(m.getItem(3)),"Whole working tool assembled");h.assertTrue(m.getItem(0).isEmpty()&&m.getItem(1).isEmpty()&&m.getItem(2).isEmpty(),"Parts consumed exactly once");
        var tool=m.removeItemNoUpdate(3);tool.setDamageValue(120);m.setItem(5,tool);m.setItem(0,new ItemStack(Items.COPPER_INGOT));m.copper.queued=1;
        for(int i=0;i<40;i++)ToolAssembly.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(3).getDamageValue(),70,"One ingot repairs a quarter of 200 life");h.assertTrue(m.getItem(0).isEmpty()&&m.getItem(5).isEmpty(),"Repair consumes ingot and moves existing assembly");h.succeed();
    }
    @GameTest public void headEnchantmentsStayWithTheHead(GameTestHelper h){
        var tool=build("wooden_handle");var enchantment=h.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(net.minecraft.world.item.enchantment.Enchantments.UNBREAKING);tool.enchant(enchantment,1);
        var head=ToolParts.extract(tool,0);var retained=ToolParts.assemble(head,item("reinforced_handle"),item("cord_binding"),ItemStack.EMPTY);
        h.assertTrue(retained.get(DataComponents.ENCHANTMENTS).equals(tool.get(DataComponents.ENCHANTMENTS)),"Handle changes retain head enchantments");
        var replacement=ToolParts.assemble(item("bronze_pickaxe_head"),ToolParts.extract(tool,1),ToolParts.extract(tool,2),ItemStack.EMPTY);
        h.assertTrue(replacement.getEnchantments().isEmpty(),"New head cannot copy old enchantments");h.assertFalse(head.getEnchantments().isEmpty(),"Old enchanted head remains the sole carrier");h.succeed();
    }
}
