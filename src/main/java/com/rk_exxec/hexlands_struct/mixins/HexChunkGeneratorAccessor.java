package com.rk_exxec.hexlands_struct.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.alcatrazescapee.hexlands.util.HexSettings;
import com.alcatrazescapee.hexlands.world.HexChunkGenerator;

@Mixin(HexChunkGenerator.class)
public interface HexChunkGeneratorAccessor {
    @Accessor("hexSettings")
    HexSettings getHexSettings();
}
