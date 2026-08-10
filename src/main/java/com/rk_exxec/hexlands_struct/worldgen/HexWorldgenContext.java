package com.rk_exxec.hexlands_struct.worldgen;

import java.util.Optional;

import com.alcatrazescapee.hexlands.util.HexSettings;
import com.alcatrazescapee.hexlands.world.HexChunkGenerator;
import com.rk_exxec.hexlands_struct.mixins.HexChunkGeneratorAccessor;

import net.minecraft.world.level.chunk.ChunkGenerator;

public final class HexWorldgenContext {
    private static final ThreadLocal<ChunkGenerator> CURRENT_GENERATOR = new ThreadLocal<>();

    private HexWorldgenContext() {
    }

    public static void push(ChunkGenerator generator) {
        CURRENT_GENERATOR.set(generator);
    }

    public static void pop() {
        CURRENT_GENERATOR.remove();
    }

    public static Optional<HexSettings> currentHexSettings() {
        ChunkGenerator generator = CURRENT_GENERATOR.get();
        if (!(generator instanceof HexChunkGenerator)) {
            return Optional.empty();
        }

        return Optional.of(((HexChunkGeneratorAccessor) (Object) generator).getHexSettings());
    }
}