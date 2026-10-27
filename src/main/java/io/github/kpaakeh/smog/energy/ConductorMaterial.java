package io.github.kpaakeh.smog.energy;

import java.util.Locale;
import net.minecraft.network.chat.Component;

/** Cable material determines current capacity and span, not a machine voltage requirement. */
public enum ConductorMaterial {
    COPPER("lv",128,24,0xC77B42,.020), ALUMINUM("aluminum",64,48,0xC5CDD3,.022),
    SILVER("hv",512,48,0xD4DDE8,.018), STEEL("steel",32,64,0x66717B,.026),
    GOLD("mv",256,32,0xD6BB61,.018), TIN("tin",32,16,0x9FBFC9,.022);
    public final String suffix;public final int transfer,reach,color;public final double radius;
    ConductorMaterial(String suffix,int transfer,int reach,int color,double radius){this.suffix=suffix;this.transfer=transfer;this.reach=reach;this.color=color;this.radius=radius;}
    public String serializedName(){return name().toLowerCase(Locale.ROOT);}
    public Component displayName(){return Component.translatable("conductor.smog."+serializedName());}
    public String connectorId(){return "connector_"+suffix;}
    public String coilId(){return "wire_coil_"+suffix;}
    public static ConductorMaterial legacy(String value){return switch(value){case "LV"->COPPER;case "MV"->GOLD;case "HV"->SILVER;default->valueOf(value.toUpperCase(Locale.ROOT));};}
}
