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

import io.github.sayuzaur.bloomingwaters.BloomingWatersMod;
import io.github.sayuzaur.bloomingwaters.block.base.BasePlant;
import io.github.sayuzaur.bloomingwaters.event.init.BlockListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.tag.BlockTags;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.IntProperty;
import net.modificationstation.stationapi.api.state.property.Properties;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class WetlandsShroom extends BasePlant {
    public static final IntProperty AGE;

    static {
        AGE = Properties.AGE_3;
    }

    public WetlandsShroom(Identifier identifier) {
        super(identifier);
        this.setTickRandomly(true);
        this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 0.45F, 0.75F);
        setDefaultState(getStateManager().getDefaultState().with(AGE, 0));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(AGE, 0);
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isOpaque();
    }

    public boolean canSpreadOnTop(World world, int x, int y, int z) {
        if (this.id == BlockListener.DRIFTWOOD_SHROOM.id) {
            return world.getBlockState(x, y, z).isIn(BloomingWatersMod.WETPLANTS_GROW_AT) || world.getBlockState(x, y, z).isIn(BlockTags.LOGS);
        }
        return world.getBlockState(x, y, z).isIn(BloomingWatersMod.WETPLANTS_GROW_AT);
    }

    public boolean isWaterNearby(World world, int x, int y, int z) {
        int waterRange = 4;
        for (int waterX = x - waterRange; waterX <= x + waterRange; ++waterX) {
            for (int waterY = y - 2; waterY <= y + 1; ++waterY) {
                for (int waterZ = z - waterRange; waterZ <= z + waterRange; ++waterZ) {
                    if (world.getMaterial(waterX, waterY, waterZ) == Material.WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (world.getBlockState(x, y, z).getMaterial() == Material.WATER || !world.isAir(x, y, z)) {
            return false;
        }
        return canPlantOnTop(world, x, y - 1, z);
    }

    @Override
    public boolean canGrow(World world, int x, int y, int z) {
        return canPlantOnTop(world, x, y - 1, z);
    }

    protected final void breakIfCannotGrow(World world, int x, int y, int z) {
        if (!this.canGrow(world, x, y, z)) {
            this.dropStacks(world, x, y, z, world.getBlockMeta(x, y, z));
            world.setBlock(x, y, z, 0);
        }
    }

    public void neighborUpdate(World world, int x, int y, int z, int id) {
        super.neighborUpdate(world, x, y, z, id);
        this.breakIfCannotGrow(world, x, y, z);
    }

    public void attemptSpread(World world, int x, int y, int z, int attempts) {
        Random random = new Random();
        for (int i = 0; i <= attempts; i++) {
            int varX = x + random.nextInt(3) - 1;
            int varY = y + random.nextInt(2) - random.nextInt(2);
            int varZ = z + random.nextInt(3) - 1;

            if (world.isAir(varX, varY, varZ)
                    && canSpreadOnTop(world, varX, varY - 1, varZ)
                    && isWaterNearby(world, varX, varY, varZ)
                    && world.getBrightness(varX, varY, varZ) < 13) {
                world.setBlock(varX, varY, varZ, this.id);
                //TODO Add puffing particle effect and sound
                world.setBlockState(x, y, z, world.getBlockState(x, y, z).with(AGE, 0));
            }
        }
    }

    public void onTick(World world, int x, int y, int z, Random random) {
        this.breakIfCannotGrow(world, x, y, z);

        int chanceToGrow = 20;

        if (random.nextInt(chanceToGrow) == 0
                && world.getBlockId(x, y, z) == this.id
                && world.getBrightness(x, y, z) < 13
                && isWaterNearby(world, x, y, z)
                && canSpreadOnTop(world, x, y - 1, z)) {

            BlockState state = world.getBlockState(x, y, z);
            int age = state.get(AGE);

            if (age == 3) {
                attemptSpread(world, x, y, z, 2);
            } else {
                age++;
                world.setBlockState(x, y, z, state.with(AGE, age));
            }
        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            int age = state.get(AGE);

            if (age == 3) {
                attemptSpread(world, x, y, z, 6);
            } else {
                world.setBlockState(x, y, z, state.with(AGE, 3));
            }
        }

        bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
