package io.github.kpaakeh.smog.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.kpaakeh.smog.machine.*;
import io.github.kpaakeh.smog.multiblock.MultiblockDefinition;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/** One normalized model stretched over the recipe's exact bounds, anchored and rotated at its controller. */
public final class MachineRenderer implements BlockEntityRenderer<MachineEntity, MachineRenderer.State> {
    public static final class State extends BlockEntityRenderState {
        public BlockModelRenderState model = new BlockModelRenderState();
        public MultiblockDefinition.Bounds bounds;
        public float angle;
        public boolean formed;
        public CraneRendering.State crane;
        public Vec3 ropeEnd;
        public boolean drawRope;
        public float cargoProgress;
        public net.minecraft.client.renderer.item.ItemStackRenderState cargo;
        public record Part(net.minecraft.core.BlockPos offset,BlockModelRenderState model){}
        public record ItemPart(float x,float y,float z,net.minecraft.client.renderer.item.ItemStackRenderState model){}
        public java.util.List<ItemPart> displayedItems=java.util.List.of();
        public java.util.List<MoltenRendering.Stream> streams=java.util.List.of();
        public java.util.List<MoltenRendering.Layer> moltenLayers=java.util.List.of();
        public java.util.List<MoltenRendering.Surface> moltenSurfaces=java.util.List.of();
        public java.util.List<Part> complexParts=java.util.List.of();
        public java.util.List<io.github.kpaakeh.smog.energy.WireLink> wires=java.util.List.of();
    }
    private final BlockModelResolver models;
    private final net.minecraft.client.renderer.item.ItemModelResolver items;
    private static final BlockDisplayContext DISPLAY = BlockDisplayContext.create();
    public MachineRenderer(BlockEntityRendererProvider.Context context) { models = context.blockModelResolver();items=context.itemModelResolver(); }
    @Override public State createRenderState() { return new State(); }
    @Override public void extractRenderState(MachineEntity machine, State state, float tick, Vec3 camera, ModelFeatureRenderer.CrumblingOverlay overlay) {
        BlockEntityRenderer.super.extractRenderState(machine, state, tick, camera, overlay);
        state.wires=machine.spec().kind()==MachineSpec.Kind.CONNECTOR?ClientWires.from(machine.getBlockPos()):java.util.List.of();
        if(!state.wires.isEmpty())state.lightCoords=net.minecraft.util.LightCoordsUtil.pack(machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.BLOCK,machine.getBlockPos().relative(ConnectorBlock.attachment(machine.getBlockState()))),machine.getLevel().getBrightness(net.minecraft.world.level.LightLayer.SKY,machine.getBlockPos().relative(ConnectorBlock.attachment(machine.getBlockState()))));
        state.ropeEnd=null;state.drawRope=false;state.cargo=null;
        if(io.github.kpaakeh.smog.copper.Ropeway.handles(machine)&&machine.copper.ropeLinked){
            var target=net.minecraft.core.BlockPos.of(machine.copper.ropeTarget);
            if(machine.getLevel().hasChunkAt(target)&&machine.getLevel().getBlockEntity(target) instanceof MachineEntity other&&other.copper.ropeLinked&&other.copper.ropeTarget==machine.getBlockPos().asLong()){
                state.drawRope=machine.getBlockPos().asLong()<target.asLong();state.ropeEnd=Vec3.atLowerCornerOf(target.subtract(machine.getBlockPos()));
                if(!machine.getItem(1).isEmpty()){state.cargo=new net.minecraft.client.renderer.item.ItemStackRenderState();items.updateForTopItem(state.cargo,new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.CHEST),net.minecraft.world.item.ItemDisplayContext.NONE,machine.getLevel(),null,0);state.cargoProgress=Math.clamp((machine.copper.work+tick)/io.github.kpaakeh.smog.copper.Ropeway.duration(machine),0,1);}
            }
        }
        state.complexParts=java.util.List.of();
        extractWorkshop(machine,state);
        state.crane=CraneRendering.extract(machine,items,tick);
        if(state.crane!=null){state.formed=true;return;}
        if(machine.complexDefinition()!=null){state.formed=false;return;}
        state.formed = machine.structure() != null && machine.assembled() && ClientFormations.controller(machine.getBlockPos());
        if (!state.formed) return;
        if(machine.complexDefinition()!=null||(io.github.kpaakeh.smog.copper.ManualWorkshops.handles(machine)||io.github.kpaakeh.smog.copper.LoadingCrane.handles(machine))){
            java.util.List<State.Part> parts=new java.util.ArrayList<>();
            var definition=machine.structure();var offsets=new java.util.ArrayList<>(definition.parts().keySet());offsets.add(net.minecraft.core.BlockPos.ZERO);
            for(var offset:offsets){var rotated=offset.rotate(machine.rotation());var block=machine.getLevel().getBlockState(machine.getBlockPos().offset(rotated));if(block.isAir())continue;
                if(block.hasProperty(MachineBlock.ASSEMBLED))block=block.setValue(MachineBlock.ASSEMBLED,false);
                var model=new BlockModelRenderState();models.update(model,block,DISPLAY);if(block.is(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("smeltery_glass")))||block.is(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getValue(io.github.kpaakeh.smog.registry.ModBlocks.id("smeltery_fuel_tank"))))((io.github.kpaakeh.smog.mixin.BlockModelRenderStateAccessor)model).smog$renderType(net.minecraft.client.renderer.rendertype.RenderTypes.entityTranslucent(net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS));else ModelRendering.enableDepth(model);parts.add(new State.Part(rotated,model));}
            state.complexParts=java.util.List.copyOf(parts);state.bounds=definition.bounds();
        }else state.bounds = machine.structure().bounds();
        var exterior = machine.getBlockPos().above(state.bounds.maxY()+1);
        var level = machine.getLevel();
        state.lightCoords = net.minecraft.util.LightCoordsUtil.pack(
            level.getBrightness(net.minecraft.world.level.LightLayer.BLOCK,exterior),
            level.getBrightness(net.minecraft.world.level.LightLayer.SKY,exterior));
        state.angle = switch (machine.getBlockState().getValue(MachineBlock.FACING)) {
            case EAST -> -90; case SOUTH -> -180; case WEST -> -270; default -> 0;
        };
        models.update(state.model, machine.getBlockState().setValue(MachineBlock.FACING, Direction.NORTH), DISPLAY);
        ModelRendering.enableDepth(state.model);
    }
    private void extractWorkshop(MachineEntity machine,State state){
        state.moltenLayers=java.util.List.of();state.streams=java.util.List.of();state.moltenSurfaces=java.util.List.of();state.displayedItems=java.util.List.of();
        if(machine.spec().kind()!=MachineSpec.Kind.COPPER_WORKSHOP)return;
        var layers=new java.util.ArrayList<MoltenRendering.Layer>();var streams=new java.util.ArrayList<MoltenRendering.Stream>();var surfaces=new java.util.ArrayList<MoltenRendering.Surface>();var shown=new java.util.ArrayList<State.ItemPart>();
        String kind=machine.machineId().getPath();var storage=machine.copper;
        if(kind.equals("copper_hearth")&&machine.assembled()&&machine.structure()!=null){
            var shape=machine.complexShape();var bounds=shape.bounds();int capacity=io.github.kpaakeh.smog.copper.CopperWorkshops.capacity(machine);
            float ratio=capacity==0?0:Math.clamp(storage.total()/(float)capacity,0,1);
            float level=.015f+ratio*(shape.height()-1-.065f);
            if(storage.total()>0){
                float bottom=.015f;var order=new java.util.ArrayList<Integer>();int selected=io.github.kpaakeh.smog.copper.Smeltery.selected(machine);if(storage.metal(selected)>0)order.add(selected);for(int metal=0;metal<io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length;metal++)if(metal!=selected&&storage.metal(metal)>0)order.add(metal);
                for(int metal:order){float height=(shape.height()-1-.065f)*storage.metal(metal)/Math.max(capacity,(float)storage.total());
                    var first=new net.minecraft.core.BlockPos(bounds.minX()+1,0,1).rotate(machine.rotation());var last=new net.minecraft.core.BlockPos(bounds.maxX()-1,0,shape.depth()-2).rotate(machine.rotation());
                    float left=Math.min(first.getX(),last.getX())+.012f,front=Math.min(first.getZ(),last.getZ())+.012f,width=Math.abs(last.getX()-first.getX())+1-.024f,depth=Math.abs(last.getZ()-first.getZ())+1-.024f;
                    layers.add(new MoltenRendering.Layer(left,bottom,front,width,height,depth,metal));bottom+=height;
                }
            }
            var shownFlow=new java.util.HashSet<net.minecraft.core.BlockPos>();
            var tables=ClientFormations.machines().stream().filter(m->io.github.kpaakeh.smog.copper.Smeltery.vessel(m)&&m.smeltery.filled>0&&machine.getLevel().getGameTime()-m.smeltery.lastFlow<4).sorted(java.util.Comparator.comparingLong(m->m.getBlockPos().asLong())).toList();
            for(var table:tables)for(var pos:flowPath(machine,table.getBlockPos()))if(shownFlow.add(pos)){
                var block=machine.getLevel().getBlockState(pos);String id=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block.getBlock()).getPath();var offset=pos.subtract(machine.getBlockPos());
                if(id.equals("casting_channel")){boolean alongX=block.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING).getAxis()==net.minecraft.core.Direction.Axis.X;surfaces.add(new MoltenRendering.Surface(offset.getX()+(alongX?0:.191f),offset.getY()+.882f,offset.getZ()+(alongX?.191f:0),alongX?1:.618f,alongX?.618f:1,Math.floorMod(table.copper.selection,io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length),true));}
                else if(id.equals("casting_faucet")){String below=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos.below()).getBlock()).getPath();float top=below.equals("casting_channel")?.882f:below.equals("casting_basin")?.22f+.6f*table.smeltery.filled/Math.max(1,table.smeltery.required):.805f;var facing=block.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING);var outlet=new net.minecraft.world.phys.Vec3(.5,.3125,.59375);outlet=switch(facing){case EAST->new net.minecraft.world.phys.Vec3(1-outlet.z,outlet.y,outlet.x);case SOUTH->new net.minecraft.world.phys.Vec3(1-outlet.x,outlet.y,1-outlet.z);case WEST->new net.minecraft.world.phys.Vec3(outlet.z,outlet.y,1-outlet.x);default->outlet;};streams.add(new MoltenRendering.Stream(offset.getX()+(float)outlet.x,offset.getY()+(float)outlet.y,offset.getZ()+(float)outlet.z,1+(float)outlet.y-top,Math.floorMod(table.copper.selection,io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES.length)));}
            }
            if(!machine.getItem(0).isEmpty()){var offset=new net.minecraft.core.BlockPos(0,0,1).rotate(machine.rotation());addItem(shown,machine.getItem(0),offset.getX()+.5f,Math.max(.25f,level+.12f),offset.getZ()+.5f,machine);}
        }else if(kind.equals("tool_workbench")){
            for(int i=0;i<io.github.kpaakeh.smog.copper.ToolDefinitions.maxParts();i++){int slot=io.github.kpaakeh.smog.copper.ToolDefinitions.slot(i);if(!machine.getItem(slot).isEmpty())addItem(shown,machine.getItem(slot),(i+1f)/(io.github.kpaakeh.smog.copper.ToolDefinitions.maxParts()+1),1.02f,.5f,machine);}
            var preview=machine.getItem(3).isEmpty()?io.github.kpaakeh.smog.copper.ToolAssembly.preview(machine):machine.getItem(3);if(!preview.isEmpty())addItem(shown,preview,.5f,1.06f,.8f,machine);
        }else if(io.github.kpaakeh.smog.copper.Smeltery.vessel(machine)){
            if(!machine.getItem(1).isEmpty())addItem(shown,machine.getItem(1),.5f,.775f,.5f,machine);
            if(!machine.getItem(3).isEmpty())addItem(shown,machine.getItem(3),.5f,.825f,.5f,machine);
            else if(machine.smeltery.filled>0){int metal=java.util.Arrays.asList(io.github.kpaakeh.smog.copper.MoltenMetalStorage.NAMES).indexOf(machine.smeltery.pourMetal);if(metal>=0){boolean basin=kind.equals("casting_basin");float fill=machine.smeltery.filled/(float)Math.max(1,machine.smeltery.required);surfaces.add(basin?new MoltenRendering.Surface(.14f,.22f+.6f*fill,.14f,.72f,.72f,metal,machine.smeltery.cooling>0):new MoltenRendering.Surface(.32f,.782f+.025f*fill,.38f,.36f,.24f,metal,machine.smeltery.cooling>0));}}

        }else if((io.github.kpaakeh.smog.copper.ManualWorkshops.handles(machine)||io.github.kpaakeh.smog.copper.LoadingCrane.handles(machine))||io.github.kpaakeh.smog.copper.WaterSupply.handles(machine)){
            if(storage.processFluid>0&&!kind.equals("hand_water_pump"))surfaces.add(new MoltenRendering.Surface(.14f,.35f+.3f*storage.processFluid/io.github.kpaakeh.smog.copper.WorkshopFluidStorage.CAPACITY,.14f,.72f,.72f,kind.equals("lever_press")?-2:-1,false));
            if(!machine.getItem(0).isEmpty())addItem(shown,machine.getItem(0),.5f,.8f,.5f,machine);
            if(!machine.getItem(3).isEmpty())addItem(shown,machine.getItem(3),.5f,.9f,.5f,machine);
        }else if(kind.equals("potter_table")){
            var selected=machine.getItem(3).isEmpty()?net.minecraft.world.item.Item.byId(io.github.kpaakeh.smog.copper.CopperWorkshops.data(machine,12)).getDefaultInstance():machine.getItem(3);addItem(shown,selected,.5f,1.035f,.5f,machine);
        }
        if(kind.equals("smeltery_fuel_tank")&&machine.smeltery.fuelAmount>0)surfaces.add(new MoltenRendering.Surface(.19f,.2f+.55f*machine.smeltery.fuelAmount/io.github.kpaakeh.smog.copper.SmelteryFuelStorage.CAPACITY,.19f,.62f,.62f,machine.smeltery.fuel.equals("smog:blazing_fuel")?-4:-3,true));
        state.moltenLayers=java.util.List.copyOf(layers);state.streams=java.util.List.copyOf(streams);state.moltenSurfaces=java.util.List.copyOf(surfaces);state.displayedItems=java.util.List.copyOf(shown);
    }
    private MoltenRendering.Surface chamberSurface(float x,float y,float z,float width,float depth,int metal,boolean hot,net.minecraft.world.level.block.Rotation rotation){
        return switch(rotation){
            case CLOCKWISE_90->new MoltenRendering.Surface(1-z-depth,y,x,depth,width,metal,hot);
            case CLOCKWISE_180->new MoltenRendering.Surface(1-x-width,y,1-z-depth,width,depth,metal,hot);
            case COUNTERCLOCKWISE_90->new MoltenRendering.Surface(z,y,1-x-width,depth,width,metal,hot);
            default->new MoltenRendering.Surface(x,y,z,width,depth,metal,hot);
        };
    }
    private java.util.List<net.minecraft.core.BlockPos> flowPath(MachineEntity machine,net.minecraft.core.BlockPos table){
        record Node(net.minecraft.core.BlockPos pos,java.util.List<net.minecraft.core.BlockPos> path){}
        var owned=new java.util.HashSet<net.minecraft.core.BlockPos>();for(var offset:machine.structure().parts().keySet()){var pos=machine.getBlockPos().offset(offset.rotate(machine.rotation()));if(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos).getBlock()).getPath().equals("casting_spout"))owned.add(pos);}
        var queue=new java.util.ArrayDeque<Node>();var seen=new java.util.HashSet<net.minecraft.core.BlockPos>();queue.add(new Node(table,java.util.List.of()));
        while(!queue.isEmpty()&&seen.size()<128){var node=queue.removeFirst();if(node.path.size()>8)continue;for(var side:net.minecraft.core.Direction.values()){
            if(side==net.minecraft.core.Direction.DOWN)continue;var pos=node.pos.relative(side);if(!seen.add(pos)||!machine.getLevel().hasChunkAt(pos))continue;String id=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos).getBlock()).getPath();
            var path=new java.util.ArrayList<>(node.path);path.add(pos);if(side==net.minecraft.core.Direction.UP&&id.equals("casting_faucet")&&machine.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.OPEN)){
                var port=pos.relative(machine.getLevel().getBlockState(pos).getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING).getOpposite());if(owned.contains(port))return java.util.List.copyOf(path);
            }if(id.equals("casting_channel"))queue.add(new Node(pos,path));
        }}return java.util.List.of();
    }
    private void addItem(java.util.List<State.ItemPart> shown,net.minecraft.world.item.ItemStack stack,float x,float y,float z,MachineEntity machine){var model=new net.minecraft.client.renderer.item.ItemStackRenderState();items.updateForTopItem(model,stack,net.minecraft.world.item.ItemDisplayContext.FIXED,machine.getLevel(),null,0);shown.add(new State.ItemPart(x,y,z,model));}
    @Override public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if(state.ropeEnd!=null){if(state.drawRope)RopeRendering.submit(state.ropeEnd,pose,collector,state.lightCoords);if(state.cargo!=null){var at=RopeRendering.point(state.ropeEnd,state.cargoProgress);pose.pushPose();pose.translate(at.x,at.y-.35,at.z);pose.scale(.6f,.6f,.6f);state.cargo.submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();}}
        if(!state.wires.isEmpty())WireRendering.submit(state.wires,state.blockPos,pose,collector,state.lightCoords);
        MoltenRendering.submitLayers(state.moltenLayers,pose,collector);
        MoltenRendering.submit(state.moltenSurfaces,pose,collector,state.lightCoords);
        MoltenRendering.submitStreams(state.streams,pose,collector);
        for(var item:state.displayedItems){pose.pushPose();pose.translate(item.x(),item.y(),item.z());pose.rotate(Axis.XP.rotationDegrees(90));pose.scale(.55f,.55f,.55f);item.model().submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();}
        if(state.crane!=null){CraneRendering.submit(state.crane,pose,collector);return;}
        if (!state.formed) return;
        if(!state.complexParts.isEmpty()){
            for(var part:state.complexParts){pose.pushPose();pose.translate(part.offset().getX(),part.offset().getY(),part.offset().getZ());part.model().submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();}return;
        }
        var b = state.bounds;
        pose.pushPose(); pose.translate(.5, 0, .5); pose.rotate(Axis.YP.rotationDegrees(state.angle)); pose.translate(-.5, 0, -.5);
        pose.translate(b.minX(), b.minY(), b.minZ()); pose.scale(b.width(), b.height(), b.depth());
        state.model.submit(pose, collector, state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, 0);
        pose.popPose();
    }
    @Override public boolean shouldRenderOffScreen() { return true; }
    @Override public int getViewDistance() { return 160; }
}
