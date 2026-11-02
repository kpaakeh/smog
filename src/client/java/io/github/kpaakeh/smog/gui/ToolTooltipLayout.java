package io.github.kpaakeh.smog.gui;

import java.util.*;
import java.util.regex.Pattern;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.*;

/** Word wrapping preserves material colors and keeps expanded views inside narrow windows. */
public final class ToolTooltipLayout {
    private static final Pattern WORDS=Pattern.compile("\\S+\\s*|\\s+");
    private ToolTooltipLayout(){}
    public static List<Component> wrap(List<Component> lines,Font font,int width){
        var output=new ArrayList<Component>();
        for(int index=0;index<lines.size();index++){
            var line=lines.get(index);if(index==0||font.width(line)<=width){output.add(line);continue;}
            MutableComponent[] current={Component.empty()};
            line.visit((style,text)->{
                var words=WORDS.matcher(text);
                while(words.find()){
                    var word=Component.literal(words.group()).setStyle(style);
                    if(!current[0].getString().isBlank()&&font.width(current[0].copy().append(word))>width){output.add(current[0]);current[0]=Component.empty();}
                    if(!current[0].getString().isEmpty()||!words.group().isBlank())current[0].append(word);
                }
                return Optional.empty();
            },Style.EMPTY);
            output.add(current[0]);
        }
        return List.copyOf(output);
    }
}
