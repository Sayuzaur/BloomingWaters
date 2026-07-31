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

import net.glasslauncher.mods.gcapi3.api.ConfigCategory;
import net.glasslauncher.mods.gcapi3.api.ConfigEntry;

public class BloomingWatersConfig {

    @ConfigCategory(name = "WorldGen")
    public WorldGenConfig worldgen = new WorldGenConfig();

    @ConfigCategory(name = "Blocks")
    public BlocksConfig blocksconfig = new BlocksConfig();

    public static class WorldGenConfig {
        @ConfigEntry(name = "New surface generation", description = "MUD replaces grass on Y 63-66 in Swamplands biome.", requiresRestart = true)
        public Boolean surfaceRulesGen = true;

        @ConfigEntry(name = "New features generation", description = "Features being new flora, if 'false' it overwrites other feature settings.", requiresRestart = true)
        public Boolean allFeaturesGen = true;

        @ConfigEntry(name = "New fog colour", description = "Fog has different colour in Swamplands biome.", requiresRestart = true)
        public Boolean newFogColour = true;

        @ConfigEntry(name = "Ground erosion", description = "Generates holes with water on lower Y, works only on MUD.", requiresRestart = true)
        public Boolean muddyHolesGen = true;

        @ConfigEntry(name = "Sinking MUD", description = "Generates patches with SINKING MUD, works only on MUD.", requiresRestart = true)
        public Boolean sinkingMudGen = true;

        @ConfigEntry(name = "Peat", description = "Generates patches with PEAT.", requiresRestart = true)
        public Boolean peatBlobsGen = true;

        @ConfigEntry(name = "Willow trees", description = "Generates WILLOW trees in Swamplands.", requiresRestart = true)
        public Boolean willowTreeGen = true;

        @ConfigEntry(name = "Oak trees", description = "Generates OAK trees in Swamplands.", requiresRestart = true)
        public Boolean oakTreeGen = true;

        @ConfigEntry(name = "Dead trees", description = "Generates DEAD trees and DRIFTWOOD", requiresRestart = true)
        public Boolean deadTreeGen = true;

        @ConfigEntry(name = "Wetland tall grasses", description = "Generates patches of REEDS, CATTAILS and TALL BOG GRASS.", requiresRestart = true)
        public Boolean tallPlantsGen = true;

        @ConfigEntry(name = "Wetland short grass", description = "Generates patches of SHORT BOG GRASS.", requiresRestart = true)
        public Boolean shortPlantsGen = true;

        @ConfigEntry(name = "Vanilla tall grass", description = "Generates patches of vanilla TALL GRASS in Swamplands.", requiresRestart = true)
        public Boolean vanillaGrassGen = true;

        @ConfigEntry(name = "Water floating plants", description = "Generates DUCKWEED, LILY PADS and FROST LILY PADS.", requiresRestart = true)
        public Boolean floatingPlantsGen = true;

        @ConfigEntry(name = "Frost Lily Pads", description = "Generates FROST LILY PADS on border of swamplands and taiga.", requiresRestart = true)
        public Boolean frostLilyGen = true;

        @ConfigEntry(name = "Flowers", description = "Generates patches of wetlands FLOWERS.", requiresRestart = true)
        public Boolean flowerGen = true;

        @ConfigEntry(name = "Mushrooms", description = "Generates patches of wetlands MUSHROOMS.", requiresRestart = true)
        public Boolean shroomsGen = true;

        @ConfigEntry(name = "Firefly bush", description = "Generates FIREFLY BUSHES.", requiresRestart = true)
        public Boolean fireflyBushGen = true;
    }

    public static class BlocksConfig {
        @ConfigEntry(name = "Stairs upside-down", description = "Allow STAIRS to be placed upside-down. It's a little junky.")
        public Boolean stairsModern = false;

        @ConfigEntry(name = "Slabs upside-down", description = "Allow SLABS to be placed upside-down. It's also junky.")
        public Boolean slabsModern = false;

        @ConfigEntry(name = "Log rotation", description = "Allow LOGS to be rotated. This one works good.")
        public Boolean logsModern = true;
    }
}
