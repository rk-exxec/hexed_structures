package com.rk_exxec.hexed_structures.worldgen.structure;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.HexedStructures;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModStructureTypes {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
        DeferredRegister.create(Registries.STRUCTURE_TYPE, HexedStructures.MODID);

    public static final RegistryObject<StructureType<HexJigsawStructure>> HEX_JIGSAW =
        STRUCTURE_TYPES.register("jigsaw", () -> new StructureType<HexJigsawStructure>() {
            @Override
            public Codec<HexJigsawStructure> codec() {
                return HexJigsawStructure.CODEC;
            }
        });

    private ModStructureTypes() {
    }
}
