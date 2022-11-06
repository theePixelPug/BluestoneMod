package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin extends HorizontalFacingBlock {
    protected FenceGateBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        boolean bl = world.isReceivingRedstonePower(blockPos) || ((WorldMixinInterface)world).isReceivingBluestonePower(blockPos);
        Direction direction = ctx.getPlayerFacing();
        Direction.Axis axis = direction.getAxis();
        boolean bl2 = axis == Direction.Axis.Z && (this.isWall(world.getBlockState(blockPos.west())) || this.isWall(world.getBlockState(blockPos.east()))) || axis == Direction.Axis.X && (this.isWall(world.getBlockState(blockPos.north())) || this.isWall(world.getBlockState(blockPos.south())));
        return (BlockState)((BlockState)((BlockState)((BlockState)this.getDefaultState().with(FACING, direction)).with(FenceGateBlock.OPEN, bl)).with(FenceGateBlock.POWERED, bl)).with(FenceGateBlock.IN_WALL, bl2);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }
        boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        if (state.get(FenceGateBlock.POWERED) != bl) {
            world.setBlockState(pos, (BlockState)((BlockState)state.with(FenceGateBlock.POWERED, bl)).with(FenceGateBlock.OPEN, bl), Block.NOTIFY_LISTENERS);
            if (state.get(FenceGateBlock.OPEN) != bl) {
                world.syncWorldEvent(null, bl ? WorldEvents.FENCE_GATE_OPENS : WorldEvents.FENCE_GATE_CLOSES, pos, 0);
                world.emitGameEvent(null, bl ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
        }
    }

    private boolean isWall(BlockState state) {
        return state.isIn(BlockTags.WALLS);
    }
}
