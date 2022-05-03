package net.unaussprechlich.warlordsplus.config;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;

/**
 * A class for producing fancy config entries like DummyConfigElement but this one saves it's values.
 * Works pretty similarly but extends ConfigElement and doesn't just implement IConfigElement
 *
 * @author palechip
 * <p>
 * Will be added back later ... DummyConfigELements are cool and i also miss those sliders
 */
public class FancyConfigElement extends ConfigElement {
    protected String[] validValues;
    protected Object minValue;
    protected Object maxValue;
    protected Class<? extends IConfigEntry> configEntryClass;

    public FancyConfigElement(Property prop, String[] validValues) {
        super(prop);
        this.validValues = validValues;
    }

    public FancyConfigElement(Property prop, Object minValue, Object maxValue) {
        super(prop);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public FancyConfigElement(Property prop, Object minValue, Object maxValue, Class<? extends IConfigEntry> configEntryClass) {
        this(prop, minValue, maxValue);
        this.configEntryClass = configEntryClass;
    }

    @Override
    public String[] getValidValues() {
        return this.validValues;
    }

    @Override
    public Object getMinValue() {
        return this.minValue;
    }

    @Override
    public Object getMaxValue() {
        return this.maxValue;
    }

    @Override
    public Class<? extends IConfigEntry> getConfigEntryClass() {
        return this.configEntryClass;
    }
}
