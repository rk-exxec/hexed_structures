package com.rk_exxec.hexed_structures.worldgen.structure;

import org.spongepowered.asm.mixin.Implements;

import com.mojang.serialization.Codec;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.structure.HexJigsawStructure;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeStructureTypes {

    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Constants.MODID);
    public static final RegistryObject<StructureType<HexJigsawStructure>> JIGSAW_REG = STRUCTURES.register("jigsaw", () -> new StructureType<HexJigsawStructure>() {
            @Override
            public Codec<HexJigsawStructure> codec() {
                return HexJigsawStructure.CODEC;
            }
        });
    // public static StructureType<HexJigsawStructure> HEX_JIGSAW;

    public static void register() {
        ModStructureTypes.HEX_JIGSAW = JIGSAW_REG.get();
    }
}
