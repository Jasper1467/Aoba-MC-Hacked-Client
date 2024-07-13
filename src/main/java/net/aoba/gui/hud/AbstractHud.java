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

package net.aoba.gui.hud;

import net.aoba.Aoba;
import net.aoba.gui.AbstractGui;
import net.aoba.gui.GuiManager;
import net.aoba.gui.Rectangle;
import net.aoba.gui.colors.Color;
import net.aoba.misc.RenderUtils;
import net.aoba.settings.SettingManager;
import net.aoba.settings.types.BooleanSetting;
import net.minecraft.client.gui.DrawContext;

public abstract class AbstractHud extends AbstractGui {

    public BooleanSetting activated;

    private static Color dragColor = new Color(255, 0, 0, 165);
    
    public AbstractHud(String ID, float x, float y, float width, float height) {
        super(ID, x, y, width, height);
        this.setVisible(true);
        this.activated = new BooleanSetting(ID + "_activated", ID + " Activated", false, (Boolean val) -> onActivatedChanged(val));
        SettingManager.registerSetting(activated, Aoba.getInstance().settingManager.configContainer);
    }

    private void onActivatedChanged(Boolean state) {
        Aoba.getInstance().hudManager.SetHudActive(this, state.booleanValue());
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks) {
    	if(visible) {
        	if (isMoving) {
        		Rectangle pos = position.getValue();
        		
        		if(pos.isDrawable()) {
            		float x = pos.getX().floatValue();
            		float y = pos.getY().floatValue();
                    float width = pos.getWidth().floatValue();
            		float height = pos.getHeight().floatValue();
                    
                    RenderUtils.drawRoundedBox(drawContext.getMatrices().peek().getPositionMatrix(), x, y, width, height, 6, dragColor);
        		}
            }
    	}
    }
}
