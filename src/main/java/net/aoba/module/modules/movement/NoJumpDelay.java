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

package net.aoba.module.modules.movement;

import net.aoba.Aoba;
import net.aoba.event.events.PostTickEvent;
import net.aoba.event.listeners.PostTickListener;
import net.aoba.mixin.interfaces.ILivingEntity;
import net.aoba.module.Category;
import net.aoba.module.Module;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NoJumpDelay extends Module implements PostTickListener {

    private FloatSetting delay;

    public NoJumpDelay() {
        super(new KeybindSetting("key.nojumpdelay", "NoJumpDelay Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

        this.setName("NoJumpDelay");
        this.setCategory(Category.of("Movement"));
        this.setDescription("Makes it so the user can jump very quickly.");

        delay = new FloatSetting("nojumpdelay_delay", "Delay", "NoJumpDelay Delay", 1f, 0.0f, 20.0f, 1f);
        this.addSetting(delay);
    }

    @Override
    public void onDisable() {
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
        ILivingEntity ent = (ILivingEntity) MC.player;
        if (ent.getJumpCooldown() > delay.getValue()) {
            ent.setJumpCooldown(delay.getValue().intValue());
        }
    }
}