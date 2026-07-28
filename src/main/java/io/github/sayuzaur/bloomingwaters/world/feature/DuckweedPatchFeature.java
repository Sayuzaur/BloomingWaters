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

package io.github.sayuzaur.bloomingwaters.world.feature;

import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class DuckweedPatchFeature extends Feature {
    protected int maxPlants;
    protected int range;

    public DuckweedPatchFeature(int patchSize, int range) {
        this.maxPlants = patchSize;
        this.range = range;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int forceY = 64;
        int attempts = 128;
        int generatedPlants = 0;

        for (int i = 0; i < attempts && generatedPlants < maxPlants; i++) {
            //Pick xyz target to generate plant
            int varX = x + random.nextInt(range) - random.nextInt(range);
            int varZ = z + random.nextInt(range) - random.nextInt(range);
            int varY = forceY;

            if (isValidTarget(world, varX, varY, varZ)) {
                world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, BlockListener.DUCKWEED.id);
                generatedPlants++;
            }
        }
        return true;
    }

    public boolean isLandNearby(World world, int x, int y, int z) {
        int checkRange = 4;
        for (int landX = x - checkRange; landX <= x + checkRange; ++landX) {
            for (int landZ = z - checkRange; landZ <= z + checkRange; ++landZ) {
                if (world.getMaterial(landX, y, landZ) != Material.ICE && world.getBlockState(landX, y, landZ).getBlock().isFullCube()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean bordersOtherDuckweed(World world, int x, int y, int z) {
        return     world.getBlockId(x + 1, y, z) == BlockListener.DUCKWEED.id
                || world.getBlockId(x - 1, y, z) == BlockListener.DUCKWEED.id
                || world.getBlockId(x, y, z + 1) == BlockListener.DUCKWEED.id
                || world.getBlockId(x, y, z - 1) == BlockListener.DUCKWEED.id;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        return     world.isAir(x, y, z)
                && BlockListener.DUCKWEED.canGrow(world, x, y, z)
                && (isLandNearby(world, x, y, z) || bordersOtherDuckweed(world, x, y, z));
    }
}
