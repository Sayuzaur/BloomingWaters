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

package io.github.sayuzaur.bloomingwaters.world.feature;

import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.block.LilyPad.FLOWERING;
import static io.github.sayuzaur.bloomingwaters.block.LilyPad.HORIZONTAL_FACING;

public class LilyPadFeature extends Feature {
    protected int flowerChance;

    public LilyPadFeature(int flowerChance) {
        this.flowerChance = flowerChance;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int forceY = 64;
        if (isValidTarget(world, x, forceY, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, forceY, z, BlockListener.LILY_PAD.id);

            BlockState state = world.getBlockState(x, forceY, z);
            Direction randDirection = null;
            boolean flowering = false;
            int randIntDirection = random.nextInt(4);
            switch (randIntDirection) {
                case 0 -> randDirection = Direction.NORTH;
                case 1 -> randDirection = Direction.SOUTH;
                case 2 -> randDirection = Direction.EAST;
                case 3 -> randDirection = Direction.WEST;
            }
            if (random.nextInt(flowerChance) == 0) {
                flowering = true;
            }
            world.setBlockStateWithoutNotifyingNeighbors(x, forceY, z, state.with(HORIZONTAL_FACING, randDirection).with(FLOWERING, flowering));
        }
        return true;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        if (world.isAir(x, y, z) && world.hasSkyLight(x, y, z) && BlockListener.LILY_PAD.canGrow(world, x, y, z)) {
            if (world.getBlockState(x, y - 2, z).getBlock().isFullCube()) {
                return true;
            } else if (world.getMaterial(x, y - 2, z) == Material.WATER) {
                if (world.getBlockState(x, y - 3, z).getBlock().isFullCube()) {
                    return true;
                } else if (world.getMaterial(x, y - 3, z) == Material.WATER && world.getBlockState(x, y - 4, z).getBlock().isFullCube()){
                    return true;
                }
            }
        }
        return false;
    }
}
