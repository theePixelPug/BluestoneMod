package net.theepixelpug.bluestone.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.theepixelpug.bluestone.BluestoneMod;
import net.theepixelpug.bluestone.block.ModBlocks;

public class ModBlockEnities<T extends BlockEntity> {
    public static BlockEntityType<BluestonePistonBlockEntity> BLUESTONE_PISTON;

    public static void registerBlockEntities() {
        BLUESTONE_PISTON = Registry.register(Registry.BLOCK_ENTITY_TYPE,
                new Identifier(BluestoneMod.MOD_ID, "bluestone_piston"),
                FabricBlockEntityTypeBuilder.create(BluestonePistonBlockEntity::new,
                        ModBlocks.BLUESTONE_MOVING_PISTON).build(null));
    }
}
