package net.theepixelpug.bluestone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.theepixelpug.bluestone.block.BluestoneWireBlock;
import net.theepixelpug.bluestone.block.ModBlocks;
import net.theepixelpug.bluestone.block.entity.BluestonePistonBlockEntity;
import net.theepixelpug.bluestone.block.entity.ModBlockEnities;
import net.theepixelpug.bluestone.client.render.block.entity.BluestonePistonBlockEntityRenderer;

public class BluestoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_WIRE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_WALL_TORCH, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_REPEATER, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_COMPARATOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_POWERED_RAIL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_DETECTOR_RAIL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BLUESTONE_ACTIVATOR_RAIL, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.POWER_CONVERTER, RenderLayer.getCutout());

        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> BluestoneWireBlock.getWireColor(state.get(BluestoneWireBlock.BLUESTONE_POWER))), ModBlocks.BLUESTONE_WIRE);

        BlockEntityRendererRegistry.register(ModBlockEnities.BLUESTONE_PISTON, BluestonePistonBlockEntityRenderer::new);
    }
}
