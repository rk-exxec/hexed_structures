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
    public static void register() {
        HEX_CENTER = register("center", HexCenterPlacement.CODEC);
        // Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, new ResourceLocation(Constants.MODID, "center"), HexCenterPlacement.);//() -> new StructurePlacementType<HexCenterPlacement>() {
        //     @Override
        //     public Codec<HexCenterPlacement> codec() {
        //         return HexCenterPlacement.CODEC;
        //     }
        // });
    }

    private static <SP extends StructurePlacement> StructurePlacementType<SP> register(String name, Codec<SP> codec) {
        return (StructurePlacementType)Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, new ResourceLocation(Constants.MODID, name), (StructurePlacementType)() -> codec);
   }
}
