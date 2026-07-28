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

package io.github.sayuzaur.bloomingwaters.block;

import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class WillowLeaves extends TemplateBlock {
    int[] decayRegion;

    public WillowLeaves(Identifier identifier) {
        super(identifier, Material.LEAVES);
        this.setSoundGroup(DIRT_SOUND_GROUP);
        this.setHardness(0.2F);
        this.setOpacity(1);
        this.setTickRandomly(true);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isSideVisible(BlockView blockView, int x, int y, int z, int side) {
        blockView.getBlockId(x, y, z);
        return true;
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    public int getDroppedItemId(int blockMeta, Random random) {
        return BlockListener.WILLOW_SAPLING.asItem().id;
    }

    public int getDroppedItemCount(Random random) {
        return random.nextInt(20) == 0 ? 1 : 0;
    }

    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        if (!world.isRemote && playerEntity.getHand() != null && playerEntity.getHand().itemId == Item.SHEARS.id) {
            this.dropStack(world, x, y, z, new ItemStack(BlockListener.WILLOW_LEAVES, 1));
        } else {
            super.afterBreak(world, playerEntity, x, y, z, meta);
        }
    }

    public void onBreak(World world, int x, int y, int z) {
        byte var5 = 1;
        int var6 = var5 + 1;
        if (world.isRegionLoaded(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
            for(int var7 = -var5; var7 <= var5; ++var7) {
                for(int var8 = -var5; var8 <= var5; ++var8) {
                    for(int var9 = -var5; var9 <= var5; ++var9) {
                        BlockState state = world.getBlockState(x + var7, y + var8, z + var9);
                        if (state.isIn(BlockTags.LEAVES) || state.getMaterial() == Material.LEAVES) {
                            int var11 = world.getBlockMeta(x + var7, y + var8, z + var9);
                            world.setBlockMetaWithoutNotifyingNeighbors(x + var7, y + var8, z + var9, var11 | 8);
                        }
                    }
                }
            }
        }
    }

    private void breakLeaves(World world, int x, int y, int z) {
        this.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z));
        world.setBlock(x, y, z, 0);
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            int meta = world.getBlockMeta(x, y, z);
            if ((meta & 8) != 0) {
                byte var7 = 4;
                int var8 = var7 + 1;
                byte var9 = 32;
                int var10 = var9 * var9;
                int var11 = var9 / 2;
                if (this.decayRegion == null) {
                    this.decayRegion = new int[var9 * var9 * var9];
                }

                if (world.isRegionLoaded(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                    for(int var12 = -var7; var12 <= var7; ++var12) {
                        for(int var13 = -var7; var13 <= var7; ++var13) {
                            for(int var14 = -var7; var14 <= var7; ++var14) {
                                BlockState state = world.getBlockState(x + var12, y + var13, z + var14);
                                if (state.isIn(BlockTags.LOGS)) {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = 0;
                                } else if (state.isIn(BlockTags.LEAVES)) {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -2;
                                } else {
                                    this.decayRegion[(var12 + var11) * var10 + (var13 + var11) * var9 + var14 + var11] = -1;
                                }
                            }
                        }
                    }

                    for(int var16 = 1; var16 <= 4; ++var16) {
                        for(int var18 = -var7; var18 <= var7; ++var18) {
                            for(int var19 = -var7; var19 <= var7; ++var19) {
                                for(int var20 = -var7; var20 <= var7; ++var20) {
                                    if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11] == var16 - 1) {
                                        if (this.decayRegion[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11 - 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11 + 1) * var10 + (var19 + var11) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11 - 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11 + 1) * var9 + var20 + var11] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + (var20 + var11 - 1)] = var16;
                                        }

                                        if (this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] == -2) {
                                            this.decayRegion[(var18 + var11) * var10 + (var19 + var11) * var9 + var20 + var11 + 1] = var16;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                int var17 = this.decayRegion[var11 * var10 + var11 * var9 + var11];
                if (var17 >= 0) {
                    world.setBlockMetaWithoutNotifyingNeighbors(x, y, z, meta & -9);
                } else {
                    this.breakLeaves(world, x, y, z);
                }
            }

        }
    }

    public void onSteppedOn(World world, int x, int y, int z, Entity entity) {
        super.onSteppedOn(world, x, y, z, entity);
    }
}
