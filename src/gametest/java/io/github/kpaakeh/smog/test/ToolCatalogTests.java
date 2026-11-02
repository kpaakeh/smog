package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

public final class ToolCatalogTests {
    @GameTest public void catalogHasExactlyRequestedToolsAndNoChiselOrMattock(GameTestHelper h){
        var expected=java.util.List.of("pickaxe","axe","saw","shovel","hammer","hoe","sword","katana","machete","sickle");
        h.assertTrue(ToolDefinitions.templates().stream().map(ToolDefinitions.Template::id).toList().equals(expected),"Registered schemas match the requested ten tools in order");
        for(var schema:ToolDefinitions.templates())for(String material:java.util.List.of("wood","stone","copper","bronze")){
            var tool=ToolParts.example(schema,material);
            h.assertTrue(ModularToolItem.configured(tool)&&!ModularToolItem.broken(tool),"Actual assembly exists for "+material+" "+schema.id());
            h.assertTrue(ToolParts.components(tool).stream().filter(ToolParts::head).allMatch(part->ToolParts.metal(part).equals(material)),"Example uses the selected material, not a fallback");
        }
        h.succeed();
    }
    @GameTest public void katanaAndMacheteHaveOppositeActualAttackTradeoffs(GameTestHelper h){
        var sword=ToolParts.example(ToolDefinitions.template("sword"),"iron");
        var katana=ToolParts.example(ToolDefinitions.template("katana"),"iron");
        var machete=ToolParts.example(ToolDefinitions.template("machete"),"iron");
        var damage=Attributes.ATTACK_DAMAGE;var speed=Attributes.ATTACK_SPEED;
        double swordDamage=sword.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),EquipmentSlot.MAINHAND);
        double swordSpeed=sword.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),EquipmentSlot.MAINHAND);
        h.assertTrue(katana.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),EquipmentSlot.MAINHAND)<swordDamage,"Katana deals less native attribute damage than an equivalent sword");
        h.assertTrue(katana.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),EquipmentSlot.MAINHAND)>swordSpeed,"Katana attacks faster through native attributes");
        h.assertTrue(machete.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),EquipmentSlot.MAINHAND)>swordDamage,"Machete trades speed for greater damage");
        h.assertTrue(machete.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),EquipmentSlot.MAINHAND)<swordSpeed,"Machete's actual attack tempo is slower");h.succeed();
    }
    @GameTest public void workingStoneHammerIsARepairableModularAssembly(GameTestHelper h){
        var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");
        h.assertTrue(ToolParts.hammer(hammer)&&hammer.has(DataComponents.CONTAINER),"Working hammer carries replaceable parts");
        hammer.setDamageValue(hammer.getMaxDamage());
        h.assertFalse(ToolParts.hammer(hammer),"Spent modular hammer cannot work");
        h.assertTrue(ToolParts.hammerAssembly(hammer),"Spent hammer keeps its identity for mounting and repair");h.succeed();
    }
}
