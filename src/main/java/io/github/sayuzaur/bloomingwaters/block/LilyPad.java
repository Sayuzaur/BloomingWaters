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
import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import io.github.sayuzaur.bloomingwaters.event.init.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LilyPad extends BasePlant {
    public static final DirectionProperty HORIZONTAL_FACING;
    public static final BooleanProperty FLOWERING;

    static {
        HORIZONTAL_FACING = Properties.FACING;
        FLOWERING = BooleanProperty.of("flowering");
    }

    public LilyPad(Identifier identifier) {
        super(identifier);
        this.setTickRandomly(true);
        this.setBoundingBox(0.0625F, -0.0625F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
        setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(FLOWERING, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, FLOWERING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(HORIZONTAL_FACING,context.getHorizontalPlayerFacing()).with(FLOWERING, false);
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return Box.createCached(x + 0.0625F, y, z + 0.0625F, x + 0.9375F, y + 0.125F, z + 0.9375F);
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).getMaterial() == Material.WATER || (this.id == BlockListener.FROST_LILY_PAD.id && world.getBlockState(x, y, z).getMaterial() == Material.ICE);
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        //Not being able to place directly on water, here's wonky fix for 1-2 blocks deep water.
        if (world.getBlockState(x, y, z).getMaterial() == Material.WATER && world.getBlockState(x, y + 1, z).isAir()) {
            return true;
        } else if (world.getBlockState(x, y, z).getMaterial() == Material.WATER && world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER && world.getBlockState(x, y + 2, z).isAir()) {
            return true;
        }
        //Normal placement
        return canPlantOnTop(world, x, y - 1, z);
    }

    //TODO Check for flowing vs still water
    @Override
    public void onPlaced(World world, int x, int y, int z) {
        //Fixing wonky fix of wonky placement
        BlockState state = world.getBlockState(x, y, z);
        Direction direction = state.get(HORIZONTAL_FACING);
        if (this.id == BlockListener.LILY_PAD.id) {
            if (world.getBlockState(x, y - 1, z).getMaterial().isSolid()
                    && world.getBlockState(x, y + 1, z).isAir()) {
                world.setBlock(x, y, z, Block.WATER.id);
                world.setBlock(x, y + 1, z, BlockListener.LILY_PAD.id);
                world.setBlockStateWithoutNotifyingNeighbors(x, y + 1, z, state.with(HORIZONTAL_FACING, direction).with(FLOWERING, false));
            } else if (world.getBlockState(x, y - 1, z).getMaterial().isSolid()
                    && world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER
                    && world.getBlockState(x, y + 2, z).isAir()) {
                world.setBlock(x, y, z, Block.WATER.id);
                world.setBlock(x, y + 2, z, BlockListener.LILY_PAD.id);
                world.setBlockStateWithoutNotifyingNeighbors(x, y + 2, z, state.with(HORIZONTAL_FACING, direction).with(FLOWERING, false));
            }
        } else if (this.id == BlockListener.FROST_LILY_PAD.id) {
            if (world.getBlockState(x, y - 1, z).getMaterial().isSolid()
                    && world.getBlockState(x, y - 1, z).getMaterial() != Material.ICE
                    && world.getBlockState(x, y + 1, z).isAir()) {
                world.setBlock(x, y, z, Block.WATER.id);
                world.setBlock(x, y + 1, z, BlockListener.FROST_LILY_PAD.id);
                world.setBlockStateWithoutNotifyingNeighbors(x, y + 1, z, state.with(HORIZONTAL_FACING, direction).with(FLOWERING, false));
            } else if (world.getBlockState(x, y - 1, z).getMaterial().isSolid()
                    && world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER
                    && world.getBlockState(x, y + 2, z).isAir()) {
                world.setBlock(x, y, z, Block.WATER.id);
                world.setBlock(x, y + 2, z, BlockListener.FROST_LILY_PAD.id);
                world.setBlockStateWithoutNotifyingNeighbors(x, y + 2, z, state.with(HORIZONTAL_FACING, direction).with(FLOWERING, false));
            }
        }
    }

    @Override
    public boolean canGrow(World world, int x, int y, int z) {
        return canPlantOnTop(world, x, y - 1, z);
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

    public void onTick(World world, int x, int y, int z, Random random) {
        this.breakIfCannotGrow(world, x, y, z);
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (state.get(FLOWERING)) {
            if (this.id == BlockListener.LILY_PAD.id) {
                drops.add(new ItemStack(ItemListener.LILY_FLOWER, 1));
            } else if (this.id == BlockListener.FROST_LILY_PAD.id) {
                drops.add(new ItemStack(ItemListener.FROST_LILY_FLOWER, 1));
            }
        }
        drops.add(new ItemStack(this, 1));

        return drops;
    }

    public void attemptSpread(World world, int x, int y, int z, BlockState state) {
        Random random= new Random();
        for (int spreadTargetX = x - 1; spreadTargetX <= x + 1; ++spreadTargetX) {
            for (int spreadTargetZ = z - 1; spreadTargetZ <= z + 1; ++spreadTargetZ) {
                if (random.nextInt(2) == 0
                        && canPlantOnTop(world, spreadTargetX, y - 1, spreadTargetZ)
                        && world.getBlockState(spreadTargetX, y, spreadTargetZ).isAir()) {
                    world.setBlock(spreadTargetX, y, spreadTargetZ, this.id);

                    Direction randDirection = null;
                    int randIntDirection = random.nextInt(4);
                    switch (randIntDirection) {
                        case 0 -> randDirection = Direction.NORTH;
                        case 1 -> randDirection = Direction.SOUTH;
                        case 2 -> randDirection = Direction.EAST;
                        case 3 -> randDirection = Direction.WEST;
                    }
                    world.setBlockStateWithoutNotifyingNeighbors(spreadTargetX, y, spreadTargetZ, state.with(HORIZONTAL_FACING, randDirection).with(FLOWERING, false));
                }
            }
        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            if (!state.get(FLOWERING)) {
                world.setBlockState(x, y, z, state.with(FLOWERING, true));
            } else {
                attemptSpread(world, x, y, z, state);
            }
        }
        bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
