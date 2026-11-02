package io.github.kpaakeh.smog.copper;

import net.minecraft.network.chat.Component;

/** Fabric units: 81 per mB, 9000 per ingot, nine ingots per block. */
public final class FluidAmounts {
    private FluidAmounts(){}
    public static Component format(long units,boolean exact){
        units=Math.max(0,units);
        if(exact){long whole=units/81,remainder=units%81;if(remainder==0)return Component.literal(whole+" mB");long divisor=gcd(remainder,81);return Component.literal(whole+" "+remainder/divisor+"/"+81/divisor+" mB");}
        long blocks=units/81000,remainder=units%81000,ingots=remainder/9000,nuggets=remainder%9000/1000,part=remainder%1000;
        var result=Component.empty();
        if(blocks>0)result.append(measure(blocks,"blocks"));
        if(ingots>0){if(!result.getSiblings().isEmpty())result.append(" ");result.append(measure(ingots,"ingots"));}
        if(nuggets>0||part>0){
            if(!result.getSiblings().isEmpty())result.append(" ");
            if(part==0)result.append(measure(nuggets,"nuggets"));
            else {long divisor=gcd(part,1000);result.append(Component.translatable("gui.smog.amount.nuggets.fraction",(nuggets>0?nuggets+" ":"")+part/divisor+"/"+1000/divisor));}
        }
        if(units==0)result.append(measure(0,"nuggets"));
        return result;
    }
    private static Component measure(long count,String unit){long tens=count%100,ones=count%10;String form=tens>=11&&tens<=14?"many":ones==1?(count==1?"one":"ending_one"):ones>=2&&ones<=4?"few":"many";return Component.translatable("gui.smog.amount."+unit+"."+form,count);}
    private static long gcd(long a,long b){while(b!=0){long next=a%b;a=b;b=next;}return a;}
}
