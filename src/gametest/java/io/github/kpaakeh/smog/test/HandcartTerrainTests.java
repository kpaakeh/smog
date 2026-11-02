package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;

public final class HandcartTerrainTests {
    private static HandcartEntity cart(GameTestHelper h,BlockPos base){
        var level=h.getLevel();for(var pos:BlockPos.betweenClosed(base.offset(0,-1,0),base.offset(12,-1,7)))level.setBlock(pos,Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        var cart=CopperTransport.HANDCART.create(level,EntitySpawnReason.COMMAND);cart.setInitialPos(base.getX()+1.5,base.getY(),base.getZ()+3.5);cart.setItem(53,new ItemStack(Items.COPPER_INGOT,64));
        cart.pull(h.makeMockServerPlayer(GameType.SURVIVAL));level.addFreshEntity(cart);return cart;
    }
    private static void move(HandcartEntity cart,int ticks){for(int n=0;n<ticks;n++)cart.move(MoverType.SELF,new Vec3(.2,-.08,0));}
    @GameTest public void pulledLoadedCartStepsOntoSlabsAndFullBlocks(GameTestHelper h){
        var base=h.absolutePos(new BlockPos(2,20,2));var cart=cart(h,base);
        for(int x=4;x<7;x++)for(int z=0;z<8;z++)h.getLevel().setBlock(base.offset(x,0,z),Blocks.STONE_SLAB.defaultBlockState(),Block.UPDATE_ALL);
        for(int x=7;x<11;x++)for(int z=0;z<8;z++)h.getLevel().setBlock(base.offset(x,0,z),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        move(cart,40);h.assertTrue(cart.getX()>base.getX()+8,"Native collision movement climbs slab then full block without teleporting");
        h.assertTrue(cart.getY()>=base.getY()+.99,"Cart is standing on the raised road");h.assertValueEqual(cart.getItem(53).getCount(),64,"Stepping preserves the loaded inventory");h.succeed();
    }
    @GameTest public void pulledCartCannotClimbThroughTallWall(GameTestHelper h){
        var base=h.absolutePos(new BlockPos(2,20,2));var cart=cart(h,base);
        for(int z=0;z<8;z++)for(int y=0;y<3;y++)h.getLevel().setBlock(base.offset(4,y,z),Blocks.STONE.defaultBlockState(),Block.UPDATE_ALL);
        move(cart,30);h.assertTrue(cart.getX()<base.getX()+4,"Stepping cannot tunnel through a tall wall");h.assertTrue(cart.getY()<base.getY()+.1,"A tall wall does not lift the cart through blocks");
        h.assertValueEqual(cart.getItem(53).getCount(),64,"Blocked movement does not lose cargo");h.succeed();
    }
}
