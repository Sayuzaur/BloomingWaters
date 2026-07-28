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

import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import io.github.sayuzaur.bloomingwaters.world.feature.WillowTreeFeature;
import io.github.sayuzaur.bloomingwaters.world.feature.WillowTreeLargeFeature;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplatePlantBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class WillowSapling extends TemplatePlantBlock {
    public WillowSapling(Identifier identifier) {
        super(identifier, 0);
        this.setSoundGroup(DIRT_SOUND_GROUP);
        this.setBoundingBox(0.1F, 0.0F, 0.1F, 0.9F, 0.8F, 0.9F);
    }

    public void generate(World world, int x, int y, int z, Random random) {
        world.setBlockWithoutNotifyingNeighbors(x, y, z, 0);
        Feature var7 = new WillowTreeFeature();
        if (random.nextInt(10) == 0) {
            var7 = new WillowTreeLargeFeature();
        }

        if (!var7.generate(world, random, x, y, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, y, z, this.id);
        }

    }

    public void onTick(World world, int x, int y, int z, Random random) {
        if (!world.isRemote) {
            super.onTick(world, x, y, z, random);
            if (world.getLightLevel(x, y + 1, z) >= 9 && random.nextInt(30) == 0) {
                int var6 = world.getBlockMeta(x, y, z);
                if ((var6 & 8) == 0) {
                    world.setBlockMeta(x, y, z, var6 | 8);
                } else {
                    this.generate(world, x, y, z, random);
                }
            }

        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            Random random = new Random();
            this.generate(world, x, y, z, random);
        }
        //I don't know, I'm too tired for this shit
        ((WetlandsTallPlant) BlockListener.CATTAILS).bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
