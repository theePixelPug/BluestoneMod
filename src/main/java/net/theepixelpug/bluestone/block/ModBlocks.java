package net.theepixelpug.bluestone.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import net.theepixelpug.bluestone.BluestoneMod;

public class ModBlocks {
    public static final Block BLUESTONE_ORE = register("bluestone_ore", new BluestoneOreBlock(FabricBlockSettings.copy(Blocks.REDSTONE_ORE)));
    public static final Block DEEPSLATE_BLUESTONE_ORE = register("deepslate_bluestone_ore", new BluestoneOreBlock(FabricBlockSettings.copy(Blocks.DEEPSLATE_REDSTONE_ORE)));

    public static final Block BLUESTONE_WIRE = register("bluestone_wire", new BluestoneWireBlock(FabricBlockSettings.copy(Blocks.REDSTONE_WIRE)));
    public static final Block BLUESTONE_TORCH = register("bluestone_torch", new BluestoneTorchBlock(FabricBlockSettings.copy(Blocks.REDSTONE_TORCH)));
    public static final Block BLUESTONE_WALL_TORCH = register("bluestone_wall_torch", new WallBluestoneTorchBlock(FabricBlockSettings.copy(Blocks.REDSTONE_WALL_TORCH)));
    public static final Block BLUESTONE_BLOCK = register("bluestone_block", new BluestoneBlock(FabricBlockSettings.copy(Blocks.REDSTONE_BLOCK)));
    public static final Block BLUESTONE_REPEATER = register("bluestone_repeater", new BluestoneRepeaterBlock(FabricBlockSettings.copy(Blocks.REPEATER)));
    public static final Block BLUESTONE_COMPARATOR = register("bluestone_comparator", new BluestoneComparatorBlock(FabricBlockSettings.copy(Blocks.COMPARATOR)));
    public static final Block BLUESTONE_PISTON = register("bluestone_piston", createPistonBlock(false));
    public static final Block BLUESTONE_STICKY_PISTON = register("bluestone_sticky_piston", createPistonBlock(true));
    public static final Block BLUESTONE_PISTON_HEAD = register("bluestone_piston_head", new BluestonePistonHeadBlock(FabricBlockSettings.copy(Blocks.PISTON_HEAD)));
    public static final Block BLUESTONE_MOVING_PISTON = register("bluestone_moving_piston", new BluestonePistonExtensionBlock(FabricBlockSettings.copy(Blocks.MOVING_PISTON)));
    public static final Block BLUESTONE_OBSERVER = register("bluestone_observer", new BluestoneObserverBlock(FabricBlockSettings.copy(Blocks.OBSERVER)));
    public static final Block BLUESTONE_DISPENSER = register("bluestone_dispenser", new BluestoneDispenserBlock(FabricBlockSettings.copy(Blocks.DISPENSER)));
    public static final Block BLUESTONE_DROPPER = register("bluestone_dropper", new BluestoneDropperBlock(FabricBlockSettings.copy(Blocks.DROPPER)));
    public static final Block BLUESTONE_TARGET = register("bluestone_target", new BluestoneTargetBlock(FabricBlockSettings.copy(Blocks.TARGET)));
    public static final Block BLUESTONE_LAMP = register("bluestone_lamp", new BluestoneLampBlock(FabricBlockSettings.copy(Blocks.REDSTONE_LAMP)));
    public static final Block BLUESTONE_NOTE_BLOCK = register("bluestone_note_block", new BluestoneNoteBlock(FabricBlockSettings.copy(Blocks.NOTE_BLOCK)));

    public static final Block POWER_CONVERTER = register("power_converter", new PowerConverterBlock(FabricBlockSettings.copy(Blocks.REPEATER)));

    public static final Block BLUESTONE_POWERED_RAIL = register("bluestone_powered_rail", new BluestonePoweredRailBlock(FabricBlockSettings.copy(Blocks.POWERED_RAIL)));
    public static final Block BLUESTONE_DETECTOR_RAIL = register("bluestone_detector_rail", new BluestoneDetectorRailBlock(FabricBlockSettings.copy(Blocks.DETECTOR_RAIL)));
    public static final Block BLUESTONE_ACTIVATOR_RAIL = register("bluestone_activator_rail", new BluestonePoweredRailBlock(FabricBlockSettings.copy(Blocks.ACTIVATOR_RAIL)));

    private static boolean never(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }

    private static BluestonePistonBlock createPistonBlock(boolean sticky) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            return !(Boolean)state.get(BluestonePistonBlock.EXTENDED);
        };
        return new BluestonePistonBlock(sticky, AbstractBlock.Settings.of(Material.PISTON).strength(1.5F).solidBlock(ModBlocks::never).suffocates(contextPredicate).blockVision(contextPredicate));
    }


    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(BluestoneMod.MOD_ID, id), block);
    }

    public static void registerModBlocks() {
        BluestoneMod.LOGGER.debug("Registering ModBlocks for " + BluestoneMod.MOD_ID);
    }
}
