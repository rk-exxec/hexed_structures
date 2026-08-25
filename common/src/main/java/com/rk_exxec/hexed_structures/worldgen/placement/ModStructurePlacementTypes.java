package com.rk_exxec.hexed_structures.worldgen.placement;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.placement.HexCenterPlacement;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;


public final class ModStructurePlacementTypes {
    public static StructurePlacementType<HexCenterPlacement> HEX_CENTER;
}
