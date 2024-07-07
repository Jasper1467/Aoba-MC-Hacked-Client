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

package net.aoba.gui.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.aoba.Aoba;
import net.aoba.gui.GuiManager;
import net.aoba.gui.hud.AbstractHud;
import net.aoba.gui.tabs.components.ColorPickerComponent;
import net.aoba.gui.tabs.components.HudComponent;
import net.aoba.gui.tabs.components.KeybindComponent;
import net.aoba.gui.tabs.components.ListComponent;
import net.aoba.gui.tabs.components.StackPanelComponent;
import net.aoba.gui.tabs.components.StringComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class HudsTab extends ClickGuiTab {
    public HudsTab(AbstractHud[] abstractHuds) {
        super("Hud Options", 50, 50, false);

        StackPanelComponent stackPanel = new StackPanelComponent(this);
        stackPanel.setTop(30);

        List<String> test = new ArrayList<String>();


        ConcurrentHashMap<String, TextRenderer> fontRenderers = Aoba.getInstance().fontManager.fontRenderers;
        Set<String> set = fontRenderers.keySet();

        for (String s : set) {
            test.add(s);
        }

        stackPanel.addChild(new StringComponent("Toggle HUD", stackPanel, GuiManager.foregroundColor.getValue(), true));

        for (AbstractHud hud : abstractHuds) {
            HudComponent hudComponent = new HudComponent(hud.getID(), stackPanel, hud);
            stackPanel.addChild(hudComponent);
        }

        // Keybinds Header
        stackPanel.addChild(new StringComponent("Keybinds", stackPanel, GuiManager.foregroundColor.getValue(), true));

        KeybindComponent clickGuiKeybindComponent = new KeybindComponent(stackPanel, Aoba.getInstance().hudManager.clickGuiButton);
        clickGuiKeybindComponent.setHeight(30);
        stackPanel.addChild(clickGuiKeybindComponent);

        // Hud Font Header
        stackPanel.addChild(new StringComponent("HUD Font", stackPanel, GuiManager.foregroundColor.getValue(), true));


        ListComponent listComponent = new ListComponent(stackPanel, test, Aoba.getInstance().fontManager.fontSetting);
        stackPanel.addChild(listComponent);

        stackPanel.addChild(new StringComponent("HUD Colors", stackPanel, GuiManager.foregroundColor.getValue(), true));

        stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.foregroundColor));
        stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.backgroundColor));
        stackPanel.addChild(new ColorPickerComponent(stackPanel, GuiManager.borderColor));

        this.children.add(stackPanel);
        this.setWidth(300);
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks) {
        super.draw(drawContext, partialTicks);
    }
}
