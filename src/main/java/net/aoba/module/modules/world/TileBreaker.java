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
 * TileBreaker Module
 */
package net.aoba.module.modules.world;

import net.aoba.Aoba;
import net.aoba.event.events.Render3DEvent;
import net.aoba.event.events.TickEvent;
import net.aoba.event.listeners.Render3DListener;
import net.aoba.event.listeners.TickListener;
import net.aoba.gui.colors.Color;
import net.aoba.misc.RenderUtils;
import net.aoba.module.Module;
import net.aoba.settings.types.ColorSetting;
import net.aoba.settings.types.FloatSetting;
import net.aoba.settings.types.KeybindSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class TileBreaker extends Module implements TickListener, Render3DListener {
    private MinecraftClient mc;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private FloatSetting radius;

    private ColorSetting color = new ColorSetting("tilebreaker_color", "Color", "Color", new Color(0, 1f, 1f));

    public TileBreaker() {
        super(new KeybindSetting("key.tilebreaker", "TileBreaker Key", InputUtil.fromKeyCode(GLFW.GLFW_KEY_UNKNOWN, 0)));

        this.setName("TileBreaker");
        this.setCategory(Category.World);
        this.setDescription("Destroys blocks that can be instantly broken around the player.");
        this.loadTileBreakerBlocks();
        this.radius = new FloatSetting("tilebreaker_radius", "Radius", "Radius", 5f, 0f, 15f, 1f);
        this.addSetting(radius);
        mc = MinecraftClient.getInstance();

        this.addSetting(color);
    }

    public void setRadius(int radius) {
        this.radius.setValue((float) radius);
    }

    @Override
    public void onDisable() {
        Aoba.getInstance().eventManager.RemoveListener(Render3DListener.class, this);
        Aoba.getInstance().eventManager.RemoveListener(TickListener.class, this);
    }

    @Override
    public void onEnable() {
        Aoba.getInstance().eventManager.AddListener(Render3DListener.class, this);
        Aoba.getInstance().eventManager.AddListener(TickListener.class, this);
    }

    @Override
    public void onToggle() {
    }

    private void loadTileBreakerBlocks() {
        this.blocks.add(Blocks.TORCH);
        this.blocks.add(Blocks.WALL_TORCH);
        this.blocks.add(Blocks.REDSTONE_TORCH);
        this.blocks.add(Blocks.REDSTONE_WALL_TORCH);
        this.blocks.add(Blocks.FERN);
        this.blocks.add(Blocks.LARGE_FERN);
        this.blocks.add(Blocks.FLOWER_POT);
        this.blocks.add(Blocks.POTATOES);
        this.blocks.add(Blocks.CARROTS);
        this.blocks.add(Blocks.WHEAT);
        this.blocks.add(Blocks.BEETROOTS);
        this.blocks.add(Blocks.SUGAR_CANE);
        this.blocks.add(Blocks.GRASS_BLOCK);
        this.blocks.add(Blocks.TALL_GRASS);
        this.blocks.add(Blocks.SEAGRASS);
        this.blocks.add(Blocks.TALL_SEAGRASS);
        this.blocks.add(Blocks.DEAD_BUSH);
        this.blocks.add(Blocks.DANDELION);
        this.blocks.add(Blocks.ROSE_BUSH);
        this.blocks.add(Blocks.POPPY);
        this.blocks.add(Blocks.BLUE_ORCHID);
        this.blocks.add(Blocks.ALLIUM);
        this.blocks.add(Blocks.AZURE_BLUET);
        this.blocks.add(Blocks.RED_TULIP);
        this.blocks.add(Blocks.ORANGE_TULIP);
        this.blocks.add(Blocks.WHITE_TULIP);
        this.blocks.add(Blocks.PINK_TULIP);
        this.blocks.add(Blocks.OXEYE_DAISY);
        this.blocks.add(Blocks.CORNFLOWER);
        this.blocks.add(Blocks.WITHER_ROSE);
        this.blocks.add(Blocks.LILY_OF_THE_VALLEY);
        this.blocks.add(Blocks.BROWN_MUSHROOM);
        this.blocks.add(Blocks.RED_MUSHROOM);
        this.blocks.add(Blocks.SUNFLOWER);
        this.blocks.add(Blocks.LILAC);
        this.blocks.add(Blocks.PEONY);
    }

    public boolean isTileBreakerBlock(Block b) {
        return this.blocks.contains(b);
    }

    @Override
    public void OnUpdate(TickEvent event) {
        int rad = this.radius.getValue().intValue();
        for (int x = -rad; x < rad; x++) {
            for (int y = rad; y > -rad; y--) {
                for (int z = -rad; z < rad; z++) {
                    BlockPos blockpos = new BlockPos(mc.player.getBlockX() + x,
                            mc.player.getBlockY() + y,
                            mc.player.getBlockZ() + z);
                    Block block = mc.world.getBlockState(blockpos).getBlock();
                    if (this.isTileBreakerBlock(block)) {
                        mc.player.networkHandler.sendPacket(
                                new PlayerActionC2SPacket(Action.START_DESTROY_BLOCK, blockpos, Direction.NORTH));
                        mc.player.networkHandler.sendPacket(
                                new PlayerActionC2SPacket(Action.STOP_DESTROY_BLOCK, blockpos, Direction.NORTH));
                    }
                }
            }
        }
    }

    @Override
    public void OnRender(Render3DEvent event) {
        int rad = this.radius.getValue().intValue();
        for (int x = -rad; x < rad; x++) {
            for (int y = rad; y > -rad; y--) {
                for (int z = -rad; z < rad; z++) {
                    BlockPos blockpos = new BlockPos((int) mc.player.getBlockX() + x,
                            mc.player.getBlockY() + y,
                            mc.player.getBlockZ() + z);
                    Block block = mc.world.getBlockState(blockpos).getBlock();
                    if (this.isTileBreakerBlock(block)) {
                        RenderUtils.draw3DBox(event.GetMatrix().peek().getPositionMatrix(), new Box(blockpos), color.getValue());
                    }
                }
            }
        }
    }
}
