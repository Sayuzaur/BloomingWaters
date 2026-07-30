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

import io.github.sayuzaur.bloomingwaters.world.feature.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.GrassPatchFeature;
import net.minecraft.world.gen.feature.OakTreeFeature;
import net.modificationstation.stationapi.api.event.worldgen.biome.BiomeModificationEvent;
import net.modificationstation.stationapi.api.worldgen.feature.DefaultFeatures;
import net.modificationstation.stationapi.api.worldgen.feature.HeightScatterFeature;
import net.modificationstation.stationapi.api.worldgen.surface.SurfaceBuilder;
import net.modificationstation.stationapi.api.worldgen.surface.condition.SurfaceCondition;

import java.util.Objects;

public class BiomeModificationListener {
    private static final int mudMinY = 63;
    private static final int mudMaxY = 66;
    private static final int mudDepth = 2;
    private static final int dirtMinWaterY = 48;

    //Punish me for my sins
    private static final SurfaceCondition SKYLIGHT_COND = (world, x, y, z, state) -> world.hasSkyLight(x, y + 1, z);
    private static final SurfaceCondition UNDERWATER_COND = (world, x, y, z, state) -> world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER;
    private static final SurfaceCondition SECOND_MUD_LAYER_COND = (world, x, y, z, blockState) ->
               world.isAir(x, y + 2, z)
            && world.hasSkyLight(x, y + 2, z)
            && y != mudMaxY
            && y != (mudMinY - 1)
            && world.getBlockState(x, y + 1, z).getMaterial() != Material.WATER;
    private static final SurfaceCondition THIRD_MUD_LAYER_COND = (world, x, y, z, blockState) ->
               world.isAir(x, y + 3, z)
            && world.hasSkyLight(x, y + 3, z)
            && y != mudMaxY
            && world.getBlockState(x, y + 2, z).getMaterial() != Material.WATER;

    private static final Feature MUDDY_HOLES_63 = new HeightScatterFeature(new MuddyHolesFeature(12, 12, 63), 1);
    private static final Feature MUDDY_HOLES_64 = new HeightScatterFeature(new MuddyHolesFeature(10, 10, 64), 1);
    private static final Feature MUDDY_HOLES_65 = new HeightScatterFeature(new MuddyHolesFeature(4, 3, 65), 1);
    private static final Feature PEAT_BLOB_LOW = new LowChanceHeightScatterFeature(new PeatBlobFeature(mudMinY, mudMaxY, false), 1, 2);
    private static final Feature PEAT_BLOB_HIGH = new LowChanceHeightScatterFeature(new PeatBlobFeature(mudMaxY + 1, 78, true), 1, 8);
    private static final Feature SINKING_MUD = new LowChanceHeightScatterFeature(new SinkingMudBlobFeature(mudMinY, mudMaxY), 1, 8);

    private static final Feature CATTAILS = new LowChanceHeightScatterFeature(new WetlandsTallPatchFeature(10, 6, 1), 1, 6);
    private static final Feature SMALL_CATTAILS = new LowChanceHeightScatterFeature(new WetlandsTallPatchFeature(3,  16, 1), 1, 2);

    private static final Feature REEDS = new LowChanceHeightScatterFeature(new WetlandsTallPatchFeature(10,  6, 2), 1, 6);
    private static final Feature SMALL_REEDS = new LowChanceHeightScatterFeature(new WetlandsTallPatchFeature(3, 16, 2), 1, 2);

    private static final Feature LILY_PAD = new LowChanceHeightScatterFeature(new LilyPadFeature(5), 6, 2);
    private static final Feature DUCKWEED_BIG = new LowChanceHeightScatterFeature(new DuckweedPatchFeature(12, 5), 2, 4);
    private static final Feature DUCKWEED_SMALL = new LowChanceHeightScatterFeature(new DuckweedPatchFeature(4, 2), 2, 2);

