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
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class WillowTreeOnMudFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        if (world.getBlockId(x, y - 1, z) != BlockListener.MUD.id) {
            return false;
        }
        Feature willowTreeFeature = new WillowTreeLargeFeature();
        world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);
        willowTreeFeature.generate(world, random, x, y, z);

        //If failed gen, fix dirt into mud again
        if (world.getBlockId(x, y, z) != BlockListener.WILLOW_LOG.id) {
            world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, BlockListener.MUD.id);
            return false;
        }

        //Replace normal logs to mossy logs
        for (int i = 0; i < 4; i++) {
            world.setBlockWithoutNotifyingNeighbors(x, y + i, z, BlockListener.MOSSY_WILLOW_LOG.id);
        }

        //Check if 'roots' block is exposed to air
        if (world.isAir(x + 1, y - 1, z)
                || world.isAir(x - 1, y - 1, z)
                || world.isAir(x, y - 1, z + 1)
                || world.isAir(x, y - 1, z - 1)) {
            world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, BlockListener.MOSSY_WILLOW_LOG.id);
            world.setBlockWithoutNotifyingNeighbors(x, y - 2, z, Block.DIRT.id);
        }

        //Moss placement
        for (int mossX = x - 1; mossX <= x + 1; ++mossX) {
            for (int mossZ = z - 1; mossZ <= z + 1; ++mossZ) {
                if (random.nextInt(3) != 0) {
                    if (world.getMaterial(mossX, y - 1, mossZ) != Material.WATER
                            && (world.isAir(mossX, y, mossZ) || world.getMaterial(mossX, y, mossZ) == Material.PLANT)
                            && !world.isAir(mossX, y - 1, mossZ)) {
                        world.setBlockWithoutNotifyingNeighbors(mossX, y - 1, mossZ, BlockListener.MOSS_BLOCK.id);
                    }
                    if ((world.getBlockId(mossX, y, mossZ) == BlockListener.MUD.id || world.getBlockId(mossX, y, mossZ) == Block.GRASS_BLOCK.id || world.getBlockId(mossX, y, mossZ) == Block.STONE.id)
                            && (world.isAir(mossX, y + 1, mossZ) || world.getMaterial(mossX, y + 1, mossZ) == Material.PLANT)) {
                        world.setBlockWithoutNotifyingNeighbors(mossX, y + 1, mossZ, BlockListener.MOSS_CARPET.id);
                    }
                }
            }
        }

        return true;
    }
}
