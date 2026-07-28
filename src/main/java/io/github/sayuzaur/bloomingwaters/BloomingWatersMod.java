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

package io.github.sayuzaur.bloomingwaters;

import io.github.sayuzaur.bloomingwaters.block.soundgroup.WetSoilSoundGroup;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.tag.TagKey;
import net.modificationstation.stationapi.api.util.Namespace;

public class BloomingWatersMod {
    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    //BLOCK TAGS
    public static final TagKey<Block> WETPLANTS_PLACE_AT = TagKey.of(BlockRegistry.KEY, NAMESPACE.id("wetlands_plants_can_place_at"));
    public static final TagKey<Block> WETPLANTS_GROW_AT = TagKey.of(BlockRegistry.KEY, NAMESPACE.id("wetlands_plants_can_grow_at"));

    //FUEL ITEM TAGS
    public static final TagKey<Item> FUEL_BASIC = TagKey.of(ItemRegistry.KEY, NAMESPACE.id("fuel/fuel_basic"));
    public static final TagKey<Item> FUEL_WEAK = TagKey.of(ItemRegistry.KEY, NAMESPACE.id("fuel/fuel_weak"));
    public static final TagKey<Item> FUEL_STRONG = TagKey.of(ItemRegistry.KEY, NAMESPACE.id("fuel/fuel_strong"));

    //SOUNDGROUPS
    public static final BlockSoundGroup WETSOIL_SOUND_GROUP = new WetSoilSoundGroup("WetSoil", 1.0F, 1.0F);
}