    private static final Feature WILLOW_TREE_SCATTERED = new HeightScatterFeature(new WillowTreeFeature(), 1);
    private static final Feature LARGE_WILLOW_TREE_SCATTERED = new LowChanceHeightScatterFeature(new WillowTreeLargeFeature(), 1, 2);
    private static final Feature OAK_TREE_SCATTERED_LOCAL = new LowChanceHeightScatterFeature(new OakTreeFeature(), 1, 2);
    private static final Feature WILLOW_TREE_UNDERGROW = new LowChanceHeightScatterFeature(new WillowTreeOnMudFeature(), 1, 7);
    private static final Feature DEAD_OAK_TREE = new LowChanceHeightScatterFeature(new DeadOakTreeFeature(), 1, 12);

    private static final Feature FALLEN_WILLOW = new LowChanceHeightScatterFeature(new FallenWillowFeature(), 1, 8);
    private static final Feature DRIFTWOOD = new LowChanceHeightScatterFeature(new DriftwoodFeature(), 1, 7);

    private static final Feature SHORT_BOG_GRASS = new HeightScatterFeature(new ShortBogGrassSpamFeature(6, 4), 4);

    private static final Feature FUNGAL_POD = new LowChanceHeightScatterFeature(new FlowerPatchFeature(2, 3, 1), 1, 6);
    private static final Feature RAIN_CAP = new LowChanceHeightScatterFeature(new FlowerPatchFeature(2, 3, 2), 1, 6);
    private static final Feature FORGET_ME_NOT = new LowChanceHeightScatterFeature(new FlowerPatchFeature(5, 5, 3), 1, 5);
    private static final Feature MARSH_MARIGOLD = new LowChanceHeightScatterFeature(new FlowerPatchFeature(5, 5, 4), 1, 5);
    private static final Feature BOG_VIOLET = new LowChanceHeightScatterFeature(new FlowerPatchFeature(5, 5, 5), 1, 5);
    private static final Feature MOSS_CARPET_SMALL = new LowChanceHeightScatterFeature(new FlowerPatchFeature(4, 2, 10), 1, 8);
    private static final Feature MOSS_CARPET_BIG = new LowChanceHeightScatterFeature(new FlowerPatchFeature(8, 4, 10), 1, 12);
    private static final Feature FIREFLY_BUSH = new LowChanceHeightScatterFeature(new FlowerPatchFeature(1, 2, 8), 1, 6);

    private static final Feature TALL_GRASS = new HeightScatterFeature(new GrassPatchFeature(31, 1), 2);

    @EventListener
    public void biomeModification(BiomeModificationEvent event) {
        if (Objects.equals(event.biome.name, "Swampland")) {
            //Fog colour
            event.biome.setFogColor(0x7EA89E);
            //Remove ALL default features
            event.biome.setNoDimensionFeatures(true);

            //Slopes first so mud doesnt spawn on steep slopes
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).replace(Block.STONE).ground(1).slope(50).range(mudMinY, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).replace(Block.STONE).ground(3).slope(50).range(mudMinY, 127).build());

