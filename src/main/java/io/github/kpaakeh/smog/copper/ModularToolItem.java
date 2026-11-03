package io.github.kpaakeh.smog.copper;

import java.util.*;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import io.github.kpaakeh.smog.registry.ModBlocks;

/** Native tool components determine harvesting. A spent head disables work without deleting the assembly. */
public final class ModularToolItem extends Item {
    public ModularToolItem(Properties properties){super(properties.stacksTo(1).durability(250).component(DataComponents.TOOLTIP_DISPLAY,TooltipDisplay.DEFAULT.withHidden(DataComponents.ATTRIBUTE_MODIFIERS,true).withHidden(DataComponents.CONTAINER,true)));}
    @Override public Component getName(ItemStack stack){
        if(!configured(stack))return super.getName(stack);
        var schema=ToolParts.schema(stack);var components=ToolParts.components(stack);var head=ToolDefinitions.part(components.get(ToolDefinitions.primary(schema)));
        if(head==null)return super.getName(stack);
        return Component.translatable("tool.smog.named."+schema.id()+"."+head.material().id());
    }
    public static CompoundTag parts(ItemStack stack){return stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();}
    public static boolean configured(ItemStack stack){return stack.getItem() instanceof ModularToolItem&&ToolParts.schema(stack)!=null&&stack.has(DataComponents.CONTAINER);}
    public static boolean broken(ItemStack stack){return !configured(stack)||stack.getDamageValue()>=stack.getMaxDamage();}
    public static void wear(ItemStack stack,Level level,int amount){
        if(!(level instanceof ServerLevel server)||broken(stack)||stack.has(DataComponents.UNBREAKABLE))return;
        int wear=net.minecraft.world.item.enchantment.EnchantmentHelper.processDurabilityChange(server,stack,amount);
        stack.setDamageValue(Math.min(stack.getMaxDamage(),stack.getDamageValue()+wear));if(broken(stack))stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
    }
    @Override public void inventoryTick(ItemStack stack,ServerLevel level,net.minecraft.world.entity.Entity holder,net.minecraft.world.entity.EquipmentSlot slot){
        ToolTraits.tick(stack,level);
        if(broken(stack)){stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);return;}
        if(!stack.has(DataComponents.ATTRIBUTE_MODIFIERS)){var restored=ToolParts.assemble(ToolParts.schema(stack),ToolParts.components(stack));if(restored.has(DataComponents.ATTRIBUTE_MODIFIERS))stack.set(DataComponents.ATTRIBUTE_MODIFIERS,restored.get(DataComponents.ATTRIBUTE_MODIFIERS));}
    }
    @Override public void hurtEnemy(ItemStack stack,LivingEntity target,LivingEntity attacker){if(!(attacker instanceof net.minecraft.world.entity.player.Player p&&p.isCreative())){if(!broken(stack))ToolTraits.hit(stack,target,attacker);wear(stack,attacker.level(),2);}}
    @Override public float getDestroySpeed(ItemStack stack,BlockState state){return broken(stack)?.2f:ToolTraits.miningSpeed(stack,state,super.getDestroySpeed(stack,state));}
    @Override public boolean isCorrectToolForDrops(ItemStack stack,BlockState state){return !broken(stack)&&super.isCorrectToolForDrops(stack,state);}
    @Override public boolean mineBlock(ItemStack stack,Level level,BlockState state,BlockPos pos,LivingEntity user){if(!level.isClientSide()&&state.getDestroySpeed(level,pos)!=0&&!(user instanceof net.minecraft.world.entity.player.Player p&&p.isCreative())){if(level instanceof ServerLevel server&&!broken(stack))ToolTraits.mined(stack,server,state);wear(stack,level,1);if(user instanceof net.minecraft.server.level.ServerPlayer player)ToolActions.afterMined(stack,(ServerLevel)level,state,pos,player);}return true;}
    @Override public void postHurtEnemy(ItemStack stack,LivingEntity target,LivingEntity attacker){}
    @Override public InteractionResult useOn(UseOnContext context){
        var stack=context.getItemInHand();if(broken(stack))return InteractionResult.PASS;
        String name=ToolParts.schema(stack).behavior();
        if(name.equals("sickle"))return ToolActions.harvest(context);
        var delegates=new java.util.LinkedHashSet<Item>();for(var component:ToolParts.components(stack)){var definition=ToolDefinitions.part(component);if(definition!=null&&!definition.form().nativeSuffix().isEmpty()){String prefix=definition.material().nativePrefix().isEmpty()?"iron":definition.material().nativePrefix();var id=net.minecraft.resources.Identifier.withDefaultNamespace(prefix+"_"+definition.form().nativeSuffix());if(BuiltInRegistries.ITEM.containsKey(id))delegates.add(BuiltInRegistries.ITEM.getValue(id));}}if(delegates.isEmpty())return InteractionResult.PASS;
        // Vanilla use actions may damage their stack. Keep the assembly and apply native Unbreaking ourselves.
        var originalTransformer=stack.get(DataComponents.BLOCK_TRANSFORMER);boolean had=stack.has(DataComponents.UNBREAKABLE);stack.set(DataComponents.UNBREAKABLE,net.minecraft.util.Unit.INSTANCE);
        InteractionResult result;
        try{result=InteractionResult.PASS;for(var delegate:delegates){var transformer=delegate.getDefaultInstance().get(DataComponents.BLOCK_TRANSFORMER);if(transformer==null)continue;stack.set(DataComponents.BLOCK_TRANSFORMER,transformer);result=delegate.useOn(context);if(result.consumesAction())break;}}finally{if(originalTransformer==null)stack.remove(DataComponents.BLOCK_TRANSFORMER);else stack.set(DataComponents.BLOCK_TRANSFORMER,originalTransformer);if(!had)stack.remove(DataComponents.UNBREAKABLE);}
        if(result.consumesAction()&&(context.getPlayer()==null||!context.getPlayer().isCreative()))wear(stack,context.getLevel(),context.getLevel().isClientSide()?1:ToolTraits.useWear(stack));return result;
    }
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);
        ToolTooltip.append(stack,lines);
    }
}
