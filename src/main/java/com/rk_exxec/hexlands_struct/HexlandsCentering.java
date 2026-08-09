package com.rk_exxec.hexlands_struct;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import java.util.regex.Pattern; 
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(HexlandsCentering.MODID)
public class HexlandsCentering
{
    public static final String MODID = "hexlands_struct";

    public static final Logger LOGGER = LogManager.getLogger(HexlandsCentering.MODID);

    public HexlandsCentering(FMLJavaModLoadingContext context)
    {
        LOGGER.info("Hello from " + MODID);
        IEventBus modEventBus = context.getModEventBus();
        // Register custom structure types for datapack structures
        // ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
    }

    // @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent e){


    }
}
