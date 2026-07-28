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

import io.github.sayuzaur.bloomingwaters.BloomingWatersMod;
import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.block.WetlandsShroom.AGE;
import static io.github.sayuzaur.bloomingwaters.block.WillowLog.FACING;

public class DriftwoodFeature extends Feature {
    public boolean generate(World world, Random random, int x, int y, int z) {
        boolean canContinue = true;
        int randDirection = random.nextInt(4);
        Direction blockDirection = null;
        BlockState blockState;

        int[] varX = new int[6];
        int[] varZ = new int[6];
        int varY = 63;
        int lenght = 3 + random.nextInt(3);
        int randLog = random.nextInt(2);
        int logId = 0;

        switch (randDirection) {
            case 0 -> {
                varX = new int[]{x, x + 1, x + 2, x + 3, x + 4, x + 5, x + 6};
                varZ = new int[]{z, z, z, z, z, z, z};
                blockDirection = Direction.WEST;
            }
            case 1 -> {
                varX = new int[]{x, x - 1, x - 2, x - 3, x - 4, x - 5, x - 6};
                varZ = new int[]{z, z, z, z, z, z, z};
                blockDirection = Direction.WEST;
            }
            case 2 -> {
                varX = new int[]{x, x, x, x, x, x, x};
                varZ = new int[]{z, z + 1, z + 2, z + 3, z + 4, z + 5, z + 6};
                blockDirection = Direction.NORTH;
            }
            case 3 -> {
                varX = new int[]{x, x, x, x, x, x, x};
                varZ = new int[]{z, z - 1, z - 2, z - 3, z - 4, z - 5, z - 6};
                blockDirection = Direction.NORTH;
            }
        }

        switch (randLog) {
            case 0 -> logId = BlockListener.MOSSY_WILLOW_LOG.id;
            case 1 -> logId = BlockListener.MOSSY_OAK_LOG.id;
        }

        for (int i = 0; i < lenght; i++) {
            if ((!world.isAir(varX[i], varY + 1, varZ[i]) && world.getMaterial(varX[i], varY + 1, varZ[i]) != Material.PLANT)
                    || world.getMaterial(varX[i], varY, varZ[i]) != Material.WATER) {
                canContinue = false;
            }
        }

        if (!isLandNearby(world, varX[0], varY, varZ[0]) && !isLandNearby(world, varX[lenght], varY, varZ[lenght])) {
            canContinue = false;
        }

        if (!canContinue) {
            return false;
        }

        for (int i = 0; i < lenght; i++) {
            world.setBlockWithoutNotifyingNeighbors(varX[i], varY + 1, varZ[i], 0);

            world.setBlockWithoutNotifyingNeighbors(varX[i], varY, varZ[i], logId);
            blockState = world.getBlockState(varX[i], varY, varZ[i]);
            world.setBlockStateWithoutNotifyingNeighbors(varX[i], varY, varZ[i], blockState.with(FACING, blockDirection));

            if (random.nextInt(3) == 0) {
                world.setBlockWithoutNotifyingNeighbors(varX[i], varY + 1, varZ[i], BlockListener.DRIFTWOOD_SHROOM.id);
                blockState = world.getBlockState(varX[i], varY + 1, varZ[i]);
                world.setBlockStateWithoutNotifyingNeighbors(varX[i], varY + 1, varZ[i], blockState.with(AGE, random.nextInt(4)));
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
}
