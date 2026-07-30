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
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class FlowerPatchFeature extends Feature {
    protected int maxPlants;
    protected int range;
    protected int plantNum;

    public FlowerPatchFeature(int patchSize, int spread, int plantMode) {
        this.maxPlants = patchSize;
        this.range = spread;
        this.plantNum = plantMode;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int attempts = 64;
        int generatedPlants = 0;
        int plantBlockId;
        int patchSizeRandSize = random.nextInt(3) - 1;
        generatedPlants = generatedPlants + patchSizeRandSize;

        switch (plantNum) {
            case 1 -> plantBlockId = BlockListener.FUNGAL_POD.id;
            case 2 -> plantBlockId = BlockListener.RAIN_CAP.id;
            case 3 -> plantBlockId = BlockListener.FORGET_ME_NOT.id;
            case 4 -> plantBlockId = BlockListener.MARSH_MARIGOLD.id;
            case 5 -> plantBlockId = BlockListener.BOG_VIOLET.id;
            case 8 -> plantBlockId = BlockListener.FIREFLY_BUSH.id;
            case 10 -> plantBlockId = BlockListener.MOSS_CARPET.id;
            default -> plantBlockId = Block.ROSE.id;
        }

        for (int i = 0; i < attempts && generatedPlants < this.maxPlants; i++) {
            int varX = x + random.nextInt(range) - random.nextInt(range);
            int varZ = z + random.nextInt(range) - random.nextInt(range);
            int varY = y + random.nextInt(4) - random.nextInt(4);

            if (isValidTarget(world, varX, varY, varZ) ) {
                world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, plantBlockId);
                generatedPlants++;
            }
        }
        return true;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        return     world.isAir(x, y, z)
                && (world.getBlockId(x, y - 1, z) == BlockListener.MUD.id
                || world.getBlockId(x, y - 1, z) == Block.GRASS_BLOCK.id
                || world.getBlockId(x, y - 1, z) == Block.DIRT.id);
    }
}
