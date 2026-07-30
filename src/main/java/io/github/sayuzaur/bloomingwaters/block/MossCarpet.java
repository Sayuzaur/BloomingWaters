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
import io.github.sayuzaur.bloomingwaters.world.feature.MossPatchFeature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class MossCarpet extends TemplateBlock {
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;

    static {
        NORTH = BooleanProperty.of("north");
        EAST = BooleanProperty.of("east");
        SOUTH = BooleanProperty.of("south");
        WEST = BooleanProperty.of("west");
    }

    public MossCarpet(Identifier identifier) {
        super(identifier, Material.PLANT);
        this.setSoundGroup(DIRT_SOUND_GROUP);
        this.setHardness(0.3F);
        this.setOpacity(1);
        this.setTickRandomly(true);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.1250F, 1.0F);
        setDefaultState(getStateManager().getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int getRenderType() {
        return 1;
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).getBlock().isFullCube();
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (!world.getBlockState(x, y, z).isAir()) {
            return false;
        } else if (world.getBlockState(x, y, z).getMaterial() == Material.WATER) {
            return false;
        }
        return canPlantOnTop(world, x, y - 1, z);
    }

    public void updateState(World world, int x, int y, int z) {
        //Without this check it can crash sometimes when breaking a block at tick
        if (world.getBlockId(x, y, z) == this.id) {
            //TODO Add check for mossBlock below
            if (!world.getBlockState(x, y, z + 1).getBlock().isFullCube() && !world.getBlockState(x, y - 1, z + 1).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(NORTH, true));
            } else if (world.getBlockState(x, y, z + 1).getBlock().isFullCube() || world.getBlockState(x, y - 1, z + 1).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(NORTH, false));
            }
            if (!world.getBlockState(x, y, z - 1).getBlock().isFullCube() && !world.getBlockState(x, y - 1, z - 1).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(SOUTH, true));
            } else if (world.getBlockState(x, y, z - 1).getBlock().isFullCube() || world.getBlockState(x, y - 1, z - 1).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(SOUTH, false));
            }
            if (!world.getBlockState(x + 1, y, z).getBlock().isFullCube() && !world.getBlockState(x + 1, y - 1, z).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(EAST, true));
            } else if (world.getBlockState(x + 1, y, z).getBlock().isFullCube() || world.getBlockState(x + 1, y - 1, z).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(EAST, false));
            }
            if (!world.getBlockState(x - 1, y, z).getBlock().isFullCube() && !world.getBlockState(x - 1, y - 1, z).getBlock().isFullCube()) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(WEST, true));
            } else if ((world.getBlockState(x - 1, y, z).getBlock().isFullCube() || world.getBlockState(x - 1, y - 1, z).getBlock().isFullCube())) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(WEST, false));
            }
        }
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        updateState(world, x, y, z);
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
        updateState(world, x, y, z);
    }


    public void onTick(World world, int x, int y, int z, Random random) {
        this.breakIfCannotGrow(world, x, y, z);
        updateState(world, x, y, z);
    }

    public void generate(World world, int x, int y, int z, Random random) {
        Feature mossFeature = new MossPatchFeature();

        if (!mossFeature.generate(world, random, x, y, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, y, z, this.id);
        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            Random random = new Random();
            this.generate(world, x, y, z, random);
        }
        //I don't know, I'm too tired for this shit
        ((WetlandsTallPlant) BlockListener.CATTAILS).bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}