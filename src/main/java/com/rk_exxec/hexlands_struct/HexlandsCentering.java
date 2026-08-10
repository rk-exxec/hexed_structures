package com.rk_exxec.hexlands_struct;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.logging.log4j.Logger;

import com.rk_exxec.hexlands_struct.worldgen.placement.HexCenterPlacement;

import org.apache.logging.log4j.LogManager;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(HexlandsCentering.MODID)
public class HexlandsCentering
{
    public static final String MODID = "hexlands_struct";

    public static final Logger LOGGER = LogManager.getLogger(HexlandsCentering.MODID);

    public static final ResourceLocation HEXLANDS = ResourceLocation.fromNamespaceAndPath("hexlands", "hexlands");

    public HexlandsCentering(FMLJavaModLoadingContext context)
    {
        LOGGER.info("Hello from " + MODID);
        IEventBus modEventBus = context.getModEventBus();
        // Register custom structure types for datapack structures
        // ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        
        // var dr = DeferredRegister.create(Registries.STRUCTURE_PLACEMENT, MODID);
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        // dr.register("hex_center", () -> {return HexCenterPlacement.CODEC;});
        
    }



    // @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent e){
        HexCenterPlacement.HEX_CENTER = Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, MODID + ":hex_center", () -> {return HexCenterPlacement.CODEC;});
        // if(BuiltInRegistries.CHUNK_GENERATOR.containsKey(HEXLANDS)){
        //     LOGGER.info("Found hexlands");
        // }
        // else throw new RuntimeException("hexlands not found");
    }
}
