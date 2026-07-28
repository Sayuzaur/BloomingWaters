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
import io.github.sayuzaur.bloomingwaters.event.init.ItemListener;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WetlandsTallPlant extends BasePlant {
    public static final BooleanProperty IS_BOTTOM;

    static {
        IS_BOTTOM = BooleanProperty.of("is_bottom");
    }

    public WetlandsTallPlant(Identifier identifier) {
        super(identifier);
        this.setTickRandomly(true);
        this.setBoundingBox(0.1F, 0.0F, 0.1F, 0.9F, 1F, 0.9F);
        setDefaultState(getStateManager().getDefaultState().with(IS_BOTTOM, true));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(IS_BOTTOM);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return getStateManager().getDefaultState().with(IS_BOTTOM, true);
    }

    public boolean canPlantOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isIn(BloomingWatersMod.WETPLANTS_PLACE_AT);
    }

    public boolean canSpreadOnTop(World world, int x, int y, int z) {
        return world.getBlockState(x, y, z).isIn(BloomingWatersMod.WETPLANTS_GROW_AT);
    }

    public boolean isWaterNearby(World world, int x, int y, int z) {
        int waterRange = 2;
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

    //TODO Can be shorten
    @Override
    public boolean canPlaceAt(World world, int x, int y, int z, int side) {
        if (!world.isAir(x, y + 1, z)) {
            return false;
        } else if (world.getBlockState(x, y, z).getMaterial() == Material.WATER) {
            return false;
        } else if (!isWaterNearby(world, x, y, z)) {
            return false;
        }
        return canPlantOnTop(world, x, y - 1, z);
    }

    @Override
    public void onPlaced(World world, int x, int y, int z) {
        if (world.getBlockState(x, y + 1, z).isAir() && world.getBlockState(x, y, z).get(IS_BOTTOM) == true) {
            world.setBlockState(x, y + 1, z, this.getDefaultState().with(IS_BOTTOM, false));
        }
    }

    @Override
    public boolean canGrow(World world, int x, int y, int z) {
        if (world.getBlockId(x, y, z) != this.id) {
            return     canPlantOnTop(world, x, y - 1, z)
                    && isWaterNearby(world, x, y, z);
        } else if (world.getBlockState(x, y, z).get(IS_BOTTOM) == true) {
            return     canPlantOnTop(world, x, y - 1, z)
                    && isWaterNearby(world, x, y, z)
                    && world.getBlockState(x,y + 1, z).isOf(this);
        } else {
            return world.getBlockState(x,y - 1, z).isOf(this);
        }
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


    public void onTick(World world, int x, int y, int z, Random random) {
        this.breakIfCannotGrow(world, x, y, z);
    }

    @Override
    public List<ItemStack> getDropList(World world, int x, int y, int z, BlockState state, int meta) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        if (state.get(IS_BOTTOM) == true) {
            if (this.id == BlockListener.CATTAILS.id) {
                drops.add(new ItemStack(ItemListener.CATTAILS_ITEM, 1));
            } else if (this.id == BlockListener.REEDS.id) {
                drops.add(new ItemStack(ItemListener.REEDS_ITEM, 1));
            } else if (this.id == BlockListener.BOG_GRASS_TALL.id) {
                drops.add(new ItemStack(ItemListener.BOG_GRASS_ITEM, 1));
            }
        }
        return drops;
    }

    public void attemptSpread(World world, int x, int y, int z) {
        int spreadTargetY = y - 1;
        if (world.getBlockState(x, y, z).get(IS_BOTTOM) == false) {
            spreadTargetY = y - 2;
        }
        Random random= new Random();
        if (canSpreadOnTop(world, x, spreadTargetY, z)) {
            for (int spreadTargetX = x - 1; spreadTargetX <= x + 1; ++spreadTargetX) {
                for (int spreadTargetZ = z - 1; spreadTargetZ <= z + 1; ++spreadTargetZ) {
                    if (random.nextInt(2) == 0
                            && canSpreadOnTop(world, spreadTargetX, spreadTargetY, spreadTargetZ)
                            && isWaterNearby(world, spreadTargetX, spreadTargetY, spreadTargetZ)
                            && world.getBlockState(spreadTargetX, spreadTargetY + 1, spreadTargetZ).isAir()
                            && world.getBlockState(spreadTargetX, spreadTargetY + 2, spreadTargetZ).isAir()) {
                        world.setBlock(spreadTargetX, spreadTargetY + 1, spreadTargetZ, this.id);
                    }
                }
            }
        }
    }

    @Override
    public boolean onBonemealUse(World world, int x, int y, int z, BlockState state) {
        if (!world.isRemote) {
            attemptSpread(world, x, y, z);
        }
        bonemealClientsideEffect(world, x, y, z);
        return true;
    }
}
