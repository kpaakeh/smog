package io.github.kpaakeh.smog.bronze;

import java.math.BigInteger;

/** Small immutable rational used for power budgets and work; never rounds between transmissions. */
public record ExactFraction(BigInteger numerator,BigInteger denominator) implements Comparable<ExactFraction> {
    public static final ExactFraction ZERO=of(0),ONE=of(1);
    public ExactFraction {
        if(denominator.signum()==0)throw new ArithmeticException("Zero denominator");
        if(denominator.signum()<0){numerator=numerator.negate();denominator=denominator.negate();}
        var gcd=numerator.gcd(denominator);numerator=numerator.divide(gcd);denominator=denominator.divide(gcd);
    }
    public static ExactFraction of(long value){return of(value,1);}
    public static ExactFraction of(long numerator,long denominator){return new ExactFraction(BigInteger.valueOf(numerator),BigInteger.valueOf(denominator));}
    public static ExactFraction of(RotationRate value){return new ExactFraction(value.numerator(),value.denominator());}
    public ExactFraction add(ExactFraction b){if(b.signum()==0)return this;if(signum()==0)return b;return new ExactFraction(numerator.multiply(b.denominator).add(b.numerator.multiply(denominator)),denominator.multiply(b.denominator));}
    public ExactFraction negate(){return signum()==0?this:new ExactFraction(numerator.negate(),denominator);}
    public ExactFraction subtract(ExactFraction b){return add(b.negate());}
    public ExactFraction multiply(ExactFraction b){if(signum()==0||b.signum()==0)return ZERO;return new ExactFraction(numerator.multiply(b.numerator),denominator.multiply(b.denominator));}
    public ExactFraction divide(ExactFraction b){return new ExactFraction(numerator.multiply(b.denominator),denominator.multiply(b.numerator));}
    public ExactFraction abs(){return signum()<0?negate():this;}
    public int signum(){return numerator.signum();}
    public int floorInt(){return numerator.divide(denominator).intValueExact();}
    public double value(){return numerator.doubleValue()/denominator.doubleValue();}
    @Override public int compareTo(ExactFraction b){return numerator.multiply(b.denominator).compareTo(b.numerator.multiply(denominator));}
    @Override public String toString(){return denominator.equals(BigInteger.ONE)?numerator.toString():numerator+"/"+denominator;}
}
