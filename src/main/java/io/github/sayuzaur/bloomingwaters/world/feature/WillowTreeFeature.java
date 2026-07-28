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
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;

import java.util.Random;

public class WillowTreeFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        int treeHeight = random.nextInt(3) + 6;
        boolean canContinue = true;

        if (y >= 1 && y + treeHeight + 1 <= 128) {
            for(int checkY = y; checkY <= y + 1 + treeHeight; ++checkY) {
                byte checkOffset = 1;
                if (checkY == y) {
                    checkOffset = 0;
                }

                if (checkY >= y + 1 + treeHeight - 2) {
                    checkOffset = 2;
                }

                for(int checkX = x - checkOffset; checkX <= x + checkOffset && canContinue; ++checkX) {
                    for(int checkZ = z - checkOffset; checkZ <= z + checkOffset && canContinue; ++checkZ) {
                        if (checkY >= 0 && checkY < 128) {
                            int checkFreeSpaceBlockId = world.getBlockId(checkX, checkY, checkZ);
                            BlockState checkFreeSpaceBlockState = world.getBlockState(checkX, checkY, checkZ);
                            if (checkFreeSpaceBlockId != 0 && !checkFreeSpaceBlockState.isIn(BlockTags.LEAVES)) {
                                canContinue = false;
                            }
                        } else {
                            canContinue = false;
                        }
                    }
                }
            }

            if (!canContinue) {
                return false;

            } else {
                //Tree base gen, changes grass to dirt
                int treeBaseBlockId = world.getBlockId(x, y - 1, z);
                if ((treeBaseBlockId == Block.GRASS_BLOCK.id || treeBaseBlockId == Block.DIRT.id) && y < 128 - treeHeight - 1) {
                    world.setBlockWithoutNotifyingNeighbors(x, y - 1, z, Block.DIRT.id);

                    //Leaves gen
                    int leavesStartY = y - 3 + treeHeight;
                    for(int leavesY = leavesStartY; leavesY <= y + treeHeight; ++leavesY) {
                        int tempY = leavesY - (y + treeHeight);
                        int tempSides = 1 - tempY / 2;

                        for(int leavesX = x - tempSides; leavesX <= x + tempSides; ++leavesX) {
                            int tempX = leavesX - x;

                            for(int leavesZ = z - tempSides; leavesZ <= z + tempSides; ++leavesZ) {
                                int tempZ = leavesZ - z;
                                if ((Math.abs(tempX) != tempSides || Math.abs(tempZ) != tempSides || random.nextInt(2) != 0 && tempY != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(leavesX, leavesY, leavesZ)]) {
                                    world.setBlockWithoutNotifyingNeighbors(leavesX, leavesY, leavesZ, BlockListener.WILLOW_LEAVES.id);
                                }
                            }
                        }
                    }

                    //Vines gen
                    for(int leavesX = x - 2; leavesX <= x + 2; ++leavesX) {
                        for (int leavesZ = z - 2; leavesZ <= z + 2; ++leavesZ) {
                            if (world.getBlockId(leavesX, leavesStartY, leavesZ) == BlockListener.WILLOW_LEAVES.id) {
                                int randVines = random.nextInt(3);

                                if (randVines != 0) {
                                    if (randVines == 1 && world.getBlockId(leavesX, leavesStartY - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY - 1, leavesZ, BlockListener.WILLOW_LEAVES.id);
                                    } else if (world.getBlockId(leavesX, leavesStartY - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY - 1, leavesZ, BlockListener.WILLOW_VINES.id);
                                    }

                                    if (random.nextInt(3) == 0 && world.getBlockId(leavesX, leavesStartY - 2, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY - 2, leavesZ, BlockListener.WILLOW_VINES.id);

                                        if (random.nextInt(2) == 0 && world.getBlockId(leavesX, leavesStartY - 3, leavesZ) == 0) {
                                            world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY - 3, leavesZ, BlockListener.WILLOW_VINES.id);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Log gen
                    for(int logY = 0; logY < treeHeight; ++logY) {
                        int logTargetBlockId = world.getBlockId(x, y + logY, z);
                        if (logTargetBlockId == 0 || logTargetBlockId == BlockListener.WILLOW_LEAVES.id || logTargetBlockId == BlockListener.WILLOW_VINES.id) {
                            world.setBlockWithoutNotifyingNeighbors(x, y + logY, z, BlockListener.WILLOW_LOG.id);
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}