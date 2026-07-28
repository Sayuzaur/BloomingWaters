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
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class ShortBogGrassSpamFeature extends Feature {
    protected int maxPlants;
    protected int range;

    public ShortBogGrassSpamFeature(int patchSize, int spread) {
        this.maxPlants = patchSize;
        this.range = spread;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        int attempts = maxPlants + 16;
        int generatedPlants = 0;

        for (int i = 0; i < attempts && generatedPlants < this.maxPlants; i++) {
            int varX = x + random.nextInt(range) - random.nextInt(range);
            int varZ = z + random.nextInt(range) - random.nextInt(range);
            int varY = world.getTopY(varX, varZ);

            if (isValidTarget(world, varX, varY, varZ) ) {
                world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, BlockListener.BOG_GRASS_SHORT.id);
                generatedPlants++;
            }
        }
        return true;
    }

    public boolean isValidTarget(World world, int x, int y, int z) {
        return     world.isAir(x, y, z)
                && world.hasSkyLight(x, y, z)
                && world.getBlockId(x, y - 1, z) == BlockListener.MUD.id;
    }
}
