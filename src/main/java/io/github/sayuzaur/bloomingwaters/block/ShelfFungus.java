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

import io.github.sayuzaur.bloomingwaters.block.base.BasePlant;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

public class ShelfFungus extends BasePlant {
    public static final DirectionProperty HORIZONTAL_FACING;

    static {
        HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
    }

    public ShelfFungus(Identifier identifier) {
        super(identifier);
        this.setBoundingBox(0.0F, 0.3F, 0.0F, 1.0F, 0.5F, 1.0F);
        setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(HORIZONTAL_FACING, context.getSide());
    }

    public boolean canPlantOn(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isIn(BlockTags.LOGS);
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (!world.getBlockState(x, y, z).isAir() || world.getBlockState(x, y, z).getMaterial() == Material.WATER) {
            return false;
        }
        return canPlantOn(world, x + 1, y, z)
                || canPlantOn(world, x - 1, y, z)
                || canPlantOn(world, x, y, z + 1)
                || canPlantOn(world, x, y, z - 1);
    }

    public boolean properPlacement(World world, int x, int y, int z, BlockState state) {
        Direction facing =  state.get(HORIZONTAL_FACING);
        if (facing == Direction.NORTH && !canPlantOn(world, x, y, z - 1)) {
            return false;
        } else if (facing == Direction.SOUTH && !canPlantOn(world, x, y, z + 1)) {
            return false;
        } else if (facing == Direction.WEST && !canPlantOn(world, x - 1, y, z)) {
            return false;
        } else if (facing == Direction.EAST && !canPlantOn(world, x + 1, y, z)) {
            return false;
        } else {
            return true;
        }
    }

    public void updateState(World world, int x, int y, int z, BlockState state) {
        if (canPlantOn(world, x, y, z - 1)) {
            world.setBlockState(x, y, z, state.with(HORIZONTAL_FACING, Direction.NORTH));
        }
        if (canPlantOn(world, x, y, z + 1)) {
            world.setBlockState(x, y, z, state.with(HORIZONTAL_FACING, Direction.SOUTH));
        }
        if (canPlantOn(world, x - 1, y, z)) {
            world.setBlockState(x, y, z, state.with(HORIZONTAL_FACING, Direction.WEST));
        }
        if (canPlantOn(world, x + 1, y, z)) {
            world.setBlockState(x, y, z, state.with(HORIZONTAL_FACING, Direction.EAST));
        }
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) == this.id) {
            BlockState state = world.getBlockState(x, y, z);
            if (!properPlacement(world, x, y, z, state)) {
                updateState(world, x, y, z, state);
            }
        }
    }

    @Override
    public boolean canGrow(World world, int x, int y, int z) {
        BlockState state = world.getBlockState(x, y, z);
        return properPlacement(world, x, y, z, state);
    }

    protected final void breakIfCannotGrow(World world, int x, int y, int z) {
        if (!this.canGrow(world, x, y, z)) {
            this.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z));
            world.setBlock(x, y, z, 0);
        }
    }

    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        this.breakIfCannotGrow(world, x, y, z);
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            float offset = 0.7F;
            float varX = world.random.nextFloat() * offset + (1.0F - offset) * 0.5F;
            float varY = world.random.nextFloat() * offset + (1.0F - offset) * 0.5F;
            float varZ = world.random.nextFloat() * offset + (1.0F - offset) * 0.5F;

            ItemEntity fungusItemEntity = new ItemEntity(world,((float)x + varX),((float)y + varY),((float)z + varZ), new ItemStack(this.asItem()));
            fungusItemEntity.pickupDelay = 10;
            world.spawnEntity(fungusItemEntity);
        }

        bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
