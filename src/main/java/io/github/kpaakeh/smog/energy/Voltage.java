package io.github.kpaakeh.smog.energy;

public enum Voltage {
    LV(32,16,0xC77B42), MV(128,32,0xD6BB61), HV(512,48,0x7C8291);
    public final int transfer, reach, color;
    Voltage(int transfer,int reach,int color) { this.transfer=transfer;this.reach=reach;this.color=color; }
}
