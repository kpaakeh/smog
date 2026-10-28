package io.github.kpaakeh.smog.copper;

import net.minecraft.world.level.storage.*;

/** Fabric fluid units; legacy 0.7.0 quarter-ingot stores migrate losslessly on load. */
public final class CopperState {
    public int copper,tin,bronze,heat,burn,air,selection,status,work;
    public String workKey="";
    public long lastPour=-10;
    public int total(){return copper+tin+bronze;}
    public int metal(int kind){return kind==0?copper:kind==1?tin:bronze;}
    public void metal(int kind,int value){if(kind==0)copper=value;else if(kind==1)tin=value;else bronze=value;}
    public void save(ValueOutput o){o.putInt("molten_storage_version",1);o.putInt("cu_metal",copper);o.putInt("sn_metal",tin);o.putInt("bronze_metal",bronze);o.putInt("copper_heat",heat);o.putInt("copper_burn",burn);o.putInt("copper_air",air);o.putInt("copper_selection",selection);o.putInt("copper_work",work);o.putString("copper_work_key",workKey);}
    public void load(ValueInput i){copper=Math.max(0,i.getIntOr("cu_metal",0));tin=Math.max(0,i.getIntOr("sn_metal",0));bronze=Math.max(0,i.getIntOr("bronze_metal",0));heat=Math.clamp(i.getIntOr("copper_heat",0),0,1200);burn=Math.max(0,i.getIntOr("copper_burn",0));air=Math.clamp(i.getIntOr("copper_air",0),0,8);selection=Math.max(0,i.getIntOr("copper_selection",0));work=Math.max(0,i.getIntOr("copper_work",0));workKey=i.getStringOr("copper_work_key","");if(i.getIntOr("molten_storage_version",0)==0){copper=Math.multiplyExact(copper,MoltenMetalStorage.QUARTER);tin=Math.multiplyExact(tin,MoltenMetalStorage.QUARTER);bronze=Math.multiplyExact(bronze,MoltenMetalStorage.QUARTER);}}
}
