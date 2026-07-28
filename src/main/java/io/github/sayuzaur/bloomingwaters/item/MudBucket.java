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

import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class MudBucket extends TemplateItem {
    public MudBucket(Identifier identifier) {
        super(identifier);
        this.maxCount = 1;
        this.setCraftingReturnItem(Item.BUCKET);
    }

    //I don't fucking understand it, I just copied bucket code and removed water/lava/milk parts
    public ItemStack use(ItemStack stack, World world, PlayerEntity user) {
        float var4 = 1.0F;
        float var5 = user.prevPitch + (user.pitch - user.prevPitch) * var4;
        float var6 = user.prevYaw + (user.yaw - user.prevYaw) * var4;
        double var7 = user.prevX + (user.x - user.prevX) * (double)var4;
        double var9 = user.prevY + (user.y - user.prevY) * (double)var4 + 1.62 - (double)user.standingEyeHeight;
        double var11 = user.prevZ + (user.z - user.prevZ) * (double)var4;
        Vec3d var13 = Vec3d.createCached(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
        float var15 = MathHelper.sin(-var6 * ((float)Math.PI / 180F) - (float)Math.PI);
        float var16 = -MathHelper.cos(-var5 * ((float)Math.PI / 180F));
        float var17 = MathHelper.sin(-var5 * ((float)Math.PI / 180F));
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = (double)5.0F;
        Vec3d var23 = var13.add((double)var18 * var21, (double)var17 * var21, (double)var20 * var21);
        HitResult var24 = world.raycast(var13, var23, false);
        if (var24 == null) {
            return stack;
        } else {
            if (var24.type == HitResultType.BLOCK) {
                int var25 = var24.blockX;
                int var26 = var24.blockY;
                int var27 = var24.blockZ;
                if (!world.canInteract(user, var25, var26, var27)) {
                    return stack;
                }

                    if (var24.side == 0) {
                        --var26;
                    }

                    if (var24.side == 1) {
                        ++var26;
                    }

                    if (var24.side == 2) {
                        --var27;
                    }

                    if (var24.side == 3) {
                        ++var27;
                    }

                    if (var24.side == 4) {
                        --var25;
                    }

                    if (var24.side == 5) {
                        ++var25;
                    }

                    if (world.isAir(var25, var26, var27) || !world.getMaterial(var25, var26, var27).isSolid()) {

                        world.setBlock(var25, var26, var27, BlockListener.SINKING_MUD.id);
                        world.playSound(var25, var26, var27, "bloomingwaters:step.wetsoil", 1.0F, 1.6F);
                        return new ItemStack(Item.BUCKET);
                    }
                }

            return stack;
        }
    }
}
