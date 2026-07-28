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

public class PeatBlobFeature extends Feature {
    protected int minY;
    protected int maxY;
    protected boolean forceMoss;

    public PeatBlobFeature(int minY, int maxY, boolean forceMoss) {
        this.minY = minY;
        this.maxY = maxY;
        this.forceMoss = forceMoss;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {

        if (y < minY || y > maxY) {
            return false;
        }

        int peatBlockId = BlockListener.PEAT.id;
        int mossCarpetBlockId = BlockListener.MOSS_CARPET.id;
        int innerSize = 1;
        int outerSize = innerSize + 1;
        int varY;
        boolean canGenerate;

        //Inner part
        for (int varX = x - innerSize; varX <= x + innerSize; varX++) {
            for (int varZ = z - innerSize; varZ <= z + innerSize; varZ++) {
                if (isValidTarget(world, varX, y, varZ)) {
                    canGenerate = true;
                    varY = y - 1;
                } else if (isValidTarget(world, varX, y - 1, varZ)) {
                    canGenerate = true;
                    varY = y - 2;
                } else if (isValidTarget(world, varX, y + 1, varZ)) {
                    canGenerate = true;
                    varY = y;
                } else {
                    canGenerate = false;
                    varY = y;
                }

                if (canGenerate) {
                    world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, peatBlockId);

                    if (random.nextInt(3) != 0 && canReplace(world, varX, varY - 1, varZ)) {
                        world.setBlockWithoutNotifyingNeighbors(varX, varY - 1, varZ, peatBlockId);

                        if (random.nextInt(3) == 0 && canReplace(world, varX, varY - 2, varZ)) {
                            world.setBlockWithoutNotifyingNeighbors(varX, varY - 2, varZ, peatBlockId);
                        }
                    }

                    if (world.isAir(varX, varY + 1, varZ)) {
                        if (forceMoss) {
                            world.setBlockWithoutNotifyingNeighbors(varX, varY + 1, varZ, mossCarpetBlockId);
                        } else if (random.nextInt(2) == 0) {
                            world.setBlockWithoutNotifyingNeighbors(varX, varY + 1, varZ, mossCarpetBlockId);
                        }
                    }
                }
            }
        }

        //Outer part, 50/50 to generate
        for (int varX = x - outerSize; varX <= x + outerSize; varX++) {
            for (int varZ = z - outerSize; varZ <= z + outerSize; varZ++) {
                if (random.nextInt(2) == 0) {
                    if (isValidTarget(world, varX, y, varZ)) {
                        canGenerate = true;
                        varY = y - 1;
                    } else if (isValidTarget(world, varX, y - 1, varZ)) {
                        canGenerate = true;
                        varY = y - 2;
                    } else if (isValidTarget(world, varX, y + 1, varZ)) {
                        canGenerate = true;
                        varY = y;
                    } else {
                        canGenerate = false;
                        varY = y;
                    }

                    if (canGenerate) {
                        world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, peatBlockId);

                        if (random.nextInt(3) != 0 && canReplace(world, varX, varY - 1, varZ)) {
                            world.setBlockWithoutNotifyingNeighbors(varX, varY - 1, varZ, peatBlockId);

                            if (random.nextInt(3) == 0 && canReplace(world, varX, varY - 2, varZ)) {
                                world.setBlockWithoutNotifyingNeighbors(varX, varY - 2, varZ, peatBlockId);
                            }
                        }

                        if (world.isAir(varX, varY + 1, varZ)) {
                            if (forceMoss) {
                                world.setBlockWithoutNotifyingNeighbors(varX, varY + 1, varZ, mossCarpetBlockId);
                            } else if (random.nextInt(2) == 0) {
                                world.setBlockWithoutNotifyingNeighbors(varX, varY + 1, varZ, mossCarpetBlockId);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean canReplace(World world, int x, int y, int z) {
        return world.getBlockId(x, y, z) == Block.DIRT.id
                || world.getBlockId(x, y, z) == Block.GRASS_BLOCK.id
                || world.getBlockId(x, y, z) == BlockListener.MUD.id
                || world.getBlockId(x, y, z) == BlockListener.PEAT.id;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        return (world.isAir(x, y, z)
                || world.getMaterial(x, y, z) == Material.PLANT
                || world.getMaterial(x, y, z) == Material.WATER)
                && canReplace(world, x, y - 1, z);
    }
}
