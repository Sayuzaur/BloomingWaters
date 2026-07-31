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

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.item.ItemStack;
import paulevs.bhcreative.api.CreativeTab;
import paulevs.bhcreative.api.SimpleTab;
import paulevs.bhcreative.registry.TabRegistryEvent;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.NAMESPACE;

public class CreativeTabListener {
    public static CreativeTab tabBloomingWaters;

    @EventListener
    public void onTabInit(TabRegistryEvent event) {
        tabBloomingWaters = new SimpleTab(NAMESPACE.id("bloomingwaters_creative_tab"), BlockListener.LILY_PAD.asItem());
        event.register(tabBloomingWaters);

        tabBloomingWaters.addItem(new ItemStack(BlockListener.MUD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.SINKING_MUD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.DRIED_MUD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.PEAT));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.DRIED_PEAT));
        tabBloomingWaters.addItem(new ItemStack(ItemListener.MUD_BUCKET));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_LOG));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_LEAVES));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_VINES));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_SAPLING));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_PLANKS));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_PLANKS_STAIRS));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.WILLOW_PLANKS_STAIRS));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSSY_WILLOW_LOG));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSSY_OAK_LOG));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSSY_SPRUCE_LOG));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSSY_BIRCH_LOG));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSS_BLOCK));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.MOSS_CARPET));

        tabBloomingWaters.addItem(new ItemStack(ItemListener.CATTAILS_ITEM));
        tabBloomingWaters.addItem(new ItemStack(ItemListener.REEDS_ITEM));
        tabBloomingWaters.addItem(new ItemStack(ItemListener.BOG_GRASS_ITEM));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.BOG_GRASS_SHORT));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.FUNGAL_POD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.RAIN_CAP));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.DRIFTWOOD_SHROOM));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.SHELF_FUNGUS));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.FORGET_ME_NOT));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.MARSH_MARIGOLD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.BOG_VIOLET));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.FIREFLY_BUSH));

        tabBloomingWaters.addItem(new ItemStack(BlockListener.LILY_PAD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.LILY_FLOWER));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.FROST_LILY_PAD));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.FROST_LILY_FLOWER));
        tabBloomingWaters.addItem(new ItemStack(BlockListener.DUCKWEED));
    }
}
