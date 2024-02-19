package net.aoba.gui.tabs;

import java.util.ArrayList;
import net.aoba.Aoba;
import net.aoba.event.events.MouseScrollEvent;
import net.aoba.event.listeners.MouseScrollListener;
import net.aoba.gui.Color;
import net.aoba.gui.hud.AbstractHud;
import net.aoba.gui.tabs.components.ColorPickerComponent;
import net.aoba.gui.tabs.components.HudComponent;
import net.aoba.gui.tabs.components.KeybindComponent;
import net.aoba.gui.tabs.components.StackPanelComponent;
import net.aoba.gui.tabs.components.StringComponent;
import net.minecraft.client.gui.DrawContext;
import net.aoba.module.Module;

public class HudsTab extends ClickGuiTab implements MouseScrollListener {

	int visibleScrollElements;
	int currentScroll;

	public HudsTab(AbstractHud[] abstractHuds) {
		super("Hud Options", 50, 50, false);

		Aoba.getInstance().eventManager.AddListener(MouseScrollListener.class, this);
		StackPanelComponent stackPanel = new StackPanelComponent(this);
		stackPanel.setTop(30);
		
		stackPanel.addChild(new StringComponent("Toggle HUD", stackPanel, Aoba.getInstance().hudManager.color.getValue(), true));
		
		for(AbstractHud hud : abstractHuds) {
			HudComponent hudComponent = new HudComponent(hud.getID(), stackPanel, hud);
			stackPanel.addChild(hudComponent);
		}
		
		stackPanel.addChild(new StringComponent("Keybinds", stackPanel, Aoba.getInstance().hudManager.color.getValue(), true));
		
		KeybindComponent clickGuiKeybindComponent = new KeybindComponent(stackPanel, Aoba.getInstance().hudManager.clickGuiButton);
		clickGuiKeybindComponent.setHeight(30);
		stackPanel.addChild(clickGuiKeybindComponent);
		
		stackPanel.addChild(new StringComponent("HUD Colors", stackPanel, Aoba.getInstance().hudManager.color.getValue(), true));
		
		stackPanel.addChild(new ColorPickerComponent(stackPanel, Aoba.getInstance().hudManager.color));
		stackPanel.addChild(new ColorPickerComponent(stackPanel, Aoba.getInstance().hudManager.backgroundColor));
		stackPanel.addChild(new ColorPickerComponent(stackPanel, Aoba.getInstance().hudManager.borderColor));
		
		this.children.add(stackPanel);
		this.setWidth(300);
	}
	
	@Override
	public void OnMouseScroll(MouseScrollEvent event) {
		 ArrayList<Module> modules = Aoba.getInstance().moduleManager.modules;
		 
		 if(event.GetVertical() > 0) 
			 this.currentScroll = Math.min(currentScroll + 1, modules.size() - visibleScrollElements - 1); 
		 else if(event.GetVertical() < 0) 
			 this.currentScroll = Math.max(currentScroll - 1, 0);
	}

	@Override
	public void draw(DrawContext drawContext, float partialTicks, Color color) {
		super.draw(drawContext, partialTicks, color);
	}
}
