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
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.block.ShelfFungus.HORIZONTAL_FACING;

public class DeadOakTreeFeature extends Feature {
    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        //Check for maxY
        if (y > 70) {
            return false;
        }

        //Check for 'roots' tree base block
        if (world.getBlockId(x, y - 1, z) != BlockListener.MUD.id
                && world.getBlockId(x, y - 1, z) != Block.GRASS_BLOCK.id
                && world.getBlockId(x, y - 1, z) != Block.DIRT.id) {
            return false;
        }

        //Check for free space above
        for (int i = 0; i < 4; i++) {
            if (!world.isAir(x, y + i, z)) {
                return false;
            }
        }

        world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);

        //Check if 'roots' block is exposed to air
        if (world.isAir(x + 1, y - 1, z)
                || world.isAir(x - 1, y - 1, z)
                || world.isAir(x, y - 1, z + 1)
                || world.isAir(x, y - 1, z - 1)) {
            world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.LOG.id);
            world.setBlockWithoutNotifyingNeighbors(x, y - 2, z, Block.DIRT.id);
        }

        //Logs placement
        int treeHeigh = 4 + random.nextInt(2);
        int offsetX = random.nextInt(3) - 1;
        int offsetZ = random.nextInt(3) - 1;

        int offsetRand = random.nextInt(3);
        if (offsetRand == 0) {
            offsetZ = 0;
        } else if (offsetRand == 1) {
            offsetX = 0;
        }

        for (int i = 0; i < treeHeigh; i++) {
            world.setBlockWithoutNotifyingNeighbors(x, y + i, z, Block.LOG.id);
            //Shroom placement
            if (i >= 1 && random.nextInt(3) == 0) {
                int randSide = random.nextInt(4);
                switch (randSide){
                    case 0 -> {
                        if (world.isAir(x + 1, y + i, z)) {
                            world.setBlockWithoutNotifyingNeighbors(x + 1, y + i, z, BlockListener.SHELF_FUNGUS.id);
                            world.setBlockStateWithoutNotifyingNeighbors(x + 1, y + i, z, world.getBlockState(x + 1, y + i, z).with(HORIZONTAL_FACING, Direction.WEST));
                        }
                    }
                    case 1 -> {
                        if (world.isAir(x - 1, y + i, z)) {
                            world.setBlockWithoutNotifyingNeighbors(x - 1, y + i, z, BlockListener.SHELF_FUNGUS.id);
                            world.setBlockStateWithoutNotifyingNeighbors(x - 1, y + i, z, world.getBlockState(x - 1, y + i, z).with(HORIZONTAL_FACING, Direction.EAST));
                        }
                    }
                    case 2 -> {
                        if (world.isAir(x, y + i, z + 1)) {
                            world.setBlockWithoutNotifyingNeighbors(x, y + i, z + 1, BlockListener.SHELF_FUNGUS.id);
                            world.setBlockStateWithoutNotifyingNeighbors(x, y + i, z + 1, world.getBlockState(x, y + i, z + 1).with(HORIZONTAL_FACING, Direction.NORTH));
                        }
                    }
                    case 3 -> {
                        if (world.isAir(x, y + i, z - 1)) {
                            world.setBlockWithoutNotifyingNeighbors(x, y + i, z - 1, BlockListener.SHELF_FUNGUS.id);
                            world.setBlockStateWithoutNotifyingNeighbors(x, y + i, z - 1, world.getBlockState(x, y + i, z - 1).with(HORIZONTAL_FACING, Direction.SOUTH));
                        }
                    }
                }
            }
            //Branch placement
            if (i == treeHeigh - 1) {
                world.setBlockWithoutNotifyingNeighbors(x + offsetX, y + i - 1, z + offsetZ, Block.LOG.id);
            }
        }

        //Shroom under a tree placement
        if (world.isAir(x + offsetX, y, z + offsetZ)
                && (world.getBlockId(x + offsetX, y - 1, z + offsetZ) == BlockListener.MUD.id || world.getBlockId(x + offsetX, y - 1, z + offsetZ) == Block.GRASS_BLOCK.id)) {
            world.setBlockWithoutNotifyingNeighbors(x + offsetX, y, z + offsetZ, BlockListener.FUNGAL_POD.id);
        }

        return true;
    }
}
