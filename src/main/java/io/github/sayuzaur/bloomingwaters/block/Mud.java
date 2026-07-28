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
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.WETSOIL_SOUND_GROUP;

public class Mud extends TemplateBlock {
    public Mud(Identifier identifier) {
        super(identifier, Material.SOIL);
        this.setHardness(0.6F);
        this.setSoundGroup(WETSOIL_SOUND_GROUP);
    }

    public Box getCollisionShape(World world, int x, int y, int z) {
        float sinkDepth = 0.125F;
        return Box.createCached(x, y, z, x + 1, (float)(y + 1) - sinkDepth, z + 1);
    }

    public void onEntityCollision(World world, int x, int y, int z, Entity entity) {
        entity.velocityX *= 0.4;
        entity.velocityZ *= 0.4;
    }

    public void dryingClientsideEffect(World world, int x, int y, int z) {
        Random random = new Random();
        world.playSound((float)x + 0.5F, (float)y + 0.5F, (float)z + 0.5F, "random.fizz", 0.2F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
        for(int i = 0; i < 5; ++i) {
            double varX = (double)x + random.nextDouble() * 0.6 + 0.2;
            double varY = (double)y + random.nextDouble() * 0.6 + 0.2;
            double varZ = (double)z + random.nextDouble() * 0.6 + 0.2;
            world.addParticle("smoke", varX, varY + 0.8F, varZ, 0.0F, 0.0F, 0.0F);
        }
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        if (world.dimension.isNether) {
            if (!world.isRemote) {
                if (this.id == BlockListener.MUD.id) {
                    world.setBlock(x, y, z, BlockListener.DRIED_MUD.id);
                } else if (this.id == BlockListener.PEAT.id) {
                    world.setBlock(x, y, z, BlockListener.DRIED_PEAT.id);
                }
            }
            dryingClientsideEffect(world, x, y, z);
        }
    }
}