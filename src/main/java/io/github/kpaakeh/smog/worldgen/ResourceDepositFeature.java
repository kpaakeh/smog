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
        long salt=random.nextLong();int changed=0;
        for(int x=-radius;x<=radius;x++)for(int z=-radius;z<=radius;z++)for(int y=-thickness;y<=thickness;y++) {
            double nx=origin.getX()+x,ny=origin.getY()+y,nz=origin.getZ()+z;
            double warpX=noise(salt,nx*.21,0,nz*.21)*radius*.3;
            double warpZ=noise(salt+1,nx*.21,0,nz*.21)*radius*.3;
            double horizontal=((x+warpX)*(x+warpX)+(z+warpZ)*(z+warpZ))/(radius*radius);
            double wave=noise(salt+2,nx*.27,0,nz*.27)*thickness*.55;
            double vertical=(y-wave)/(double)thickness;
            double boundary=shape.equals("stratum")?horizontal+Math.pow(Math.abs(vertical),6):horizontal+vertical*vertical;
            double edge=noise(salt+3,nx*.38,ny*.5,nz*.38)*.38+noise(salt+4,nx*.85,ny*.85,nz*.85)*.16;
            if(boundary> .82+edge)continue;
            BlockPos pos=origin.offset(x,y,z);
            if(level.isOutsideBuildHeight(pos)||!level.ensureCanWrite(pos))continue;
            var previous=level.getBlockState(pos);boolean dark=previous.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
            if(!dark&&!previous.is(BlockTags.STONE_ORE_REPLACEABLES)&&!(shape.equals("placer")&&(previous.is(BlockTags.SAND)||previous.is(Blocks.GRAVEL)||previous.is(Blocks.DIRT))))continue;
            if(contained){boolean sealed=true;for(var side:Direction.values())if(level.getBlockState(pos.relative(side)).isAir()){sealed=false;break;}if(!sealed)continue;}
            level.setBlock(pos,dark?deep:stone,Block.UPDATE_CLIENTS);changed++;
        }
        return changed>0;
    }
    // Smooth seeded value noise: coherent lobes and warped layers instead of spherical blobs.
    private static double noise(long seed,double x,double y,double z) {
        int ix=(int)Math.floor(x),iy=(int)Math.floor(y),iz=(int)Math.floor(z);
        double fx=x-ix,fy=y-iy,fz=z-iz;
        fx=fx*fx*(3-2*fx);fy=fy*fy*(3-2*fy);fz=fz*fz*(3-2*fz);
        double result=0;
        for(int a=0;a<2;a++)for(int b=0;b<2;b++)for(int c=0;c<2;c++) {
            long h=seed^((ix+a)*341873128712L)^((iy+b)*42317861L)^((iz+c)*132897987541L);
            h=(h^(h>>>30))*0xbf58476d1ce4e5b9L;h=(h^(h>>>27))*0x94d049bb133111ebL;h^=h>>>31;
            double value=(h>>>11)*0x1.0p-53*2-1;
            result+=value*(a==0?1-fx:fx)*(b==0?1-fy:fy)*(c==0?1-fz:fz);
        }
        return result;
    }
}
