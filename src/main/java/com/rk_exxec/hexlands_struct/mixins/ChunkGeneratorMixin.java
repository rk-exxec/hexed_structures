package com.rk_exxec.hexlands_struct.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.alcatrazescapee.hexlands.util.Hex;
import com.alcatrazescapee.hexlands.util.HexSettings;
import com.alcatrazescapee.hexlands.world.HexChunkGenerator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.util.Pair;
import com.rk_exxec.hexlands_struct.HexlandsCentering;
import com.rk_exxec.hexlands_struct.util.HexCoord;
import com.rk_exxec.hexlands_struct.worldgen.HexWorldgenContext;
import com.rk_exxec.hexlands_struct.worldgen.placement.HexCenterPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

@Mixin(JigsawStructure.class)
public class ChunkGeneratorMixin {

    // // @Shadow
    // // private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, boolean pSkipKnownStructures, StructurePlacement pPlacement, ChunkPos pChunkPos){throw new AssertionError();};
    @Shadow Holder<StructureTemplatePool> startPool;
    @Shadow Optional<ResourceLocation> startJigsawName;
    @Shadow int maxDepth;
    @Shadow HeightProvider startHeight;
    @Shadow boolean useExpansionHack;
    @Shadow Optional<Heightmap.Types> projectStartToHeightmap;
    @Shadow int maxDistanceFromCenter;

    @Overwrite
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
        ChunkPos chunkpos = pContext.chunkPos();
        int i = this.startHeight.sample(pContext.random(), new WorldGenerationContext(pContext.chunkGenerator(), pContext.heightAccessor()));
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), i, chunkpos.getMinBlockZ());
        HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
        if (pContext.chunkGenerator() instanceof HexChunkGenerator && hexSettings != null) {
            Hex hex = Hex.blockToHex(blockpos.getX(), blockpos.getZ(), hexSettings.hexSize());
            blockpos = hex.center();
            
            HexlandsCentering.LOGGER.debug("Moving hex to" + blockpos);
            return JigsawPlacement.addPieces(pContext, this.startPool, this.startJigsawName, this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, 1);
        }
        return JigsawPlacement.addPieces(pContext, this.startPool, this.startJigsawName, this.maxDepth, blockpos, this.useExpansionHack, this.projectStartToHeightmap, this.maxDistanceFromCenter);
   }
//    @ModifyReturnValue(method = "findGenerationPoint", at = @At("RETURN"))
//     private Optional<Structure.GenerationStub> hexlands_struct$recenterHexJigsaw(Optional<Structure.GenerationStub> original) {
//         if (original == null || original.isEmpty()) {
//             return original;
//         }

//         HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
//         if (hexSettings == null) {
//             return original;
//         }

//         Structure.GenerationStub stub = original.get();
//         BlockPos originalPos = stub.position();
//         Hex hex = Hex.blockToHex(originalPos.getX(), originalPos.getZ(), hexSettings.hexSize());
//         BlockPos centerPos = hex.center();

//         return Optional.of(new Structure.GenerationStub(centerPos, stub.generator()));
//     }
    


    // MIXIN JOGSAWSATRUCTURE! findGenerationPoint


    // @Redirect(method="findNearestMapStructure", 
    // at=@At(value = "INVOKE", target="Lnet/minecraft/world/level/chunk/ChunkGenerator;getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;IIIZJLnet/minecraft/world/level/levelgen/structure/placement/RandomSpreadStructurePlacement;)Lcom/mojang/datafixers/util/Pair;"))
    // private Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, int cX, int cZ, int searchRad, boolean pSkipKnownStructures, long pSeed, RandomSpreadStructurePlacement pSpreadPlacement) {
    //     int i = pSpreadPlacement.spacing();

    //     // if (pSpreadPlacement instanceof HexCenterPlacement){
    //         ChunkPos curChunk = new ChunkPos(cX, cZ);
    //         HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
    //         if (hexSettings == null)
    //             return null;

    //         // Create list of coordinates to check
    //         // hex ring of spacing radius
    //         // https://www.redblobgames.com/grids/hexagons/#rings
    //         Hex start = Hex.blockToHex(curChunk.getMiddleBlockX(), curChunk.getMiddleBlockZ(), hexSettings.hexSize());
    //         ArrayList<HexCoord> ring = HexCoord.hexRing(new HexCoord(start.q(),start.r()), i);

    //         for (HexCoord hexCoord : ring) {
    //             ChunkPos toCheck = new ChunkPos(new Hex(hexCoord.q, hexCoord.r, hexSettings.hexSize()).center());
    //             toCheck = pSpreadPlacement.getPotentialStructureChunk(pSeed, toCheck.x, toCheck.z);
    //             Pair<BlockPos, Holder<Structure>> pair = ChunkGenerator.getStructureGeneratingAt(pStructureHoldersSet, pLevel, pStructureManager, pSkipKnownStructures, pSpreadPlacement, toCheck);
    //             if (pair != null) {
    //                 return pair;
    //             }
    //         }
    //     // }
    //     // else{
    //     //     return ChunkGenerator.getNearestGeneratedStructure(pStructureHoldersSet, pLevel, pStructureManager, cX, cZ, searchRad, pSkipKnownStructures, pSeed, pSpreadPlacement);
            
    //     // }
    //     return null;
    // }
}
