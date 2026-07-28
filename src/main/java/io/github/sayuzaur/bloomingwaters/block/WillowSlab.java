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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.state.property.DirectionProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

import java.util.ArrayList;
import java.util.List;

public class WillowSlab extends TemplateBlock {
    public static final DirectionProperty VERTICAL_FACING;
    public static final BooleanProperty DOUBLE_SLAB;
    private static World world;

    static {
        VERTICAL_FACING = DirectionProperty.of("vertical_facing", Direction.Type.VERTICAL);
        DOUBLE_SLAB = BooleanProperty.of("double_slab");
    }

    public WillowSlab(Identifier identifier) {
        super(identifier, Material.WOOD);
        this.setSoundGroup(WOOD_SOUND_GROUP);
        this.setHardness(2.0F);
        this.setResistance(5.0F / 3.0F);
        this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        setDefaultState(getStateManager().getDefaultState().with(VERTICAL_FACING, Direction.DOWN).with(DOUBLE_SLAB, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(VERTICAL_FACING, DOUBLE_SLAB);
    }

    @Override
    //TODO Revisit placement, add option to turn off modern upside down placement
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(VERTICAL_FACING, context.getVerticalPlayerLookDirection()).with(DOUBLE_SLAB, false);
    }

    public boolean isOpaque() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }
    //TODO I FUCKING HATE THIS
    @Override
    public void updateBoundingBox(BlockView blockView, int x, int y, int z) {
        if (!(blockView instanceof BlockStateView view)) {
            return;
        }
        if (!view.getBlockState(x, y, z).get(DOUBLE_SLAB)) {
            if (view.getBlockState(x, y, z).get(VERTICAL_FACING) == Direction.DOWN) {
                this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
            } else {
                this.setBoundingBox(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
            }
        } else {
            this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    protected Box createBox(World world, int x, int y, int z) {
        if (!world.getBlockState(x, y, z).get(DOUBLE_SLAB)) {
            if (world.getBlockState(x, y, z).get(VERTICAL_FACING) == Direction.DOWN) {
                return Box.createCached(x + 0.0F, y + 0.0F, z + 0.0F, x + 1.0F, y + 0.5F, z + 1.0F);
            } else {
                return Box.createCached(x + 0.0F, y + 0.5F, z + 0.0F, x + 1.0F, y + 1.0F, z + 1.0F);
            }
        } else {
            return Box.createCached(x + 0.0F, y + 0.0F, z + 0.0F, x + 1.0F, y + 1.0F, z + 1.0F);
        }
    }

    @Override
    public Box getBoundingBox(World world, int x, int y, int z) {
        return createBox(world, x, y, z);
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) != this.id) {
            return null;
        }
        return createBox(world, x, y, z);
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (state.get(DOUBLE_SLAB) == true) {
            drops.add(new ItemStack(this, 2));
        } else {
            drops.add(new ItemStack(this, 1));
        }
        return drops;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (!world.isRemote) {
            ItemStack userHand = player.getHand();
            BlockState state = world.getBlockState(x, y, z);
            if (userHand == null
                    || state.get(DOUBLE_SLAB)
                    || player.isSneaking()) {
                return false;
            } else if (userHand.getItem() == this.asItem()) {
                world.setBlockState(x, y, z, state.with(DOUBLE_SLAB, true));
                userHand.count--;
                world.playSound(x, y, z, "step.wood", 1.0F, 1.0F);
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
