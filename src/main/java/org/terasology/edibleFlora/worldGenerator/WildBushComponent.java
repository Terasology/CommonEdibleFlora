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

import org.terasology.engine.entitySystem.Component;

/**
 * A flag component used to mark prefabs that contain a bush definition.
 * The current stage of the bush definition will then be placed in the world on chunk generation.
 */
public class WildBushComponent implements Component {
}
