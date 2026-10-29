package io.github.kpaakeh.smog.copper;

import java.util.*;
import io.github.kpaakeh.smog.machine.MachineEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

/** The same workbench accepts every JSON template and any number of component roles. */
public final class ToolAssembly {
    public static boolean handles(MachineEntity m){return CopperWorkshops.workshop(m,"tool_workbench");}
    public static ToolDefinitions.Template schema(Container inventory){return !inventory.getItem(5).isEmpty()?ToolParts.schema(inventory.getItem(5)):inventory instanceof MachineEntity machine?ToolDefinitions.at(machine.copper.selection):ToolDefinitions.at(0);}
    public static boolean accepts(MachineEntity machine,int slot,ItemStack stack){
        if(slot==0&&CopperWorkshops.is(stack,"smog:molding_sand")||slot==1&&CopperWorkshops.is(machine.getItem(0),"smog:molding_sand")&&MoldMaking.pattern(stack)!=null)return true;
        if(slot==5)return canMount(stack);if(slot==0&&(ToolParts.nativeSource(stack)!=null||ToolDefinitions.parts().stream().anyMatch(p->ToolParts.materialIngredient(stack,p.material()))))return true;
        int role=ToolDefinitions.role(slot);var schema=schema(machine);return role>=0&&role<schema.roles().size()&&schema.roles().get(role).accepts(ToolDefinitions.part(stack));
    }
    public static boolean canMount(ItemStack tool){return ModularToolItem.configured(tool)&&ToolParts.schema(tool).roles().size()<=ToolDefinitions.maxParts();}
    private static boolean empty(Container inventory,int except){for(int i=0;i<ToolDefinitions.maxParts();i++){int slot=ToolDefinitions.slot(i);if(slot!=except&&!inventory.getItem(slot).isEmpty())return false;}return true;}
    public static List<ItemStack> selected(Container inventory,ToolDefinitions.Template schema){var mounted=inventory.getItem(5);var old=mounted.isEmpty()?List.<ItemStack>of():ToolParts.components(mounted);List<ItemStack> parts=new ArrayList<>();for(int i=0;i<schema.roles().size();i++){var added=inventory.getItem(ToolDefinitions.slot(i));parts.add(!added.isEmpty()?added:i<old.size()?old.get(i):ItemStack.EMPTY);}return parts;}
    public static boolean shapingMode(Container inventory){return inventory.getItem(5).isEmpty()&&empty(inventory,0)&&!ToolParts.shapingChoices(inventory.getItem(0)).isEmpty();}
    public static int activePart(MachineEntity machine){var choices=ToolParts.shapingChoices(machine.getItem(0));if(choices.isEmpty())return 0;if(machine.copper.partSelection>=0)return Math.floorMod(machine.copper.partSelection,choices.size());var role=schema(machine).roles().get(ToolDefinitions.primary(schema(machine)));for(int i=0;i<choices.size();i++)if(role.accepts(choices.get(i)))return i;return 0;}
    public static ToolParts.Shaping shapingPlan(MachineEntity machine){var choices=ToolParts.shapingChoices(machine.getItem(0));return choices.isEmpty()?null:ToolParts.shaping(machine.getItem(0),choices.get(activePart(machine)));}
    public static ItemStack preview(Container inventory){return preview(inventory,schema(inventory),inventory instanceof MachineEntity machine?activePart(machine):-1);}
    public static ItemStack preview(Container inventory,ToolDefinitions.Template schema){return preview(inventory,schema,-1);}
    public static ItemStack preview(Container inventory,ToolDefinitions.Template schema,int selectedPart){
        var imprint=MoldMaking.preview(inventory);if(!imprint.isEmpty())return imprint;
        var mounted=inventory.getItem(5);if(!mounted.isEmpty())schema=ToolParts.schema(mounted);for(int i=schema.roles().size();i<ToolDefinitions.maxParts();i++)if(!inventory.getItem(ToolDefinitions.slot(i)).isEmpty())return ItemStack.EMPTY;var input=inventory.getItem(0);
        if(mounted.isEmpty()&&empty(inventory,0)){var finished=ToolParts.finish(input);if(!finished.isEmpty())return finished;var choices=ToolParts.shapingChoices(input);var shaped=selectedPart>=0&&!choices.isEmpty()?ToolParts.shaping(input,choices.get(Math.floorMod(selectedPart,choices.size()))):ToolParts.shaping(input,schema);if(shaped!=null&&input.getCount()>=shaped.count())return shaped.result();}
        if(!mounted.isEmpty()&&!input.isEmpty()&&ToolDefinitions.part(input)==null&&empty(inventory,0))return ToolParts.repair(mounted,input);
        return ToolParts.preserveIdentity(mounted,ToolParts.assemble(schema,selected(inventory,schema)));
    }
    public static Component problem(Container inventory,ToolDefinitions.Template schema){
        if(!inventory.getItem(5).isEmpty())schema=ToolParts.schema(inventory.getItem(5));
        for(int i=schema.roles().size();i<ToolDefinitions.maxParts();i++)if(!inventory.getItem(ToolDefinitions.slot(i)).isEmpty())return Component.translatable("tool.smog.extra_parts");
        String role=ToolParts.problem(schema,selected(inventory,schema));
        return role.isEmpty()?Component.translatable("tool.smog.missing_parts"):Component.translatable("tool.smog.need_role",Component.translatableWithFallback("tool.smog.role."+role,role));
    }
    public static boolean button(MachineEntity machine,int id){if((id!=20&&id!=21)||machine.copper.queued>0||!machine.getItem(5).isEmpty())return false;if(shapingMode(machine)){machine.copper.partSelection=Math.floorMod(activePart(machine)+(id==20?-1:1),ToolParts.shapingChoices(machine.getItem(0)).size());machine.sync();return true;}if(!empty(machine,-1))return false;machine.copper.partSelection=-1;machine.copper.selection=Math.floorMod(machine.copper.selection+(id==20?-1:1),ToolDefinitions.templates().size());machine.sync();return true;}
    public static void tick(ServerLevel level,MachineEntity m){
        if(m.copper.queued==0)return;var schema=schema(m);var result=preview(m);
        if(result.isEmpty()){m.copper.work=0;m.copper.queued=0;m.sync();return;}if(!m.getItem(3).isEmpty()){m.copper.status=5;return;}if(++m.copper.work<40){m.setChanged();return;}
        var mounted=m.getItem(5);boolean repair=!mounted.isEmpty()&&!m.getItem(0).isEmpty()&&ToolDefinitions.part(m.getItem(0))==null;
        if(!mounted.isEmpty()&&!repair)for(int i=0;i<schema.roles().size();i++){int slot=ToolDefinitions.slot(i);if(!m.getItem(slot).isEmpty()){var old=ToolParts.extract(mounted,slot);if(!old.isEmpty())Containers.dropItemStack(level,m.getBlockPos().getX()+.5,m.getBlockPos().getY()+1,m.getBlockPos().getZ()+.5,old);}}
        boolean imprint=!MoldMaking.preview(m).isEmpty();
        var shaping=shapingMode(m)?shapingPlan(m):null;
        for(int i=0;i<schema.roles().size();i++)if(!(imprint&&i==1)&&!m.getItem(ToolDefinitions.slot(i)).isEmpty())m.getItem(ToolDefinitions.slot(i)).shrink(i==0&&shaping!=null?shaping.count():1);
        m.setItem(5,ItemStack.EMPTY);m.setItem(3,result);m.copper.queued=0;m.copper.work=0;m.copper.status=0;m.sync();
    }
    private static void give(Player player,ItemStack stack){player.getInventory().add(stack);if(!stack.isEmpty())Containers.dropItemStack(player.level(),player.getX(),player.getY(),player.getZ(),stack);}
    public static InteractionResult use(ServerLevel level,MachineEntity m,Player player,InteractionHand hand){
        var held=player.getItemInHand(hand);if(m.copper.queued>0){player.sendOverlayMessage(Component.translatable("tool.smog.working"));return InteractionResult.SUCCESS;}
        if(ToolParts.hammer(held)){
            if(player.isShiftKeyDown()&&!m.getItem(5).isEmpty()&&empty(m,-1)){var old=ToolParts.components(m.getItem(5));for(int i=0;i<old.size();i++)m.setItem(ToolDefinitions.slot(i),old.get(i));m.setItem(5,ItemStack.EMPTY);m.sync();return InteractionResult.SUCCESS;}
            var result=preview(m);if(result.isEmpty()||!m.getItem(3).isEmpty()){player.sendOverlayMessage(Component.translatable("tool.smog.assembly_help"));return InteractionResult.SUCCESS;}
            m.copper.queued=1;m.copper.work=0;if(!player.isCreative()){if(held.getItem() instanceof ModularToolItem)ModularToolItem.wear(held,level,1);else held.hurtAndBreak(1,player,net.minecraft.world.entity.EquipmentSlot.MAINHAND);}m.sync();return InteractionResult.SUCCESS;
        }
        if(player.isShiftKeyDown()&&!held.isEmpty()){player.openMenu(m);return InteractionResult.SUCCESS;}
        if(held.isEmpty()){
            if(!m.getItem(3).isEmpty()){give(player,m.removeItemNoUpdate(3));m.sync();return InteractionResult.SUCCESS;}
            if(player.isShiftKeyDown()){
                for(int i=ToolDefinitions.maxParts()-1;i>=0;i--){int slot=ToolDefinitions.slot(i);if(!m.getItem(slot).isEmpty()){give(player,m.removeItemNoUpdate(slot));m.sync();return InteractionResult.SUCCESS;}}
                if(!m.getItem(5).isEmpty()){give(player,m.removeItemNoUpdate(5));m.sync();return InteractionResult.SUCCESS;}button(m,21);
            }
            var result=preview(m);player.sendOverlayMessage(result.isEmpty()?Component.translatable("tool.smog.selected_schema",Component.translatable("item.smog.modular_"+schema(m).id())):Component.translatable("tool.smog.preview",result.getHoverName(),result.getMaxDamage()));return InteractionResult.SUCCESS;
        }
        if(m.getItem(5).isEmpty()&&canMount(held)){var template=ToolParts.schema(held);m.copper.selection=ToolDefinitions.index(template.id());m.setItem(5,held.split(1));m.sync();return InteractionResult.SUCCESS;}
        var input=m.getItem(0);
        if(shapingMode(m)&&ToolDefinitions.part(input)==null&&ItemStack.isSameItemSameComponents(input,held)&&input.getCount()<Math.min(input.getMaxStackSize(),m.getMaxStackSize(input))){input.grow(1);held.shrink(1);m.sync();return InteractionResult.SUCCESS;}
        for(int i=0;i<ToolDefinitions.maxParts();i++){int slot=ToolDefinitions.slot(i);if(m.getItem(slot).isEmpty()&&accepts(m,slot,held)){m.setItem(slot,held.split(1));m.sync();return InteractionResult.SUCCESS;}}
        return InteractionResult.PASS;
    }
}
