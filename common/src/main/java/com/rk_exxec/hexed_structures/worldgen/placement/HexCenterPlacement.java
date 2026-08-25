package com.rk_exxec.hexed_structures.worldgen.placement;

import com.alcatrazescapee.hexlands.util.Hex;
import com.alcatrazescapee.hexlands.util.HexSettings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.HexWorldgenContext;
import com.rk_exxec.hexed_structures.worldgen.placement.ModStructurePlacementTypes;

import java.util.Optional;
import net.minecraft.core.Vec3i;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import javax.annotation.Nonnull;

import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

/**
 * 
 * HexCenterPlacement
 * Custom placement type basing off of random spread
 * Only returns nearest hexagon center as valid chunks, not really random
 */
public class HexCenterPlacement extends RandomSpreadStructurePlacement {
   public static final Codec<HexCenterPlacement> CODEC = ExtraCodecs.validate(RecordCodecBuilder.mapCodec((p_204996_) -> {
      return placementCodec(p_204996_).and(p_204996_.group(Codec.intRange(0, 4096).fieldOf("spacing").forGetter(HexCenterPlacement::spacing), Codec.intRange(0, 4096).fieldOf("separation").forGetter(HexCenterPlacement::separation), RandomSpreadType.CODEC.optionalFieldOf("spread_type", RandomSpreadType.LINEAR).forGetter(HexCenterPlacement::spreadType))).apply(p_204996_, HexCenterPlacement::new);
   }), HexCenterPlacement::validate).codec();

   // public static StructurePlacementType<HexCenterPlacement> HEX_CENTER;

   private static DataResult<HexCenterPlacement> validate(HexCenterPlacement placement) {
      return placement.spacing <= placement.separation ? DataResult.error(() -> {
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

   @Override
   public ChunkPos getPotentialStructureChunk(long seed, int chX, int chZ) {
      ChunkPos chunkPos = new ChunkPos(chX, chZ);
      HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
      if (hexSettings == null) {
         return super.getPotentialStructureChunk(seed, chX, chZ);
      }
      final int hexSize = (int)hexSettings.hexSize();
      int i = Math.floorDiv(chX, (int)(this.spacing));
      int j = Math.floorDiv(chZ, (int)(this.spacing));
      WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(0L));
      worldgenrandom.setLargeFeatureWithSalt(seed, i, j, this.salt());
      int k = this.spacing - this.separation;
      int l = this.spreadType().evaluate(worldgenrandom, k);
      int i1 = this.spreadType().evaluate(worldgenrandom, k);
      ChunkPos potHexChunk = new ChunkPos(i * this.spacing + l, j * this.spacing + i1);
      final Hex hex = Hex.blockToHex(potHexChunk.getMiddleBlockX(), potHexChunk.getMiddleBlockZ(), hexSize);

      // int dir = worldgenrandom.nextIntBetweenInclusive(0,5);
      // float rQ = (this.separation + this.spreadType().evaluate(worldgenrandom, this.spacing - this.separation)) * (dir % 2 == 0?-1:1);
      // float rR = (this.spacing - rQ) * (dir / 2 == 0?-1:1) ;

      // Hex nHex = hex.adjacent(rQ, rR);
      ChunkPos center = new ChunkPos(hex.center());
      // if(chX == center.x && chZ == center.z)
      // HexedStructures.LOGGER.debug(hex + "(" + center + ")" + " has pot structure chunk " + chunkPos);
      // returns chunk most center of hex
      return center;
   }

   @Override
   protected boolean isPlacementChunk(@Nonnull ChunkGeneratorStructureState p_256267_, int p_256050_, int p_255975_) {
      ChunkPos chunkPos = this.getPotentialStructureChunk(p_256267_.getLevelSeed(), p_256050_, p_255975_);
      boolean result = chunkPos.x == p_256050_ && chunkPos.z == p_255975_;//delta < 8;
      // HexedStructures.LOGGER.debug("Did " + (result?"":"not ") + "match");
      return result;
   }

   public StructurePlacementType<?> type() {
      return ModStructurePlacementTypes.HEX_CENTER;
   }
}