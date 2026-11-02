package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;

public final class ToolMaterialInteropTests {
    @GameTest public void commonTaggedIngotsShapeRepairAndMeltAsTheSameMaterial(GameTestHelper h){
        var foreign=new ItemStack(ToolParts.item("test_interop_iron_ingot"));var rod=ToolDefinitions.find("iron","standard_handle",false);
        h.assertTrue(ToolParts.materialIngredient(foreign,rod.material()),"A noncanonical item in c:ingots/iron is valid iron");
        var shaped=ToolParts.shaping(foreign,rod);
        h.assertValueEqual(shaped.count(),1,"Tagged ingot pays the same input cost as vanilla iron");h.assertValueEqual(shaped.result().getCount(),2,"One full ingot produces two half-ingot rods");
        h.assertValueEqual(CopperWorkshops.melt(shaped.result())[1]*shaped.result().getCount(),CopperWorkshops.melt(foreign)[1],"Cross-mod shaping and melting never create metal");
        var tool=ToolParts.example(ToolDefinitions.template("pickaxe"),"iron");tool.setDamageValue(100);
        var repaired=ToolParts.repair(tool,foreign);h.assertFalse(repaired.isEmpty(),"Tagged metal repairs its matching head");h.assertTrue(repaired.getDamageValue()<100,"Repair actually restores paid durability");
        h.assertTrue(ToolParts.repairIngredients().contains(foreign.getItem()),"Material ingredient discovery includes the tag alternative");h.succeed();
    }
    @GameTest public void ingotCompatibilityDoesNotAcceptNuggetsRawOreOrWrongMetal(GameTestHelper h){
        var iron=ToolDefinitions.find("iron","standard_handle",false).material();
        for(Item item:new Item[]{Items.IRON_NUGGET,Items.RAW_IRON,Items.IRON_BLOCK,Items.GOLD_INGOT})h.assertFalse(ToolParts.materialIngredient(new ItemStack(item),iron),"Only one-ingot equivalents are accepted: "+item);
        var tool=ToolParts.example(ToolDefinitions.template("pickaxe"),"iron");tool.setDamageValue(100);var before=tool.copy();
        h.assertTrue(ToolParts.repair(tool,new ItemStack(Items.GOLD_INGOT)).isEmpty(),"Wrong metal cannot repair iron");h.assertTrue(ItemStack.matches(tool,before),"Rejected compatibility checks never alter the tool");h.succeed();
    }
}
