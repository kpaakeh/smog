package io.github.kpaakeh.smog.worldgen;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;

/** Bounded, deterministic natural masses. Writes only replaceable rock, never loads chunks. */
public record ResourceDepositFeature(BlockState stone,BlockState deep,int radius,int thickness,String shape,boolean contained) implements Feature {
    public static final MapCodec<ResourceDepositFeature> CODEC=RecordCodecBuilder.mapCodec(i->i.group(
        BlockState.CODEC.fieldOf("stone_state").forGetter(ResourceDepositFeature::stone),
        BlockState.CODEC.fieldOf("deep_state").forGetter(ResourceDepositFeature::deep),
        Codec.intRange(2,14).fieldOf("radius").forGetter(ResourceDepositFeature::radius),
        Codec.intRange(1,12).fieldOf("thickness").forGetter(ResourceDepositFeature::thickness),
        Codec.STRING.fieldOf("shape").forGetter(ResourceDepositFeature::shape),
        Codec.BOOL.optionalFieldOf("contained",false).forGetter(ResourceDepositFeature::contained)).apply(i,ResourceDepositFeature::new));
    public static void initialize(){net.minecraft.core.Registry.register(BuiltInRegistries.FEATURE_TYPE,Identifier.fromNamespaceAndPath("smog","resource_deposit"),CODEC);}
    @Override public MapCodec<ResourceDepositFeature> codec(){return CODEC;}
    @Override public boolean place(WorldGenLevel level,ChunkGenerator generator,RandomSource random,BlockPos origin){
        double richness=1;
        if(shape.equals("placer")) {
            long region=(Math.floorDiv(origin.getX(),96)*341873128712L)^(Math.floorDiv(origin.getZ(),96)*132897987541L)^level.getSeed();
            region^=region>>>33;richness=(region&1L)==0?1:.55;
        }
        long salt=random.nextLong();int changed=0;
        for(int x=-radius;x<=radius;x++)for(int z=-radius;z<=radius;z++)for(int y=-thickness;y<=thickness;y++) {
            double horizontal=(x*x+z*z)/(radius*radius*richness*richness);
            double wave=Math.sin((origin.getX()+x)*.23+salt%101)*.35+Math.cos((origin.getZ()+z)*.19)*.25;
            double vertical=(y-wave)/(double)thickness;
            double boundary=shape.equals("stratum")?horizontal+Math.pow(Math.abs(vertical),6):horizontal+vertical*vertical;
            if(boundary>1 || boundary>.65 && random.nextFloat()<.12f)continue;
            BlockPos pos=origin.offset(x,y,z);
            if(level.isOutsideBuildHeight(pos)||!level.ensureCanWrite(pos))continue;
            var previous=level.getBlockState(pos);boolean dark=previous.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
            if(!dark&&!previous.is(BlockTags.STONE_ORE_REPLACEABLES)&&!(shape.equals("placer")&&(previous.is(BlockTags.SAND)||previous.is(Blocks.GRAVEL)||previous.is(Blocks.DIRT))))continue;
            if(contained){boolean sealed=true;for(var side:Direction.values())if(level.getBlockState(pos.relative(side)).isAir()){sealed=false;break;}if(!sealed)continue;}
            level.setBlock(pos,dark?deep:stone,Block.UPDATE_CLIENTS);changed++;
        }
        return changed>0;
    }
}
