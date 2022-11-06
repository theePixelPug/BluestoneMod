package net.theepixelpug.bluestone.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.theepixelpug.bluestone.BluestoneMod;
import net.theepixelpug.bluestone.block.ModBlocks;

public class ModItems {
    public static final Item BLUESTONE_ORE = register("bluestone_ore", ModBlocks.BLUESTONE_ORE, ItemGroup.BUILDING_BLOCKS);
    public static final Item DEEPSLATE_BLUESTONE_ORE = register("deepslate_bluestone_ore", ModBlocks.DEEPSLATE_BLUESTONE_ORE, ItemGroup.BUILDING_BLOCKS);

    public static final Item BLUESTONE = register("bluestone", new AliasedBlockItem(ModBlocks.BLUESTONE_WIRE, (new FabricItemSettings().group(ItemGroup.REDSTONE))));
    public static final Item BLUESTONE_TORCH = register("bluestone_torch", new WallStandingBlockItem(ModBlocks.BLUESTONE_TORCH, ModBlocks.BLUESTONE_WALL_TORCH, (new FabricItemSettings().group(ItemGroup.REDSTONE))));
    private static final Item BLUESTONE_BLOCK = register("bluestone_block", ModBlocks.BLUESTONE_BLOCK, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_REPEATER = register("bluestone_repeater", ModBlocks.BLUESTONE_REPEATER, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_COMPARATOR = register("bluestone_comparator", ModBlocks.BLUESTONE_COMPARATOR, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_PISTON = register("bluestone_piston", ModBlocks.BLUESTONE_PISTON, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_STICKY_PISTON = register("bluestone_sticky_piston", ModBlocks.BLUESTONE_STICKY_PISTON, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_OBSERVER = register("bluestone_observer", ModBlocks.BLUESTONE_OBSERVER, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_DISPENSER = register("bluestone_dispenser", ModBlocks.BLUESTONE_DISPENSER, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_DROPPER = register("bluestone_dropper", ModBlocks.BLUESTONE_DROPPER, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_TARGET = register("bluestone_target", ModBlocks.BLUESTONE_TARGET, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_LAMP = register("bluestone_lamp", ModBlocks.BLUESTONE_LAMP, ItemGroup.REDSTONE);
    public static final Item BLUESTONE_NOTE_BLOCK = register("bluestone_note_block", ModBlocks.BLUESTONE_NOTE_BLOCK, ItemGroup.REDSTONE);

    public static final Item POWER_CONVERTER = register("power_converter", ModBlocks.POWER_CONVERTER, ItemGroup.REDSTONE);

    public static final Item BLUESTONE_POWERED_RAIL = register("bluestone_powered_rail", ModBlocks.BLUESTONE_POWERED_RAIL, ItemGroup.TRANSPORTATION);
    public static final Item BLUESTONE_DETECTOR_RAIL = register("bluestone_detector_rail", ModBlocks.BLUESTONE_DETECTOR_RAIL, ItemGroup.TRANSPORTATION);
    public static final Item BLUESTONE_ACTIVATOR_RAIL = register("bluestone_activator_rail", ModBlocks.BLUESTONE_ACTIVATOR_RAIL, ItemGroup.TRANSPORTATION);


    private static Item register(String id, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(BluestoneMod.MOD_ID, id),
                new BlockItem(block, new FabricItemSettings().group(group)));
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(BluestoneMod.MOD_ID, id), item);
    }

    public static void registerModItems() {
        BluestoneMod.LOGGER.debug("Registering Mod Items for " + BluestoneMod.MOD_ID);
    }
}
