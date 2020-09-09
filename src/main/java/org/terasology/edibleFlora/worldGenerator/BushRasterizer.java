// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.edibleFlora.worldGenerator;


import org.terasology.coreworlds.generator.facets.FloraFacet;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.entitySystem.prefab.PrefabManager;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.utilities.random.FastRandom;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizerPlugin;
import org.terasology.engine.world.generator.plugin.RegisterPlugin;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.simpleFarming.components.BushDefinitionComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The bush rasterizer looks for all prefabs with a WildBushComponent and BushDefinitionComponent and replaces flora
 * with a bush.
 * <p>
 * Be sure that your bushes meet these requirements: 1. The block that is placed into the world has a related entity
 * that includes the BushDefinitionComponent 2. The currentStage of the BushDefinitionComponent matches the block that
 * gets put into the world
 */
@RegisterPlugin
public class BushRasterizer implements WorldRasterizerPlugin {
    private final FastRandom random = new FastRandom();
    private final List<Block> bushes = new ArrayList<>();
    private BlockManager blockManager;
    private PrefabManager prefabManager;
    private Block air;

    @Override
    public void initialize() {
        blockManager = CoreRegistry.get(BlockManager.class);
        prefabManager = CoreRegistry.get(PrefabManager.class);

        air = blockManager.getBlock(BlockManager.AIR_ID);

        for (Prefab bushPrefab : prefabManager.listPrefabs(WildBushComponent.class)) {
            BushDefinitionComponent bushDefinition = bushPrefab.getComponent(BushDefinitionComponent.class);
            if (bushDefinition != null) {
                String blockUri =
                        bushDefinition.growthStages.keySet().stream().skip(bushDefinition.currentStage).findFirst().get();
                Block block = blockManager.getBlock(blockUri);
                bushes.add(block);
            }
        }
    }

    /**
     * Called once for each chunk being generated. Places the WildBush randomly throughout the chunk.
     *
     * @param chunk The chunk being generated
     * @param chunkRegion The chunk's region
     */
    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {

        FloraFacet facet = chunkRegion.getFacet(FloraFacet.class);
        facet.getRelativeEntries().keySet().stream().forEach((BaseVector3i pos) -> {
            if (random.nextFloat() < 0.02 && chunk.getBlock(pos).equals(air)) {
                Block bush = bushes.get(random.nextInt(bushes.size()));
                chunk.setBlock(pos, bush);
            }
        });
    }
}
