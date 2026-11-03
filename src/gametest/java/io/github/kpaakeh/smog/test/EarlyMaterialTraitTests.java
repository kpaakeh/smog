package io.github.kpaakeh.smog.test;
import java.util.*;
import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;
public final class EarlyMaterialTraitTests {
 private static ItemStack tool(String type,String material){return ToolParts.example(ToolDefinitions.template(type),material);}
 private static ItemStack wrapped(String wrap){var parts=new ArrayList<>(ToolParts.components(tool("pickaxe","copper")));parts.set(3,new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.parse("smog:"+wrap+"_wrap"))));return ToolParts.assemble(ToolDefinitions.template("pickaxe"),parts);}
 @GameTest public void stoneTinAndLeadHaveDistinctBoundedCombatBehaviors(GameTestHelper h){
  var p=h.makeMockServerPlayer(GameType.SURVIVAL);p.setPos(Vec3.atCenterOf(h.absolutePos(new BlockPos(1,2,1))));var target=(LivingEntity)BuiltInRegistries.ENTITY_TYPE.getValue(Identifier.withDefaultNamespace("cow")).create(h.getLevel(),EntitySpawnReason.COMMAND);target.setPos(Vec3.atCenterOf(h.absolutePos(new BlockPos(3,2,1))));h.getLevel().addFreshEntity(target);
  var stone=tool("axe","stone");ToolTraits.hit(stone,target,p);h.assertTrue(target.getDeltaMovement().x>0,"Stone impact pushes an actual creature away");target.setDeltaMovement(Vec3.ZERO);ToolTraits.hit(stone,target,p);h.assertTrue(target.getDeltaMovement().lengthSqr()==0,"Impact has a real cooldown");
  var tin=tool("axe","tin");target.setRemainingFireTicks(100);ToolTraits.hit(tin,target,p);h.assertFalse(target.isOnFire(),"Tin extinguishes a burning target");target.setRemainingFireTicks(100);ToolTraits.hit(tin,target,p);h.assertTrue(target.isOnFire(),"Quenching cannot bypass its cooldown");
  var lead=tool("axe","lead");ToolTraits.hit(lead,target,p);h.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.WEAKNESS),"Lead weakens rather than duplicating bronze slow");
  var other=h.makeMockServerPlayer(GameType.SURVIVAL);ToolTraits.hit(lead,other,p);h.assertFalse(other.hasEffect(net.minecraft.world.effect.MobEffects.WEAKNESS),"Material effects remain excluded from PvP");h.succeed();
 }
 @GameTest public void woodenHoeHydratesOnlyActuallyTilledSoil(GameTestHelper h){
  var p=h.makeMockServerPlayer(GameType.SURVIVAL);var pos=h.absolutePos(new BlockPos(2,2,2));h.getLevel().setBlockAndUpdate(pos,Blocks.DIRT.defaultBlockState());h.getLevel().setBlockAndUpdate(pos.above(),Blocks.AIR.defaultBlockState());var stack=tool("hoe","wood");p.setItemInHand(InteractionHand.MAIN_HAND,stack);
  var use=new UseOnContext(p,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(pos),Direction.UP,pos,false));h.assertTrue(stack.getItem().useOn(use).consumesAction(),"Normal hoe action succeeds");h.assertTrue(h.getLevel().getBlockState(pos).is(Blocks.FARMLAND),"Vanilla tilling is preserved");h.assertValueEqual(h.getLevel().getBlockState(pos).getValue(FarmlandBlock.MOISTURE),7,"Wood cultivation hydrates newly tilled soil");h.succeed();
 }
 @GameTest public void ironAttractsOnlyEligibleDropsAfterSuitableWorkWhileEquipped(GameTestHelper h){
  var p=h.makeMockServerPlayer(GameType.SURVIVAL);var at=Vec3.atCenterOf(h.absolutePos(new BlockPos(2,3,2)));p.setPos(at);var iron=tool("pickaxe","iron");p.setItemInHand(InteractionHand.MAIN_HAND,iron);
  var drop=new ItemEntity(h.getLevel(),at.x+2,at.y,at.z,new ItemStack(Items.DIAMOND,4));drop.setNoPickUpDelay();drop.setDeltaMovement(Vec3.ZERO);h.getLevel().addFreshEntity(drop);
  ToolTraits.heldTick(iron,h.getLevel(),p);h.assertTrue(drop.getDeltaMovement().lengthSqr()==0,"Idle iron is not a permanent magnet");ToolTraits.mined(iron,h.getLevel(),Blocks.STONE.defaultBlockState());ToolTraits.heldTick(iron,h.getLevel(),p);h.assertTrue(drop.getDeltaMovement().x<0,"Suitable work attracts a nearby actual drop");h.assertValueEqual(drop.getItem().getCount(),4,"Attraction creates no items");
  drop.setDeltaMovement(Vec3.ZERO);drop.setPickUpDelay(40);ToolTraits.heldTick(iron,h.getLevel(),p);h.assertTrue(drop.getDeltaMovement().lengthSqr()==0,"Pickup delay is respected");drop.setNoPickUpDelay();p.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);ToolTraits.heldTick(iron,h.getLevel(),p);h.assertTrue(drop.getDeltaMovement().lengthSqr()==0,"Stored tools grant no attraction");h.succeed();
 }
 @GameTest public void woolInsulationRequiresAnEquippedUsableAssembly(GameTestHelper h){
  var p=h.makeMockServerPlayer(GameType.SURVIVAL);var wool=wrapped("wool");p.setTicksFrozen(100);ToolTraits.heldTick(wool,h.getLevel(),p);h.assertValueEqual(p.getTicksFrozen(),100,"Stored wool has no passive effect");p.setItemInHand(InteractionHand.MAIN_HAND,wool);ToolTraits.heldTick(wool,h.getLevel(),p);h.assertValueEqual(p.getTicksFrozen(),98,"Equipped wool reduces actual freezing");wool.setDamageValue(wool.getMaxDamage());ToolTraits.heldTick(wool,h.getLevel(),p);h.assertValueEqual(p.getTicksFrozen(),98,"A spent tool grants no insulation");h.succeed();
 }
}
