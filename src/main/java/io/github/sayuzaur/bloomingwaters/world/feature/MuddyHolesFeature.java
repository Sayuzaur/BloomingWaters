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

public class MuddyHolesFeature extends Feature {
    protected int maxHoles;
    protected int targetY;
    protected int range;

    public MuddyHolesFeature(int maxHoles,  int spread, int y) {
        this.maxHoles = maxHoles;
        this.range = spread;
        this.targetY = y;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int attempts = 128;
        int dirtIntoMudChance = 3;
        int generatedHoles = 0;

        for (int i = 0; i < attempts && generatedHoles < maxHoles; i++) {
            //Pick xyz target to generate hole
            int varX = x + random.nextInt(range) - random.nextInt(range);
            int varZ = z + random.nextInt(range) - random.nextInt(range);
            int varY = targetY;

            if (isValidTarget(world, varX, varY, varZ)) {
                world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, Block.WATER.id);
                world.setBlockWithoutNotifyingNeighbors(varX, varY - 1, varZ, BlockListener.MUD.id);
                //Swaps some dirt into mud only on Y63, it's not needed on Y > 63
                if (targetY == 63) {
                    if (world.getBlockId(varX + 1, varY - 1, varZ) == Block.DIRT.id && random.nextInt(dirtIntoMudChance) != 0) {
                        world.setBlockWithoutNotifyingNeighbors(varX + 1, varY - 1, varZ, BlockListener.MUD.id);
                    }
                    if (world.getBlockId(varX - 1, varY - 1, varZ) == Block.DIRT.id && random.nextInt(dirtIntoMudChance) != 0) {
                        world.setBlockWithoutNotifyingNeighbors(varX - 1, varY - 1, varZ, BlockListener.MUD.id);
                    }
                    if (world.getBlockId(varX, varY - 1, varZ + 1) == Block.DIRT.id && random.nextInt(dirtIntoMudChance) != 0) {
                        world.setBlockWithoutNotifyingNeighbors(varX, varY - 1, varZ + 1, BlockListener.MUD.id);
                    }
                    if (world.getBlockId(varX, varY - 1, varZ - 1) == Block.DIRT.id && random.nextInt(dirtIntoMudChance) != 0) {
                        world.setBlockWithoutNotifyingNeighbors(varX, varY - 1, varZ - 1, BlockListener.MUD.id);
                    }
                }
                generatedHoles++;
            }
        }
        return true;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        return     world.getBlockId(x, y, z) == BlockListener.MUD.id
                && world.isAir(x, y + 1, z)
                && world.hasSkyLight(x, y + 1, z)
                && (world.getBlockState(x, y - 1, z).getBlock().isFullCube() || world.getMaterial(x, y - 1, z) == Material.WATER)
                && (world.getBlockState(x + 1, y, z).getBlock().isFullCube() || world.getMaterial(x + 1, y, z) == Material.WATER)
                && (world.getBlockState(x - 1, y, z).getBlock().isFullCube() || world.getMaterial(x - 1, y, z) == Material.WATER)
                && (world.getBlockState(x, y, z + 1).getBlock().isFullCube() || world.getMaterial(x, y, z + 1) == Material.WATER)
                && (world.getBlockState(x, y, z - 1).getBlock().isFullCube() || world.getMaterial(x, y, z - 1) == Material.WATER);
    }
}