            //Replace surface with mud
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).ground(mudDepth).replace(Block.STONE).range(mudMinY, mudMaxY).condition(SKYLIGHT_COND, 0).build());
            //Have to add second mud layer below by hand with this
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).replace(Block.STONE).range(mudMinY - 1, mudMaxY).condition(SECOND_MUD_LAYER_COND, 0).build());
            //Dirt under mud
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).replace(Block.STONE).range(mudMinY - 2, mudMaxY).condition(THIRD_MUD_LAYER_COND, 0).build());
            //Because Stapi is buggy, need to add checks for every possible bugged ore and gravel/dirt patches
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).ground(mudDepth).replace(Block.GRAVEL).range(mudMinY, mudMaxY).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).ground(mudDepth).replace(Block.DIRT).range(mudMinY, mudMaxY).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).ground(mudDepth).replace(Block.COAL_ORE).range(mudMinY, mudMaxY).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(BlockListener.MUD).ground(mudDepth).replace(Block.IRON_ORE).range(mudMinY, mudMaxY).build());

            //Grass on top above set Y
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).ground(1).replace(Block.STONE).range(mudMaxY + 1, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).ground(1).replace(Block.GRAVEL).range(mudMaxY + 1, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).ground(1).replace(Block.DIRT).range(mudMaxY + 1, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).ground(1).replace(Block.COAL_ORE).range(mudMaxY + 1, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.GRASS_BLOCK).ground(1).replace(Block.IRON_ORE).range(mudMaxY + 1, 127).build());

            //Dirt on top above set Y under grass
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(4).replace(Block.STONE).range(mudMaxY, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(4).replace(Block.GRAVEL).range(mudMaxY, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(4).replace(Block.COAL_ORE).range(mudMaxY, 127).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(4).replace(Block.IRON_ORE).range(mudMaxY, 127).build());

            //Put dirt below water level
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(1).replace(Block.STONE).range(dirtMinWaterY, mudMinY - 1).condition(UNDERWATER_COND, 0).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(1).replace(Block.GRAVEL).range(dirtMinWaterY, mudMinY - 1).condition(UNDERWATER_COND, 0).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(1).replace(Block.COAL_ORE).range(dirtMinWaterY, mudMinY - 1).condition(UNDERWATER_COND, 0).build());
            event.biome.addSurfaceRule(SurfaceBuilder.start(Block.DIRT).ground(1).replace(Block.IRON_ORE).range(dirtMinWaterY, mudMinY - 1).condition(UNDERWATER_COND, 0).build());

            //Terrain features
            event.biome.addFeature(MUDDY_HOLES_63);
            event.biome.addFeature(MUDDY_HOLES_64);
            event.biome.addFeature(MUDDY_HOLES_65);
            event.biome.addFeature(PEAT_BLOB_LOW);
            event.biome.addFeature(PEAT_BLOB_HIGH);
            event.biome.addFeature(SINKING_MUD);

            //Wetland plant features
            event.biome.addFeature(CATTAILS);
            event.biome.addFeature(SMALL_CATTAILS);
            event.biome.addFeature(REEDS);
            event.biome.addFeature(SMALL_REEDS);

            event.biome.addFeature(LILY_PAD);
            event.biome.addFeature(DUCKWEED_BIG);
            event.biome.addFeature(DUCKWEED_SMALL);

            //Tree features
            event.biome.addFeature(WILLOW_TREE_SCATTERED);
            event.biome.addFeature(LARGE_WILLOW_TREE_SCATTERED);
            event.biome.addFeature(OAK_TREE_SCATTERED_LOCAL);
            event.biome.addFeature(WILLOW_TREE_UNDERGROW);
            event.biome.addFeature(DEAD_OAK_TREE);

            event.biome.addFeature(FALLEN_WILLOW);
            event.biome.addFeature(DRIFTWOOD);

            //Low priority wetlands plant features
            event.biome.addFeature(SHORT_BOG_GRASS);

            event.biome.addFeature(FUNGAL_POD);
            event.biome.addFeature(RAIN_CAP);
            event.biome.addFeature(FORGET_ME_NOT);
            event.biome.addFeature(MARSH_MARIGOLD);
            event.biome.addFeature(BOG_VIOLET);
            event.biome.addFeature(MOSS_CARPET_SMALL);
            event.biome.addFeature(MOSS_CARPET_BIG);
            event.biome.addFeature(FIREFLY_BUSH);

            event.biome.addFeature(TALL_GRASS);


            //Default ore features
            event.biome.addFeature(DefaultFeatures.DIRT_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.GRAVEL_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.COAL_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.IRON_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.GOLD_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.REDSTONE_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.DIAMOND_ORE_SCATTERED);
            event.biome.addFeature(DefaultFeatures.LAPIS_LAZULI_ORE_SCATTERED);

            //Dungeon feature
            event.biome.addFeature(DefaultFeatures.DUNGEON_SCATTERED);
        }
    }
}
