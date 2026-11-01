package io.github.kpaakeh.smog.copper;

import com.google.gson.JsonObject;
import java.util.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.*;
import net.minecraft.world.level.block.state.BlockState;

/** Profile-driven material behavior. No extra drops, tier changes or regenerated metal. */
public final class ToolTraits {
    public record Trait(String id,JsonObject parameters,ToolDefinitions.Part source) {}
    private static List<Trait> fromPart(ToolDefinitions.Part part){
        if(part==null||part.cast())return List.of();var profile=part.material().profile(part.form());
        if(!profile.has("traits"))return List.of();var result=new ArrayList<Trait>();
        for(var value:profile.getAsJsonArray("traits")){var p=value.getAsJsonObject();result.add(new Trait(p.get("id").getAsString(),p,part));}return result;
    }
    public static List<Trait> traits(ItemStack stack){
        var direct=ToolDefinitions.part(stack);if(direct!=null)return fromPart(direct);
        if(!ModularToolItem.configured(stack))return List.of();var result=new ArrayList<Trait>();
        // Read stored definitions directly: querying speed must not repeatedly recalculate head wear.
        for(var part:stack.getOrDefault(DataComponents.CONTAINER,ItemContainerContents.EMPTY).nonEmptyItemCopyStream().toList())result.addAll(fromPart(ToolDefinitions.part(part)));
        return List.copyOf(result);
    }
    public static void validate(JsonObject profile){
        if(!profile.has("traits"))return;
        for(var value:profile.getAsJsonArray("traits")){var p=value.getAsJsonObject();String id=p.get("id").getAsString();
            if(!Set.of("work_hardening","staggering","marking","grip").contains(id))throw new IllegalArgumentException("Unknown tool trait: "+id);
            for(String key:List.of("max_stacks","speed_per_stack","timeout_ticks","duration_ticks","cooldown_ticks","every"))if(p.has(key)){float n=ToolDefinitions.number(p,key,1);if(n<=0||n>12000)throw new IllegalArgumentException("Invalid trait parameter: "+key);}
        }
    }
    private static int number(Trait t,String key,int fallback){return (int)ToolDefinitions.number(t.parameters(),key,fallback);}
    private static boolean matches(Trait t,BlockState state){String tag=t.source().form().miningTag();return !tag.isEmpty()&&state.is(TagKey.create(Registries.BLOCK,Identifier.parse(tag)));}
    private static void save(ItemStack stack,net.minecraft.nbt.CompoundTag data){stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));}
    public static float miningSpeed(ItemStack stack,BlockState state,float speed){
        int count=ModularToolItem.parts(stack).getIntOr("trait_work_stacks",0);float bonus=0;
        for(var t:traits(stack))if(t.id().equals("work_hardening")&&matches(t,state))bonus=Math.max(bonus,Math.min(count,number(t,"max_stacks",4))*ToolDefinitions.number(t.parameters(),"speed_per_stack",.04f));
        return speed*(1+bonus);
    }
    public static void mined(ItemStack stack,ServerLevel level,BlockState state){
        if(!stack.isCorrectToolForDrops(state))return;
        for(var t:traits(stack))if(t.id().equals("work_hardening")&&matches(t,state)){
            var data=ModularToolItem.parts(stack);long now=level.getGameTime();int count=now>data.getLongOr("trait_work_until",0)?0:data.getIntOr("trait_work_stacks",0);
            data.putInt("trait_work_stacks",Math.min(number(t,"max_stacks",4),count+1));data.putLong("trait_work_until",now+number(t,"timeout_ticks",100));save(stack,data);return;
        }
    }
    public static void tick(ItemStack stack,ServerLevel level){var data=ModularToolItem.parts(stack);if(data.getIntOr("trait_work_stacks",0)>0&&level.getGameTime()>data.getLongOr("trait_work_until",0)){data.remove("trait_work_stacks");data.remove("trait_work_until");save(stack,data);}}
    public static void hit(ItemStack stack,LivingEntity target,LivingEntity attacker){
        if(!(attacker.level() instanceof ServerLevel level)||target instanceof net.minecraft.world.entity.player.Player)return;
        var seen=new HashSet<String>();
        for(var t:traits(stack)){if(!seen.add(t.id())||!Set.of("staggering","marking").contains(t.id()))continue;var data=ModularToolItem.parts(stack);String key="trait_"+t.id()+"_until";
            if(level.getGameTime()<data.getLongOr(key,0))continue;
            target.addEffect(new MobEffectInstance(t.id().equals("staggering")?MobEffects.SLOWNESS:MobEffects.GLOWING,number(t,"duration_ticks",20),0));
            data.putLong(key,level.getGameTime()+number(t,"cooldown_ticks",60));save(stack,data);
        }
    }
    /** Saves wear on every Nth successful native use, never restores existing damage. */
    public static int useWear(ItemStack stack){
        for(var t:traits(stack))if(t.id().equals("grip")){var data=ModularToolItem.parts(stack);int every=Math.max(2,number(t,"every",4)),count=(data.getIntOr("trait_grip_uses",0)+1)%every;data.putInt("trait_grip_uses",count);save(stack,data);return count==0?0:1;}return 1;
    }
    public static void tooltip(ItemStack stack,java.util.function.Consumer<Component> lines){var seen=new HashSet<String>();for(var t:traits(stack))if(seen.add(t.id()))lines.accept(Component.translatable("tool.smog.trait."+t.id()));}
}
