package me.shedaniel.ink.gui;

import me.shedaniel.ink.HudState;
import me.shedaniel.ink.INeedKeybinds;
import me.shedaniel.ink.api.KeyFunction;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Collections;
import java.util.List;

import static me.shedaniel.ink.INeedKeybinds.*;

public class KeybindingItemWidget extends Widget {

    static final int SPIN = 10000;
    private Rectangle bounds;
    private HudState hudState;
    private int id;

    public KeybindingItemWidget(Rectangle bounds, HudState hudState, int id) {
        this.bounds = bounds;
        this.hudState = hudState;
        this.id = id;
    }

    @Override
    public void render(MatrixStack matrices, float var3, long ms) {
        if (INeedKeybinds.hudState == hudState || (INeedKeybinds.hudState == HudState.HIDDEN && lastState == hudState)) {
            Rectangle bounds = getBounds();
            float alpha = INeedKeybinds.hudWidget.getAlpha();
            Rectangle title = new Rectangle((int) (10 - (1 - alpha) * (WIDTH + 10)), bounds.y, WIDTH, 16);
            List<KeyFunction> keyFunctions = configObject.categories.get(category).getFunctions();
            KeyFunction keyFunction = id < keyFunctions.size() ? keyFunctions.get(id) : null;
            fill(matrices, title.x, title.y, title.x + 16, title.y + title.height, color(keyFunction == null || keyFunction.isNull() ? 50 : 0, 0, 0, (int) (200f * alpha)));
            fill(matrices, title.x + 21, title.y, title.x + title.width, title.y + title.height, color(keyFunction == null || keyFunction.isNull() ? 50 : 0, 0, 0, (int) (200f * alpha)));
            textRenderer.drawWithShadow(matrices, String.valueOf(id + 1), bounds.x + 5, bounds.y + 4, 16777215);
            String s = keyFunction == null || keyFunction.isNull() ? "Not Set" : keyFunction.hasCommand() ? keyFunction.getCommand() : keyFunction.getFormattedName();
            int offset = 0;
            /*if (textRenderer.getWidth(s) > title.width - 21) {
                offset = textRenderer.getWidth(s) + 8 - (title.width - 21);
                offset *= ms % SPIN >= (SPIN / 2) ? 1 - ((ms % SPIN) - (SPIN / 2)) / (SPIN / 2f) : Math.min(ms % SPIN, (SPIN / 2)) / (SPIN / 2f);
            }*/
            textRenderer.drawWithShadow(matrices, s, bounds.x + 25 - offset, bounds.y + 4, 16777215);
        }
    }

    public float getProgress() {
        return INeedKeybinds.hudWidget.getAlpha();
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) (bounds.x - (1 - getProgress()) * (WIDTH + 10)), bounds.y, bounds.width, bounds.y + bounds.height);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.emptyList();
    }

}
