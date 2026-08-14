package com.rk_exxec.hexed_structures;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Logger;

import com.rk_exxec.hexed_structures.worldgen.placement.ModStructurePlacementTypes;
import com.rk_exxec.hexed_structures.worldgen.structure.ModStructureTypes;

import org.apache.logging.log4j.LogManager;


@Mod(HexedStructures.MODID)
public class HexedStructures
{
    public static final String MODID = "hexed_structures";

    public static final Logger LOGGER = LogManager.getLogger(HexedStructures.MODID);

    public static final ResourceLocation HEXLANDS = ResourceLocation.fromNamespaceAndPath("hexlands", "hexlands");

    public HexedStructures(FMLJavaModLoadingContext context)
    {
        LOGGER.info("Hello from " + MODID);
        IEventBus modEventBus = context.getModEventBus();
        ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        ModStructurePlacementTypes.STRUCTURE_PLACEMENTS.register(modEventBus);
        
        // modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        // // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        // no config needed
    }



    // @SubscribeEvent

}
