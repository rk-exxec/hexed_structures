package com.rk_exxec.hexed_structures;

import net.fabricmc.api.ModInitializer;

import com.rk_exxec.hexed_structures.worldgen.placement.ModStructurePlacementTypes;
import com.rk_exxec.hexed_structures.worldgen.structure.ModStructureTypes;

public class HexedStructures implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOGGER.info("Hello Fabric world!");
        ModStructurePlacementTypes.register();
        ModStructureTypes.register();
        CommonClass.init();
    }
}
