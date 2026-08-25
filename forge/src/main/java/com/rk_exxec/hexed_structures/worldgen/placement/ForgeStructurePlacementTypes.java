package com.rk_exxec.hexed_structures.worldgen.placement;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.placement.HexCenterPlacement;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;


public final class ForgeStructurePlacementTypes {
    public static final DeferredRegister<StructurePlacementType<?>> PLACEMENTS = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, Constants.MODID);
    public static final RegistryObject<StructurePlacementType<HexCenterPlacement>> CENTER_REG = PLACEMENTS.register("center", () -> new StructurePlacementType<HexCenterPlacement>() {
            @Override
            public Codec<HexCenterPlacement> codec() {
                return HexCenterPlacement.CODEC;
            }
        });

    public static void register() {
        ModStructurePlacementTypes.HEX_CENTER = CENTER_REG.get();
    }
}
