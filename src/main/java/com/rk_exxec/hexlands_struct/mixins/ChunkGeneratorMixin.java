package com.rk_exxec.hexlands_struct.mixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.alcatrazescapee.hexlands.util.Hex;
import com.alcatrazescapee.hexlands.util.HexSettings;
import com.alcatrazescapee.hexlands.world.HexChunkGenerator;
import com.mojang.datafixers.util.Pair;
import com.rk_exxec.hexlands_struct.worldgen.HexWorldgenContext;
import com.rk_exxec.hexlands_struct.worldgen.placement.HexCenterPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

@Mixin(ChunkGenerator.class)
public class ChunkGeneratorMixin {

    @Shadow
    private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, boolean pSkipKnownStructures, StructurePlacement pPlacement, ChunkPos pChunkPos){throw new AssertionError();};
    
    @Redirect(method="findNearestMapStructure", 
    at=@At(value = "INVOKE", target="Lnet/minecraft/world/level/chunk/ChunkGenerator;getNearestGeneratedStructure(Ljava/util/Set;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/world/level/StructureManager;IIIZJLnet/minecraft/world/level/levelgen/structure/placement/StructurePlacement;)Lcom/mojang/datafixers/util/Pair;"))
    private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(ChunkGenerator cg, Set<Holder<Structure>> pStructureHoldersSet, LevelReader pLevel, StructureManager pStructureManager, int cX, int cZ, int searchRad, boolean pSkipKnownStructures, long pSeed, RandomSpreadStructurePlacement pSpreadPlacement) {
        int i = pSpreadPlacement.spacing();

        if (pSpreadPlacement instanceof HexCenterPlacement){
            ChunkPos curChunk = new ChunkPos(cX, cZ);
            HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
            if (hexSettings == null)
                return null;

            // Create list of coordinates to check
            // hex ring of spacing radius
            // https://www.redblobgames.com/grids/hexagons/#rings
            Hex start = Hex.blockToHex(curChunk.getMiddleBlockX(), curChunk.getMiddleBlockZ(), hexSettings.hexSize());
            ArrayList<HexCoord> ring = hexRing(new HexCoord(start.q(),start.r()), i);

            for (HexCoord hexCoord : ring) {
                ChunkPos toCheck = new ChunkPos(new Hex(hexCoord.q, hexCoord.r, hexSettings.hexSize()).center());
                toCheck = pSpreadPlacement.getPotentialStructureChunk(pSeed, toCheck.x, toCheck.z);
                Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAt(pStructureHoldersSet, pLevel, pStructureManager, pSkipKnownStructures, pSpreadPlacement, toCheck);
                if (pair != null) {
                    return pair;
                }
            }
        }
        else{
            for(int j = -searchRad; j <= searchRad; ++j) {
                boolean flag = j == -searchRad || j == searchRad;

                for(int k = -searchRad; k <= searchRad; ++k) {
                    boolean flag1 = k == -searchRad || k == searchRad;
                    if (flag || flag1) {
                    int l = cX + i * j;
                    int i1 = cZ + i * k;
                    ChunkPos chunkpos = pSpreadPlacement.getPotentialStructureChunk(pSeed, l, i1);
                    Pair<BlockPos, Holder<Structure>> pair = getStructureGeneratingAt(pStructureHoldersSet, pLevel, pStructureManager, pSkipKnownStructures, pSpreadPlacement, chunkpos);
                    if (pair != null) {
                        return pair;
                    }
                    }
                }
            }
        }
        return null;
    }


    public static class HexCoord{
        public int q;
        public int r;    

        public HexCoord(int q, int r){
            this.q = q;
            this.r = r;
        }

        public HexCoord add(int q, int r){
            return new HexCoord(this.q+q, this.r+r);
        }

        public HexCoord add(HexCoord o){
            return new HexCoord(this.q+o.q, this.r+o.r);
        }

        public HexCoord mult(int fac){
            return new HexCoord(this.q*fac, this.r*fac);
        }

        public static HexCoord getDir(int corner){
            switch ( corner){
                case 0: return new HexCoord(+1, 0);
                case 1: return new HexCoord(+1, -1);
                case 2: return new HexCoord(0, -1);
                case 3: return new HexCoord(-1, 0);
                case 4: return new HexCoord(-1, +1);
                case 5: return new HexCoord(0, +1);
                default: return new HexCoord(0, +1);
            }
        }
    }

    

    private static ArrayList<HexCoord> hexRing(HexCoord center, int radius){
        ArrayList<HexCoord> results = new ArrayList<>();
        // this code doesn't work for radius == 0; can you see why?
        HexCoord hex = center.add(HexCoord.getDir(4).mult(radius));
        for(int i = 0; i<6;i++){
            for(int j = 0; j< radius; j++){
                results.add(hex);
                hex = hex.add(HexCoord.getDir(i));
            }
        }
        return results;
    }
}
