/*
 * Aoba Hacked Client
 * Copyright (C) 2019-2024 coltonk9043
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Sneak Module
 */
package net.aoba.module.modules.movement;

import net.aoba.Aoba;
import net.aoba.event.events.PostTickEvent;
import net.aoba.event.listeners.PostTickListener;
import net.aoba.module.Category;
import net.aoba.module.Module;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class Sneak extends Module implements PostTickListener {

    private final MinecraftClient MC = MinecraftClient.getInstance();

    public Sneak() {
        super(new KeybindSetting("key.sneakhack", "Sneak Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

        this.setName("Sneak");
        this.setCategory(Category.of("Movement"));
        this.setDescription("Makes the player appear like they're sneaking.");
    }

    @Override
    public void onDisable() {
        ClientPlayerEntity player = MC.player;
        if (player != null) {
            MC.options.sneakKey.setPressed(false);
        }
        Aoba.getInstance().eventManager.RemoveListener(PostTickListener.class, this);
    }

    @Override
    public void onEnable() {
        Aoba.getInstance().eventManager.AddListener(PostTickListener.class, this);
    }

    @Override
    public void onToggle() {

    }

    @Override
    public void onPostTick(PostTickEvent event) {
        ClientPlayerEntity player = MC.player;
        if (player != null) {
            MC.options.sneakKey.setPressed(true);
        }
    }
}