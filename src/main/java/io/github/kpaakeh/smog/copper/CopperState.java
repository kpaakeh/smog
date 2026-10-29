package io.github.kpaakeh.smog.copper;

import net.minecraft.world.level.storage.*;

/** Fabric fluid units; legacy 0.7.0 quarter-ingot stores migrate losslessly on load. */
public final class CopperState {
    public int copper,tin,bronze,heat,burn,air,selection,status,work,processFluid,queued,pumpBudget;
    public int partSelection=-1;
    public boolean autoWork,ropeLinked;
    public long ropeTarget;
    public String workKey="";
    public long lastPour=-10;
    private final int[] otherMetals=new int[Math.max(0,MoltenMetalStorage.NAMES.length-3)];
    public int total(){return copper+tin+bronze+java.util.Arrays.stream(otherMetals).sum();}
    public int metal(int kind){return kind==0?copper:kind==1?tin:kind==2?bronze:otherMetals[kind-3];}
    public void metal(int kind,int value){if(kind==0)copper=value;else if(kind==1)tin=value;else if(kind==2)bronze=value;else otherMetals[kind-3]=value;}
    public void save(ValueOutput o){o.putBoolean("rope_linked",ropeLinked);o.putLong("rope_target",ropeTarget);for(int k=3;k<MoltenMetalStorage.NAMES.length;k++)o.putInt("metal_"+MoltenMetalStorage.NAMES[k],metal(k));o.putInt("tool_part_selection",partSelection);o.putBoolean("automatic_work",autoWork);o.putInt("pump_budget",pumpBudget);o.putInt("workshop_fluid",processFluid);o.putInt("workshop_queued",queued);o.putInt("molten_storage_version",1);o.putInt("cu_metal",copper);o.putInt("sn_metal",tin);o.putInt("bronze_metal",bronze);o.putInt("copper_heat",heat);o.putInt("copper_burn",burn);o.putInt("copper_air",air);o.putInt("copper_selection",selection);o.putInt("copper_work",work);o.putString("copper_work_key",workKey);}
    public void load(ValueInput i){ropeLinked=i.getBooleanOr("rope_linked",false);ropeTarget=i.getLongOr("rope_target",0);for(int k=3;k<MoltenMetalStorage.NAMES.length;k++)metal(k,Math.max(0,i.getIntOr("metal_"+MoltenMetalStorage.NAMES[k],0)));partSelection=i.getIntOr("tool_part_selection",-1);autoWork=i.getBooleanOr("automatic_work",false);pumpBudget=Math.clamp(i.getIntOr("pump_budget",0),0,4*WorkshopFluidStorage.BUCKET);processFluid=Math.clamp(i.getIntOr("workshop_fluid",0),0,WorkshopFluidStorage.CAPACITY);queued=Math.clamp(i.getIntOr("workshop_queued",0),0,8);copper=Math.max(0,i.getIntOr("cu_metal",0));tin=Math.max(0,i.getIntOr("sn_metal",0));bronze=Math.max(0,i.getIntOr("bronze_metal",0));heat=Math.clamp(i.getIntOr("copper_heat",0),0,1200);burn=Math.max(0,i.getIntOr("copper_burn",0));air=Math.clamp(i.getIntOr("copper_air",0),0,8);selection=Math.max(0,i.getIntOr("copper_selection",0));work=Math.max(0,i.getIntOr("copper_work",0));workKey=i.getStringOr("copper_work_key","");if(i.getIntOr("molten_storage_version",0)==0){copper=Math.multiplyExact(copper,MoltenMetalStorage.QUARTER);tin=Math.multiplyExact(tin,MoltenMetalStorage.QUARTER);bronze=Math.multiplyExact(bronze,MoltenMetalStorage.QUARTER);}}
}
