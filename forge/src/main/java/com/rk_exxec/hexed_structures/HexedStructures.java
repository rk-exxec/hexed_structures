package com.rk_exxec.hexed_structures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(Constants.MODID)
public class HexedStructures
{
    public HexedStructures(FMLJavaModLoadingContext context)
    {
        Constants.LOGGER.info("Hello from " + Constants.MODID);
        // ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        // ModStructurePlacementTypes.STRUCTURE_PLACEMENTS.register(modEventBus);
        CommonClass.init();
    }
}
