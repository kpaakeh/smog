package io.github.kpaakeh.smog.copper;

import java.math.BigInteger;
import net.minecraft.nbt.CompoundTag;

/** Exact fraction of head life. Swapping handles changes displayed capacity without changing this fraction. */
public record HeadWear(BigInteger numerator,BigInteger denominator) {
    public HeadWear{
        if(denominator.signum()<=0)denominator=BigInteger.ONE;
        numerator=numerator.max(BigInteger.ZERO).min(denominator);var gcd=numerator.gcd(denominator);numerator=numerator.divide(gcd);denominator=denominator.divide(gcd);
    }
    public static HeadWear read(CompoundTag data){try{return new HeadWear(new BigInteger(data.getStringOr("wear_n","0")),new BigInteger(data.getStringOr("wear_d","1")));}catch(NumberFormatException bad){return new HeadWear(BigInteger.ZERO,BigInteger.ONE);}}
    public void write(CompoundTag data){data.putString("wear_n",numerator.toString());data.putString("wear_d",denominator.toString());}
    public HeadWear add(int damage,int maximum){return new HeadWear(numerator.multiply(BigInteger.valueOf(maximum)).add(denominator.multiply(BigInteger.valueOf(damage))),denominator.multiply(BigInteger.valueOf(maximum)));}
    public int damage(int maximum){return numerator.multiply(BigInteger.valueOf(maximum)).add(denominator.subtract(BigInteger.ONE)).divide(denominator).intValueExact();}
    public int recover(int fullUnits){return denominator.subtract(numerator).multiply(BigInteger.valueOf(fullUnits)).divide(denominator).intValueExact();}
}
