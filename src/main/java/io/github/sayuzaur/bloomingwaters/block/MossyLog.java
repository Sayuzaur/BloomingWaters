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
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.Identifier;

public class MossyLog extends WillowLog {
    public MossyLog(Identifier identifier) {
        super(identifier);
    }

    @Override
    public boolean onUse(World world, int x, int y, int z, PlayerEntity player) {
        if (!world.isRemote) {
            ItemStack userHand = player.getHand();
            if (userHand != null && player.inventory.getSelectedItem().getItem() instanceof AxeItem) {
                int newLogId = 0;
                int newLogMeta = 0;

                if (this.id == BlockListener.MOSSY_WILLOW_LOG.id) {
                    newLogId = BlockListener.WILLOW_LOG.id;
                } else if (this.id == BlockListener.MOSSY_OAK_LOG.id) {
                    newLogId = Block.LOG.id;
                } else if (this.id == BlockListener.MOSSY_SPRUCE_LOG.id) {
                    newLogId = Block.LOG.id;
                    newLogMeta = 1;
                } else if (this.id == BlockListener.MOSSY_BIRCH_LOG.id) {
                    newLogId = Block.LOG.id;
                    newLogMeta = 2;
                }

                int newDamage = userHand.getDamage() + 1;
                if (newDamage >= userHand.getMaxDamage()) {
                    int slot = player.inventory.selectedSlot;
                    player.inventory.removeStack(slot, 1);
                } else {
                    userHand.setDamage(newDamage);
                }

                float varX = x - ((x - (float)player.x) * 0.5F);
                float varY = y - ((y - (float)player.y) * 0.5F);
                float varZ = z - ((z - (float)player.z) * 0.5F);

                ItemEntity mossStackEntity = new ItemEntity(world, varX, varY, varZ, new ItemStack(BlockListener.MOSS_CARPET));
                mossStackEntity.pickupDelay = 10;
                world.spawnEntity(mossStackEntity);

                world.playSound(x, y, z, "step.wood", 1.0F, 1.0F);
                world.setBlock(x, y, z, newLogId, newLogMeta);

                return true;
            } else {
                return false;
            }
        }
        return true;
    }
}
