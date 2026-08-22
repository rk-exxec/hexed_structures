package com.rk_exxec.hexed_structures.mixin;

import com.mojang.datafixers.util.Pair;
import com.rk_exxec.hexed_structures.worldgen.HexWorldgenContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 
 * ActiveChunkGeneratorMixin
 * stores current chunk generator to the storage during the important generation function calls
 */
@Mixin(ChunkGenerator.class)
public class ActiveChunkGeneratorMixin {
    @Inject(method = "createStructures", at = @At("HEAD"))
    private void hexlands_struct$pushCreateStructuresContext(RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunkAccess, StructureTemplateManager templateManager, CallbackInfo callbackInfo) {
        HexWorldgenContext.push((ChunkGenerator) (Object) this);
    }

    @Inject(method = "createStructures", at = @At("RETURN"))
    private void hexlands_struct$popCreateStructuresContext(RegistryAccess registryAccess, ChunkGeneratorStructureState structureState, StructureManager structureManager, ChunkAccess chunkAccess, StructureTemplateManager templateManager, CallbackInfo callbackInfo) {
        HexWorldgenContext.pop();
    }

    @Inject(method = "findNearestMapStructure", at = @At("HEAD"))
    private void hexlands_struct$pushFindNearestContext(ServerLevel serverLevel, HolderSet<Structure> structures, BlockPos position, int radius, boolean skipExistingChunks, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> callbackInfo) {
        HexWorldgenContext.push((ChunkGenerator) (Object) this);
    }

    @Inject(method = "findNearestMapStructure", at = @At("RETURN"))
    private void hexlands_struct$popFindNearestContext(ServerLevel serverLevel, HolderSet<Structure> structures, BlockPos position, int radius, boolean skipExistingChunks, CallbackInfoReturnable<Pair<BlockPos, Holder<Structure>>> callbackInfo) {
        HexWorldgenContext.pop();
    }
}