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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class BogGrassShort extends WetlandsShortPlant {
    public BogGrassShort(Identifier identifier) {
        super(identifier);
    }

    public int getDroppedItemId(int blockMeta, Random random) {
        int randDrop = random.nextInt(3);
        return switch (randDrop) {
            case 0 -> Item.SEEDS.id;
            case 1 -> Item.FLINT.id;
            case 2 -> Item.STICK.id;
            default -> -1;
        };
    }

    public int getDroppedItemCount(Random random) {
        return random.nextInt(6) == 0 ? 1 : 0;
    }

    public void afterBreak(World world, PlayerEntity playerEntity, int x, int y, int z, int meta) {
        if (!world.isRemote && playerEntity.getHand() != null && playerEntity.getHand().itemId == Item.SHEARS.id) {
            this.dropStack(world, x, y, z, new ItemStack(this, 1));
        } else {
            super.afterBreak(world, playerEntity, x, y, z, meta);
        }
    }
}
