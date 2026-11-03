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
import net.minecraft.world.level.Level;

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
        var seen=new HashSet<String>();
        for(var value:profile.getAsJsonArray("traits")){var p=value.getAsJsonObject();String id=p.get("id").getAsString();
            if(!Set.of("work_hardening","staggering","marking","grip","cultivation","impact","magnetic","quenching","sapping","breathing","insulation").contains(id))throw new IllegalArgumentException("Unknown tool trait: "+id);
            if(!seen.add(id))throw new IllegalArgumentException("Duplicate tool trait: "+id);
            var allowed=switch(id){case "work_hardening"->Set.of("id","max_stacks","speed_per_stack","timeout_ticks");case "grip"->Set.of("id","every");default->Set.of("id","duration_ticks","cooldown_ticks");};
            for(String key:p.keySet())if(!allowed.contains(key))throw new IllegalArgumentException("Unknown parameter for "+id+": "+key);
            switch(id){
                case "work_hardening"->{int stacks=integer(p,"max_stacks",4,1,64);integer(p,"timeout_ticks",100,1,12000);double speed=p.has("speed_per_stack")?numeric(p,"speed_per_stack"):0.04;
                    if(!Double.isFinite(speed)||speed<=0||speed*stacks>1)throw new IllegalArgumentException("Work-hardening bonus must be positive and at most 100%");}
                case "grip"->integer(p,"every",4,2,12000);
                default->{integer(p,"duration_ticks",20,1,12000);integer(p,"cooldown_ticks",60,0,12000);}
            }
        }
    }
    private static double numeric(JsonObject p,String key){var value=p.get(key);if(!value.isJsonPrimitive()||!value.getAsJsonPrimitive().isNumber())throw new IllegalArgumentException("Trait parameter must be numeric: "+key);return value.getAsDouble();}
    private static int integer(JsonObject p,String key,int fallback,int minimum,int maximum){
        if(!p.has(key))return fallback;numeric(p,key);final int result;
        try{result=p.get(key).getAsBigDecimal().intValueExact();}catch(ArithmeticException|NumberFormatException invalid){throw new IllegalArgumentException("Trait parameter must be a whole number: "+key,invalid);}
        if(result<minimum||result>maximum)throw new IllegalArgumentException("Trait parameter out of range: "+key);return result;
    }
    private static int number(Trait t,String key,int fallback){return integer(t.parameters(),key,fallback,0,12000);}
    private static boolean matches(Trait t,BlockState state){String tag=t.source().form().miningTag();return !tag.isEmpty()&&state.is(TagKey.create(Registries.BLOCK,Identifier.parse(tag)));}
    private static void save(ItemStack stack,net.minecraft.nbt.CompoundTag data){stack.set(DataComponents.CUSTOM_DATA,CustomData.of(data));}
    public static float miningSpeed(ItemStack stack,BlockState state,float speed){
        int count=Math.max(0,ModularToolItem.parts(stack).getIntOr("trait_work_stacks",0));float bonus=0;
        for(var t:traits(stack))if(t.id().equals("work_hardening")&&matches(t,state))bonus=Math.max(bonus,Math.min(count,number(t,"max_stacks",4))*ToolDefinitions.number(t.parameters(),"speed_per_stack",.04f));
        return speed*(1+bonus);
    }
    public static void mined(ItemStack stack,ServerLevel level,BlockState state){
        if(!stack.isCorrectToolForDrops(state))return;
        if(has(stack,"magnetic")){var data=ModularToolItem.parts(stack);data.putLong("trait_magnetic_until",level.getGameTime()+40);save(stack,data);}
        for(var t:traits(stack))if(t.id().equals("work_hardening")&&matches(t,state)){
            var data=ModularToolItem.parts(stack);long now=level.getGameTime();int count=now>=data.getLongOr("trait_work_until",0)?0:Math.clamp(data.getIntOr("trait_work_stacks",0),0,number(t,"max_stacks",4));
            data.putInt("trait_work_stacks",Math.min(number(t,"max_stacks",4),count+1));data.putLong("trait_work_until",now+number(t,"timeout_ticks",100));save(stack,data);return;
        }
    }
    /** Called with the player's actual world on both logical sides before mining speed is read. */
    public static void tick(ItemStack stack,Level level){var data=ModularToolItem.parts(stack);if(data.getIntOr("trait_work_stacks",0)>0&&level.getGameTime()>=data.getLongOr("trait_work_until",0)){data.remove("trait_work_stacks");data.remove("trait_work_until");save(stack,data);}}
    public static void hit(ItemStack stack,LivingEntity target,LivingEntity attacker){
        if(!(attacker.level() instanceof ServerLevel level)||target.level()!=level||!target.isAlive()||target instanceof net.minecraft.world.entity.player.Player)return;
        var seen=new HashSet<String>();
        for(var t:traits(stack)){if(!seen.add(t.id())||!Set.of("staggering","marking","impact","quenching","sapping").contains(t.id()))continue;var data=ModularToolItem.parts(stack);String key="trait_"+t.id()+"_until";
            if(level.getGameTime()<data.getLongOr(key,0))continue;
            boolean applied=switch(t.id()){
                case "impact"->{var away=target.position().subtract(attacker.position()).multiply(1,0,1).normalize().scale(.35);target.push(away.x,.08,away.z);yield true;}
                case "quenching"->{boolean burning=target.isOnFire();if(burning)target.clearFire();yield burning;}
                default->target.addEffect(new MobEffectInstance(t.id().equals("staggering")?MobEffects.SLOWNESS:t.id().equals("sapping")?MobEffects.WEAKNESS:MobEffects.GLOWING,number(t,"duration_ticks",20),0),attacker);
            };
            if(applied){
                data.putLong(key,level.getGameTime()+number(t,"cooldown_ticks",60));save(stack,data);
            }
        }
    }
    /** Saves wear on every Nth successful native use, never restores existing damage. */
    public static int useWear(ItemStack stack){
        int every=traits(stack).stream().filter(t->t.id().equals("grip")).mapToInt(t->Math.max(2,number(t,"every",4))).min().orElse(0);if(every==0)return 1;var data=ModularToolItem.parts(stack);int count=(Math.floorMod(data.getIntOr("trait_grip_uses",0),every)+1)%every;data.putInt("trait_grip_uses",count);save(stack,data);return count==0?0:1;
    }
    /** Cooldowns follow the part which grants the trait; dismantling is not a free reset. */
    public static void exportRuntime(ItemStack tool,ItemStack part){
        var data=ModularToolItem.parts(part);var runtime=ModularToolItem.parts(tool);boolean changed=false;
        for(var t:fromPart(ToolDefinitions.part(part))){
            if(Set.of("staggering","marking","impact","quenching","sapping").contains(t.id())){String key="trait_"+t.id()+"_until";data.putLong(key,Math.max(data.getLongOr(key,0),runtime.getLongOr(key,0)));changed=true;}
            if(t.id().equals("grip")){data.putInt("trait_grip_uses",runtime.getIntOr("trait_grip_uses",0));changed=true;}
        }if(changed)save(part,data);
    }
    public static void importRuntime(List<ItemStack> parts,ItemStack tool){
        var data=ModularToolItem.parts(tool);boolean changed=false,grip=false;
        for(var part:parts)for(var t:fromPart(ToolDefinitions.part(part))){var stored=ModularToolItem.parts(part);
            if(Set.of("staggering","marking","impact","quenching","sapping").contains(t.id())){String key="trait_"+t.id()+"_until";data.putLong(key,Math.max(data.getLongOr(key,0),stored.getLongOr(key,0)));changed=true;}
            if(t.id().equals("grip")&&!grip){data.putInt("trait_grip_uses",Math.floorMod(stored.getIntOr("trait_grip_uses",0),Math.max(2,number(t,"every",4))));changed=true;grip=true;}
        }if(changed)save(tool,data);
    }
    private static boolean has(ItemStack stack,String id){return traits(stack).stream().anyMatch(t->t.id().equals(id));}
    /** Passive effects require an equipped, usable tool; inventory storage never grants them. */
    public static void heldTick(ItemStack stack,ServerLevel level,LivingEntity holder){
        if(ModularToolItem.broken(stack)||(holder.getMainHandItem()!=stack&&holder.getOffhandItem()!=stack))return;
        if(has(stack,"insulation")&&holder.getTicksFrozen()>0)holder.setTicksFrozen(Math.max(0,holder.getTicksFrozen()-2));
        if(has(stack,"magnetic")&&level.getGameTime()<ModularToolItem.parts(stack).getLongOr("trait_magnetic_until",0)){
            for(var drop:level.getEntitiesOfClass(net.minecraft.world.entity.item.ItemEntity.class,holder.getBoundingBox().inflate(2.5),e->!e.hasPickUpDelay()&&e.isAlive()&&(e.getOwner()==null||e.getOwner()==holder))){
                var pull=holder.position().add(0,.5,0).subtract(drop.position());if(pull.lengthSqr()>.25)drop.setDeltaMovement(drop.getDeltaMovement().add(pull.normalize().scale(.035)));
            }
        }
    }
    public static void harvested(ItemStack stack,LivingEntity holder,BlockState state){
        if(stack.isCorrectToolForDrops(state)&&has(stack,"breathing")&&holder.isUnderWater())holder.setAirSupply(Math.min(holder.getMaxAirSupply(),holder.getAirSupply()+4));
    }
    public static void used(ItemStack stack,net.minecraft.world.item.context.UseOnContext context){
        if(!(context.getLevel() instanceof ServerLevel level)||!has(stack,"cultivation"))return;
        var pos=context.getClickedPos();var state=level.getBlockState(pos);
        if(state.is(net.minecraft.world.level.block.Blocks.FARMLAND))level.setBlockAndUpdate(pos,state.setValue(net.minecraft.world.level.block.FarmlandBlock.MOISTURE,7));
    }
    private static String displayNumber(double value){return String.format(Locale.ROOT,"%.2f",value).replaceAll("\\.?0+$","");}
    /** Values come from the same profile parameters as the behavior, not fixed prose. */
    public static void tooltip(ItemStack stack,java.util.function.Consumer<Component> lines){
        var seen=new HashSet<String>();
        for(var t:traits(stack))if(seen.add(t.id()+t.parameters().toString())){
            Component description=switch(t.id()){
                case "work_hardening"->Component.translatable("tool.smog.trait.work_hardening",displayNumber(100.0*number(t,"max_stacks",4)*ToolDefinitions.number(t.parameters(),"speed_per_stack",.04f)),displayNumber(number(t,"timeout_ticks",100)/20.0));
                case "staggering"->Component.translatable("tool.smog.trait.staggering",displayNumber(number(t,"duration_ticks",20)/20.0),displayNumber(number(t,"cooldown_ticks",60)/20.0));
                case "marking"->Component.translatable("tool.smog.trait.marking",displayNumber(number(t,"duration_ticks",20)/20.0),displayNumber(number(t,"cooldown_ticks",60)/20.0));
                case "grip"->Component.translatable("tool.smog.trait.grip",number(t,"every",4));
                case "impact","quenching","sapping"->Component.translatable("tool.smog.trait."+t.id(),displayNumber(number(t,"duration_ticks",20)/20.0),displayNumber(number(t,"cooldown_ticks",60)/20.0));
                default->Component.translatable("tool.smog.trait."+t.id());
            };
            lines.accept(Component.translatable("tool.smog.tip.trait."+t.id()).append(": ").append(description));
        }
    }
}
