package com.rk_exxec.hexed_structures.worldgen.structure;

import java.util.Optional;

import com.alcatrazescapee.hexlands.util.Hex;
import com.alcatrazescapee.hexlands.util.HexSettings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.rk_exxec.hexed_structures.Constants;
import com.rk_exxec.hexed_structures.worldgen.HexWorldgenContext;
import com.rk_exxec.hexed_structures.worldgen.structure.pools.HexJigsawPlacement;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

/**
 * HexJigsawStructure
 * Copy of JigsawStructure with overwritten findGenerationPoint method for hexlands specific changes
 */
public class HexJigsawStructure extends Structure {

    public record Bounds(int lower, int upper) {
        public static final int DEFAULT_LOWER = Integer.MIN_VALUE;
        public static final int DEFAULT_UPPER = Integer.MAX_VALUE;

        public Bounds() {
            this(DEFAULT_LOWER, DEFAULT_UPPER);
        }
    }

    public static final Codec<Bounds> BOUNDS_CODEC =
        RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.INT.optionalFieldOf("min", Bounds.DEFAULT_LOWER)
                    .forGetter(Bounds::lower),

                Codec.INT.optionalFieldOf("max", Bounds.DEFAULT_UPPER)
                    .forGetter(Bounds::upper)
            ).apply(instance, Bounds::new)
        );
        
    public static final Codec<HexJigsawStructure> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            settingsCodec(instance),
            StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
            ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
            Codec.intRange(0, 20).fieldOf("size").forGetter(structure -> structure.maxDepth),
            HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
            Codec.BOOL.fieldOf("use_expansion_hack").forGetter(structure -> structure.useExpansionHack),
            Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
            Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter),
            BOUNDS_CODEC.optionalFieldOf("valid_height_range", new Bounds()).forGetter(structure -> structure.heightRange)
        ).apply(instance, HexJigsawStructure::new)
    );

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int maxDepth;
    private final HeightProvider startHeight;
    private final boolean useExpansionHack;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;
    private final Bounds heightRange;

    public Bounds heightRange() {return this.heightRange;}

    public HexJigsawStructure(Structure.StructureSettings settings, Holder<StructureTemplatePool> startPool, Optional<ResourceLocation> startJigsawName,int maxDepth,HeightProvider startHeight,boolean useExpansionHack,Optional<Heightmap.Types> projectStartToHeightmap,int maxDistanceFromCenter,Bounds heightRange)
    {
        super(settings);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.maxDepth = maxDepth;
        this.startHeight = startHeight;
        this.useExpansionHack = useExpansionHack;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
        this.heightRange = heightRange;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int startY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        // stop generation if not in height range
        if(startY < heightRange.lower() || startY > heightRange.upper()) return Optional.empty();
        BlockPos startPos = new BlockPos(chunkPos.getMinBlockX(), startY, chunkPos.getMinBlockZ());
        // -- custom part start
        // pull hex settings if available, fallback to "vanilla" behavior otherwise
        HexSettings hexSettings = HexWorldgenContext.currentHexSettings().orElse(null);
        if (hexSettings != null) {
            Hex hex = Hex.blockToHex(startPos.getX(), startPos.getZ(), hexSettings.hexSize());
            startPos = hex.center(); // move structure start to actual actual center, not just center chunk

            Constants.LOGGER.debug("Centered structure for hex "+ hex);
        }
        // -- custom part end
        // yes for this i needed an entire new thing, cus mixins suck

        return HexJigsawPlacement.addPieces(
            context,
            this.startPool,
            this.startJigsawName,
            this.maxDepth,
            startPos,
            this.useExpansionHack,
            this.projectStartToHeightmap,
            this.maxDistanceFromCenter
        );
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.HEX_JIGSAW;
    }
}
