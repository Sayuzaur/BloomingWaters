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

import io.github.sayuzaur.bloomingwaters.event.init.ItemListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.WETSOIL_SOUND_GROUP;

public class SinkingMud extends TemplateBlock {
    public static final BooleanProperty ACTIVE;

    static {
        ACTIVE = BooleanProperty.of("active");
    }

    public SinkingMud(Identifier identifier) {
        super(identifier, Material.SOIL);
        this.setSoundGroup(WETSOIL_SOUND_GROUP);
        this.setHardness(0.5F);
        this.setOpacity(8);
        this.setTickRandomly(true);
        setDefaultState(getStateManager().getDefaultState().with(ACTIVE, false));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(ACTIVE, false);
    }

    @Override
    public Box getCollisionShape(World world, int x, int y, int z) {
        return null;
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
        return 0;
    }

    public int getTickRate() {
        return 5;
    }

    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        if (!(entity instanceof Particle)) {
            entity.slowed = true;
            BlockState state = world.getBlockState(x, y, z);
            world.setBlockState(x, y, z, state.with(ACTIVE, true));
            world.setBlockMeta(x, y, z, 2);
            world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
        }
    }

    public int getPistonBehavior() {
        return 1;
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        super.onTick(world, x, y, z, random);
        int meta = world.getBlockMeta(x, y, z);
        if (meta >= 1) {
            meta--;
            world.setBlockMetaWithoutNotifyingNeighbors(x, y, z, meta);
            if (meta == 0) {
                BlockState state = world.getBlockState(x, y, z);
                world.setBlockState(x, y, z, state.with(ACTIVE, false));
            }
        }
    }

    public int getDroppedItemCount(Random random) {
        return 0;
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (!world.isRemote) {
            ItemStack userHand = player.getHand();
            if (userHand == null) {
                return false;
            } else if (userHand.itemId == Item.BUCKET.id) {
                world.setBlock(x, y, z, 0);

                userHand.count--;
                ItemStack stack = new ItemStack(ItemListener.MUD_BUCKET);
                ItemEntity itemEntity = new ItemEntity(world, player.x, player.y, player.z, stack);
                world.spawnEntity(itemEntity);
            } else {
                return false;
            }
        }
        return true;
    }
}