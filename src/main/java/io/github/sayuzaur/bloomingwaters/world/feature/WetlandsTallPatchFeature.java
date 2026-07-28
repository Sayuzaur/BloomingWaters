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

public class WetlandsTallPatchFeature extends Feature {
    protected int maxPlants;
    protected int bogGrassChance;
    protected int mainPlantType;

    public WetlandsTallPatchFeature(int patchSize, int bogGrass, int plantMode) {
        this.maxPlants = patchSize;
        this.bogGrassChance = bogGrass;
        this.mainPlantType = plantMode;
    }

    public boolean generate(World world, Random random, int x, int y, int z) {
        //Some bitchass basic vars
        Block mainPlant;
        Block mainPlantInwater;
        Block bogGrass = BlockListener.BOG_GRASS_TALL;
        Block bogGrassInwater = BlockListener.INWATER_BOG_GRASS;
        int attempts = 128;
        int generatedPlants = 0;
        int range = 8;

        //Set main plant based on plantMode
        switch (mainPlantType) {
            case 1 -> {
                mainPlant = BlockListener.CATTAILS;
                mainPlantInwater = BlockListener.INWATER_CATTAILS;
            }
            case 2 -> {
                mainPlant = BlockListener.REEDS;
                mainPlantInwater = BlockListener.INWATER_REEDS;
            }
            default -> {
                mainPlant = BlockListener.BOG_GRASS_TALL;
                mainPlantInwater = BlockListener.INWATER_BOG_GRASS;
            }
        }

        for (int i = 0; i < attempts && generatedPlants < this.maxPlants; i++) {
            //Pick xyz target to generate plant
            int varX = x + random.nextInt(range) - random.nextInt(range);
            int varZ = z + random.nextInt(range) - random.nextInt(range);
            int varY = world.getTopY(varX, varZ);

            int randPlant = random.nextInt(this.bogGrassChance);
            //1 in X chance to generate tall bog grass instead of main plant
            if (randPlant == 0) {
                if (world.isAir(varX, varY, varZ) && bogGrassInwater.canGrow(world, varX, varY, varZ)) {
                    world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, bogGrassInwater.id);
                    generatedPlants++;
                } else if (world.isAir(varX, varY, varZ) && bogGrass.canGrow(world, varX, varY, varZ)) {
                    world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, bogGrass.id);
                    generatedPlants++;
                }
            //Default main plant gen
            } else {
                if (world.isAir(varX, varY, varZ) && mainPlantInwater.canGrow(world, varX, varY, varZ)) {
                    world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, mainPlantInwater.id);
                    generatedPlants++;
                } else if (world.isAir(varX, varY, varZ) && mainPlant.canGrow(world, varX, varY, varZ)) {
                    world.setBlockWithoutNotifyingNeighbors(varX, varY, varZ, mainPlant.id);
                    generatedPlants++;
                }
            }
        }
        return true;
    }
}
