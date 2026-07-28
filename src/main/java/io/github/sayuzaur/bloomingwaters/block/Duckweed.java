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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class Duckweed extends BasePlant {
    public Duckweed(Identifier identifier) {
        super(identifier);
        this.setTickRandomly(true);
        this.setBoundingBox(0.0F, -0.0625F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).getMaterial() == Material.WATER;
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

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        //Fixing wonky fix of wonky placement
        if (world.getBlockState(x, y - 1, z).getMaterial().isSolid() && world.getBlockState(x, y + 1, z).isAir()) {
            world.setBlock(x, y, z, Block.WATER.id);
            world.setBlock(x, y + 1, z, BlockListener.DUCKWEED.id);
        } else if (world.getBlockState(x, y - 1, z).getMaterial().isSolid() && world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER && world.getBlockState(x, y + 2, z).isAir()) {
            world.setBlock(x, y, z, Block.WATER.id);
            world.setBlock(x, y + 2, z, BlockListener.DUCKWEED.id);
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

    public void attemptSpread(World world, int x, int y, int z) {
        Random random= new Random();
        for (int spreadTargetX = x - 1; spreadTargetX <= x + 1; ++spreadTargetX) {
            for (int spreadTargetZ = z - 1; spreadTargetZ <= z + 1; ++spreadTargetZ) {
                if (random.nextInt(2) == 0
                        && canPlantOnTop(world, spreadTargetX, y - 1, spreadTargetZ)
                        && world.getBlockState(spreadTargetX, y, spreadTargetZ).isAir()) {
                    world.setBlock(spreadTargetX, y, spreadTargetZ, BlockListener.DUCKWEED.id);
                }
            }
        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            attemptSpread(world, x, y, z);
        }
        bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
