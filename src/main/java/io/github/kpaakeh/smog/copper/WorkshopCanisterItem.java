package io.github.kpaakeh.smog.copper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
public final class WorkshopCanisterItem extends Item {
    public WorkshopCanisterItem(Properties properties){super(properties.stacksTo(1));}
    @Override public void appendHoverText(ItemStack stack,TooltipContext context,TooltipDisplay display,java.util.function.Consumer<Component> lines,TooltipFlag flag){super.appendHoverText(stack,context,display,lines,flag);var data=stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();var id=Identifier.tryParse(data.getStringOr("smog_fluid",""));if(id!=null)lines.accept(Component.translatable("block."+id.getNamespace()+"."+id.getPath()));lines.accept(Component.literal((data.getIntOr("smog_fluid_units",0)/81)+" / 8000 mB"));}
}
