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

import io.github.sayuzaur.bloomingwaters.world.feature.TelvafrostLilyPadFeature;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.world.biome.Biome;
import net.modificationstation.stationapi.api.event.world.gen.WorldGenEvent;

public class ChunkDecorationListener {

    @EventListener
    public void chunkDecoration(WorldGenEvent.ChunkDecoration event) {
        /*Debug
        event.world.method_1781().getBiomesInArea(event.x, event.z, 1, 1);
        double temp = event.world.method_1781().temperatureMap[0];
        double rain = event.world.method_1781().downfallMap[0];
        double wird = event.world.method_1781().weirdnessMap[0];
        rain *= temp;

        System.out.println("Temp:" + temp + ", Rain:" + rain + ", Weird:" + wird);
        */

        if (event.world.dimension.id == 0 && (event.biome == Biome.TAIGA || event.biome == Biome.FOREST || event.biome == Biome.SWAMPLAND)) {
            TelvafrostLilyPad(event);
        }
    }

    public void TelvafrostLilyPad(WorldGenEvent.ChunkDecoration event) {
        int frostLilyIterations = 8;
        //Check for climate
        event.world.method_1781().getBiomesInArea(event.x, event.z, 1, 1);
        double temp = event.world.method_1781().temperatureMap[0];
        double rain = event.world.method_1781().downfallMap[0];
        rain *= temp;

        if (temp > 0.4D && temp < 0.6D && rain > 0.4D && rain < 0.6D){
            for (int i = 0; i < frostLilyIterations; i++) {
                int varX = event.x + event.random.nextInt(16);
                int varZ = event.z + event.random.nextInt(16);
                int varY = event.world.getTopY(varX, varZ);

                //Check again for climate but for every block, more precise
                event.world.method_1781().getBiomesInArea(varX, varZ, 1, 1);
                double temp2 = event.world.method_1781().temperatureMap[0];
                double rain2 = event.world.method_1781().downfallMap[0];
                rain2 *= temp2;

                if (temp2 > 0.45D && temp2 < 0.55D && rain2 > 0.45D && rain2 < 0.55D) {
                    new TelvafrostLilyPadFeature(5).generate(event.world, event.random, varX, varY, varZ);
                }
            }
        }
    }
}
