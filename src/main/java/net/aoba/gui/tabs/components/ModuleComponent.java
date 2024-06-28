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

package net.aoba.gui.tabs.components;

import org.joml.Matrix4f;
import net.aoba.Aoba;
import net.aoba.event.events.MouseClickEvent;
import net.aoba.event.listeners.MouseClickListener;
import net.aoba.module.Module;
import net.aoba.utils.types.MouseAction;
import net.aoba.utils.types.MouseButton;
import net.aoba.gui.GuiManager;
import net.aoba.gui.IGuiElement;
import net.aoba.gui.colors.Color;
import net.aoba.gui.tabs.ModuleSettingsTab;
import net.aoba.misc.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ModuleComponent extends Component implements MouseClickListener {
	private String text;
	private Module module;

	private ModuleSettingsTab lastSettingsTab = null;
	
	public final Identifier gear;
	
	public ModuleComponent(String text, IGuiElement parent, Module module) {
		super(parent);
		
		gear = Identifier.of("aoba", "/textures/gear.png");
		this.text = text;
		this.module = module;
		
		this.setLeft(2);
		this.setRight(2);
		this.setHeight(30);
	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks) {
		super.draw(drawContext, partialTicks);
		
		MatrixStack matrixStack = drawContext.getMatrices();
		Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		
		RenderUtils.drawString(drawContext, this.text, actualX + 8, actualY + 8, module.getState() ? 0x00FF00 : this.hovered ? GuiManager.foregroundColor.getValue().getColorAsInt() : 0xFFFFFF);
		if(module.hasSettings()) {
			Color hudColor = GuiManager.foregroundColor.getValue();
			RenderUtils.drawTexturedQuad(matrix4f, gear, (actualX + actualWidth - 20), (actualY + 6), 16, 16, hudColor);
		}
	}
	
	@Override
	public void OnVisibilityChanged() {
		if(this.isVisible()) {
			Aoba.getInstance().eventManager.AddListener(MouseClickListener.class, this);
		}else {
			Aoba.getInstance().eventManager.RemoveListener(MouseClickListener.class, this);
		}
	}

	@Override
	public void OnMouseClick(MouseClickEvent event) {
		if(event.button == MouseButton.LEFT && event.action == MouseAction.DOWN) {
			double mouseX = event.mouseX;
			if (hovered && Aoba.getInstance().hudManager.isClickGuiOpen()) {
					boolean isOnOptionsButton = (mouseX >= (actualX + actualWidth - 34) && mouseX <= (actualX + actualWidth));
					if (isOnOptionsButton) {
						if(lastSettingsTab == null) {
							lastSettingsTab = new ModuleSettingsTab(this.module.getName(), this.actualX + this.actualWidth + 1, this.actualY, this.module);
							lastSettingsTab.setVisible(true);
							Aoba.getInstance().hudManager.AddHud(lastSettingsTab, "Modules");
						}else {
							Aoba.getInstance().hudManager.RemoveHud(lastSettingsTab, "Modules");
							lastSettingsTab = null;
						}
					} else {
						module.toggle();
						return;
					}
			}
		}
	}
}
