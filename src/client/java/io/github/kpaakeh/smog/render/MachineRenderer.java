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
        public record Part(net.minecraft.core.BlockPos offset,BlockModelRenderState model){}
        public record ItemPart(float x,float y,float z,net.minecraft.client.renderer.item.ItemStackRenderState model){}
        public java.util.List<ItemPart> displayedItems=java.util.List.of();
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
        state.complexParts=java.util.List.of();
        extractWorkshop(machine,state);
        state.formed = machine.structure() != null && machine.assembled() && ClientFormations.controller(machine.getBlockPos());
        if (!state.formed) return;
        if(machine.complexDefinition()!=null){
            java.util.List<State.Part> parts=new java.util.ArrayList<>();
            var definition=machine.structure();var offsets=new java.util.ArrayList<>(definition.parts().keySet());offsets.add(net.minecraft.core.BlockPos.ZERO);
            for(var offset:offsets){var rotated=offset.rotate(machine.rotation());var block=machine.getLevel().getBlockState(machine.getBlockPos().offset(rotated));if(block.isAir())continue;
                if(block.hasProperty(MachineBlock.ASSEMBLED))block=block.setValue(MachineBlock.ASSEMBLED,false);
                var model=new BlockModelRenderState();models.update(model,block,DISPLAY);ModelRendering.enableDepth(model);parts.add(new State.Part(rotated,model));}
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
        state.moltenSurfaces=java.util.List.of();state.displayedItems=java.util.List.of();
        if(machine.spec().kind()!=MachineSpec.Kind.COPPER_WORKSHOP)return;
        var surfaces=new java.util.ArrayList<MoltenRendering.Surface>();var shown=new java.util.ArrayList<State.ItemPart>();
        String kind=machine.machineId().getPath();var storage=machine.copper;
        if(kind.equals("copper_hearth")&&machine.assembled()&&machine.structure()!=null){
            var vessels=new java.util.ArrayList<net.minecraft.core.BlockPos>();int capacity=0;
            for(var offset:machine.structure().parts().keySet()){var rotated=offset.rotate(machine.rotation());var block=machine.getLevel().getBlockState(machine.getBlockPos().offset(rotated));String name=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block.getBlock()).getPath();if(name.equals("ceramic_crucible")||name.equals("large_ceramic_crucible")){vessels.add(rotated);capacity+=name.startsWith("large")?128:64;}}
            float ratio=capacity==0?0:Math.clamp(storage.total()/(float)(capacity*2250),0,1);
            for(var offset:vessels){boolean large=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(machine.getBlockPos().offset(offset)).getBlock()).getPath().startsWith("large");float lo=large?2.05f/16:4.05f/16,span=1-2*lo;
                float at=offset.getX()+lo;for(int metal=0;metal<3;metal++)if(storage.metal(metal)>0){float width=span*storage.metal(metal)/storage.total();surfaces.add(new MoltenRendering.Surface(at,offset.getY()+(3.15f+10.5f*ratio)/16,offset.getZ()+lo,width,span,metal,storage.heat>=600));at+=width;}
            }
            var shownFlow=new java.util.HashSet<net.minecraft.core.BlockPos>();
            var tables=ClientFormations.machines().stream().filter(m->m.machineId().getPath().equals("casting_table")&&m.copper.work>0&&m.copper.status==7&&m.getItem(3).isEmpty()).sorted(java.util.Comparator.comparingLong(m->m.getBlockPos().asLong())).toList();
            for(var table:tables)for(var pos:flowPath(machine,table.getBlockPos()))if(shownFlow.add(pos)){
                var block=machine.getLevel().getBlockState(pos);String id=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(block.getBlock()).getPath();var offset=pos.subtract(machine.getBlockPos());
                if(id.equals("casting_channel")){boolean alongX=block.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING).getAxis()==net.minecraft.core.Direction.Axis.X;surfaces.add(new MoltenRendering.Surface(offset.getX()+(alongX?0:.191f),offset.getY()+.882f,offset.getZ()+(alongX?.191f:0),alongX?1:.618f,alongX?.618f:1,Math.floorMod(table.copper.selection,3),true));}
                else if(id.equals("casting_spout")){var facing=block.getValue(io.github.kpaakeh.smog.copper.WorkshopPartBlock.FACING);boolean alongX=facing.getAxis()==net.minecraft.core.Direction.Axis.X;float x=facing==net.minecraft.core.Direction.EAST?.75f:facing==net.minecraft.core.Direction.WEST?0:.315f,z=facing==net.minecraft.core.Direction.SOUTH?.75f:facing==net.minecraft.core.Direction.NORTH?0:.315f;surfaces.add(new MoltenRendering.Surface(offset.getX()+x,offset.getY()+.882f,offset.getZ()+z,alongX?.25f:.37f,alongX?.37f:.25f,Math.floorMod(table.copper.selection,3),true));}
            }
            if(!vessels.isEmpty()&&!machine.getItem(0).isEmpty()){var offset=vessels.getFirst();addItem(shown,machine.getItem(0),offset.getX()+.5f,offset.getY()+.99f,offset.getZ()+.5f,machine);}
        }else if(kind.equals("casting_table")){
            if(!machine.getItem(1).isEmpty())addItem(shown,machine.getItem(1),.5f,.775f,.5f,machine);
            if(!machine.getItem(3).isEmpty())addItem(shown,machine.getItem(3),.5f,.825f,.5f,machine);
            else if(storage.work>0&&storage.status==7)surfaces.add(new MoltenRendering.Surface(.32f,.805f,.38f,.36f,.24f,Math.floorMod(storage.selection,3),true));
        }else if(kind.equals("potter_table")){
            var selected=machine.getItem(3).isEmpty()?net.minecraft.world.item.Item.byId(io.github.kpaakeh.smog.copper.CopperWorkshops.data(machine,12)).getDefaultInstance():machine.getItem(3);addItem(shown,selected,.5f,1.035f,.5f,machine);
        }
        state.moltenSurfaces=java.util.List.copyOf(surfaces);state.displayedItems=java.util.List.copyOf(shown);
    }
    private java.util.List<net.minecraft.core.BlockPos> flowPath(MachineEntity machine,net.minecraft.core.BlockPos table){
        record Node(net.minecraft.core.BlockPos pos,java.util.List<net.minecraft.core.BlockPos> path){}
        var owned=new java.util.HashSet<net.minecraft.core.BlockPos>();for(var offset:machine.structure().parts().keySet()){var pos=machine.getBlockPos().offset(offset.rotate(machine.rotation()));if(net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos).getBlock()).getPath().equals("casting_spout"))owned.add(pos);}
        var queue=new java.util.ArrayDeque<Node>();var seen=new java.util.HashSet<net.minecraft.core.BlockPos>();queue.add(new Node(table,java.util.List.of()));
        while(!queue.isEmpty()&&seen.size()<128){var node=queue.removeFirst();if(node.path.size()>8)continue;for(var side:net.minecraft.core.Direction.values()){
            if(side==net.minecraft.core.Direction.DOWN)continue;var pos=node.pos.relative(side);if(!seen.add(pos)||!machine.getLevel().hasChunkAt(pos))continue;String id=net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(machine.getLevel().getBlockState(pos).getBlock()).getPath();
            var path=new java.util.ArrayList<>(node.path);path.add(pos);if(id.equals("casting_spout"))return owned.contains(pos)?java.util.List.copyOf(path):java.util.List.of();if(id.equals("casting_channel"))queue.add(new Node(pos,path));
        }}return java.util.List.of();
    }
    private void addItem(java.util.List<State.ItemPart> shown,net.minecraft.world.item.ItemStack stack,float x,float y,float z,MachineEntity machine){var model=new net.minecraft.client.renderer.item.ItemStackRenderState();items.updateForTopItem(model,stack,net.minecraft.world.item.ItemDisplayContext.FIXED,machine.getLevel(),null,0);shown.add(new State.ItemPart(x,y,z,model));}
    @Override public void submit(State state, PoseStack pose, SubmitNodeCollector collector, CameraRenderState camera) {
        if(!state.wires.isEmpty())WireRendering.submit(state.wires,state.blockPos,pose,collector,state.lightCoords);
        MoltenRendering.submit(state.moltenSurfaces,pose,collector,state.lightCoords);
        for(var item:state.displayedItems){pose.pushPose();pose.translate(item.x(),item.y(),item.z());pose.rotate(Axis.XP.rotationDegrees(90));pose.scale(.55f,.55f,.55f);item.model().submit(pose,collector,state.lightCoords,net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,0);pose.popPose();}
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
