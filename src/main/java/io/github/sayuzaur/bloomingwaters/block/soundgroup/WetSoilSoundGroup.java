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

package io.github.sayuzaur.bloomingwaters.block.soundgroup;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sound.BlockSoundGroup;

public final class WetSoilSoundGroup extends BlockSoundGroup {
    public WetSoilSoundGroup(String soundName, float volume, float pitch) {
        super(soundName, volume, pitch);
    }

    public String getSound() {
        return "bloomingwaters:step.wetsoil";
    }

    @Environment(EnvType.CLIENT)
    public String getBreakSound() {
        return "bloomingwaters:step.wetsoil";
    }
}