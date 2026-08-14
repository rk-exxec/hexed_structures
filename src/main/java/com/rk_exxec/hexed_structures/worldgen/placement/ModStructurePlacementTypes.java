package com.rk_exxec.hexed_structures.worldgen.placement;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.HexedStructures;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModStructurePlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> STRUCTURE_PLACEMENTS =
        DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, HexedStructures.MODID);

    public static final RegistryObject<StructurePlacementType<HexCenterPlacement>> HEX_CENTER=
        STRUCTURE_PLACEMENTS.register("center", () -> new StructurePlacementType<HexCenterPlacement>() {
            @Override
            public Codec<HexCenterPlacement> codec() {
                return HexCenterPlacement.CODEC;
            }
        });

    private ModStructurePlacementTypes() {
    }
}
