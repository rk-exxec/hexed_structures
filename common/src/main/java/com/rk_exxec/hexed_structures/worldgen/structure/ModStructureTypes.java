package com.rk_exxec.hexed_structures.worldgen.structure;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.placement.HexCenterPlacement;
import com.rk_exxec.hexed_structures.worldgen.structure.HexJigsawStructure;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public final class ModStructureTypes {

    public static StructureType<HexJigsawStructure> HEX_JIGSAW;

    public static void register() {
        HEX_JIGSAW = register("jigsaw", HexJigsawStructure.CODEC);// () -> new StructureType<HexJigsawStructure>() {
        //     @Override
        //     public Codec<HexJigsawStructure> codec() {
        //         return HexJigsawStructure.CODEC;
        //     }
        // });
    }

    private static <SP extends Structure> StructureType<SP> register(String name, Codec<SP> codec) {
        return (StructureType)Registry.register(BuiltInRegistries.STRUCTURE_TYPE, new ResourceLocation(Constants.MODID, name), (StructureType)() -> codec);
   }
}
