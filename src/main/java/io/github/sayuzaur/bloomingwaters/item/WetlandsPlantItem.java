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

package io.github.sayuzaur.bloomingwaters.item;

import io.github.sayuzaur.bloomingwaters.block.WetlandsTallPlant;
import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import io.github.sayuzaur.bloomingwaters.event.init.ItemListener;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class WetlandsPlantItem extends TemplateItem {
    public WetlandsPlantItem(Identifier identifier) {
        super(identifier);
    }

    //TODO Check for canPlaceAt or canGrow
    @Override
    public boolean useOnBlock(ItemStack stack, PlayerEntity user, World world, int x, int y, int z, int side) {
        if (((WetlandsTallPlant) BlockListener.CATTAILS).canPlantOnTop(world, x, y, z)
                && ((WetlandsTallPlant) BlockListener.CATTAILS).isWaterNearby(world, x, y, z)) {

            if (world.getBlockState(x, y + 1, z).isAir()) {
                if (this.id == ItemListener.CATTAILS_ITEM.id) {
                    world.setBlock(x, y + 1, z, BlockListener.CATTAILS.id);
                } else if (this.id == ItemListener.REEDS_ITEM.id) {
                    world.setBlock(x, y + 1, z, BlockListener.REEDS.id);
                } else if (this.id == ItemListener.BOG_GRASS_ITEM.id) {
                    world.setBlock(x, y + 1, z, BlockListener.BOG_GRASS_TALL.id);
                }
                stack.count--;
                world.playSound(x, y, z, "step.grass", 1.0F, 1.0F);
                return true;

            } else if (world.getBlockState(x, y + 1, z).getMaterial() == Material.WATER && world.getBlockState(x, y + 2, z).isAir()) {
                if (this.id == ItemListener.CATTAILS_ITEM.id) {
                    world.setBlock(x, y + 2, z, BlockListener.INWATER_CATTAILS.id);
                } else if (this.id == ItemListener.REEDS_ITEM.id) {
                    world.setBlock(x, y + 2, z, BlockListener.INWATER_REEDS.id);
                } else if (this.id == ItemListener.BOG_GRASS_ITEM.id) {
                    world.setBlock(x, y + 2, z, BlockListener.INWATER_BOG_GRASS.id);
                }
                stack.count--;
                world.playSound(x, y, z, "step.grass", 1.0F, 1.0F);
                return true;
            }
        }
        return false;
    }
}
