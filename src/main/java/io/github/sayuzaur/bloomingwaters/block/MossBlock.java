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
import io.github.sayuzaur.bloomingwaters.world.feature.MossPatchFeature;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class MossBlock extends TemplateBlock {
    public MossBlock(Identifier identifier) {
        super(identifier, Material.SOLID_ORGANIC);
        this.setHardness(0.3F);
        this.setSoundGroup(DIRT_SOUND_GROUP);
    }

    public void generate(World world, int x, int y, int z, Random random) {
        Feature mossFeature = new MossPatchFeature();

        if (!mossFeature.generate(world, random, x, y, z)) {
            world.setBlockWithoutNotifyingNeighbors(x, y, z, this.id);
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
