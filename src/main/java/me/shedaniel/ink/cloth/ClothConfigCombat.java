package me.shedaniel.ink.cloth;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import me.shedaniel.ink.ConfigObject;
import me.shedaniel.ink.INeedKeybinds;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static me.shedaniel.ink.INeedKeybinds.configObject;

public class ClothConfigCombat {
    
    public static Screen getScreen(Screen screen) {
        ConfigBuilder builder = ConfigBuilder.create().setTitle(new TranslatableText("config.i-need-keybinds.title")).setParentScreen(screen);
        ConfigEntryBuilder eb = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("config.category.general"));
        general.addEntry(eb.startIntSlider(new TranslatableText("config.field.animationTicks"), (int) configObject.animate, 0, 5000).setTextGetter(integer -> {
            return Text.of(integer <= 0 ? "Value: Disabled" : (integer > 1500 ? String.format("Value: %.1fs", integer / 1000f) : "Value: " + integer + "ms"));
        }).setDefaultValue(300).setSaveConsumer(i -> configObject.animate = i).build());
        for (int i = 0; i < 8; i++) {
            ConfigCategory category = builder.getOrCreateCategory(new TranslatableText("config.category.category_" + i));
            ConfigObject.CategoryObject object = configObject.categories.get(i);
            StringListEntry entry;
            category.addEntry(entry = eb.startTextField(new TranslatableText("config.category.name"), object.name == null ? "null" : object.name).setDefaultValue("null").setSaveConsumer(s -> {
                if (s.equalsIgnoreCase("null") || s.isEmpty()) {
                    object.getKeybinds().clear();
                    object.name = null;
                } else {
                    object.name = s;
                }
            }).build());
            category.addEntry(new KeybindingListEntry(entry, "config.category.keys", new ArrayList<>(object.getFunctions()), true, strings -> {
                object.getKeybinds().clear();
                object.getKeybinds().addAll(strings.stream().map(Object::toString).collect(Collectors.toList()));
            }, eb.getResetButtonKey(), false));
        }
        builder.setSavingRunnable(() -> {
            try {
                INeedKeybinds.configManager.saveConfig();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return builder.build();
    }
    
}
