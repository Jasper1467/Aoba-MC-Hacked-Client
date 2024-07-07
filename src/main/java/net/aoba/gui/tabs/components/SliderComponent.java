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

import net.aoba.Aoba;
import net.aoba.event.events.MouseClickEvent;
import net.aoba.event.events.MouseMoveEvent;
import net.aoba.event.listeners.MouseClickListener;
import net.aoba.event.listeners.MouseMoveListener;
import net.aoba.gui.GuiManager;
import net.aoba.gui.IGuiElement;
import net.aoba.gui.colors.Color;
import net.aoba.misc.RenderUtils;
import net.aoba.settings.types.FloatSetting;
import net.aoba.utils.types.MouseAction;
import net.aoba.utils.types.MouseButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class SliderComponent extends Component implements MouseClickListener, MouseMoveListener {

    private String text;
    private float currentSliderPosition = 0.4f;
    float r;
    float g;
    float b;
    private boolean isSliding = false;


    FloatSetting slider;

    public SliderComponent(String text, IGuiElement parent) {
        super(parent);
        this.text = text;
        this.slider = null;

        this.setHeight(50);
        this.setLeft(4);
        this.setRight(4);

        Aoba.getInstance().eventManager.AddListener(MouseClickListener.class, this);
    }

    public SliderComponent(IGuiElement parent, FloatSetting slider) {
        super(parent);
        this.text = slider.displayName;
        this.slider = slider;
        this.currentSliderPosition = (float) ((slider.getValue() - slider.min_value)
                / (slider.max_value - slider.min_value));

        this.setHeight(50);
        this.setLeft(4);
        this.setRight(4);

        Aoba.getInstance().eventManager.AddListener(MouseClickListener.class, this);
    }

    public float getSliderPosition() {
        return this.currentSliderPosition;
    }

    public void setSliderPosition(float pos) {
        this.currentSliderPosition = pos;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void OnMouseClick(MouseClickEvent event) {
        if (event.button == MouseButton.LEFT) {
            if (event.action == MouseAction.DOWN) {
                if (hovered && Aoba.getInstance().hudManager.isClickGuiOpen()) {
                    isSliding = true;
                }
            } else if (event.action == MouseAction.UP) {
                isSliding = false;
            }
        }
    }

    @Override
    public void OnMouseMove(MouseMoveEvent event) {
        super.OnMouseMove(event);

        double mouseX = event.GetHorizontal();
        if (Aoba.getInstance().hudManager.isClickGuiOpen() && this.isSliding) {
            this.currentSliderPosition = (float) Math.min((((mouseX - (actualX + 4)) - 1) / (actualWidth - 8)), 1f);
            this.currentSliderPosition = (float) Math.max(0f, this.currentSliderPosition);
            this.slider.setValue((this.currentSliderPosition * (slider.max_value - slider.min_value)) + slider.min_value);
        }
    }


    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks) {
        MinecraftClient mc = MinecraftClient.getInstance();
        MatrixStack matrixStack = drawContext.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        // Draw the rest of the box.
        float xLength = ((actualWidth - 18) * (float) ((slider.getValue() - slider.min_value) / (slider.max_value - slider.min_value)));

        RenderUtils.drawBox(matrix4f, actualX + 10, actualY + 35, xLength, 2, GuiManager.foregroundColor.getValue());
        RenderUtils.drawBox(matrix4f, actualX + 10 + xLength, actualY + 35, (actualWidth - xLength - 18), 2, new Color(255, 255, 255, 255));
        RenderUtils.drawCircle(matrix4f, actualX + 10 + xLength, actualY + 35, 6, GuiManager.foregroundColor.getValue());

        if (this.slider == null)
            return;
        RenderUtils.drawString(drawContext, this.text, actualX + 6, actualY + 6, 0xFFFFFF);

        String valueText = String.format("%.02f", this.slider.getValue());
        int textSize = mc.textRenderer.getWidth(valueText) * mc.options.getGuiScale().getValue();
        RenderUtils.drawString(drawContext, valueText, actualX + actualWidth - 6 - textSize, actualY + 6, 0xFFFFFF);
    }
}
