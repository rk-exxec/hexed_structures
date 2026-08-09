package com.rk_exxec.hexlands_struct.mixins;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.mojang.datafixers.util.Pair;
import com.rk_exxec.hexlands_struct.CommonConfig;
import com.rk_exxec.hexlands_struct.HexlandsCentering;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;



@Mixin(ChunkGenerator.class)
public class StructurePositionMixin {

    // @Redirect("getNearestGeneratedStructure",at=@At(""))
    // private static Pair<BlockPos, Holder<Structure>> getStructre(Set<Holder<Structure>> p_223189_, LevelReader p_223190_, StructureManager p_223191_, int p_223192_, int p_223193_, int p_223194_, boolean p_223195_, long p_223196_, RandomSpreadStructurePlacement p_223197_){

    // }

    // @Shadow    private final RandomState randomState;
    // @Shadow    private final BiomeSource biomeSource;
    // @Shadow    private final long levelSeed;
    // @Shadow    private final long concentricRingsSeed;
    // @Shadow    private final Map<Structure, List<StructurePlacement>> placementsForStructure = new Object2ObjectOpenHashMap<>();
    // @Shadow    private final Map<ConcentricRingsStructurePlacement, CompletableFuture<List<ChunkPos>>> ringPositions = new Object2ObjectArrayMap<>();
    // @Shadow    private boolean hasGeneratedPositions;
    // @Shadow    private final List<Holder<StructureSet>> possibleStructureSets;

    // @Inject()
    // @Unique
    // private static final HashMap<String, NotedBag> randomBags = new HashMap<>();
    // //TODO 
    // // why so many pulls at once?? -> switch all logging to info again
    // // try move compstruct boulders in its own category somehow to test patternmatching, boulder selection doesnt work rn cus compstruct doesnt have subcategories
    // // check also startJigsawName
    // @Redirect(method = "addPieces",
    //     at = @At(value = "INVOKE", 
    //         target = "Lnet/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool;getRandomTemplate(Lnet/minecraft/util/RandomSource;)Lnet/minecraft/world/level/levelgen/structure/pools/StructurePoolElement;"))
    // private static StructurePoolElement randomBag$getRandomTemplate(
    //         @Nonnull StructureTemplatePool instance,
    //         @Nonnull RandomSource random,
    //         Structure.GenerationContext context,
    //         Holder<StructureTemplatePool> startPool,
    //         Optional<ResourceLocation> startJigsawName,
    //         int maxDepth,
    //         BlockPos pos,
    //         boolean useExpansionHack,
    //         Optional<Heightmap.Types> projectStartToHeightmap,
    //         int maxDistanceFromCenter
    // )
    // {        
    //     // getting structure pool location
    //     Registry<StructureTemplatePool> poolRegistry = context.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);

    //     ResourceLocation poolId = poolRegistry.getKey(instance);
    //     if (poolId == null) {
    //         throw new IllegalStateException("Unregistered pool: " + instance);
    //     }

    //     String resourceLoc = poolId.toString();
    //     matchIfNotCached(resourceLoc);
    //     var bagHolder = randomBags.get(resourceLoc);

    //     // if no pattern has matched, fall back to vanilla behaviour
    //     if (bagHolder == null) return instance.getRandomTemplate(random);

    //     // else: random bag
    //     HexlandsCentering.LOGGER.info("Bagging a structure");
    //     HexlandsCentering.LOGGER.debug("Pulling one (1) variant out of the bag for pool: <" + resourceLoc + ">");
    //     ObjectArrayList<StructurePoolElement> bag = bagHolder.bag();
    //     Integer multiplier = CommonConfig.BAG_SIZE_MULTS.get().get(bagHolder.matchIdx());

    //     if (bag == null || bag.isEmpty()) {
    //         HexlandsCentering.LOGGER.debug("Bag empty, reshuffling...");
    //         bagHolder.bag().clear();
    //         // depending on the config BAG_SIZE_MULTS add multiple times to allow for more permutations
    //         for(int i=0; i<multiplier ; i++){
    //             bagHolder.bag().addAll(instance.getShuffledTemplates(random)); // there is no other way to get all templates easily
    //         }
    //         // shuffle again, cus appending shuffled lists is not the goal, this way elements may acutally come up twice in a row etc
    //         if(multiplier > 1) Collections.shuffle(bagHolder.bag()); 
    //     }
    //     StructurePoolElement curSelection = bagHolder.bag().pop();
    //     HexlandsCentering.LOGGER.debug("Pulled: <" + curSelection + "> | " + bagHolder.bag().size() + "/" + instance.size()*multiplier + " variants left in this bag.");
    //     return curSelection;
    // }

    // /**
    //  * skip matching if bag already exists, use hashmap key as cache </br>
    //  * modifies static variable randomBags
    //  * @param resourceLoc location of the resource to match
    //  */
    // private static void matchIfNotCached(String resourceLoc){

    //     if(!randomBags.containsKey(resourceLoc)){
    //         HexlandsCentering.LOGGER.debug("Checking structure pool <" + resourceLoc + "> for random bag");
    //         @Nonnull Integer matchingPatternIdx = -1;
    //         matchingPatternIdx = doesResourceMatch(resourceLoc);

    //         // no match, will use normal randomization
    //         if(matchingPatternIdx == -1) {
    //             HexlandsCentering.LOGGER.debug("Bag patterns didnt match <" + resourceLoc + ">");
    //             // RandomBag.matchPatternCache.put(resourceLoc,0);
    //             randomBags.put(resourceLoc, null);
    //         }
    //         else
    //         {
    //             HexlandsCentering.LOGGER.debug("Structure pool <" + resourceLoc + "> allowed for random bag");
    //             randomBags.put(resourceLoc, new NotedBag(matchingPatternIdx, new ObjectArrayList<StructurePoolElement>()));
    //         }
    //     }
    // }

    // /**
    //  * Determines if a resource location matches any of the configured pattern filters.
    //  * 
    //  * @param resourceLoc the resource location string to match against configured patterns
    //  * @return the index of the matching pattern in the configuration, or -1 if no match is found
    //  */
    // private static @Nonnull Integer doesResourceMatch(String resourceLoc){
    //     @Nonnull Integer matchingIdx = -1;
    //     // match current structure location to allowed config
    //     HexlandsCentering.LOGGER.debug("First time matching <" + resourceLoc + ">");
    //     int i = 0;
    //     for (Map.Entry<Pattern,Integer> set : HexlandsCentering.matchPatternCycle.entrySet()) {
    //         // "compstruct:aluminium_boulder etc"
    //         if(set.getKey().matcher(resourceLoc).find()) {
    //             HexlandsCentering.LOGGER.debug("Found match <" + set.getKey().toString() + ">");
    //             matchingIdx = i;
    //             break;
    //         }
    //         HexlandsCentering.LOGGER.debug("Bag pattern <" + set.getKey().toString() + "> didnt match <" + resourceLoc + ">");
    //         i++;
    //     }
    //     return matchingIdx;
    // }
}