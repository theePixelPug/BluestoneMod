package net.theepixelpug.bluestone.state.property;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;

public class ModProperties {
    public static final BooleanProperty BLUESTONE_POWERED = BooleanProperty.of("bluestone_powered");
    public static final IntProperty BLUESTONE_POWER = IntProperty.of("bluestone_power", 0, 15);

    public static final BooleanProperty RED_TO_BLUE_POWERED = BooleanProperty.of("red_to_blue_powered");
    public static final BooleanProperty BLUE_TO_RED_POWERED = BooleanProperty.of("blue_to_red_powered");
}
