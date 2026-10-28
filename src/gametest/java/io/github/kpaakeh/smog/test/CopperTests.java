package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.copper.CopperWorkshops;
import io.github.kpaakeh.smog.multiblock.complex.*;
import io.github.kpaakeh.smog.registry.ModBlocks;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public final class CopperTests {
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static void set(GameTestHelper h,BlockPos pos,Block block){h.getLevel().setBlock(pos,block.defaultBlockState(),Block.UPDATE_ALL);}
    private static MachineEntity hearth(GameTestHelper h,BlockPos origin,int width){
        int left=width/2;
        for(int x=-left;x<width-left;x++)for(int y=-1;y<=0;y++)for(int z=0;z<width;z++){
            if(x==0&&y==0&&z==0)continue;
            var b=y==-1?Blocks.COBBLESTONE:x==-left||x==width-left-1||z==0||z==width-1?Blocks.BRICKS:Blocks.AIR;
            set(h,origin.offset(x,y,z),b);
        }
        set(h,origin.offset(0,-1,1),block("solid_firebox"));set(h,origin.offset(0,0,1),block("ceramic_crucible"));
        set(h,origin.offset(-left,0,0),block("casting_spout"));set(h,origin.offset(left,0,0),block("air_intake"));
        if(!h.getLevel().getBlockState(origin).is(block("copper_hearth")))set(h,origin,block("copper_hearth"));
        return (MachineEntity)h.getLevel().getBlockEntity(origin);
    }
    @GameTest public void complexHearthResizesAndAcceptsFunctionalReplacements(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,origin,3);
        h.assertTrue(m.tryAssemble(),"Small hearth assembles");h.assertValueEqual(m.complexShape().width(),3,"Small width");h.assertValueEqual(CopperWorkshops.capacity(m),64,"Basic crucible = 16 ingots");
        m.copper.copper=40;m.disassemble();hearth(h,origin,5);set(h,origin.offset(0,0,1),block("large_ceramic_crucible"));set(h,origin.offset(1,0,1),block("ceramic_crucible"));
        h.assertTrue(m.tryAssemble(),"Larger hearth is the same machine type");h.assertValueEqual(m.complexShape().width(),5,"Width changed");h.assertValueEqual(CopperWorkshops.capacity(m),192,"Functional parts define capacity");h.assertValueEqual(m.copper.copper,40,"Stored metal survives resizing");
        m.disassemble();set(h,origin.offset(0,0,1),Blocks.AIR);set(h,origin.offset(1,0,1),Blocks.AIR);h.assertFalse(m.tryAssemble(),"No crucible is not a functioning hearth");h.assertValueEqual(m.copper.copper,40,"Invalid structure preserves contents");h.succeed();
    }
    @GameTest public void complexOwnershipPersistsAndRejectsOverlap(GameTestHelper h){
        var data=new ComplexFormationData();var a=new BlockPos(1,2,3);var b=new BlockPos(4,5,6);var part=new BlockPos(7,8,9);
        h.assertTrue(data.claim(a,java.util.List.of(a,part)),"Initial exclusive claim");h.assertFalse(data.claim(b,java.util.List.of(b,part)),"Shared functional block rejected");
        var encoded=ComplexFormationData.TYPE.codec().encodeStart(com.mojang.serialization.JsonOps.INSTANCE,data).getOrThrow();var restored=ComplexFormationData.TYPE.codec().parse(com.mojang.serialization.JsonOps.INSTANCE,encoded).getOrThrow();
        h.assertTrue(restored.owner(part).equals(a),"Ownership survives save/load");restored.release(a);h.assertTrue(restored.claim(b,java.util.List.of(b,part)),"Disassembly releases ownership");h.succeed();
    }
    @GameTest public void bronzeAndCastingConserveMetalAndKeepMolds(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Working hearth");m.copper.heat=1000;m.copper.copper=12;m.copper.tin=4;
        h.assertTrue(CopperWorkshops.button(m,23),"Bronze action");h.assertValueEqual(m.copper.bronze,16,"3 copper + 1 tin = 4 bronze");h.assertValueEqual(m.copper.total(),16,"No alloy multiplication");
        var tablePos=origin.offset(-1,0,-1);set(h,tablePos,block("casting_table"));var table=(MachineEntity)h.getLevel().getBlockEntity(tablePos);table.copper.selection=2;table.setItem(1,new ItemStack(item("ingot_mold")));
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).is(item("bronze_ingot")),"Connected table casts bronze");h.assertTrue(table.getItem(1).is(item("ingot_mold")),"Mold is reusable");h.assertValueEqual(m.copper.total()+table.getItem(3).getCount()*4,16,"Casting conserves quarter-ingot units");h.succeed();
    }
}
