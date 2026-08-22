package com.rk_exxec.hexed_structures.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.alcatrazescapee.hexlands.util.HexSettings;
import com.alcatrazescapee.hexlands.world.HexChunkGenerator;

@Mixin(HexChunkGenerator.class)
public interface HexChunkGeneratorAccessor {
    @Accessor("hexSettings")
    HexSettings hexcenter$getHexSettings();
}
