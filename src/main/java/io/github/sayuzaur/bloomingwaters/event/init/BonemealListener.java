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
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.bonemeal.BonemealAPI;
import net.modificationstation.stationapi.api.event.registry.BlockRegistryEvent;

public class BonemealListener {
    @EventListener
    public void registerItems(BlockRegistryEvent event) {
        BlockState mud = BlockListener.MUD.getDefaultState();
        BlockState peat = BlockListener.PEAT.getDefaultState();

        BonemealAPI.addPlant(mud, BlockListener.BOG_VIOLET.getDefaultState(), 1);
        BonemealAPI.addPlant(mud, BlockListener.MARSH_MARIGOLD.getDefaultState(), 1);
        BonemealAPI.addPlant(mud, BlockListener.FORGET_ME_NOT.getDefaultState(), 1);
        BonemealAPI.addPlant(mud, BlockListener.BOG_GRASS_SHORT.getDefaultState(), 3);

        BonemealAPI.addPlant(peat, BlockListener.FUNGAL_POD.getDefaultState(), 1);
        BonemealAPI.addPlant(peat, BlockListener.RAIN_CAP.getDefaultState(), 1);
        BonemealAPI.addPlant(peat, BlockListener.MOSS_CARPET.getDefaultState(), 2);
        BonemealAPI.addPlant(peat, BlockListener.BOG_GRASS_SHORT.getDefaultState(), 3);
    }
}
