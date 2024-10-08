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

package net.aoba.gui.components;

import net.aoba.Aoba;
import net.aoba.event.events.MouseClickEvent;
import net.aoba.event.events.MouseMoveEvent;
import net.aoba.gui.GuiManager;
import net.aoba.gui.IGuiElement;
import net.aoba.gui.Margin;
import net.aoba.gui.Rectangle;
import net.aoba.gui.colors.Color;
import net.aoba.gui.colors.Colors;
import net.aoba.utils.render.Render2D;
import net.aoba.settings.types.FloatSetting;
import net.aoba.utils.types.MouseAction;
import net.aoba.utils.types.MouseButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

public class SliderComponent extends Component {
    private String text;
    
    private float currentSliderPosition = 0.4f;
    
    private float minValue;
    private float maxValue;
    private float value;
    
    private boolean isSliding = false;

    FloatSetting floatSetting;

    public SliderComponent(String text, IGuiElement parent) {
        super(parent, new Rectangle(null, null, null, 45f));
        this.text = text;
        this.floatSetting = null;
        this.setMargin(new Margin(8f, 2f, 8f, 2f));
    }

    public SliderComponent(IGuiElement parent, FloatSetting floatSetting) {
        super(parent, new Rectangle(null, null, null, 45f));
        this.floatSetting = floatSetting;
        text = floatSetting.displayName;
        minValue = floatSetting.min_value;
        maxValue = floatSetting.max_value;
        value = floatSetting.getValue();
        currentSliderPosition = (float) ((value - minValue) / (maxValue - minValue));
        
        floatSetting.addOnUpdate(f -> {
        	value = f;
        	currentSliderPosition = (float) Math.min(Math.max((value - minValue) / (maxValue - minValue), 0f), 1f);
        });
        
        this.setMargin(new Margin(8f, 2f, 8f, 2f));
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

    @Override
    public void onMouseClick(MouseClickEvent event) {
        super.onMouseClick(event);
        if (event.button == MouseButton.LEFT) {
            if (event.action == MouseAction.DOWN) {
                if (hovered) {
                    isSliding = true;
                    event.cancel();
                }
            } else if (event.action == MouseAction.UP) {
                isSliding = false;
            }
        }
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        super.onMouseMove(event);

        if (Aoba.getInstance().guiManager.isClickGuiOpen() && this.isSliding) {
            double mouseX = event.getX();

            float actualX = this.getActualSize().getX();
            float actualWidth = this.getActualSize().getWidth();

            float targetPosition = (float) Math.min(((mouseX - actualX) / actualWidth), 1f);
            targetPosition = Math.max(0f, targetPosition);

            currentSliderPosition = targetPosition;
            value = (currentSliderPosition * (maxValue - minValue)) + minValue;
            
            if(floatSetting != null)
            	floatSetting.setValue(value);
        }
    }


    @Override
    public void update() {
        super.update();
    }

    @Override
    public void draw(DrawContext drawContext, float partialTicks) {
        if (this.floatSetting == null) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        MatrixStack matrixStack = drawContext.getMatrices();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

        float actualX = this.getActualSize().getX();
        float actualY = this.getActualSize().getY();
        float actualWidth = this.getActualSize().getWidth();

        float filledLength = actualWidth * currentSliderPosition;
        
        if(floatSetting != null) {
        	float defaultLength = actualWidth * ((floatSetting.getDefaultValue() - minValue) / (maxValue - minValue));
        	Render2D.drawBox(matrix4f, actualX + defaultLength - 2, actualY + 28, 4, 14, Colors.White);
        }
        
        Render2D.drawBox(matrix4f, actualX, actualY + 34, filledLength, 2, GuiManager.foregroundColor.getValue());
        Render2D.drawBox(matrix4f, actualX + filledLength, actualY + 34, (actualWidth - filledLength), 2, new Color(255, 255, 255, 255));
        
        Render2D.drawCircle(matrix4f, actualX + filledLength, actualY + 35, 6, GuiManager.foregroundColor.getValue());
        Render2D.drawString(drawContext, this.text, actualX, actualY + 8, 0xFFFFFF);

        String valueText = String.format("%.02f", value);
        int textSize = mc.textRenderer.getWidth(valueText) * 2;
        Render2D.drawString(drawContext, valueText, actualX + actualWidth - 6 - textSize, actualY + 8, 0xFFFFFF);
    }
}
