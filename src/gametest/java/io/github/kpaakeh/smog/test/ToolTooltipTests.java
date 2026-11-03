package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.copper.*;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TooltipDisplay;

public final class ToolTooltipTests {
    private static boolean section(java.util.List<Component> lines,String name){return lines.stream().anyMatch(line->line.getContents() instanceof TranslatableContents text&&text.getKey().equals("tool.smog.tip."+name));}
    @GameTest public void viewsStayCompactAndExposeOnlyRequestedDetails(GameTestHelper h){
        var tool=ToolParts.example(ToolDefinitions.template("pickaxe"),"copper");var before=tool.copy();h.assertFalse(tool.has(DataComponents.TOOLTIP_STYLE),"Tools use vanilla tooltip background and frame");
        var overview=ToolTooltip.lines(tool,0);var properties=ToolTooltip.lines(tool,ToolTooltip.PROPERTIES);var construction=ToolTooltip.lines(tool,ToolTooltip.CONSTRUCTION);
        h.assertTrue(overview.size()<=7,"Overview is a short tooltip, not the full property dump");
        h.assertFalse(section(overview,"properties")||section(overview,"construction"),"Default view hides expanded sections");
        h.assertTrue(section(properties,"properties")&&!section(properties,"construction"),"Ctrl selects properties only");
        h.assertTrue(section(construction,"construction")&&!section(construction,"properties"),"Alt selects construction only");
        h.assertTrue(properties.size()>overview.size()&&construction.size()>overview.size(),"Both keys reveal actual extra information");
        h.assertTrue(ItemStack.matches(before,tool),"Reading every view never changes wear, parts or runtime traits");
        h.assertFalse(tool.getOrDefault(DataComponents.TOOLTIP_DISPLAY,TooltipDisplay.DEFAULT).shows(DataComponents.ATTRIBUTE_MODIFIERS),"Vanilla attributes are hidden to avoid duplicate combat numbers");
        h.assertFalse(tool.getOrDefault(DataComponents.TOOLTIP_DISPLAY,TooltipDisplay.DEFAULT).shows(DataComponents.CONTAINER),"Vanilla container contents cannot leak construction into the compact view");h.succeed();
    }
    @GameTest public void everyToolAndEveryPartHasAllThreeReadableViews(GameTestHelper h){
        for(var schema:ToolDefinitions.templates())for(String material:java.util.List.of("wood","stone","copper","iron","gold")){
            var tool=ToolParts.example(schema,material);h.assertFalse(tool.isEmpty(),"Example exists: "+schema.id()+"/"+material);
            for(int mode=0;mode<4;mode++)h.assertTrue(!ToolTooltip.lines(tool,mode).isEmpty(),"Configured tools always have useful text");
        }
        for(var part:ToolDefinitions.parts()){
            var stack=new ItemStack(ToolParts.item(part.item().toString()));h.assertFalse(stack.has(DataComponents.TOOLTIP_STYLE),"Parts use vanilla tooltip styling");
            h.assertTrue(section(ToolTooltip.lines(stack,ToolTooltip.PROPERTIES),"properties"),"Every part supports properties");
            h.assertTrue(section(ToolTooltip.lines(stack,ToolTooltip.CONSTRUCTION),"construction"),"Every part supports construction");
        }
        h.succeed();
    }
}
