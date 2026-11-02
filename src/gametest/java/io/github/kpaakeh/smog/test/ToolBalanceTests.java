package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

/** Cross-material assembly and actual attributes, not a second copy of the balance table. */
public final class ToolBalanceTests {
    private static ItemStack stack(ToolDefinitions.Part p){return new ItemStack(ToolParts.item(p.item().toString()));}
    private static double speed(ItemStack tool){var a=Attributes.ATTACK_SPEED;return tool.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(a,a.value().getDefaultValue(),EquipmentSlot.MAINHAND);}
    @GameTest public void everyHeadAndRodMaterialPairAssemblesAndRetainsItsOwnIdentity(GameTestHelper h){
        var heads=ToolDefinitions.parts().stream().filter(p->!p.cast()&&p.form().id().equals("pickaxe_head")).toList();
        var rods=ToolDefinitions.parts().stream().filter(p->p.form().category().equals("handle")).toList();
        var binding=new ItemStack(ToolParts.item("cord_binding"));
        h.assertTrue(heads.size()>=37&&rods.size()==heads.size(),"All head materials have an ordinary rod, including copper, stone and wood");
        for(var head:heads)for(var rod:rods){
            var tool=ToolParts.assemble(ToolDefinitions.template("pickaxe"),java.util.List.of(stack(head),stack(rod),binding,ItemStack.EMPTY));
            h.assertTrue(ModularToolItem.configured(tool),"Mixed materials assemble: "+head.material().id()+" / "+rod.material().id());
            var parts=ToolParts.components(tool);
            h.assertTrue(ToolDefinitions.part(parts.get(0)).material().id().equals(head.material().id())&&ToolDefinitions.part(parts.get(1)).material().id().equals(rod.material().id()),"Assembly preserves both selected materials without substitution");
            h.assertTrue(tool.getMaxDamage()>0&&Float.isFinite(tool.getDestroySpeed(Blocks.STONE.defaultBlockState()))&&speed(tool)>0,"Mixed material stats remain finite and usable");
        }
        for(var schema:ToolDefinitions.templates()){
            var role=schema.roles().stream().filter(r->r.category().equals("handle")).findFirst().orElseThrow();
            h.assertTrue(role.forms().equals(java.util.List.of("standard_handle")),"Every tool accepts exactly the same rod geometry");
        }
        h.succeed();
    }
    @GameTest public void areaToolsPayForUtilityAndEveryToolFamilyHasItsOwnStats(GameTestHelper h){
        var pick=ToolParts.example(ToolDefinitions.template("pickaxe"),"iron");
        var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"iron");
        var axe=ToolParts.example(ToolDefinitions.template("axe"),"iron");
        var saw=ToolParts.example(ToolDefinitions.template("saw"),"iron");
        h.assertTrue(hammer.getDestroySpeed(Blocks.STONE.defaultBlockState())<pick.getDestroySpeed(Blocks.STONE.defaultBlockState()),"Hammer's 3x3 utility trades off first-block speed");
        h.assertTrue(saw.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState())<axe.getDestroySpeed(Blocks.OAK_LOG.defaultBlockState()),"Whole-tree sawing is slower per first block than ordinary chopping");
        h.assertTrue(hammer.getMaxDamage()>pick.getMaxDamage()&&saw.getMaxDamage()>axe.getMaxDamage(),"Per-block wear is supported by finite larger heavy-tool reserves");
        h.assertTrue(speed(hammer)<speed(pick)&&speed(axe)<speed(pick),"Heavy work tools swing more slowly than a pickaxe");
        var sword=ToolParts.example(ToolDefinitions.template("sword"),"iron");var katana=ToolParts.example(ToolDefinitions.template("katana"),"iron");
        h.assertTrue(katana.getMaxDamage()<sword.getMaxDamage()&&speed(katana)>speed(sword),"Katana's faster cadence has a real durability tradeoff");
        h.succeed();
    }
    @GameTest public void ordinaryRodCastingAndShapingReturnNoMoreThanPaidMetal(GameTestHelper h){
        for(int metal=0;metal<MoltenMetalStorage.NAMES.length;metal++){
            var rod=CastingProcesses.hollowPart(metal);if(rod==null)continue;
            h.assertTrue(rod.form().id().equals("standard_handle"),"Casting never creates another rod variant");
            var one=stack(rod);h.assertValueEqual(ToolParts.melt(one)[1],ToolParts.units(rod),"One cast rod remelts to exactly its cost");
            var shaped=ToolParts.shaping(ItemStack.EMPTY,rod);
            h.assertTrue((long)shaped.result().getCount()*ToolParts.units(rod)<=(long)shaped.count()*MoltenMetalStorage.INGOT,"Shaping and remelting cannot multiply metal");
        }
        h.succeed();
    }
}
