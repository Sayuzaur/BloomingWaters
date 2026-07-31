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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.BLOCKS_CONFIG;

public class WillowLog extends TemplateBlock {
    public static final DirectionProperty FACING;

    static {
        FACING = Properties.FACING;
    }

    public WillowLog(Identifier identifier) {
        super(identifier, Material.WOOD);
        this.setSoundGroup(WOOD_SOUND_GROUP);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        if (BLOCKS_CONFIG.logsModern) {
            return getStateManager().getDefaultState().with(FACING, context.getSide());
        } else {
            return getStateManager().getDefaultState().with(FACING, Direction.DOWN);
        }
    }

    public void onBreak(World world, int x, int y, int z) {
        byte var5 = 4;
        int var6 = var5 + 1;
        if (world.isRegionLoaded(x - var6, y - var6, z - var6, x + var6, y + var6, z + var6)) {
            for(int var7 = -var5; var7 <= var5; ++var7) {
                for(int var8 = -var5; var8 <= var5; ++var8) {
                    for(int var9 = -var5; var9 <= var5; ++var9) {
                        BlockState state = world.getBlockState(x + var7, y + var8, z + var9);
                        if (state.isIn(BlockTags.LEAVES) || state.getMaterial() == Material.LEAVES) {
                            int var11 = world.getBlockMeta(x + var7, y + var8, z + var9);
                            if ((var11 & 8) == 0) {
                                world.setBlockMetaWithoutNotifyingNeighbors(x + var7, y + var8, z + var9, var11 | 8);
                            }
                        }
                    }
                }
            }
        }

    }
}
