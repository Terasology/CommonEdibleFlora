/*
 * Copyright 2017 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.edibleFlora.worldGenerator;


import org.terasology.core.world.generator.facets.FloraFacet;
import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.entitySystem.prefab.PrefabManager;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.simpleFarming.components.BushDefinitionComponent;
import org.terasology.utilities.random.FastRandom;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizerPlugin;
import org.terasology.world.generator.plugin.RegisterPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * The bush rasterizer looks for all prefabs with a WildBushComponent and BushDefinitionComponent
 * and replaces flora with a bush.
 *
 * Be sure that your bushes meet these requirements:
 * 1. The block that is placed into the world has a related entity that includes the BushDefinitionComponent
 * 2. The currentStage of the BushDefinitionComponent matches the block that gets put into the world
 */
@RegisterPlugin
public class BushRasterizer implements WorldRasterizerPlugin {
    private BlockManager blockManager;
    private PrefabManager prefabManager;

    private FastRandom random = new FastRandom();
    private Block air;
    private List<Block> bushes = new ArrayList<>();

    @Override
    public void initialize() {
        blockManager = CoreRegistry.get(BlockManager.class);
        prefabManager = CoreRegistry.get(PrefabManager.class);

        air = blockManager.getBlock(BlockManager.AIR_ID);

        for(Prefab bushPrefab: prefabManager.listPrefabs(WildBushComponent.class)) {
            BushDefinitionComponent bushDefinition = bushPrefab.getComponent(BushDefinitionComponent.class);
            if( bushDefinition != null) {
                String blockUri = bushDefinition.growthStages.keySet().stream().skip(bushDefinition.currentStage).findFirst().get();
                Block block = blockManager.getBlock(blockUri);
                bushes.add(block);
            }
        }
    }

    /**
     * Called once for each chunk being generated.
     * Places the WildBush randomly throughout the chunk.
     *
     * @param chunk       The chunk being generated
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
