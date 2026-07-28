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

import io.github.sayuzaur.bloomingwaters.block.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;

import java.lang.invoke.MethodHandles;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.NAMESPACE;

public class BlockListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    public static Block MUD;
    public static Block SINKING_MUD;
    public static Block PEAT;
    public static Block SILT;

    public static Block DRIED_PEAT;
    public static Block DRIED_MUD;

    public static Block CATTAILS;
    public static Block REEDS;
    public static Block BOG_GRASS_TALL;
    public static Block BOG_GRASS_SHORT;

    public static Block INWATER_CATTAILS;
    public static Block INWATER_REEDS;
    public static Block INWATER_BOG_GRASS;

    public static Block LILY_PAD;
    public static Block FROST_LILY_PAD;
    public static Block DUCKWEED;

    public static Block WILLOW_LOG;
    public static Block WILLOW_LEAVES;
    public static Block WILLOW_VINES;
    public static Block WILLOW_SAPLING;
    public static Block WILLOW_PLANKS;
    public static Block WILLOW_PLANKS_STAIRS;
    public static Block WILLOW_PLANKS_SLAB;

    public static Block MOSSY_WILLOW_LOG;
    public static Block MOSSY_OAK_LOG;
    public static Block MOSSY_BIRCH_LOG;
    public static Block MOSSY_SPRUCE_LOG;

    public static Block MOSS_BLOCK;
    public static Block MOSS_CARPET;

    public static Block FUNGAL_POD;
    public static Block RAIN_CAP;
    public static Block DRIFTWOOD_SHROOM;
    public static Block SHELF_FUNGUS;

    public static Block FORGET_ME_NOT;
    public static Block MARSH_MARIGOLD;
    public static Block BOG_VIOLET;

    @EventListener
    private static void registerBlocks(BlockRegistryEvent event) {
        //Technical blocks goes first so it's not a mess in AMI
        CATTAILS = new WetlandsTallPlant(NAMESPACE.id("cattails"));
        REEDS = new WetlandsTallPlant(NAMESPACE.id("reeds"));
        BOG_GRASS_TALL = new WetlandsTallPlant(NAMESPACE.id("bog_grass_tall"));

        INWATER_CATTAILS = new FakeWaterTallPlant(NAMESPACE.id("inwater_cattails"));
        INWATER_REEDS = new FakeWaterTallPlant(NAMESPACE.id("inwater_reeds"));
        INWATER_BOG_GRASS = new FakeWaterTallPlant(NAMESPACE.id("inwater_bog_grass"));

        MUD = new Mud(NAMESPACE.id("mud"));
        SINKING_MUD = new SinkingMud(NAMESPACE.id("sinking_mud"));
        PEAT = new Mud(NAMESPACE.id("peat"));
        SILT = new Silt(NAMESPACE.id("silt"));

        DRIED_MUD = new TemplateBlock(NAMESPACE.id("dried_mud"), Material.SOIL).setHardness(0.4F).setSoundGroup(Block.DIRT_SOUND_GROUP);
        DRIED_PEAT = new TemplateBlock(NAMESPACE.id("dried_peat"), Material.SOIL).setHardness(0.4F).setSoundGroup(Block.DIRT_SOUND_GROUP);

        LILY_PAD = new LilyPad(NAMESPACE.id("lily_pad"));
        FROST_LILY_PAD = new LilyPad(NAMESPACE.id("frost_lily_pad"));
        DUCKWEED = new Duckweed(NAMESPACE.id("duckweed"));

        WILLOW_LOG = new WillowLog(NAMESPACE.id("willow_log"));
        WILLOW_LEAVES = new WillowLeaves(NAMESPACE.id("willow_leaves"));
        WILLOW_VINES = new WillowVines(NAMESPACE.id("willow_vines"));
        WILLOW_SAPLING = new WillowSapling(NAMESPACE.id("willow_sapling"));
        WILLOW_PLANKS = new WillowPlanks(NAMESPACE.id("willow_planks"));
        WILLOW_PLANKS_STAIRS = new WillowStairs(NAMESPACE.id("willow_stairs"));
        WILLOW_PLANKS_SLAB = new WillowSlab(NAMESPACE.id("willow_slab"));

        MOSSY_WILLOW_LOG = new WillowLog(NAMESPACE.id("mossy_willow_log"));
        MOSSY_OAK_LOG = new WillowLog(NAMESPACE.id("mossy_oak_log"));
        MOSSY_BIRCH_LOG = new WillowLog(NAMESPACE.id("mossy_birch_log"));
        MOSSY_SPRUCE_LOG = new WillowLog(NAMESPACE.id("mossy_spruce_log"));

        MOSS_BLOCK = new MossBlock(NAMESPACE.id("moss_block"));
        MOSS_CARPET = new MossCarpet(NAMESPACE.id("moss_carpet"));

        BOG_GRASS_SHORT = new WetlandsShortPlant(NAMESPACE.id("bog_grass_short"));

        FUNGAL_POD = new WetlandsShroom(NAMESPACE.id("fungal_pod"));
        RAIN_CAP = new WetlandsShroom(NAMESPACE.id("rain_cap"));
        DRIFTWOOD_SHROOM = new WetlandsShroom(NAMESPACE.id("driftwood_shroom"));
        SHELF_FUNGUS = new ShelfFungus(NAMESPACE.id("shelf_fungus"));

        FORGET_ME_NOT = new WetlandsShortPlant(NAMESPACE.id("forget_me_not"));
        MARSH_MARIGOLD = new WetlandsShortPlant(NAMESPACE.id("marsh_marigold"));
        BOG_VIOLET = new WetlandsShortPlant(NAMESPACE.id("bog_violet"));
    }
}
