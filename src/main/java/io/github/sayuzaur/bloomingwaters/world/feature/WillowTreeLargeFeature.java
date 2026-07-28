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

public class WillowTreeLargeFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        int treeHeight = random.nextInt(4) + 6;
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
                //Prep branches
                int branchRandX = 0;
                int branchRandZ = 0;
                //Can't be both 0
                while (branchRandX == 0 && branchRandZ == 0) {
                    branchRandX = random.nextInt(3) - random.nextInt(3);
                    branchRandZ = random.nextInt(3) - random.nextInt(3);
                }
                int x1 = x + branchRandX;
                int z1 = z + branchRandZ;
                int x2 = x + (branchRandX * -1);
                int z2 = z + (branchRandZ * -1);
                int branch1Heigh = random.nextInt(2) - random.nextInt(2);
                int branch2Heigh = random.nextInt(2) - random.nextInt(2);

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

                    //Leaves branch 1 gen
                    int treeHeight1 = treeHeight + branch1Heigh;
                    int leavesStartY1 = y - 3 + treeHeight1;
                    for(int leavesY = leavesStartY1; leavesY <= y + treeHeight1 - 1; ++leavesY) {
                        int tempY = leavesY - (y + treeHeight1);
                        int tempSides = 1 - tempY / 2;

                        for(int leavesX = x1 - tempSides; leavesX <= x1 + tempSides; ++leavesX) {
                            int tempX = leavesX - x1;

                            for(int leavesZ = z1 - tempSides; leavesZ <= z1 + tempSides; ++leavesZ) {
                                int tempZ = leavesZ - z1;
                                if ((Math.abs(tempX) != tempSides || Math.abs(tempZ) != tempSides || random.nextInt(2) != 0 && tempY != 0) && !Block.BLOCKS_OPAQUE[world.getBlockId(leavesX, leavesY, leavesZ)]) {
                                    world.setBlockWithoutNotifyingNeighbors(leavesX, leavesY, leavesZ, BlockListener.WILLOW_LEAVES.id);
                                }
                            }
                        }
                    }

                    //Leaves branch 2 gen
                    int treeHeight2 = treeHeight + branch2Heigh;
                    int leavesStartY2 = y - 3 + treeHeight2;
                    for(int leavesY = leavesStartY2; leavesY <= y + treeHeight2 - 1; ++leavesY) {
                        int tempY = leavesY - (y + treeHeight2);
                        int tempSides = 1 - tempY / 2;

                        for(int leavesX = x2 - tempSides; leavesX <= x2 + tempSides; ++leavesX) {
                            int tempX = leavesX - x2;

                            for(int leavesZ = z2 - tempSides; leavesZ <= z2 + tempSides; ++leavesZ) {
                                int tempZ = leavesZ - z2;
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

                    //Vines branch 1 gen
                    for(int leavesX = x1 - 2; leavesX <= x1 + 2; ++leavesX) {
                        for (int leavesZ = z1 - 2; leavesZ <= z1 + 2; ++leavesZ) {
                            if (world.getBlockId(leavesX, leavesStartY1, leavesZ) == BlockListener.WILLOW_LEAVES.id) {
                                int randVines = random.nextInt(4);

                                if (randVines != 0) {
                                    if (randVines == 1 && world.getBlockId(leavesX, leavesStartY1 - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY1 - 1, leavesZ, BlockListener.WILLOW_LEAVES.id);
                                    } else if (world.getBlockId(leavesX, leavesStartY1 - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY1 - 1, leavesZ, BlockListener.WILLOW_VINES.id);
                                    }

                                    if (random.nextInt(2) == 0 && world.getBlockId(leavesX, leavesStartY1 - 2, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY1 - 2, leavesZ, BlockListener.WILLOW_VINES.id);

                                        if (random.nextInt(2) == 0 && world.getBlockId(leavesX, leavesStartY1 - 3, leavesZ) == 0) {
                                            world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY1 - 3, leavesZ, BlockListener.WILLOW_VINES.id);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    //Vines branch 2 gen
                    for(int leavesX = x2 - 2; leavesX <= x2 + 2; ++leavesX) {
                        for (int leavesZ = z2 - 2; leavesZ <= z2 + 2; ++leavesZ) {
                            if (world.getBlockId(leavesX, leavesStartY2, leavesZ) == BlockListener.WILLOW_LEAVES.id) {
                                int randVines = random.nextInt(4);

                                if (randVines != 0) {
                                    if (randVines == 1 && world.getBlockId(leavesX, leavesStartY2 - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY2 - 1, leavesZ, BlockListener.WILLOW_LEAVES.id);
                                    } else if (world.getBlockId(leavesX, leavesStartY2 - 1, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY2 - 1, leavesZ, BlockListener.WILLOW_VINES.id);
                                    }

                                    if (random.nextInt(2) == 0 && world.getBlockId(leavesX, leavesStartY2 - 2, leavesZ) == 0) {
                                        world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY2 - 2, leavesZ, BlockListener.WILLOW_VINES.id);

                                        if (random.nextInt(2) == 0 && world.getBlockId(leavesX, leavesStartY2 - 3, leavesZ) == 0) {
                                            world.setBlockWithoutNotifyingNeighbors(leavesX, leavesStartY2 - 3, leavesZ, BlockListener.WILLOW_VINES.id);
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

                    //Log branch 1 gen
                    int tempX1 = (x - x1) / 2;
                    int tempZ1 = (z - z1) / 2;
                    if (branch1Heigh == 1) {
                        world.setBlockWithoutNotifyingNeighbors(x1 + tempX1, leavesStartY1 - 2, z1 + tempZ1, BlockListener.WILLOW_LOG.id);
                    }
                    world.setBlockWithoutNotifyingNeighbors(x1 + tempX1, leavesStartY1 - 1, z1 + tempZ1, BlockListener.WILLOW_LOG.id);
                    world.setBlockWithoutNotifyingNeighbors(x1, leavesStartY1, z1, BlockListener.WILLOW_LOG.id);

                    //Log branch 2 gen
                    int tempX2 = (x - x2) / 2;
                    int tempZ2 = (z - z2) / 2;
                    if (branch2Heigh == 1) {
                        world.setBlockWithoutNotifyingNeighbors(x2 + tempX2, leavesStartY2 - 2, z2 + tempZ2, BlockListener.WILLOW_LOG.id);
                    }
                    world.setBlockWithoutNotifyingNeighbors(x2 + tempX2, leavesStartY2 - 1, z2 + tempZ2, BlockListener.WILLOW_LOG.id);
                    world.setBlockWithoutNotifyingNeighbors(x2, leavesStartY2, z2, BlockListener.WILLOW_LOG.id);

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