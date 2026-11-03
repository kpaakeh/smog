package io.github.kpaakeh.smog.copper;

import io.github.kpaakeh.smog.machine.MachineEntity;

/** Read-only reasons for the next manual-workshop action; separate from legacy process status IDs.
 * needed/available are item counts except for NEED_WATER and NEED_OIL_SPACE, which use exact Fabric fluid units.
 */
public final class ManualWorkshopStatus {
    public static final int UNASSEMBLED=0,DISABLED=1,AUTO_PAUSED=2,NO_INPUT=3,WRONG_INPUT=4,
        INSUFFICIENT_INPUT=5,NEED_WATER=6,NEED_OIL_SPACE=7,OUTPUT_BLOCKED=8,WAITING_START=9,
        WORKING_MANUAL=10,WORKING_AUTO=11,AUTOMATIC_READY=12,OUTPUT_READY=13;
    public record State(int code,int needed,int available){}
    private ManualWorkshopStatus(){}
    private static State state(int code){return new State(code,0,0);}
    public static State inspect(MachineEntity machine){
        if(!machine.assembled())return state(UNASSEMBLED);
        if(!machine.enabled())return state(DISABLED);
        var process=machine.copper;boolean wash=machine.processFluid.washing();
        boolean drive=!wash&&BronzeDrive.connected(machine);
        if(process.queued>0&&process.autoWork&&!drive)return state(AUTO_PAUSED);
        var input=machine.getItem(0);var output=machine.getItem(3);
        if(input.isEmpty())return output.isEmpty()?state(NO_INPUT):new State(OUTPUT_READY,0,output.getCount());
        if(!ManualWorkshops.accepts(machine,input))return state(WRONG_INPUT);
        int batch=ManualWorkshops.batchSize(machine);
        if(input.getCount()<batch)return new State(INSUFFICIENT_INPUT,batch,input.getCount());
        if(!ManualWorkshops.fiber(machine)){
            int fluid=wash?WashingRecipes.find(input).water():WorkshopFluidStorage.BUCKET/4;
            if(wash&&process.processFluid<fluid)return new State(NEED_WATER,fluid,process.processFluid);
            int free=Math.max(0,WorkshopFluidStorage.CAPACITY-process.processFluid);
            if(!wash&&free<fluid)return new State(NEED_OIL_SPACE,fluid,free);
        }
        if(!machine.canOutput(ManualWorkshops.result(machine)))return state(OUTPUT_BLOCKED);
        if(process.queued<=0){
            return state(drive?AUTOMATIC_READY:WAITING_START);
        }
        return state(process.autoWork?WORKING_AUTO:WORKING_MANUAL);
    }
}
