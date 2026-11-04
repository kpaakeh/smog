package io.github.kpaakeh.smog.test;

import io.github.kpaakeh.smog.bronze.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

/** Controlled native-world test source; its block, provider and resources never enter the release JAR. */
public final class ShaftRotationFixture implements ModInitializer {
    public static final EnumProperty<Direction> FACING=BlockStateProperties.FACING;
    public static final int[] SPEEDS={1,4,8,16,32,64,128,256,RotationRate.MAX_SPEED};
    public static final IntegerProperty TEMPO=IntegerProperty.create("tempo",0,SPEEDS.length-1);
    public static final IntegerProperty FAULT=IntegerProperty.create("fault",0,2);
    public static final BooleanProperty ACTIVE=BooleanProperty.create("active"),REVERSE=BooleanProperty.create("reverse");
    public static Block SOURCE;
    private static final class SourceBlock extends Block {
        SourceBlock(Properties properties){super(properties);registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(TEMPO,3).setValue(ACTIVE,true).setValue(REVERSE,false).setValue(FAULT,0));}
        @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> b){b.add(FACING,TEMPO,ACTIVE,REVERSE,FAULT);}
    }
    @Override public void onInitialize(){
        var id=Identifier.fromNamespaceAndPath("smog_tests","rotation_source");
        SOURCE=Registry.register(BuiltInRegistries.BLOCK,id,new SourceBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK,id)).strength(2)));
        BronzeNetwork.registerSource(SOURCE,(level,pos)->{
            var state=level.getBlockState(pos);if(!state.getValue(ACTIVE))return null;
            var rate=RotationRate.of((state.getValue(REVERSE)?-1:1)*SPEEDS[state.getValue(TEMPO)],1);
            if(state.getValue(FAULT)==1)rate=RotationRate.of(1,2);if(state.getValue(FAULT)==2)rate=RotationRate.of(RotationRate.MAX_SPEED+1,1);
            return new BronzeNetwork.Source(16,rate);
        });
    }
}
