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
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.ArrayList;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.BLOCKS_CONFIG;

public class WillowStairs extends TemplateBlock {
    public static final DirectionProperty HORIZONTAL_FACING;
    public static final DirectionProperty VERTICAL_FACING;
    public static final IntProperty STAIR_SHAPE;

    static {
        HORIZONTAL_FACING = Properties.HORIZONTAL_FACING;
        VERTICAL_FACING = DirectionProperty.of("vertical_facing", Direction.Type.VERTICAL);
        STAIR_SHAPE = IntProperty.of("stair_shape", 0, 3);
    }

    public WillowStairs(Identifier identifier) {
        super(identifier, Material.WOOD);
        this.setSoundGroup(WOOD_SOUND_GROUP);
        this.setHardness(2.0F);
        this.setResistance(5.0F / 3.0F);
        setDefaultState(getStateManager().getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(VERTICAL_FACING, Direction.DOWN).with(STAIR_SHAPE, 0));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING, VERTICAL_FACING, STAIR_SHAPE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        if (BLOCKS_CONFIG.stairsModern) {
            return getStateManager().getDefaultState().with(HORIZONTAL_FACING, context.getHorizontalPlayerFacing()).with(VERTICAL_FACING, context.getVerticalPlayerLookDirection()).with(STAIR_SHAPE, 0);
        } else {
            return getStateManager().getDefaultState().with(HORIZONTAL_FACING, context.getHorizontalPlayerFacing()).with(VERTICAL_FACING, Direction.DOWN).with(STAIR_SHAPE, 0);
        }
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    @Override
    public void addIntersectingBoundingBox(World world, int x, int y, int z, Box box, ArrayList boxes) {
        Direction vertical_facing = world.getBlockState(x, y, z).get(VERTICAL_FACING);
        Direction horizontal_facing = world.getBlockState(x, y, z).get(HORIZONTAL_FACING);
        if (vertical_facing == Direction.DOWN) {
            if (horizontal_facing == Direction.EAST) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.WEST) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.SOUTH) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.NORTH) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            }
        } else {
            if (horizontal_facing == Direction.EAST) {
                this.setBoundingBox(0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.WEST) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.SOUTH) {
                this.setBoundingBox(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            } else if (horizontal_facing == Direction.NORTH) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
                this.setBoundingBox(0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F);
                super.addIntersectingBoundingBox(world, x, y, z, box, boxes);
            }
        }
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }
}
