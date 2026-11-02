package io.github.kpaakeh.smog.copper;

import net.minecraft.network.chat.Component;

/** Fabric units: 81 per mB, 9000 per ingot, nine ingots per block. */
public final class FluidAmounts {
    private FluidAmounts(){}
    public static Component format(long units,boolean exact){
        units=Math.max(0,units);
        if(exact){long whole=units/81,remainder=units%81;if(remainder==0)return Component.literal(whole+" mB");long divisor=gcd(remainder,81);return Component.literal(whole+" "+remainder/divisor+"/"+81/divisor+" mB");}
        long blocks=units/81000,remainder=units%81000,ingots=remainder/9000,part=remainder%9000;
        var result=Component.empty();
        if(blocks>0)result.append(Component.translatable("gui.smog.amount.blocks",blocks));
        if(ingots>0){if(blocks>0)result.append(" ");result.append(Component.translatable("gui.smog.amount.ingots",ingots));}
        if(part>0){if(blocks>0||ingots>0)result.append(" ");long divisor=gcd(part,9000);result.append(Component.translatable("gui.smog.amount.ingots",part/divisor+"/"+9000/divisor));}
        if(units==0)result.append(Component.translatable("gui.smog.amount.ingots",0));
        return result;
    }
    private static long gcd(long a,long b){while(b!=0){long next=a%b;a=b;b=next;}return a;}
}
