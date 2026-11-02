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
    private static final int U=io.github.kpaakeh.smog.copper.MoltenMetalStorage.QUARTER;
    private static Block block(String name){return BuiltInRegistries.BLOCK.getValue(ModBlocks.id(name));}
    private static Item item(String name){return BuiltInRegistries.ITEM.getValue(ModBlocks.id(name));}
    private static void set(GameTestHelper h,BlockPos pos,Block block){h.getLevel().setBlock(pos,block.defaultBlockState(),Block.UPDATE_ALL);}
    @GameTest public void canonicalWorkshopCraftsResolveWithoutAmbiguousResults(GameTestHelper h) throws java.io.IOException {
        try(var stream=CopperTests.class.getResourceAsStream("/smog.content.json")){
            var root=com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(stream,java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            int checked=0;var problems=new java.util.ArrayList<String>();
            for(var entry:root.getAsJsonArray("crafting")){
                var definition=entry.getAsJsonObject();var recipe=definition.getAsJsonObject("data");String type=recipe.get("type").getAsString();if(!type.equals("minecraft:crafting_shaped")&&!type.equals("minecraft:crafting_shapeless"))continue;
                var grid=new java.util.ArrayList<ItemStack>(java.util.Collections.nCopies(9,ItemStack.EMPTY));
                if(type.equals("minecraft:crafting_shaped")){
                    var rows=recipe.getAsJsonArray("pattern");var key=recipe.getAsJsonObject("key");for(int y=0;y<rows.size();y++){String row=rows.get(y).getAsString();for(int x=0;x<row.length();x++)if(row.charAt(x)!=' ')grid.set(y*3+x,craftIngredient(key.get(String.valueOf(row.charAt(x))).getAsString()));}
                }else{var ingredients=recipe.getAsJsonArray("ingredients");for(int n=0;n<ingredients.size();n++)grid.set(n,craftIngredient(ingredients.get(n).getAsString()));}
                var input=net.minecraft.world.item.crafting.CraftingInput.of(3,3,grid);var expected=recipe.getAsJsonObject("result");var expectedItem=BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(expected.get("id").getAsString()));int count=expected.has("count")?expected.get("count").getAsInt():1;
                String name=definition.get("name").getAsString();var actual=h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,input,h.getLevel()).map(holder->holder.value().assemble(input)).orElse(ItemStack.EMPTY);
                if(!actual.is(expectedItem)||actual.getCount()!=count)problems.add("Actual lookup for "+name+" expected "+expectedItem+" x"+count+", got "+actual);
                // Native matches() handles trimming, horizontal mirrors, tags and shaped/shapeless overlap.
                for(var holder:h.getLevel().getServer().getRecipeManager().getRecipes())if(holder.value() instanceof net.minecraft.world.item.crafting.CraftingRecipe candidate&&candidate.matches(input,h.getLevel())){
                    var result=candidate.assemble(input);if(!result.is(expectedItem)||result.getCount()!=count)problems.add("Competing recipe for "+name+": "+holder.id()+" gives "+result);
                }
                checked++;
            }
            int expectedCount=0;for(var entry:root.getAsJsonArray("crafting")){String type=entry.getAsJsonObject().getAsJsonObject("data").get("type").getAsString();if(type.equals("minecraft:crafting_shaped")||type.equals("minecraft:crafting_shapeless"))expectedCount++;}
            if(checked!=expectedCount||checked==0)problems.add("Not every catalog recipe was checked: "+checked+" / "+expectedCount);
            h.assertTrue(problems.isEmpty(),"All workshop crafting problems ("+problems.size()+"): "+String.join("; ",problems));h.succeed();
        }
    }
    private static ItemStack craftIngredient(String ingredient){
        if(!ingredient.startsWith("#"))return new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.parse(ingredient)));
        var tag=net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.ITEM,net.minecraft.resources.Identifier.parse(ingredient.substring(1)));
        return BuiltInRegistries.ITEM.stream().map(Item::getDefaultInstance).filter(stack->stack.is(tag)).findFirst().orElseThrow();
    }
    @GameTest public void automatedSandCoreCastingKeepsToolingUntilOutputCanComplete(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));var m=(MachineEntity)h.getLevel().getBlockEntity(pos);
        var input=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),pos,Direction.UP);
        var core=net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(item("ceramic_core"));var mold=net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(item("sand_pipe_mold"));
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(input.insert(mold,2,tx),2L,"Automation supplies two sand molds");h.assertValueEqual(input.insert(core,2,tx),2L,"Automation supplies two cores into their separate slot");tx.commit();}
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),6750,tx),6750L,"One cored pour accepts exactly the handle's metal content");tx.commit();}
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var menu=m.createMenu(1,player.getInventory(),player);
        h.assertTrue(menu.slots.get(0).getItem().is(item("ceramic_core")),"The actual menu exposes the core as a separate slot");h.assertFalse(menu.slots.get(0).mayPickup(player),"The GUI cannot pick up a locked core");h.assertFalse(menu.slots.get(1).mayPickup(player),"The GUI cannot pick up a locked mold");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(input.extract(core,1,tx),0L,"Automation cannot extract the locked core");h.assertValueEqual(input.insert(core,1,tx),0L,"Automation cannot add to the locked core stack");}
        m.setItem(3,new ItemStack(Items.COBBLESTONE,64));for(int tick=0;tick<60;tick++)io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(1).getCount(),2,"Blocked output has not consumed the sand mold");h.assertValueEqual(m.getItem(2).getCount(),2,"Blocked output has not consumed the core");h.assertValueEqual(m.smeltery.filled,6750,"Blocked output retains every poured metal unit");
        m.setItem(3,ItemStack.EMPTY);io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(3).is(item("hollow_copper_handle")),"Unblocking output completes the saved cast");h.assertValueEqual(m.getItem(1).getCount(),1,"Completion consumes exactly one sand mold");h.assertValueEqual(m.getItem(2).getCount(),1,"Completion consumes exactly one core");h.assertTrue(menu.slots.get(0).mayPickup(player),"The spare core unlocks after completion");h.succeed();
    }
    @GameTest public void legacyPipeCastingWithoutCoreSnapshotStillCompletes(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));var m=(MachineEntity)h.getLevel().getBlockEntity(pos);m.setItem(1,new ItemStack(item("pipe_mold")));
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),9000,tx);tx.commit();}
        var legacy=m.saveCustomOnly(h.getLevel().registryAccess());legacy.remove("casting_core");legacy.remove("casting_consumes_core");
        m.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),legacy));
        h.assertTrue(m.smeltery.castingCore.isEmpty()&&!m.smeltery.consumeCore,"Older saves default to no core requirement");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),9000,tx),9000L,"A legacy ordinary pipe casting accepts its remaining metal");tx.commit();}
        for(int tick=0;tick<100;tick++)io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(3).is(item("copper_pipe_part")),"A legacy pipe recipe does not turn into a hollow handle");h.assertValueEqual(m.getItem(3).getCount(),4,"Legacy casting preserves its four-pipe output");h.succeed();
    }
    @GameTest public void hollowCastingLocksSavesAndConsumesOneCoreWithoutMetalGain(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));var m=(MachineEntity)h.getLevel().getBlockEntity(pos);
        m.setItem(1,new ItemStack(item("pipe_mold")));m.setItem(2,new ItemStack(item("ceramic_core"),2));
        var fluid=io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0);
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.castingFluid.insert(fluid,123,tx),123L,"Partial cored pour accepted");}
        h.assertFalse(m.smeltery.casting(),"Rollback removes the tentative cast");h.assertFalse(m.smeltery.consumeCore,"Rollback restores the core consumption flag");h.assertTrue(m.smeltery.castingCore.isEmpty(),"Rollback restores the core snapshot");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.castingFluid.insert(fluid,123,tx);tx.commit();}
        h.assertFalse(m.canPlaceItem(2,new ItemStack(item("ceramic_core"))),"Automation cannot alter the core during a cast");
        var saved=m.saveCustomOnly(h.getLevel().registryAccess());m.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));
        h.assertTrue(m.smeltery.consumeCore&&m.smeltery.castingCore.is(item("ceramic_core")),"The cored recipe and tooling snapshot survive reload");
        var cores=m.getItem(2).copy();m.setItem(2,ItemStack.EMPTY);
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.castingFluid.insert(fluid,9000,tx),0L,"A missing locked core cannot silently turn the job into ordinary pipes");}
        m.setItem(2,cores);
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.castingFluid.insert(fluid,9000,tx),6627L,"Hollow copper handle requires exactly 6750 units");tx.commit();}
        for(int tick=0;tick<60;tick++)io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(3).is(item("hollow_copper_handle")),"The cored mold produces a hollow handle");h.assertValueEqual(m.getItem(2).getCount(),1,"Exactly one ceramic core is consumed");h.assertValueEqual(m.getItem(1).getCount(),1,"The ceramic pipe mold remains reusable");
        h.assertValueEqual(CopperWorkshops.melt(m.getItem(3))[1],6750,"Remelting returns only the metal poured, with no duplication");h.succeed();
    }
    @GameTest public void coredPipeMoldsRequireCompatibleMetalButPlainPipesRemainAvailable(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));var m=(MachineEntity)h.getLevel().getBlockEntity(pos);m.setItem(1,new ItemStack(item("pipe_mold")));
        h.assertValueEqual(io.github.kpaakeh.smog.copper.CastingProcesses.recipe(m,0).units(),18000,"Without a core the existing four-pipe recipe remains intact");
        m.setItem(2,new ItemStack(item("ceramic_core")));int checked=0;
        for(int metal=0;metal<io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length;metal++){
            var part=io.github.kpaakeh.smog.copper.CastingProcesses.hollowPart(metal);var recipe=io.github.kpaakeh.smog.copper.CastingProcesses.recipe(m,metal);
            if(part==null){h.assertTrue(recipe==null,"Unsupported cored metal cannot fall back to a plain pipe recipe");continue;}
            checked++;h.assertTrue(recipe.result().is(BuiltInRegistries.ITEM.getValue(part.item()))&&recipe.consumeCore(),"Every compatible hollow material has a cored casting route");h.assertValueEqual(recipe.units(),io.github.kpaakeh.smog.copper.ToolParts.units(part),"The casting uses the registered material content");
        }
        h.assertTrue(checked>0,"At least one compatible hollow material is registered");m.setItem(1,new ItemStack(item("ingot_mold")));h.assertTrue(io.github.kpaakeh.smog.copper.CastingProcesses.recipe(m,0)==null,"A core in the wrong mold rejects pouring");h.succeed();
    }
    @GameTest public void partialCanistersPourIntoWorldMoldWithoutLosingRemainder(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));
        var table=(MachineEntity)h.getLevel().getBlockEntity(pos);table.setItem(1,new ItemStack(item("ingot_mold")));
        // InLevel creates a creative player: its fluid interactions intentionally retain the source item.
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var hand=net.minecraft.world.InteractionHand.MAIN_HAND;
        h.assertFalse(player.isCreative(),"Conservation is tested with a survival player");
        var copper=io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0);
        player.setItemInHand(hand,io.github.kpaakeh.smog.copper.FluidCanisterStorage.filled(copper,1234));
        io.github.kpaakeh.smog.copper.CopperInteractions.use(h.getLevel(),table,player,hand,pos);
        h.assertValueEqual(table.smeltery.filled,1234,"World interaction pours every unit from a partial recovery canister");
        player.setItemInHand(hand,io.github.kpaakeh.smog.copper.FluidCanisterStorage.filled(copper,8000));
        io.github.kpaakeh.smog.copper.CopperInteractions.use(h.getLevel(),table,player,hand,pos);
        h.assertValueEqual(table.smeltery.filled,4*U,"A locked partial casting can be topped up by hand");
        var remainder=player.getItemInHand(hand).getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("smog_fluid_units",0);
        h.assertValueEqual(remainder,234,"Excess metal remains in the canister with exact precision");
        for(int tick=0;tick<60;tick++)io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).is(Items.COPPER_INGOT),"Hand-poured metal cools into the expected ingot");
        h.assertValueEqual(table.getItem(3).getCount(),1,"One mold yields exactly one ingot");h.succeed();
    }
    @GameTest public void sidedAutomationSuppliesSandMoldsAndExtractsCastings(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("casting_table"));var table=(MachineEntity)h.getLevel().getBlockEntity(pos);
        var input=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),pos,Direction.UP);
        var output=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),pos,Direction.DOWN);
        var mold=net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(item("sand_ingot_mold"));
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(input.insert(mold,2,tx),2L,"Top-side automation supplies consumable molds to their real slot");tx.commit();
        }
        for(int batch=0;batch<2;batch++){
            try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                h.assertValueEqual(table.castingFluid.insert(io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0),4*U,tx),(long)4*U,"Each mold accepts exactly one ingot of copper");tx.commit();
            }
            try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                h.assertValueEqual(input.insert(mold,1,tx),0L,"Automation cannot replace or add a mold while a casting is locked");
                h.assertValueEqual(output.extract(mold,1,tx),0L,"Output automation cannot steal a working mold");
            }
            for(int tick=0;tick<60;tick++)io.github.kpaakeh.smog.copper.CastingProcesses.tick(h.getLevel(),table);
            try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
                h.assertValueEqual(output.extract(net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(Items.COPPER_INGOT),1,tx),1L,"Bottom-side automation extracts the finished ingot");tx.commit();
            }
            h.assertValueEqual(table.getItem(1).getCount(),1-batch,"Only the mold used for this casting is consumed");
        }
        h.succeed();
    }
    @GameTest public void potteryWorldInteractionOpensMenuWithoutInsertingOrCrafting(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,2,3));set(h,pos,block("potter_table"));
        var table=(MachineEntity)h.getLevel().getBlockEntity(pos);table.serverTick();
        var player=h.makeMockServerPlayerInLevel();player.setPos(pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5);
        var hand=net.minecraft.world.InteractionHand.MAIN_HAND;player.setItemInHand(hand,new ItemStack(Items.CLAY_BALL,32));
        io.github.kpaakeh.smog.copper.CopperInteractions.use(h.getLevel(),table,player,hand,pos);
        h.assertTrue(player.containerMenu instanceof io.github.kpaakeh.smog.gui.MachineMenu menu&&menu.layout.id.equals("potter_table"),"World use opens the dedicated pottery GUI");
        h.assertValueEqual(player.getItemInHand(hand).getCount(),32,"Opening the screen does not insert or consume held clay");
        h.assertTrue(table.getItem(0).isEmpty()&&table.getItem(3).isEmpty(),"World use never performs an automatic batch");h.succeed();
    }

    private static MachineEntity hearth(GameTestHelper h,BlockPos origin,int width){return hearth(h,origin,width,3);}
    private static MachineEntity hearth(GameTestHelper h,BlockPos origin,int width,int height){
        int left=width/2;
        for(int x=-left;x<width-left;x++)for(int y=-1;y<height-1;y++)for(int z=0;z<width;z++){
            if(x==0&&y==0&&z==0)continue;
            var b=y==-1?Blocks.COBBLESTONE:x==-left||x==width-left-1||z==0||z==width-1?Blocks.BRICKS:Blocks.AIR;
            set(h,origin.offset(x,y,z),b);
        }
        set(h,origin.offset(0,-1,1),block("solid_firebox"));
        set(h,origin.offset(-left,0,1),block("casting_spout"));set(h,origin.offset(left,0,1),block("air_intake"));
        if(!h.getLevel().getBlockState(origin).is(block("copper_hearth")))set(h,origin,block("copper_hearth"));
        return (MachineEntity)h.getLevel().getBlockEntity(origin);
    }
    @GameTest public void solidFuelStacksPersistWhileEveryWorkSlotRemainsSingleItem(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,pos,3);h.assertTrue(m.tryAssemble(),"Hearth assembles");
        var player=h.makeMockServerPlayer(net.minecraft.world.level.GameType.SURVIVAL);var hand=net.minecraft.world.InteractionHand.MAIN_HAND;
        player.setItemInHand(hand,new ItemStack(Items.COAL,64));io.github.kpaakeh.smog.copper.CopperInteractions.use(h.getLevel(),m,player,hand,pos);
        h.assertTrue(player.getItemInHand(hand).isEmpty(),"One world action loads the fuel stack");h.assertValueEqual(m.getItem(2).getCount(),64,"Fuel has a normal stack capacity independent of work cells");
        m.serverTick();h.assertValueEqual(m.getItem(2).getCount(),64,"Idle server ticks do not eject sixty-three fuel items");
        var saved=m.saveCustomOnly(h.getLevel().registryAccess());m.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));h.assertValueEqual(m.getItem(2).getCount(),64,"The stored fuel stack survives saving and loading");
        var menu=m.createMenu(1,player.getInventory(),player);h.assertValueEqual(menu.slots.get(io.github.kpaakeh.smog.copper.Smeltery.MAX_WORKERS).getMaxStackSize(new ItemStack(Items.COAL)),64,"The real fuel GUI slot accepts a full stack");h.assertValueEqual(menu.slots.get(0).getMaxStackSize(new ItemStack(Items.RAW_COPPER)),1,"The real worker GUI slot still accepts one item");
        player.setItemInHand(hand,new ItemStack(Items.RAW_COPPER,16));io.github.kpaakeh.smog.copper.CopperInteractions.use(h.getLevel(),m,player,hand,pos);h.assertValueEqual(m.getItem(0).getCount(),1,"World insertion respects the single-item work cell");h.assertValueEqual(player.getItemInHand(hand).getCount(),15,"All excess raw input stays in the player's hand");h.succeed();
    }
    @GameTest public void fuelAndWorkTransferViewsHaveIndependentTransactionalCapacities(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,pos,3);h.assertTrue(m.tryAssemble(),"Hearth assembles");
        var fuel=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),pos,Direction.NORTH);var top=net.fabricmc.fabric.api.transfer.v1.item.ItemStorage.SIDED.find(h.getLevel(),pos,Direction.UP);var coal=net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(Items.COAL);var copper=net.fabricmc.fabric.api.transfer.v1.item.ItemVariant.of(Items.RAW_COPPER);
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(fuel.insert(coal,64,tx),64L,"A pipe can provision the whole fuel stack");}
        h.assertTrue(m.getItem(2).isEmpty(),"A rolled-back fuel transfer restores the empty slot");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(fuel.insert(coal,64,tx),64L,"Committed pipe provisioning accepts sixty-four fuel");h.assertValueEqual(top.insert(copper,64,tx),(long)io.github.kpaakeh.smog.copper.Smeltery.workers(m),"Bulk raw input is limited to one item per available worker");tx.commit();}
        h.assertValueEqual(m.getItem(2).getCount(),64,"Fuel stack is retained after commit");
        for(int worker=0;worker<io.github.kpaakeh.smog.copper.Smeltery.workers(m);worker++)h.assertValueEqual(m.getItem(io.github.kpaakeh.smog.copper.Smeltery.slot(worker)).getCount(),1,"Every controller transfer slot has its own one-item capacity");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(top.insert(copper,1,tx),0L,"A filled set of work cells rejects additional pipe input");}
        for(int worker=0;worker<io.github.kpaakeh.smog.copper.Smeltery.workers(m);worker++)m.setItem(io.github.kpaakeh.smog.copper.Smeltery.slot(worker),ItemStack.EMPTY);
        int accepted=0;for(int item=0;item<8;item++)if(net.minecraft.world.level.block.entity.HopperBlockEntity.addItem(null,m,new ItemStack(Items.RAW_COPPER),Direction.UP).isEmpty())accepted++;
        h.assertValueEqual(accepted,io.github.kpaakeh.smog.copper.Smeltery.workers(m),"Native hopper-style insertion also stops at one item per work cell");h.succeed();
    }
    @GameTest public void bellowsBudgetFollowsMetalVolumeForBlocksAndNuggets(GameTestHelper h){
        var m=hearth(h,h.absolutePos(new BlockPos(6,2,2)),3);h.assertTrue(m.tryAssemble(),"Hearth assembles");
        m.copper.heat=600;m.copper.burn=10000;m.copper.fillBellows();m.setItem(0,new ItemStack(BuiltInRegistries.ITEM.getValue(net.minecraft.resources.Identifier.withDefaultNamespace("copper_block"))));
        for(int tick=0;tick<320;tick++)io.github.kpaakeh.smog.copper.Smeltery.tick(h.getLevel(),m);
        h.assertValueEqual(m.copper.bellowsUnits(),0,"Eight ingots of boosted work exhaust one bellows charge during a nine-ingot block");
        h.assertFalse(m.getItem(0).isEmpty(),"A whole nine-ingot block does not fit within the eight-ingot boost");
        for(int tick=0;tick<80;tick++)io.github.kpaakeh.smog.copper.Smeltery.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(0).isEmpty(),"The remaining block processing continues at normal speed");
        h.assertValueEqual(m.copper.copper,9*9000,"Bellows never multiply the block's metal output");
        m.copper.fillBellows();
        for(int nugget=0;nugget<9;nugget++){
            m.setItem(0,new ItemStack(Items.GOLD_NUGGET));
            for(int tick=0;tick<20;tick++)io.github.kpaakeh.smog.copper.Smeltery.tick(h.getLevel(),m);
            h.assertTrue(m.getItem(0).isEmpty(),"Each nugget completes its boosted job");
        }
        h.assertValueEqual(m.copper.bellowsUnits(),7*9000,"Nine nuggets consume one ingot of air budget, without rounding each nugget up");h.succeed();
    }
    @GameTest public void bellowsLegacyChargesAndFractionalBudgetSurviveSave(GameTestHelper h){
        var legacy=new net.minecraft.nbt.CompoundTag();legacy.putInt("copper_air",3);legacy.putInt("molten_storage_version",1);
        var state=new io.github.kpaakeh.smog.copper.CopperState();
        state.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),legacy));
        h.assertValueEqual(state.bellowsUnits(),3*9000,"Legacy three-charge saves migrate to three ingots of boost");
        state.consumeBellows(1000);
        var output=net.minecraft.world.level.storage.TagValueOutput.createWithoutContext(net.minecraft.util.ProblemReporter.DISCARDING);state.save(output);
        var restored=new io.github.kpaakeh.smog.copper.CopperState();restored.load(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),output.buildResult()));
        h.assertValueEqual(restored.bellowsUnits(),26000,"Sub-ingot bellows budget survives saving without rounding");
        h.assertValueEqual(restored.air,3,"The existing coarse display still shows the remaining partial charge");h.succeed();
    }
    @GameTest public void complexHearthResizesWithEmptyChamber(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,origin,3);
        h.assertTrue(m.tryAssemble(),"3x3x3 hollow hearth assembles");h.assertValueEqual(m.complexShape().height(),3,"Actual rim height");h.assertValueEqual(CopperWorkshops.capacity(m),64*U,"Small chamber = 16 ingots");
        m.copper.copper=40*U;m.disassemble();hearth(h,origin,5);
        h.assertTrue(m.tryAssemble(),"5x3x5 hollow hearth assembles");h.assertValueEqual(m.complexShape().width(),5,"Width changed");h.assertValueEqual(CopperWorkshops.capacity(m),576*U,"Capacity follows free chamber volume");h.assertValueEqual(m.copper.copper,40*U,"Stored metal survives resizing");
        m.disassemble();set(h,origin.offset(0,0,1),block("ceramic_crucible"));h.assertFalse(m.tryAssemble(),"Crucible obstructs required empty chamber");h.assertValueEqual(m.copper.copper,40*U,"Invalid structure preserves contents");h.succeed();
    }
    @GameTest public void fullHeightAndCornerRules(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(7,2,2));var m=hearth(h,origin,11,11);
        h.assertTrue(m.tryAssemble(),"Maximum 11x11x11 hearth assembles");h.assertValueEqual(m.complexShape().height(),11,"Tall shell cannot be detected as smaller open prefix");h.assertValueEqual(CopperWorkshops.capacity(m),6480*9000,"Maximum chamber capacity");
        m.disassemble();set(h,origin.offset(-5,0,1),Blocks.BRICKS);set(h,origin.offset(-5,0,0),block("casting_spout"));h.assertFalse(m.tryAssemble(),"Wall corner rejects functional component");
        set(h,origin.offset(-5,0,0),Blocks.BRICKS);set(h,origin.offset(-5,0,1),block("casting_spout"));h.assertTrue(m.tryAssemble(),"Face-mounted component works");h.succeed();
    }
    @GameTest public void wallFireboxAndIndependentOrientation(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);
        set(h,origin.offset(0,-1,1),Blocks.COBBLESTONE);set(h,origin.offset(0,0,2),block("solid_firebox"));
        h.getLevel().setBlock(origin,m.getBlockState().setValue(MachineBlock.FACING,Direction.SOUTH),Block.UPDATE_ALL);
        var clicked=origin.offset(-1,0,1);
        h.assertTrue(ComplexMultiblocks.nearbyControllers(h.getLevel(),clicked).contains(m),"Unassembled port finds its controller");
        var player=h.makeMockServerPlayerInLevel();var tool=new ItemStack(item("configurator"));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        var hit=new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(clicked),Direction.WEST,clicked,false);
        player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);
        h.assertTrue(m.assembled(),"Real configurator click on an unassembled port assembles reversed controller and wall firebox");
        h.assertTrue(m.rotation()==Rotation.NONE,"Shell direction follows actual walls");
        h.assertTrue(m.getBlockState().getValue(MachineBlock.FACING)==Direction.SOUTH,"Assembly does not rotate controller");
        h.assertTrue(ComplexMultiblocks.owner(h.getLevel(),clicked)==m,"Ownership uses detected orientation");
        m.disassemble();h.assertFalse(m.tryAssembleAt(origin.offset(4,0,0)),"Unrelated clicked part cannot assemble this hearth");h.succeed();
    }
    private static MachineEntity manual(GameTestHelper h,String name,BlockPos origin){
        var definition=io.github.kpaakeh.smog.multiblock.Multiblocks.get(ModBlocks.id(name));
        definition.parts().forEach((offset,ingredient)->set(h,origin.offset(offset),ingredient.block()));set(h,origin,block(name));
        var machine=(MachineEntity)h.getLevel().getBlockEntity(origin);h.assertTrue(machine.tryAssemble(),"Manual workshop assembles");return machine;
    }
    @GameTest public void washingConsumesOnlyPrimaryOreAndWater(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.RAW_COPPER,16));
        h.assertFalse(m.canPlaceItem(0,new ItemStack(Items.COPPER_INGOT)),"Finished metal cannot be enriched");
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.processFluid.insert(net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant.of(net.minecraft.world.level.material.Fluids.WATER),81000,tx);}
        h.assertValueEqual(m.copper.processFluid,0,"Water transfer rollback");
        io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);for(int i=0;i<120;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(0).getCount(),16,"No water means no input consumption");m.copper.processFluid=81000;
        for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(0).getCount(),8,"Exactly eight raw copper per batch");h.assertValueEqual(m.getItem(3).getCount(),5,"Five concentrate per batch");h.assertValueEqual(m.copper.processFluid,0,"One bucket consumed");
        h.assertValueEqual(CopperWorkshops.melt(m.getItem(3))[1],18000,"Concentrate melts to two ingots");h.succeed();
    }
    @GameTest public void washingEnrichesFivePrimaryMetalsButNeverFinishedMetal(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));
        for(var recipe:io.github.kpaakeh.smog.copper.WashingRecipes.all()){
            if(recipe.metal().isEmpty())continue;
            m.setItem(0,recipe.ingredients());m.setItem(3,ItemStack.EMPTY);m.copper.processFluid=recipe.water();m.copper.queued=0;
            h.assertValueEqual(io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m),1,"One primary-ore batch is queued");
            for(int i=0;i<recipe.ticks();i++)CopperWorkshops.tick(h.getLevel(),m);
            h.assertTrue(m.getItem(0).isEmpty(),"Washing consumes the actual primary ore");
            h.assertTrue(ItemStack.isSameItemSameComponents(m.getItem(3),recipe.result()),"Concentrate retains metal identity: "+recipe.metal());
            h.assertValueEqual(m.getItem(3).getCount(),5,"Eight raw ore yields five concentrate");
            var melt=CopperWorkshops.melt(m.getItem(3));h.assertTrue(melt!=null,"Concentrate has a real melting route");
            h.assertValueEqual(melt[0],java.util.Arrays.asList(io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES).indexOf(recipe.metal()),"Concentrate melts to its own metal");
            h.assertValueEqual(melt[1],18000,"Every concentrate holds two ingots, a total 25 percent bonus");
            h.assertValueEqual(m.copper.processFluid,0,"Only the configured water is consumed");
            h.assertFalse(m.canPlaceItem(0,m.getItem(3)),"Concentrate cannot be enriched twice");
        }
        for(var metal:java.util.List.of(Items.COPPER_INGOT,Items.IRON_INGOT,Items.GOLD_INGOT,Items.IRON_NUGGET,Items.GOLD_NUGGET))h.assertFalse(m.canPlaceItem(0,new ItemStack(metal)),"Finished metal is never a washing input");
        h.succeed();
    }
    @GameTest public void washingSeparatesUsefulMineralInputsWithExactWaterAndFiniteQueue(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));
        for(var recipe:io.github.kpaakeh.smog.copper.WashingRecipes.all()){
            if(!recipe.metal().isEmpty())continue;
            m.setItem(0,recipe.ingredients());m.setItem(3,new ItemStack(Items.STICK,64));m.copper.processFluid=recipe.water();m.copper.queued=0;
            io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);
            for(int i=0;i<recipe.ticks()+1;i++)CopperWorkshops.tick(h.getLevel(),m);
            h.assertValueEqual(m.getItem(0).getCount(),recipe.count(),"Blocked output preserves mineral input");
            h.assertValueEqual(m.copper.processFluid,recipe.water(),"Blocked output preserves water");
            m.setItem(3,ItemStack.EMPTY);m.copper.processFluid=recipe.water()-1;
            for(int i=0;i<recipe.ticks();i++)CopperWorkshops.tick(h.getLevel(),m);
            h.assertTrue(m.getItem(3).isEmpty(),"A partial water supply cannot complete the batch");
            m.copper.processFluid=recipe.water();
            for(int i=0;i<recipe.ticks();i++)CopperWorkshops.tick(h.getLevel(),m);
            h.assertTrue(ItemStack.isSameItemSameComponents(m.getItem(3),recipe.result()),"Separation gives the configured useful output");
            h.assertValueEqual(m.getItem(3).getCount(),recipe.outputCount(),"Exact separation yield");h.assertValueEqual(m.copper.processFluid,0,"Exact recipe water consumption");
            h.assertValueEqual(m.copper.queued,0,"The manual order ends");
            for(int i=0;i<recipe.ticks();i++)CopperWorkshops.tick(h.getLevel(),m);
            h.assertValueEqual(m.getItem(3).getCount(),recipe.outputCount(),"Idle washing cannot duplicate the output");
        }
        h.succeed();
    }
    @GameTest public void changingWashingInputCannotReusePreviousProgress(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.RAW_COPPER,8));m.copper.processFluid=162000;
        io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);for(int i=0;i<50;i++)CopperWorkshops.tick(h.getLevel(),m);
        m.setItem(0,new ItemStack(Items.RAW_IRON,8));for(int i=0;i<50;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(3).isEmpty(),"Changing ore resets progress instead of carrying copper work into iron");
        h.assertValueEqual(m.copper.processFluid,162000,"Changing inputs does not consume water early");
        for(int i=0;i<50;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(3).is(item("iron_concentrate")),"The new full batch produces iron concentrate");
        h.assertValueEqual(m.copper.processFluid,81000,"Exactly one batch of water is consumed");h.succeed();
    }
    @GameTest public void washingSmallBatchesKeepFiniteOrderAndProgressAcrossSaveLoad(GameTestHelper h){
        var m=manual(h,"washing_trough",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.GRAVEL,64));m.copper.processFluid=162000;
        h.assertValueEqual(io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m),8,"One order is capped at eight batches, matching the persisted state");
        for(int i=0;i<45;i++)CopperWorkshops.tick(h.getLevel(),m);
        io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);h.assertValueEqual(m.copper.work,45,"Repeated order does not restart progress");
        var saved=m.saveCustomOnly(h.getLevel().registryAccess());m.loadCustomOnly(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));
        h.assertValueEqual(m.copper.queued,8,"The full allowed order survives save/load without being truncated");h.assertValueEqual(m.copper.work,45,"Partial progress survives save/load");
        for(int i=45;i<8*80;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertValueEqual(m.getItem(0).getCount(),32,"Only the ordered eight four-item batches are consumed");h.assertValueEqual(m.getItem(3).getCount(),8,"One flint per ordered batch");h.assertValueEqual(m.copper.processFluid,0,"Exactly eight quarter buckets consumed");
        for(int i=0;i<160;i++)CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.getItem(0).getCount(),32,"An idle washer never starts the remaining half-stack automatically");
        m.copper.processFluid=162000;h.assertValueEqual(io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m),8,"A new explicit order accepts the remaining half-stack");
        for(int i=0;i<8*80;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(0).isEmpty(),"Two explicit orders complete all sixteen batches");h.assertValueEqual(m.getItem(3).getCount(),16,"Sixteen batches retain exact combined yield");h.assertValueEqual(m.copper.queued,0,"The second finite order also ends");h.succeed();
    }
    @GameTest public void pressingQueuesAndPausesWithoutDuplication(GameTestHelper h){
        var m=manual(h,"lever_press",h.absolutePos(new BlockPos(5,2,4)));m.setItem(0,new ItemStack(Items.WHEAT_SEEDS,16));
        h.assertValueEqual(io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m),2,"One action queues loaded batches");
        for(int i=0;i<20;i++){io.github.kpaakeh.smog.copper.ManualWorkshops.queue(m);CopperWorkshops.tick(h.getLevel(),m);}
        h.assertTrue(m.getItem(3).isEmpty(),"Repeated clicks do not finish early");
        for(int i=0;i<180;i++)CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.getItem(0).isEmpty(),"Two batches consume sixteen seeds");h.assertValueEqual(m.getItem(3).getCount(),2,"Two cakes produced");h.assertValueEqual(m.copper.processFluid,40500,"Two quarter-bucket oil batches");h.assertValueEqual(m.copper.queued,0,"Finite queue ends");
        for(int i=0;i<100;i++)CopperWorkshops.tick(h.getLevel(),m);h.assertValueEqual(m.copper.processFluid,40500,"Idle device cannot duplicate oil");
        h.assertTrue(item("seed_cake").getDefaultInstance().has(net.minecraft.core.component.DataComponents.COMPOSTABLE),"Cake is actually compostable");h.succeed();
    }
    @GameTest public void manualWaterSupplyIsFiniteAndTransactional(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(5,2,5));set(h,pos,block("hand_water_pump"));set(h,pos.below(),Blocks.WATER);
        set(h,pos.east(),block("water_tank"));var pump=(MachineEntity)h.getLevel().getBlockEntity(pos);var tank=(MachineEntity)h.getLevel().getBlockEntity(pos.east());pump.serverTick();tank.serverTick();
        h.assertTrue(io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump),"Source primes pump");
        h.assertValueEqual(pump.copper.pumpBudget,324000,"One action grants four buckets");
        io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump);h.assertValueEqual(pump.copper.pumpBudget,324000,"Repeated use does not extend active budget");
        tank.copper.processFluid=81000;
        try(var tx=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){
            h.assertValueEqual(tank.processFluid.extract(tank.processFluid.resource(),12345,tx),12345L,"Exact finite extraction");
        }
        h.assertValueEqual(tank.copper.processFluid,81000,"Cancelled transfer preserves tank");
        set(h,pos.below(),Blocks.AIR);h.assertFalse(io.github.kpaakeh.smog.copper.WaterSupply.prime(h.getLevel(),pump),"No source cannot prime");h.succeed();
    }
    @GameTest public void moltenTransferRollsBackWithoutRounding(GameTestHelper h){
        var m=hearth(h,h.absolutePos(new BlockPos(6,2,2)),3);h.assertTrue(m.tryAssemble(),"Assembled tank");
        var copper=io.github.kpaakeh.smog.copper.MoltenMetalStorage.variant(0);
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.molten.insert(copper,9001,transaction),9001L,"Accept exact fluid units");}
        h.assertValueEqual(m.copper.total(),0,"Uncommitted transfer rolls back");
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){m.molten.insert(copper,9001,transaction);transaction.commit();}
        try(var transaction=net.fabricmc.fabric.api.transfer.v1.transaction.Transaction.openOuter()){h.assertValueEqual(m.molten.extract(copper,1,transaction),1L,"Single-unit extraction is supported");transaction.commit();}
        h.assertValueEqual(m.copper.copper,9000,"No quarter-ingot rounding");h.assertTrue(CopperWorkshops.melt(ItemStack.EMPTY)==null,"Empty input cannot create tin from absent item IDs");h.succeed();
    }
    @GameTest public void complexOwnershipPersistsAndRejectsOverlap(GameTestHelper h){
        var data=new ComplexFormationData();var a=new BlockPos(1,2,3);var b=new BlockPos(4,5,6);var part=new BlockPos(7,8,9);
        h.assertTrue(data.claim(a,java.util.List.of(a,part)),"Initial exclusive claim");h.assertFalse(data.claim(b,java.util.List.of(b,part)),"Shared functional block rejected");
        var encoded=ComplexFormationData.TYPE.codec().encodeStart(com.mojang.serialization.JsonOps.INSTANCE,data).getOrThrow();var restored=ComplexFormationData.TYPE.codec().parse(com.mojang.serialization.JsonOps.INSTANCE,encoded).getOrThrow();
        h.assertTrue(restored.owner(part).equals(a),"Ownership survives save/load");restored.release(a);h.assertTrue(restored.claim(b,java.util.List.of(b,part)),"Disassembly releases ownership");h.succeed();
    }
    @GameTest(maxTicks=100) public void starterFaucetCraftLeadsToFirstFittingsWithoutCastParts(GameTestHelper h){
        var input=net.minecraft.world.item.crafting.CraftingInput.of(3,3,java.util.List.of(ItemStack.EMPTY,new ItemStack(Items.BRICK),ItemStack.EMPTY,new ItemStack(Items.COPPER_INGOT),new ItemStack(Items.BRICK),new ItemStack(Items.COPPER_INGOT),ItemStack.EMPTY,new ItemStack(Items.BRICK),ItemStack.EMPTY));
        var crafted=h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,input,h.getLevel()).orElseThrow().value().assemble(input);
        h.assertTrue(crafted.is(item("casting_faucet"))&&crafted.getCount()==1,"Three vanilla bricks and two vanilla copper ingots craft the first actual faucet");
        var economical=net.minecraft.world.item.crafting.CraftingInput.of(3,2,java.util.List.of(ItemStack.EMPTY,new ItemStack(item("copper_fitting")),ItemStack.EMPTY,new ItemStack(item("copper_pipe_part")),new ItemStack(item("copper_fitting")),new ItemStack(item("copper_pipe_part"))));
        h.assertTrue(h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,economical,h.getLevel()).orElseThrow().value().assemble(economical).is(crafted.getItem()),"The existing cheaper cast-parts faucet recipe remains available");
        var nozzleInput=net.minecraft.world.item.crafting.CraftingInput.of(3,2,java.util.List.of(new ItemStack(item("copper_pipe_part")),new ItemStack(item("copper_fitting")),new ItemStack(item("copper_pipe_part")),ItemStack.EMPTY,new ItemStack(item("copper_fitting")),ItemStack.EMPTY));
        h.assertTrue(h.getLevel().recipeAccess().getRecipeFor(net.minecraft.world.item.crafting.RecipeType.CRAFTING,nozzleInput,h.getLevel()).orElseThrow().value().assemble(nozzleInput).is(item("air_nozzle")),"The inverted two-row layout produces the air nozzle without shadowing the faucet recipe");
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Starter hearth assembles without cast copper parts");m.copper.copper=9000;m.smeltery.selected="copper";m.sync();
        var tablePos=origin.offset(-2,-1,1);set(h,tablePos,block("casting_table"));var table=(MachineEntity)h.getLevel().getBlockEntity(tablePos);table.setItem(1,new ItemStack(item("fitting_mold")));
        var faucet=((BlockItem)crafted.getItem()).getBlock().defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.WEST).setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,true);h.getLevel().setBlock(tablePos.above(),faucet,Block.UPDATE_ALL);
        h.assertTrue(CopperWorkshops.route(h.getLevel(),tablePos)!=null,"The crafted starter faucet connects the real wall port to the first mold");
        h.runAfterDelay(14,()->{h.assertValueEqual(table.smeltery.filled,9000,"First pour transfers exactly one ingot through the physical route");h.getLevel().setBlock(tablePos.above(),faucet.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,false),Block.UPDATE_ALL);});
        h.runAfterDelay(70,()->{h.assertTrue(table.getItem(3).is(item("copper_fitting")),"The first mold produces useful fittings");h.assertValueEqual(table.getItem(3).getCount(),4,"One poured ingot produces exactly four first fittings");h.assertValueEqual(m.copper.total()+table.smeltery.filled,0,"No metal remains duplicated in the source or form");h.assertValueEqual(table.getItem(1).getCount(),1,"The starter fitting mold remains reusable");h.succeed();});
    }
    @GameTest(maxTicks=100) public void bronzeAndCastingConserveMetalAndKeepMolds(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,3));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Working hearth");m.copper.heat=1000;m.copper.copper=12*U;m.copper.tin=4*U;
        m.setItem(2,new ItemStack(Items.COAL));
        CopperWorkshops.tick(h.getLevel(),m);
        h.assertTrue(m.smeltery.fuelActive&&m.smeltery.fuelHeat>=600,"The physical firebox burns actual fuel at sufficient alloying heat");
        h.assertTrue(m.getItem(2).isEmpty(),"Alloying starts by consuming the supplied coal");
        h.assertValueEqual(m.copper.bronze,16*U,"3 copper + 1 tin = 4 bronze");h.assertValueEqual(m.copper.total(),16*U,"No alloy multiplication");
        var tablePos=origin.offset(-2,-1,1);set(h,tablePos,block("casting_table"));var table=(MachineEntity)h.getLevel().getBlockEntity(tablePos);m.smeltery.selected="bronze";table.setItem(1,new ItemStack(item("ingot_mold")));
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).isEmpty(),"Bare wall port does not pour");
        var faucet=tablePos.above();var closed=block("casting_faucet").defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.WEST);
        h.getLevel().setBlock(faucet,closed,Block.UPDATE_ALL);
        for(int i=0;i<110;i++)CopperWorkshops.tick(h.getLevel(),table);
        h.assertTrue(table.getItem(3).isEmpty(),"Closed faucet does not pour");
        h.getLevel().setBlock(faucet,closed.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);
        // The port budget is per real game tick; repeated direct calls in one tick cannot fill a mold.
        h.runAfterDelay(14,()->{
            h.assertValueEqual(table.smeltery.filled,4*U,"The open faucet fills one ingot mold over multiple real ticks");
            h.getLevel().setBlock(faucet,closed,Block.UPDATE_ALL);
        });
        h.runAfterDelay(70,()->{
            h.assertTrue(table.getItem(3).is(item("bronze_ingot")),"Connected table casts bronze");
            h.assertValueEqual(table.getItem(3).getCount(),1,"Closing the faucet permits only the completed first pour");
            h.assertTrue(table.getItem(1).is(item("ingot_mold")),"Mold is reusable");
            h.assertValueEqual(m.copper.total()+table.smeltery.filled+table.getItem(3).getCount()*4*U,16*U,"Casting conserves stored, in-flight and finished metal");h.succeed();
        });
    }
    @GameTest public void airPortNeedsOpenNozzle(GameTestHelper h){
        var origin=h.absolutePos(new BlockPos(6,2,2));var m=hearth(h,origin,3);h.assertTrue(m.tryAssemble(),"Formed hearth");
        var bellowsPos=origin.offset(3,0,1);set(h,bellowsPos,block("hand_bellows"));var bellows=(MachineEntity)h.getLevel().getBlockEntity(bellowsPos);
        h.assertFalse(CopperWorkshops.pump(h.getLevel(),bellows),"Port without a nozzle has no air connection");
        var pos=origin.offset(2,0,1);var nozzle=block("air_nozzle").defaultBlockState().setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING,Direction.EAST);
        h.getLevel().setBlock(pos,nozzle,Block.UPDATE_ALL);h.assertFalse(CopperWorkshops.pump(h.getLevel(),bellows),"Closed nozzle blocks air");
        h.getLevel().setBlock(pos,nozzle.setValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN,true),Block.UPDATE_ALL);h.assertTrue(CopperWorkshops.pump(h.getLevel(),bellows),"Open nozzle supplies air through wall port");h.assertValueEqual(m.copper.air,8,"Air budget preserved");h.succeed();
    }
    @GameTest public void loosePartsRotateOnlyWithShift(GameTestHelper h){
        var pos=h.absolutePos(new BlockPos(3,1,3));set(h,pos,block("casting_spout"));var player=h.makeMockServerPlayerInLevel();var tool=new ItemStack(item("configurator"));player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND,tool);
        var hit=new net.minecraft.world.phys.BlockHitResult(net.minecraft.world.phys.Vec3.atCenterOf(pos),Direction.UP,pos,false);
        player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);h.assertTrue(h.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING)==Direction.NORTH,"Normal click never turns part");
        player.setShiftKeyDown(true);player.gameMode.useItemOn(player,h.getLevel(),tool,net.minecraft.world.InteractionHand.MAIN_HAND,hit);h.assertTrue(h.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING)==Direction.EAST,"Sneak item path rotates exactly once");h.succeed();
    }

    @GameTest public void workshopAdvancementTreeLoadsAndClayUnlocksItsRoot(GameTestHelper h){
        var manager=h.getLevel().getServer().getAdvancements();
        for(String name:java.util.List.of("root","pottery","foundry","casting","kiln","bench","tools","hollow","washing","water","pressing","fiber","cart","crane","ropeway","bronze")){
            var node=manager.get(ModBlocks.id("copper/"+name));h.assertTrue(node!=null,"Workshop advancement loads: "+name);h.assertTrue(node.value().display().isPresent(),"Workshop node has a display: "+name);
            if(!name.equals("root"))h.assertTrue(node.value().parent().isPresent()&&manager.get(node.value().parent().get())!=null,"Every branch has a loaded parent: "+name);
        }
        h.assertTrue(CopperTests.class.getResource("/assets/smog/textures/block/refractory_bricks.png")!=null,"Requested refractory brick background exists");
        var toolIcon=manager.get(ModBlocks.id("copper/tools")).value().display().orElseThrow().icon();h.assertTrue(toolIcon.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA)!=null&&toolIcon.get(net.minecraft.core.component.DataComponents.CUSTOM_MODEL_DATA).strings().contains("pickaxe_head"),"Tool advancement supplies visible assembled icon layers, not an empty modular shell");
        var player=h.makeMockServerPlayerInLevel();var clay=new ItemStack(Items.CLAY_BALL);player.getInventory().setItem(0,clay);
        net.minecraft.advancements.triggers.CriteriaTriggers.INVENTORY_CHANGED.trigger(player,player.getInventory(),clay);
        h.assertTrue(player.getAdvancements().getOrStartProgress(manager.get(ModBlocks.id("copper/root"))).isDone(),"Obtaining clay really unlocks the workshop root");h.succeed();
    }
}
