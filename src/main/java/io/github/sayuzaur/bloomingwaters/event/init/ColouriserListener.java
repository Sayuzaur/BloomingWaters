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

package io.github.sayuzaur.bloomingwaters.event.init;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

import java.lang.invoke.MethodHandles;

public class ColouriserListener {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    protected int getDefaultTint(BlockState ignoredState, BlockView world, BlockPos pos, int ignoredTintIndex) {
        world.method_1781().getBiomesInArea(pos.x, pos.z, 1, 1);
        double temp = world.method_1781().temperatureMap[0];
        double rain = world.method_1781().downfallMap[0];
        return GrassColors.getColor(temp, rain);
    }

    @EventListener
    public void registerBlockColours(BlockColorsRegisterEvent event) {
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.CATTAILS);
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.INWATER_CATTAILS);

        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.REEDS);
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.INWATER_REEDS);

        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.BOG_GRASS_TALL);
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.BOG_GRASS_SHORT);
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.INWATER_BOG_GRASS);

        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.LILY_PAD);
        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> 9361389, BlockListener.FROST_LILY_PAD);

        event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            assert world != null;
            assert pos != null;
            return getDefaultTint(state, world, pos, tintIndex);
        }, BlockListener.FIREFLY_BUSH);
    }
}