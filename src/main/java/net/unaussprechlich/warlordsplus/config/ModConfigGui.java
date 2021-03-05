package net.unaussprechlich.warlordsplus.config;


import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.unaussprechlich.warlordsplus.WarlordsPlus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ConfigGUI
 */
public class ModConfigGui extends GuiConfig {

    /**
     * static HasMap to store the element lists with key categiry
     */
    private static final HashMap<CCategory, ArrayList<IConfigElement>> configCategoryMap = new HashMap<>();

    /**
     * Constructor calls super() for the parent GUI
     *
     * @param parent ParentGUI
     */
    public ModConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), WarlordsPlus.MODID, false, false, "WarlordsPlus Settings");
        populateHasMapWithCategories();
    }

    /**
     * Will populate the configCategoryMap with the predefined categories
     */
    private static void populateHasMapWithCategories() {
        for (CCategory category : CCategory.values()) {
            if (!configCategoryMap.containsKey(category)) {
                configCategoryMap.put(category, new ArrayList<>());
            }
        }
    }

    /**
     * a single list for the ConfigGUI
     *
     * @return all ConfigElements in a list
     */
    private static List<IConfigElement> getConfigElements() {
        return configCategoryMap.keySet()
                .stream().sorted()
                .filter(cCategory -> !configCategoryMap.get(cCategory).isEmpty())
                .map(cCategory -> new DummyConfigElement.DummyCategoryElement(
                        cCategory.getEnumChatFormatting() + cCategory.getName(),
                        "",
                        configCategoryMap.get(cCategory).stream()
                                .sorted(Comparator.comparing(IConfigElement::getName))
                                .collect(Collectors.toList())
                        )

                )
                .collect(Collectors.toList());
    }

    /**
     * Call the method before reloading the  config, so the GUI gets cleared.
     */
    public static void deleteBeforReload() {
        configCategoryMap.clear();
        populateHasMapWithCategories();
    }


    /**
     * Adds a Element to the ConfigGUI element HasMap. Made it use generics, because generics are cool :)
     * Will throw a IllegalParameterTypeException if the Type is not supported.
     *
     * @param category The category the element will be pushed
     * @param id       The ID the element has in the config
     * @param defEntry The default value of the element
     * @param comment  We all like comments, so we know what option we are really changing.
     * @param <T>      used generics for defEntry, will throw a exception if type is not supported
     */
    public static <T> void addElement(CCategory category, String id, T defEntry, String comment) {

        //adds the category if it doesn't exists
        //we leave this here in case the category has not been loaded by populateHasMapWithCategories already
        if (!configCategoryMap.containsKey(category)) {
            configCategoryMap.put(category, new ArrayList<>());
        }

        //generates & puts the element in the right list
        if (defEntry.getClass() == String.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(WarlordsPlus.CONFIG.get(category.getName(), id, (String) defEntry, comment))));
        } else if (defEntry.getClass() == Boolean.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(WarlordsPlus.CONFIG.get(category.getName(), id, (Boolean) defEntry, comment))));
        } else if (defEntry.getClass() == Integer.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(WarlordsPlus.CONFIG.get(category.getName(), id, (Integer) defEntry, comment))));
        } else if (defEntry.getClass() == Double.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(WarlordsPlus.CONFIG.get(category.getName(), id, (Double) defEntry, comment))));
        } else if (defEntry.getClass() == Float.class) {
            configCategoryMap.get(category).add((
                    new ConfigElement(WarlordsPlus.CONFIG.get(category.getName(), id, (Float) defEntry, comment))));
        } else {
            throw new IllegalArgumentException(defEntry.toString());
        }
    }
}

