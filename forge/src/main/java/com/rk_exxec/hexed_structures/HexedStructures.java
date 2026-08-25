package com.rk_exxec.hexed_structures;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import com.rk_exxec.hexed_structures.worldgen.placement.ForgeStructurePlacementTypes;
import com.rk_exxec.hexed_structures.worldgen.structure.ForgeStructureTypes;
import com.rk_exxec.hexed_structures.worldgen.structure.ModStructureTypes;


@Mod(Constants.MODID)
public class HexedStructures
{
    public HexedStructures(FMLJavaModLoadingContext context)
    {
        Constants.LOGGER.info("Hello from " + Constants.MODID);
        IEventBus modEventBus = context.getModEventBus();
        // ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        // ModStructurePlacementTypes.STRUCTURE_PLACEMENTS.register(modEventBus);
        ForgeStructurePlacementTypes.PLACEMENTS.register(modEventBus);
        ForgeStructureTypes.STRUCTURES.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
    }

    public void commonSetup(FMLCommonSetupEvent e)
    {
        CommonClass.init();
        ForgeStructurePlacementTypes.register();
        ForgeStructureTypes.register();
    }

}
