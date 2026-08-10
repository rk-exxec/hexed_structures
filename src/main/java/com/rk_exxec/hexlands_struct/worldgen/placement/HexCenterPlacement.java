package com.rk_exxec.hexlands_struct.worldgen.placement;

import com.alcatrazescapee.hexlands.util.Hex;
import com.alcatrazescapee.hexlands.util.HexSettings;
import com.rk_exxec.hexlands_struct.worldgen.HexWorldgenContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import javax.annotation.Nonnull;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class HexCenterPlacement extends RandomSpreadStructurePlacement {
      //  public static final MapCodec<HexCenterPlacement> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec(instance -> {placementCodec(instance).and(instance.group(
      //          Codec.intRange(0, 4096).fieldOf("spacing").forGetter(HexCenterPlacement::spacing), 
      //          Codec.intRange(0, 4096).fieldOf("separation").forGetter(HexCenterPlacement::separation), 
      //          RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(HexCenterPlacement::spreadType)
      //  )).apply(instance, HexCenterPlacement::new);}), HexCenterPlacement::validate).codec();
   public static final Codec<HexCenterPlacement> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((p_204996_) -> {
      return placementCodec(p_204996_).and(p_204996_.group(Codec.intRange(0, 4096).fieldOf("spacing").forGetter(HexCenterPlacement::spacing), Codec.intRange(0, 4096).fieldOf("separation").forGetter(HexCenterPlacement::separation), RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(HexCenterPlacement::spreadType))).apply(p_204996_, HexCenterPlacement::new);
   }), HexCenterPlacement::validate).codec();

   // private final int spacing;
   // private final int separation;
   // private final RandomSpreadType spreadType;

   public static StructurePlacementType<HexCenterPlacement> HEX_CENTER;

   private static DataResult<HexCenterPlacement> validate(HexCenterPlacement placement) {
      return placement.spacing() <= placement.separation() ? DataResult.error(() -> {
         return "Spacing has to be larger than separation";
      }) : DataResult.success(placement);
   }

   @SuppressWarnings("deprecation")
   public HexCenterPlacement(Vec3i p_227000_, StructurePlacement.FrequencyReductionMethod p_227001_, float p_227002_, int p_227003_, Optional<StructurePlacement.ExclusionZone> p_227004_, int p_227005_, int p_227006_, RandomSpreadType p_227007_) {
      super(p_227000_, p_227001_, p_227002_, p_227003_, p_227004_, p_227005_, p_227006_, p_227007_);
   }

   public HexCenterPlacement(int p_204980_, int p_204981_, RandomSpreadType p_204982_, int p_204983_) {
      this(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, p_204983_, Optional.empty(), p_204980_, p_204981_, p_204982_);
   }

   // @Override
   // public int spacing() {
   //    return this.spacing;
   // }

   // @Override
   // public int separation() {
   //    return this.separation;
   // }

   // @Override
   // public RandomSpreadType spreadType() {
   //    return this.spreadType;
   // }


   @Override
   public ChunkPos getPotentialStructureChunk(long seed, int chX, int chZ) {
      // select random hex via hexagonal coords, apply slight jitter with rng
      // use different spacing? in hexes maybe
       // i,j iterate over possible spacing positions around current pos row by row col by col
       // need to get block pos to calc distance in hexes -> mixin ChunkGenerator.getNearestGeneratedStructure
       // maybe noit add new fun to ChunkGenerator that takes HExCenterPlacement and iterates over near hexes instead?
       //where does structuryfy mixin?

      ChunkPos chunkPos = new ChunkPos(chX, chZ);
      HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
      if (hexSettings == null) {
         return super.getPotentialStructureChunk(seed, chX, chZ);
      }
      final double hexScale = hexSettings.biomeScale();
      final double hexSize = hexSettings.hexSize() * hexScale;
      final Hex hex = Hex.blockToHex(chunkPos.getMiddleBlockPosition(0).getX() * hexScale, chunkPos.getMiddleBlockPosition(0).getZ() * hexScale, hexSize);
      // returns chunk most center of hex
      return new ChunkPos(hex.center().getX(), hex.center().getZ());
   }

   protected boolean isPlacementChunk(@Nonnull ChunkGeneratorStructureState p_256267_, int p_256050_, int p_255975_) {
      ChunkPos chunkpos = this.getPotentialStructureChunk(p_256267_.getLevelSeed(), p_256050_, p_255975_);
      return chunkpos.x == p_256050_ && chunkpos.z == p_255975_;
   }

   public StructurePlacementType<?> type() {
      return HEX_CENTER;
   }
}