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
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class LilyFlower extends BasePlant {
    public LilyFlower(Identifier identifier) {
        super(identifier);
        this.setBoundingBox(0.2F, 0.0F, 0.2F, 0.8F, 0.4F, 0.8F);
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isOpaque();
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (world.getBlockState(x, y, z).getMaterial() == Material.WATER || !world.isAir(x, y + 1, z)) {
            return false;
        }
        return canPlantOnTop(world, x, y - 1, z);
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
}
