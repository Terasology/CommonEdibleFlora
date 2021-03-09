// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.edibleFlora.system;


import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.hunger.component.FoodComponent;
import org.terasology.simpleFarming.events.ModifyFilling;

/**
 * This system is used to return any modified value of the FoodComponent of a seed
 * Can be used with any modifiers for seeds
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class FillingModifierSystem extends BaseComponentSystem {

    @ReceiveEvent
    public void onFillingModifiedEvent(ModifyFilling event, EntityRef entity) {
        entity.getComponent(FoodComponent.class).filling.preAdd(event.getNewFilling());
        event.filling = entity.getComponent(FoodComponent.class).filling;
    }
}
