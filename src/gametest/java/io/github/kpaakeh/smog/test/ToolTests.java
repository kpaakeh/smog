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
    @GameTest public void stoneWorkingHammerBootstrapStartsFromCobblestoneWithoutAHammer(GameTestHelper h){
        var bench=table(h);bench.copper.selection=ToolDefinitions.index("hammer");bench.setItem(0,new ItemStack(Items.COBBLESTONE,3));
        h.assertFalse(ToolAssembly.requiresHammer(bench),"Shaping the first stone hammer head cannot require a metalworking hammer");
        h.assertTrue(ToolAssembly.button(bench,22),"The first head can be shaped by hand from actual cobblestone");
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(bench.getItem(3).is(ToolParts.item("stone_hammer_head"))&&bench.getItem(0).isEmpty(),"The declared stone cost produces one finished working-hammer head");
        bench.setItem(0,bench.removeItemNoUpdate(3));bench.setItem(1,item("wooden_handle"));bench.setItem(2,item("cord_binding"));h.assertTrue(ToolAssembly.button(bench,22),"The nonmetal head, handle and binding assemble without a circular requirement");
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(ToolParts.hammer(bench.getItem(3)),"The resulting paid modular hammer can perform later metalwork");h.succeed();
    }
    @GameTest public void requestedToolSetAndWeaponTradeoffsAreNativeAttributes(GameTestHelper h){
        var expected=java.util.Set.of("pickaxe","axe","saw","shovel","hammer","hoe","sword","katana","machete","sickle");
        h.assertTrue(ToolDefinitions.templates().stream().map(ToolDefinitions.Template::id).collect(java.util.stream.Collectors.toSet()).equals(expected),"Exactly the requested ten tool templates exist");
        var sword=ToolParts.example(ToolDefinitions.template("sword"),"copper");var katana=ToolParts.example(ToolDefinitions.template("katana"),"copper");var machete=ToolParts.example(ToolDefinitions.template("machete"),"copper");
        var speed=net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED;var damage=net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;var hand=net.minecraft.world.entity.EquipmentSlot.MAINHAND;
        double normalSpeed=sword.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),hand),fast=katana.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),hand),slow=machete.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(speed,speed.value().getDefaultValue(),hand);
        double normalDamage=sword.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),hand),weak=katana.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),hand),strong=machete.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(damage,damage.value().getDefaultValue(),hand);
        h.assertTrue(fast>normalSpeed&&normalSpeed>slow,"Katana attacks faster than a sword; machete attacks slower");h.assertTrue(weak<normalDamage&&normalDamage<strong,"Katana trades damage for speed and machete makes the reverse trade");h.succeed();
    }
    @GameTest public void nonmetalWorkingHammerAndWoodenToolsAssembleWithoutAHammer(GameTestHelper h){
        var bench=table(h);bench.copper.selection=ToolDefinitions.index("hammer");bench.setItem(0,item("stone_hammer_head"));bench.setItem(1,item("wooden_handle"));bench.setItem(2,item("cord_binding"));
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;
        ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertValueEqual(bench.copper.queued,1,"The first stone working hammer requires no pre-existing hammer");
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        var hammer=bench.removeItemNoUpdate(3);h.assertTrue(ModularToolItem.configured(hammer)&&ToolParts.hammer(hammer),"The working hammer itself is a real usable modular assembly");
        h.assertTrue(bench.getItem(0).isEmpty()&&bench.getItem(1).isEmpty()&&bench.getItem(2).isEmpty(),"Hand assembly consumes each component once");
        bench.copper.selection=ToolDefinitions.index("pickaxe");bench.setItem(0,item("wood_pickaxe_head"));bench.setItem(1,item("wooden_handle"));bench.setItem(2,item("cord_binding"));
        h.assertTrue(ToolAssembly.button(bench,22),"GUI hand assembly is available for a nonmetal tool");
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(ModularToolItem.configured(bench.getItem(3))&&ToolParts.metal(ToolParts.extract(bench.getItem(3),0)).equals("wood"),"GUI produces the paid wooden tool without using the working hammer");
        h.assertValueEqual(hammer.getDamageValue(),0,"A hammer is not charged for hand assembly");h.succeed();
    }
    @GameTest public void anyMetalComponentRequiresAWorkingModularHammer(GameTestHelper h){
        var bench=table(h);bench.copper.selection=ToolDefinitions.index("hammer");bench.setItem(0,item("stone_hammer_head"));bench.setItem(1,item("wooden_handle"));bench.setItem(2,item("copper_clamp"));
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;
        ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertValueEqual(bench.copper.queued,0,"A metal clamp requires hammering even when the head and handle are nonmetal");
        h.assertFalse(ToolAssembly.button(bench,22),"GUI cannot bypass the metal-component requirement");
        h.assertTrue(bench.getItem(0).getCount()==1&&bench.getItem(1).getCount()==1&&bench.getItem(2).getCount()==1,"Rejected hand assembly preserves all components");
        var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,hammer);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertValueEqual(bench.copper.queued,1,"The assembled stone hammer starts the metal clamp operation");h.assertValueEqual(hammer.getDamageValue(),1,"One assembly costs one real working-hammer use");
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(ModularToolItem.configured(bench.getItem(3)),"Hammered parts produce the configured tool");h.succeed();
    }
    @GameTest public void modularHammerCanMountRepairAndDisassembleThroughDirectUse(GameTestHelper h){
        var bench=table(h);var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;
        var hammer=ToolParts.assemble(item("copper_hammer_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);hammer.setDamageValue(70);hammer.set(DataComponents.CUSTOM_NAME,net.minecraft.network.chat.Component.literal("Favorite hammer"));
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,hammer);
        ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertTrue(bench.getItem(5).isEmpty()&&!player.getMainHandItem().isEmpty(),"Ordinary hammer use still means a strike, not mounting");
        player.setShiftKeyDown(true);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertTrue(player.getMainHandItem().isEmpty()&&ModularToolItem.configured(bench.getItem(5)),"Sneaking mounts the actual modular hammer for service");
        player.setShiftKeyDown(false);player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,new ItemStack(Items.COPPER_INGOT));ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertTrue(bench.getItem(0).is(Items.COPPER_INGOT)&&player.getMainHandItem().isEmpty(),"Direct repair ingredient insertion is still available");
        var stoneHammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,stoneHammer);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        for(int n=0;n<40;n++)ToolAssembly.tick(h.getLevel(),bench);
        var repaired=bench.removeItemNoUpdate(3);h.assertValueEqual(repaired.getDamageValue(),20,"Mounted hammer repairs exactly one ingot's worth");h.assertTrue(repaired.has(DataComponents.CUSTOM_NAME),"Repair retains its name");
        player.setShiftKeyDown(true);player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,repaired);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,stoneHammer);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);
        h.assertTrue(bench.getItem(5).isEmpty()&&ToolParts.shape(bench.getItem(0)).equals("hammer")&&!bench.getItem(1).isEmpty()&&!bench.getItem(2).isEmpty(),"Shift with a working hammer still dismantles an already mounted assembly");
        h.assertValueEqual(stoneHammer.getDamageValue(),1,"Mounting and dismantling do not charge an extra strike");h.succeed();
    }
    @GameTest public void finishingNamedCastsRetainsIdentityAndRejectsMixedOutput(GameTestHelper h){
        var bench=table(h);var casts=item("copper_cast_pickaxe_head");casts.setCount(2);casts.set(DataComponents.CUSTOM_NAME,net.minecraft.network.chat.Component.literal("Mine crew"));casts.set(DataComponents.REPAIR_COST,7);bench.setItem(0,casts);bench.setItem(3,item("copper_pickaxe_head"));
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);((net.minecraft.server.level.ServerPlayer)player).connection=h.makeMockServerPlayerInLevel().connection;var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,hammer);
        ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);h.assertValueEqual(bench.copper.queued,0,"Named results cannot silently merge into a plain output stack");h.assertValueEqual(hammer.getDamageValue(),0,"Rejected mixed output costs no strike");
        bench.setItem(3,ItemStack.EMPTY);ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.MAIN_HAND);for(int n=0;n<80;n++)ToolAssembly.tick(h.getLevel(),bench);
        var finished=bench.getItem(3);h.assertValueEqual(finished.getCount(),2,"Finite finishing batch keeps both named heads");h.assertTrue(net.minecraft.network.chat.Component.literal("Mine crew").equals(finished.get(DataComponents.CUSTOM_NAME)),"Finishing carries the chosen cast name");h.assertValueEqual(finished.getOrDefault(DataComponents.REPAIR_COST,0),7,"Finishing cannot reset prior anvil work");h.succeed();
    }
    @GameTest public void offhandHammerCanFinishAssemblyWithoutTouchingMainHand(GameTestHelper h){
        var bench=table(h);bench.copper.selection=ToolDefinitions.index("pickaxe");bench.setItem(0,item("copper_pickaxe_head"));bench.setItem(1,item("wooden_handle"));bench.setItem(2,item("cord_binding"));
        // InLevel's native mock overrides gameMode() to CREATIVE even after setGameMode.
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        var main=new ItemStack(Items.DIAMOND,7);var hammer=ToolParts.example(ToolDefinitions.template("hammer"),"stone");hammer.setDamageValue(hammer.getMaxDamage()-1);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,main);player.setItemInHand(net.minecraft.world.InteractionHand.OFF_HAND,hammer);
        h.assertFalse(player.isCreative(),"The hammer fixture uses survival mode");
        h.assertTrue(hammer.isDamageableItem()&&hammer.getDamageValue()==hammer.getMaxDamage()-1,"The fixture holds a damageable hammer with one use remaining");
        h.assertFalse(ToolAssembly.preview(bench).isEmpty(),"The pickaxe components form a valid recipe before the strike");
        ToolAssembly.use(h.getLevel(),bench,player,net.minecraft.world.InteractionHand.OFF_HAND);
        h.assertValueEqual(bench.copper.queued,1,"The offhand hammer queues the valid recipe");
        h.assertTrue(ModularToolItem.broken(player.getOffhandItem())&&!player.getOffhandItem().isEmpty(),"Last hammer use preserves the spent offhand assembly; remaining damage="+player.getOffhandItem().getDamageValue()+", max="+player.getOffhandItem().getMaxDamage()+", count="+player.getOffhandItem().getCount()+", infinite="+player.getAbilities().instabuild);
        h.assertTrue(player.getMainHandItem().is(Items.DIAMOND)&&player.getMainHandItem().getCount()==7,"Hammer break preserves the unrelated main-hand stack");
        for(int tick=0;tick<40;tick++)ToolAssembly.tick(h.getLevel(),bench);
        h.assertTrue(ModularToolItem.configured(bench.getItem(3)),"The final hammer use still completes the queued tool");h.succeed();
    }
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
        var saved=new ItemStack[]{ItemStack.EMPTY};var json=com.google.gson.JsonParser.parseString("{\"id\":\"pickaxe\",\"behavior\":\"pickaxe\",\"parts\":[{\"id\":\"pick\",\"category\":\"head\",\"forms\":[\"pickaxe_head\"],\"primary\":true},{\"id\":\"axe\",\"category\":\"head\",\"forms\":[\"axe_head\"]},{\"id\":\"spade\",\"category\":\"head\",\"forms\":[\"shovel_head\"]},{\"id\":\"handle\",\"category\":\"handle\"},{\"id\":\"binding\",\"category\":\"binding\"},{\"id\":\"wrap\",\"category\":\"wrap\",\"optional\":true}]}").getAsJsonObject();
        {
            var schema=ToolDefinitions.parseTemplate(json);
            var composed=ToolParts.assemble(schema,java.util.List.of(item("copper_pickaxe_head"),item("iron_axe_head"),item("bronze_shovel_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY));
            saved[0]=composed;h.assertTrue(ModularToolItem.configured(composed),"Three different heads compose without a new Java item implementation");h.assertValueEqual(ToolParts.components(composed).size(),6,"All JSON roles retained");
            h.assertFalse(composed.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Iron axe cannot upgrade copper pick head");h.assertTrue(composed.getDestroySpeed(Blocks.DIRT.defaultBlockState())>1,"Secondary shovel works");
            var ops=h.getLevel().registryAccess().createSerializationContext(net.minecraft.nbt.NbtOps.INSTANCE);var encoded=ItemStack.CODEC.encodeStart(ops,composed).getOrThrow();var restored=ItemStack.CODEC.parse(ops,encoded).getOrThrow();h.assertTrue(ItemStack.isSameItemSameComponents(composed,restored),"Arbitrary composition round-trips native serialization");
            composed.setDamageValue(60);var before=ToolParts.components(composed);var repaired=ToolParts.repair(composed,new ItemStack(Items.COPPER_INGOT));var after=ToolParts.components(repaired);
            h.assertTrue(ToolParts.wear(after.get(1)).equals(ToolParts.wear(before.get(1))),"Repairing copper does not heal iron head");h.assertTrue(ToolParts.wear(after.get(2)).equals(ToolParts.wear(before.get(2))),"Repairing copper does not heal bronze head");
        }h.assertValueEqual(ToolParts.components(saved[0]).size(),6,"Saved schema cannot truncate parts after the current definition changes");h.succeed();
    }
    @GameTest public void profileShapingAndCountedRolesWork(GameTestHelper h){
        var schema=ToolDefinitions.template("hammer");var plan=ToolParts.shaping(new ItemStack(Items.IRON_INGOT,4),schema);h.assertTrue(plan!=null,"Iron custom head has a reachable profile-driven shaping route");h.assertValueEqual(plan.count(),4,"Three material units plus one unit shaping loss");
        var table=table(h);table.copper.selection=ToolDefinitions.index("hammer");table.setItem(0,new ItemStack(Items.IRON_INGOT,4));table.copper.queued=1;for(int i=0;i<40;i++)ToolAssembly.tick(h.getLevel(),table);h.assertTrue(table.getItem(0).isEmpty(),"Actual shaping consumes the full declared cost");h.assertTrue(ToolParts.metal(table.getItem(3)).equals("iron"),"No copper hardcoding in production");
        var raw=com.google.gson.JsonParser.parseString("{\"id\":\"pickaxe\",\"parts\":[{\"id\":\"heads\",\"category\":\"head\",\"forms\":[\"pickaxe_head\"],\"count\":5,\"primary\":true},{\"id\":\"handle\",\"category\":\"handle\"},{\"id\":\"binding\",\"category\":\"binding\"}]}").getAsJsonObject();var counted=ToolDefinitions.parseTemplate(raw);h.assertValueEqual(counted.roles().size(),7,"Count is read from JSON, not fixed slot names");
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
    @GameTest public void materialTraitsReactToWorkAndCombatWithoutChangingDrops(GameTestHelper h){
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var copper=build("wooden_handle");var state=Blocks.STONE.defaultBlockState();float initial=copper.getDestroySpeed(state);
        for(int i=0;i<4;i++)((ModularToolItem)copper.getItem()).mineBlock(copper,h.getLevel(),state,h.absolutePos(new BlockPos(1,2,1)),player);
        h.assertTrue(copper.getDestroySpeed(state)>initial,"Copper work hardening responds to successful suitable mining");
        h.assertFalse(copper.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Work hardening never raises harvest tier");
        var data=ModularToolItem.parts(copper);data.putLong("trait_work_until",h.getLevel().getGameTime()-1);copper.set(DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));ToolTraits.tick(copper,h.getLevel());
        h.assertTrue(Math.abs(copper.getDestroySpeed(state)-initial)<.00001,"Copper bonus expires after idle timeout");
        var target=(net.minecraft.world.entity.LivingEntity)BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("cow")).create(h.getLevel(),net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        var bronze=ToolParts.assemble(item("bronze_axe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);
        ((ModularToolItem)bronze.getItem()).hurtEnemy(bronze,target,player);
        h.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Bronze hit actually slows a mob");target.removeEffect(net.minecraft.world.effect.MobEffects.SLOWNESS);
        ((ModularToolItem)bronze.getItem()).hurtEnemy(bronze,target,player);
        h.assertFalse(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Bronze effect obeys its cooldown");
        var gold=ToolParts.assemble(item("gold_axe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);((ModularToolItem)gold.getItem()).hurtEnemy(gold,target,player);
        h.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.GLOWING),"Gold hit marks the mob");h.succeed();
    }
    @GameTest public void traitProfilesRejectFractionalCountersAndUnboundedBonuses(GameTestHelper h){
        for(String trait:java.util.List.of(
            "{\"id\":\"work_hardening\",\"max_stacks\":0.5}",
            "{\"id\":\"work_hardening\",\"timeout_ticks\":0.5}",
            "{\"id\":\"work_hardening\",\"max_stacks\":64,\"speed_per_stack\":1}",
            "{\"id\":\"staggering\",\"duration_ticks\":0.5}",
            "{\"id\":\"marking\",\"cooldown_ticks\":1.5}",
            "{\"id\":\"grip\",\"every\":1}",
            "{\"id\":\"grip\",\"every\":\"4\"}",
            "{\"id\":\"grip\",\"everry\":4}")){
            boolean rejected=false;try{ToolTraits.validate(com.google.gson.JsonParser.parseString("{\"traits\":["+trait+"]}").getAsJsonObject());}catch(IllegalArgumentException invalid){rejected=true;}
            h.assertTrue(rejected,"Reject invalid material trait: "+trait);
        }
        ToolTraits.validate(com.google.gson.JsonParser.parseString("{\"traits\":[{\"id\":\"work_hardening\",\"max_stacks\":1,\"speed_per_stack\":1,\"timeout_ticks\":1},{\"id\":\"marking\",\"cooldown_ticks\":0},{\"id\":\"grip\",\"every\":2}]}").getAsJsonObject());h.succeed();
    }
    @GameTest public void miningQueryExpiresWorkAtTheExactDeadline(GameTestHelper h){
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var tool=build("wooden_handle");player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        float ordinary=player.getDestroySpeed(Blocks.STONE.defaultBlockState());var data=ModularToolItem.parts(tool);data.putInt("trait_work_stacks",4);data.putLong("trait_work_until",h.getLevel().getGameTime()+1);tool.set(DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));
        h.assertTrue(player.getDestroySpeed(Blocks.STONE.defaultBlockState())>ordinary,"Work bonus is active before its deadline");
        data=ModularToolItem.parts(tool);data.putLong("trait_work_until",h.getLevel().getGameTime());tool.set(DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.of(data));
        h.assertTrue(Math.abs(player.getDestroySpeed(Blocks.STONE.defaultBlockState())-ordinary)<.00001,"Player mining query expires the bonus without waiting for an inventory tick");h.succeed();
    }
    @GameTest public void combatCooldownFollowsHeadThroughDisassemblyAndRepair(GameTestHelper h){
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        var target=(net.minecraft.world.entity.LivingEntity)BuiltInRegistries.ENTITY_TYPE.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("cow")).create(h.getLevel(),net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        var tool=ToolParts.assemble(item("bronze_axe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);((ModularToolItem)tool.getItem()).hurtEnemy(tool,target,player);target.removeEffect(net.minecraft.world.effect.MobEffects.SLOWNESS);
        var head=ToolParts.extract(tool,0);long deadline=ModularToolItem.parts(head).getLongOr("trait_staggering_until",0);
        h.assertTrue(deadline>h.getLevel().getGameTime(),"Extracted bronze head carries its active cooldown");
        var rebuilt=ToolParts.assemble(head,item("reinforced_handle"),item("cord_binding"),ItemStack.EMPTY);
        ((ModularToolItem)rebuilt.getItem()).hurtEnemy(rebuilt,target,player);h.assertFalse(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Changing handles cannot bypass the head's cooldown");
        var repaired=ToolParts.repair(rebuilt,item("bronze_ingot"));h.assertFalse(repaired.isEmpty(),"Fixture repairs the damaged bronze head");
        ((ModularToolItem)repaired.getItem()).hurtEnemy(repaired,target,player);h.assertFalse(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Material repair does not reset combat cooldown");
        var fresh=ToolParts.assemble(item("bronze_axe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);((ModularToolItem)fresh.getItem()).hurtEnemy(fresh,target,player);
        h.assertTrue(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"A genuinely different fresh head keeps its independent cooldown");h.succeed();
    }
    @GameTest public void gripProgressFollowsWrapAndPveEffectsDoNotAffectPlayers(GameTestHelper h){
        var tool=ToolParts.assemble(item("copper_axe_head"),item("wooden_handle"),item("cord_binding"),item("leather_wrap"));
        for(int i=0;i<3;i++)ToolTraits.useWear(tool);
        var rebuilt=ToolParts.assemble(ToolParts.extract(tool,0),ToolParts.extract(tool,1),ToolParts.extract(tool,2),ToolParts.extract(tool,4));
        h.assertValueEqual(ToolTraits.useWear(rebuilt),0,"A wrap keeps its earned fourth-use grip through disassembly");h.assertValueEqual(ToolTraits.useWear(rebuilt),1,"Disassembly does not grant permanent free uses");
        var attacker=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var target=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        var bronze=ToolParts.assemble(item("bronze_axe_head"),item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);((ModularToolItem)bronze.getItem()).hurtEnemy(bronze,target,attacker);
        h.assertFalse(target.hasEffect(net.minecraft.world.effect.MobEffects.SLOWNESS),"Material crowd-control is PvE only");
        h.assertValueEqual(ModularToolItem.parts(bronze).getLongOr("trait_staggering_until",0),0L,"Excluded player hits do not consume the PvE effect cooldown");h.succeed();
    }
    @GameTest public void leatherGripSavesWearOnlyOnSuccessfulNativeUse(GameTestHelper h){
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        var axe=ToolParts.assemble(item("copper_axe_head"),item("wooden_handle"),item("cord_binding"),item("leather_wrap"));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,axe);
        for(int i=0;i<4;i++){var pos=h.absolutePos(new BlockPos(1+i,2,1));h.getLevel().setBlock(pos,Blocks.OAK_LOG.defaultBlockState(),Block.UPDATE_ALL);
            var context=new net.minecraft.world.item.context.UseOnContext(player,net.minecraft.world.InteractionHand.MAIN_HAND,new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos),Direction.UP,pos,false));
            ((ModularToolItem)axe.getItem()).useOn(context);h.assertTrue(h.getLevel().getBlockState(pos).is(Blocks.STRIPPED_OAK_LOG),"Native stripping still executes");}
        h.assertValueEqual(axe.getDamageValue(),3,"Four successful native actions cost three wear with leather grip");
        ((ModularToolItem)axe.getItem()).mineBlock(axe,h.getLevel(),Blocks.OAK_LOG.defaultBlockState(),h.absolutePos(new BlockPos(1,2,1)),player);
        h.assertValueEqual(axe.getDamageValue(),4,"Ordinary mining does not receive free native-action wear");h.succeed();
    }
    @GameTest public void handleBalanceChangesActualAttackTempo(GameTestHelper h){
        var ordinary=build("wooden_handle");var light=build("light_handle");var reinforced=build("reinforced_handle");
        var attribute=net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED;double base=attribute.value().getDefaultValue();
        double normalSpeed=ordinary.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(attribute,base,net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        double lightSpeed=light.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(attribute,base,net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        double heavySpeed=reinforced.get(DataComponents.ATTRIBUTE_MODIFIERS).compute(attribute,base,net.minecraft.world.entity.EquipmentSlot.MAINHAND);
        h.assertTrue(Math.abs(normalSpeed-1.2)<0.00001,"Ordinary handle retains baseline attack tempo");
        h.assertTrue(Math.abs(lightSpeed-normalSpeed*1.08)<0.00001,"Light handle improves actual attacks per second, not the negative modifier");
        h.assertTrue(Math.abs(heavySpeed-normalSpeed*.97)<0.00001,"Reinforcement trades attack tempo for head life");
        h.assertTrue(light.getMaxDamage()<ordinary.getMaxDamage()&&reinforced.getMaxDamage()>ordinary.getMaxDamage(),"Handling trade-offs retain their durability costs");
        h.assertFalse(light.isCorrectToolForDrops(Blocks.DIAMOND_ORE.defaultBlockState()),"Faster handling never upgrades the copper head tier");h.succeed();
    }
    @GameTest public void complexHandlesCannotBypassTheirAssemblyIngredients(GameTestHelper h){
        h.assertFalse(ToolParts.shapingChoices(new ItemStack(Items.COPPER_INGOT)).stream().anyMatch(p->p.form().id().equals("hollow_handle")),"A bare ingot cannot bypass the ceramic core and pipe assembly");
        var plankChoices=ToolParts.shapingChoices(new ItemStack(Items.OAK_PLANKS));
        h.assertFalse(plankChoices.stream().anyMatch(p->p.form().id().equals("reinforced_handle")),"A bare plank cannot bypass reinforcing fittings and cord");
        h.assertTrue(plankChoices.stream().anyMatch(p->p.form().id().equals("standard_handle")),"Plain wooden handles remain directly shapeable");h.succeed();
    }
    @GameTest public void headConversionsAndDisassemblyRetainAnvilHistory(GameTestHelper h){
        var nativeTool=new ItemStack(Items.IRON_PICKAXE);var name=net.minecraft.network.chat.Component.literal("Workshop favorite");
        nativeTool.set(DataComponents.CUSTOM_NAME,name);nativeTool.set(DataComponents.REPAIR_COST,7);nativeTool.setDamageValue(50);
        var head=ToolParts.finish(nativeTool);
        h.assertValueEqual(head.getOrDefault(DataComponents.REPAIR_COST,0),7,"Native conversion retains prior anvil work");
        var assembled=ToolParts.assemble(head,item("wooden_handle"),item("cord_binding"),ItemStack.EMPTY);
        h.assertTrue(name.equals(assembled.get(DataComponents.CUSTOM_NAME)),"Head conversion keeps the named favorite");
        assembled.set(DataComponents.REPAIR_COST,15);
        var extracted=ToolParts.extract(assembled,0);
        var rebuilt=ToolParts.assemble(extracted,item("reinforced_handle"),item("cord_binding"),ItemStack.EMPTY);
        h.assertValueEqual(rebuilt.getOrDefault(DataComponents.REPAIR_COST,0),15,"Disassembly cannot reset anvil cost");
        h.assertTrue(name.equals(rebuilt.get(DataComponents.CUSTOM_NAME)),"Disassembly retains the name with the primary head");
        rebuilt.remove(DataComponents.CUSTOM_NAME);
        h.assertFalse(ToolParts.extract(rebuilt,0).has(DataComponents.CUSTOM_NAME),"Removing a name does not resurrect the stored head name");
        var expensive=assembled.copy();expensive.set(DataComponents.REPAIR_COST,31);
        var preserved=ToolParts.preserveIdentity(assembled,expensive);
        h.assertValueEqual(preserved.getOrDefault(DataComponents.REPAIR_COST,0),31,"A cheap mounted assembly cannot reset an expensive replacement head");
        h.succeed();
    }
    @GameTest public void headEnchantmentsStayWithTheHead(GameTestHelper h){
        var tool=build("wooden_handle");var enchantment=h.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(net.minecraft.world.item.enchantment.Enchantments.UNBREAKING);tool.enchant(enchantment,1);
        var head=ToolParts.extract(tool,0);var retained=ToolParts.assemble(head,item("reinforced_handle"),item("cord_binding"),ItemStack.EMPTY);
        h.assertTrue(retained.get(DataComponents.ENCHANTMENTS).equals(tool.get(DataComponents.ENCHANTMENTS)),"Handle changes retain head enchantments");
        var replacement=ToolParts.assemble(item("bronze_pickaxe_head"),ToolParts.extract(tool,1),ToolParts.extract(tool,2),ItemStack.EMPTY);
        h.assertTrue(replacement.getEnchantments().isEmpty(),"New head cannot copy old enchantments");h.assertFalse(head.getEnchantments().isEmpty(),"Old enchanted head remains the sole carrier");h.succeed();
    }
}
