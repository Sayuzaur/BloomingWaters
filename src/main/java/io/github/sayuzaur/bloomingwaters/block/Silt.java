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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

import static io.github.sayuzaur.bloomingwaters.BloomingWatersMod.WETSOIL_SOUND_GROUP;

public class Silt extends TemplateBlock {
    public Silt(Identifier identifier) {
        super(identifier, Material.SOIL);
        this.setHardness(0.6F);
        this.setSoundGroup(WETSOIL_SOUND_GROUP);
    }

    public void onPlaced(World world, int x, int y, int z) {
        world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
    }

    public void neighborUpdate(World world, int x, int y, int z, int id) {
        world.scheduleBlockUpdate(x, y, z, this.id, this.getTickRate());
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        this.processFall(world, x, y, z);
    }

    private void processFall(World world, int x, int y, int z) {
        if (canFallThrough(world, x, y - 1, z) && y >= 0) {
            byte var8 = 32;
            if (world.isRegionLoaded(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
                world.setBlock(x, y, z, 0);
                world.setBlock(x, y - 1, z, this.id);
            } else {
                world.setBlock(x, y, z, 0);

                while(canFallThrough(world, x, y - 1, z) && y > 0) {
                    --y;
                }

                if (y > 0) {
                    world.setBlock(x, y, z, this.id);
                }
            }
        }

    }

    public int getTickRate() {
        return 3;
    }

    public static boolean canFallThrough(World world, int x, int y, int z) {
        int targetBlockId = world.getBlockId(x, y, z);
        if (targetBlockId == 0) {
            return true;
        } else if (targetBlockId == Block.FIRE.id) {
            return true;
        } else {
            Material var5 = Block.BLOCKS[targetBlockId].material;
            if (var5 == Material.WATER) {
                return true;
            } else {
                return var5 == Material.LAVA;
            }
        }
    }
}