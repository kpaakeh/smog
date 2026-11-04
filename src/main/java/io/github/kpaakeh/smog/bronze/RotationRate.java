package io.github.kpaakeh.smog.bronze;

import java.math.BigInteger;

/** Exact signed tempo ratio. Transmission chains do not round a quarter turn to zero. */
public record RotationRate(BigInteger numerator,BigInteger denominator) {
    public static final RotationRate ZERO=of(0,1),ONE=of(1,1);
    public static final RotationRate NOMINAL=of(16,1);
    public RotationRate {
        if(denominator.signum()==0)throw new IllegalArgumentException("Zero rotation denominator");
        if(denominator.signum()<0){numerator=numerator.negate();denominator=denominator.negate();}
        var gcd=numerator.gcd(denominator);numerator=numerator.divide(gcd);denominator=denominator.divide(gcd);
    }
    public static RotationRate of(long numerator,long denominator){return new RotationRate(BigInteger.valueOf(numerator),BigInteger.valueOf(denominator));}
    public RotationRate multiply(RotationRate other){return new RotationRate(numerator.multiply(other.numerator),denominator.multiply(other.denominator));}
    public RotationRate negate(){return new RotationRate(numerator.negate(),denominator);}
    public double value(){return numerator.doubleValue()/denominator.doubleValue();}
    public boolean belowMinimum(){return numerator.signum()!=0&&numerator.abs().compareTo(denominator)<0;}
    public boolean aboveMaximum(){return numerator.abs().compareTo(denominator.multiply(BigInteger.valueOf(64)))>0;}
}
