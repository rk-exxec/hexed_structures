package com.rk_exxec.hexed_structures.worldgen.structure.pools;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.mojang.logging.LogUtils;
import com.rk_exxec.hexed_structures.Constants;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.pools.EmptyPoolElement;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;

/**
 * 
 * HexJigsawPlacement
 * clones JigsawPlacement to be able to shift structures, so their center aligns with the intended spawn pos
 */
public class HexJigsawPlacement extends JigsawPlacement{

    public static Optional<Structure.GenerationStub> addPieces(Structure.GenerationContext pContext, Holder<StructureTemplatePool> pStartPool, Optional<ResourceLocation> pStartJigsawName, int pMaxDepth, BlockPos pPos, boolean pUseExpansionHack, Optional<Heightmap.Types> pProjectStartToHeightmap, int pMaxDistanceFromCenter) {
        RegistryAccess registryaccess = pContext.registryAccess();
        ChunkGenerator chunkgenerator = pContext.chunkGenerator();
        StructureTemplateManager structuretemplatemanager = pContext.structureTemplateManager();
        LevelHeightAccessor levelheightaccessor = pContext.heightAccessor();
        WorldgenRandom worldgenrandom = pContext.random();
        Registry<StructureTemplatePool> registry = registryaccess.registryOrThrow(Registries.TEMPLATE_POOL);
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        StructureTemplatePool structuretemplatepool = pStartPool.value();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(worldgenrandom);
        if (structurepoolelement == EmptyPoolElement.INSTANCE) {
            return Optional.empty();
        } else {
            BlockPos blockpos;
            if (pStartJigsawName.isPresent()) {
                ResourceLocation resourcelocation = pStartJigsawName.get();
                                                // access transformer
                Optional<BlockPos> optional = getRandomNamedJigsaw(structurepoolelement, resourcelocation, pPos, rotation, structuretemplatemanager, worldgenrandom);
                if (optional.isEmpty()) {
                // access transformer
                LOGGER.error("No starting jigsaw {} found in start pool {}", resourcelocation, pStartPool.unwrapKey().map((p_248484_) -> {
                    return p_248484_.location().toString();
                }).orElse("<unregistered>"));
                return Optional.empty();
                }

                blockpos = optional.get();
            } else {
                blockpos = pPos;
            }

            Vec3i vec3i = blockpos.subtract(pPos);
            BlockPos blockpos1 = pPos.subtract(vec3i);

            // start modification
            // shift jiggies to center
            BoundingBox initialBoundingBox =
            structurepoolelement.getBoundingBox(
                structuretemplatemanager,
                blockpos1,
                rotation
            );

            int centerX = (initialBoundingBox.minX() + initialBoundingBox.maxX()) / 2;
            int centerZ = (initialBoundingBox.minZ() + initialBoundingBox.maxZ()) / 2;

            int offsetX = pPos.getX() - centerX;
            int offsetZ = pPos.getZ() - centerZ;

            blockpos1 = blockpos1.offset(offsetX, 0, offsetZ);
            // ----

            PoolElementStructurePiece poolelementstructurepiece = new PoolElementStructurePiece(structuretemplatemanager, structurepoolelement, blockpos1, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(structuretemplatemanager, blockpos1, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            if (pProjectStartToHeightmap.isPresent()) {
                k = pPos.getY() + chunkgenerator.getFirstFreeHeight(i, j, pProjectStartToHeightmap.get(), levelheightaccessor, pContext.randomState());
            } else {
                k = blockpos1.getY();
            }

            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            int i1 = k + vec3i.getY();
            return Optional.of(new Structure.GenerationStub(new BlockPos(i, i1, j), (p_227237_) -> {
                List<PoolElementStructurePiece> list = Lists.newArrayList();
                list.add(poolelementstructurepiece);
                if (pMaxDepth > 0) {
                AABB aabb = new AABB((double)(i - pMaxDistanceFromCenter), (double)(i1 - pMaxDistanceFromCenter), (double)(j - pMaxDistanceFromCenter), (double)(i + pMaxDistanceFromCenter + 1), (double)(i1 + pMaxDistanceFromCenter + 1), (double)(j + pMaxDistanceFromCenter + 1));
                VoxelShape voxelshape = Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST);
                // access transformer
                addPieces(pContext.randomState(), pMaxDepth, pUseExpansionHack, chunkgenerator, structuretemplatemanager, levelheightaccessor, worldgenrandom, registry, poolelementstructurepiece, list, voxelshape);
                list.forEach(p_227237_::addPiece);
                }
            }));
        }
    }

}
