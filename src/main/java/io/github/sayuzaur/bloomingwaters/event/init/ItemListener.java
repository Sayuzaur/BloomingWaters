/*
 * Copyright (C) 2026 Sayuzaur
 *
 * This file is part of BloomingWaters.
 * BloomingWaters is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * BloomingWaters is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with BloomingWaters.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.sayuzaur.bloomingwaters.event.init;

import io.github.sayuzaur.bloomingwaters.item.MudBucket;
import io.github.sayuzaur.bloomingwaters.item.WetlandsPlantItem;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.event.registry.ItemRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.template.item.TemplateItem;

import java.lang.invoke.MethodHandles;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.NAMESPACE;

public class ItemListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    public static Item CATTAILS_ITEM;
    public static Item REEDS_ITEM;
    public static Item BOG_GRASS_ITEM;
    public static Item MUD_BUCKET;

    @EventListener
    public static void registerItems(ItemRegistryEvent event) {
        CATTAILS_ITEM = new WetlandsPlantItem(NAMESPACE.id("cattails_item"));
        REEDS_ITEM = new WetlandsPlantItem(NAMESPACE.id("reeds_item"));
        BOG_GRASS_ITEM = new WetlandsPlantItem(NAMESPACE.id("bog_grass_item"));
        MUD_BUCKET = new MudBucket(NAMESPACE.id("mud_bucket"));
    }

}
