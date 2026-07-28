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
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FakeWaterTallPlant extends BasePlant {
    public FakeWaterTallPlant(Identifier identifier) {
        super(identifier);
        this.setTickRandomly(true);
        this.setBoundingBox(0.1F, -1.0F, 0.1F, 0.9F, 1F, 0.9F);
    }

    protected boolean canPlantOnTop(World world, int x, int y, int z) {
        return ((WetlandsTallPlant) BlockListener.CATTAILS).canPlantOnTop(world, x, y, z);
    }

    protected boolean canSpreadOnTop(World world, int x, int y, int z) {
        return ((WetlandsTallPlant) BlockListener.CATTAILS).canSpreadOnTop(world, x, y, z);
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (world.getBlockState(x, y, z).isAir() && world.getBlockState(x, y - 1, z).getMaterial() == Material.WATER) {
            return canPlantOnTop(world, x, y - 2, z);
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, int x, int y, int z) {
        return     canPlantOnTop(world, x, y - 2, z)
                && world.getBlockState(x, y - 1, z).getMaterial() == Material.WATER;
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
    //TODO Faster break if can't grow
    public void onTick(World world, int x, int y, int z, Random random) {
        this.breakIfCannotGrow(world, x, y, z);
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (this.id == BlockListener.INWATER_CATTAILS.id) {
            drops.add(new ItemStack(ItemListener.CATTAILS_ITEM, 1));
        } else if (this.id == BlockListener.INWATER_REEDS.id) {
            drops.add(new ItemStack(ItemListener.REEDS_ITEM, 1));
        } else if (this.id == BlockListener.INWATER_BOG_GRASS.id) {
            drops.add(new ItemStack(ItemListener.BOG_GRASS_ITEM, 1));
        }
        return drops;
    }

    public void attemptSpread(World world, int x, int y, int z) {
        Random random= new Random();
        if (canSpreadOnTop(world, x, y - 2, z)) {
            for (int spreadTargetX = x - 1; spreadTargetX <= x + 1; ++spreadTargetX) {
                for (int spreadTargetZ = z - 1; spreadTargetZ <= z + 1; ++spreadTargetZ) {
                    if (random.nextInt(2) == 0
                            && canSpreadOnTop(world, spreadTargetX, y - 2, spreadTargetZ)
                            && world.getBlockState(spreadTargetX, y - 1, spreadTargetZ).getMaterial() == Material.WATER
                            && world.getBlockState(spreadTargetX, y, spreadTargetZ).isAir()) {
                        world.setBlock(spreadTargetX, y, spreadTargetZ, this.id);
                    }
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
