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
    public ModularToolItem(Properties properties){super(properties.stacksTo(1).durability(250));}
    public static CompoundTag parts(ItemStack stack){return stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();}
    public static boolean configured(ItemStack stack){return stack.getItem() instanceof ModularToolItem&&ToolParts.schema(stack)!=null&&stack.has(DataComponents.CONTAINER);}
    public static boolean broken(ItemStack stack){return !configured(stack)||stack.getDamageValue()>=stack.getMaxDamage();}
    public static void wear(ItemStack stack,Level level,int amount){
        if(!(level instanceof ServerLevel server)||broken(stack)||stack.has(DataComponents.UNBREAKABLE))return;
        int wear=net.minecraft.world.item.enchantment.EnchantmentHelper.processDurabilityChange(server,stack,amount);
        stack.setDamageValue(Math.min(stack.getMaxDamage(),stack.getDamageValue()+wear));if(broken(stack))stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);
    }
    @Override public void inventoryTick(ItemStack stack,ServerLevel level,net.minecraft.world.entity.Entity holder,net.minecraft.world.entity.EquipmentSlot slot){
        if(broken(stack)){stack.remove(DataComponents.ATTRIBUTE_MODIFIERS);return;}
        if(!stack.has(DataComponents.ATTRIBUTE_MODIFIERS)){var restored=ToolParts.assemble(ToolParts.schema(stack),ToolParts.components(stack));if(restored.has(DataComponents.ATTRIBUTE_MODIFIERS))stack.set(DataComponents.ATTRIBUTE_MODIFIERS,restored.get(DataComponents.ATTRIBUTE_MODIFIERS));}
    }
    @Override public void hurtEnemy(ItemStack stack,LivingEntity target,LivingEntity attacker){if(!(attacker instanceof net.minecraft.world.entity.player.Player p&&p.isCreative()))wear(stack,attacker.level(),2);}
    @Override public float getDestroySpeed(ItemStack stack,BlockState state){return broken(stack)?.2f:super.getDestroySpeed(stack,state);}
    @Override public boolean isCorrectToolForDrops(ItemStack stack,BlockState state){return !broken(stack)&&super.isCorrectToolForDrops(stack,state);}
    @Override public boolean mineBlock(ItemStack stack,Level level,BlockState state,BlockPos pos,LivingEntity user){if(!level.isClientSide()&&state.getDestroySpeed(level,pos)!=0&&!(user instanceof net.minecraft.world.entity.player.Player p&&p.isCreative()))wear(stack,level,1);return true;}
    @Override public void postHurtEnemy(ItemStack stack,LivingEntity target,LivingEntity attacker){}
    @Override public InteractionResult useOn(UseOnContext context){
        var stack=context.getItemInHand();if(broken(stack))return InteractionResult.PASS;
        String name=ToolParts.schema(stack).behavior();
        if(name.equals("chisel")){
            var level=context.getLevel();var pos=context.getClickedPos();var old=level.getBlockState(pos);
            String block=BuiltInRegistries.BLOCK.getKey(old.getBlock()).toString();String result=switch(block){case "smog:firebrick_casing"->"firebrick_tiles";case "smog:firebrick_tiles"->"carved_firebrick";case "smog:carved_firebrick"->"firebrick_casing";default->null;};
            if(result==null)return InteractionResult.PASS;
            if(!level.isClientSide()){level.setBlock(pos,BuiltInRegistries.BLOCK.getValue(ModBlocks.id(result)).defaultBlockState(),net.minecraft.world.level.block.Block.UPDATE_ALL);if(context.getPlayer()==null||!context.getPlayer().isCreative())wear(stack,level,1);}
            return InteractionResult.SUCCESS;
        }
        var delegates=new java.util.LinkedHashSet<Item>();for(var component:ToolParts.components(stack)){var definition=ToolDefinitions.part(component);if(definition!=null&&!definition.form().nativeSuffix().isEmpty()){String prefix=definition.material().nativePrefix().isEmpty()?"iron":definition.material().nativePrefix();var id=net.minecraft.resources.Identifier.withDefaultNamespace(prefix+"_"+definition.form().nativeSuffix());if(BuiltInRegistries.ITEM.containsKey(id))delegates.add(BuiltInRegistries.ITEM.getValue(id));}}if(delegates.isEmpty())return InteractionResult.PASS;
        // Vanilla use actions may damage their stack. Keep the assembly and apply native Unbreaking ourselves.
        boolean had=stack.has(DataComponents.UNBREAKABLE);stack.set(DataComponents.UNBREAKABLE,net.minecraft.util.Unit.INSTANCE);
        InteractionResult result;
        try{result=InteractionResult.PASS;for(var delegate:delegates){result=delegate.useOn(context);if(result.consumesAction())break;}}finally{if(!had)stack.remove(DataComponents.UNBREAKABLE);}
        if(result.consumesAction()&&(context.getPlayer()==null||!context.getPlayer().isCreative()))wear(stack,context.getLevel(),1);return result;
    }
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){
        super.appendHoverText(stack,context,display,lines,flag);
        if(!configured(stack)){lines.accept(Component.translatable("tool.smog.unconfigured"));return;}
        var schema=ToolParts.schema(stack);var components=ToolParts.components(stack);
        for(int i=0;i<components.size();i++)if(!components.get(i).isEmpty())lines.accept(Component.translatable("tool.smog.part_"+schema.roles().get(i).category(),components.get(i).getHoverName()));
        for(var component:components){var head=ToolDefinitions.part(component);if(head==null||!head.form().category().equals("head"))continue;var profile=head.material().profile(head.form());lines.accept(Component.translatable("tool.smog.tier",Component.translatable("tool.smog.tier."+ToolDefinitions.string(profile,"harvest_tier","wooden"))));}
        var mining=stack.get(DataComponents.TOOL);if(mining!=null)lines.accept(Component.translatable("tool.smog.speed",String.format(java.util.Locale.ROOT,"%.2f",mining.rules().stream().flatMap(r->r.speed().stream()).max(Float::compare).orElse(1f))));
        lines.accept(Component.translatable(broken(stack)?"tool.smog.broken":"tool.smog.durability",Math.max(0,stack.getMaxDamage()-stack.getDamageValue()),stack.getMaxDamage()));
    }
}
