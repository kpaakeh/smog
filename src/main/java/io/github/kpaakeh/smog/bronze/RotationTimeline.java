package io.github.kpaakeh.smog.bronze;

/** The phase belongs to the circuit, not to the time each rendered shaft was placed. */
public final class RotationTimeline {
    public static final double DEGREES_PER_TICK=.25;
    private long tick;
    private double phase;
    private RotationRate rate=RotationRate.ZERO;
    public RotationTimeline(long tick,double phase){this.tick=tick;this.phase=wrap(phase);}
    public double update(long now,RotationRate next){
        // An unobserved/unloaded circuit does not accumulate offline turns.
        if(now-tick==1)phase=wrap(phase+rate.value()*DEGREES_PER_TICK);
        tick=now;rate=next;return phase;
    }
    public long tick(){return tick;}
    public static double wrap(double degrees){double result=degrees%360;return result<0?result+360:result;}
}
