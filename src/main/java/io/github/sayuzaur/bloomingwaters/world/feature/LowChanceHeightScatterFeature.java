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

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class LowChanceHeightScatterFeature extends Feature {
    protected final Feature feature;
    protected final int iterations;
    protected final int chance;

    public LowChanceHeightScatterFeature(Feature feature, int iterations, int chance) {
        this.feature = feature;
        this.iterations = iterations;
        this.chance = chance;
    }

    @Override
    public boolean generate(World world, Random random, int x, int y, int z) {
        //Chance 1 in X chunks to generate
        if (chance > 1 && random.nextInt(chance) != 0) {
            return false;
        }
        //Left iterations, allows to generate multiple features in one chunk, even tho chunk gen itself is rare
        boolean result = false;
        for (int i = 0; i < iterations; i++) {
            int newX = x + random.nextInt(16);
            int newZ = z + random.nextInt(16);
            int newY = world.getTopY(newX, newZ);
            result = feature.generate(world, random, newX, newY, newZ) | result;
        }
        return result;
    }
}
