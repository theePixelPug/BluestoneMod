package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrapdoorBlock.class)
public abstract class TrapdoorBlockMixin extends HorizontalFacingBlock implements Waterloggable {
    protected TrapdoorBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }
        boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        if (bl != state.get(TrapdoorBlock.POWERED)) {
            if (state.get(TrapdoorBlock.OPEN) != bl) {
                state = (BlockState)state.with(TrapdoorBlock.OPEN, bl);
                this.playToggleSound(null, world, pos, bl);
            }
            world.setBlockState(pos, (BlockState)state.with(TrapdoorBlock.POWERED, bl), Block.NOTIFY_LISTENERS);
            if (state.get(TrapdoorBlock.WATERLOGGED).booleanValue()) {
                world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            }
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
        Direction direction = ctx.getSide();
        blockState = ctx.canReplaceExisting() || !direction.getAxis().isHorizontal() ? (BlockState)((BlockState)blockState.with(FACING, ctx.getPlayerFacing().getOpposite())).with(TrapdoorBlock.HALF, direction == Direction.UP ? BlockHalf.BOTTOM : BlockHalf.TOP) : (BlockState)((BlockState)blockState.with(FACING, direction)).with(TrapdoorBlock.HALF, ctx.getHitPos().y - (double)ctx.getBlockPos().getY() > 0.5 ? BlockHalf.TOP : BlockHalf.BOTTOM);
        if (ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()) || ((WorldMixinInterface)ctx.getWorld()).isReceivingBluestonePower(ctx.getBlockPos())) {
            blockState = (BlockState)((BlockState)blockState.with(TrapdoorBlock.OPEN, true)).with(TrapdoorBlock.POWERED, true);
        }
        return (BlockState)blockState.with(TrapdoorBlock.WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    public void playToggleSound(@Nullable PlayerEntity player, World world, BlockPos pos, boolean open) {
        if (open) {
            int i = this.material == Material.METAL ? WorldEvents.IRON_TRAPDOOR_OPENS : WorldEvents.WOODEN_TRAPDOOR_OPENS;
            world.syncWorldEvent(player, i, pos, 0);
        } else {
            int i = this.material == Material.METAL ? WorldEvents.IRON_TRAPDOOR_CLOSES : WorldEvents.WOODEN_TRAPDOOR_CLOSES;
            world.syncWorldEvent(player, i, pos, 0);
        }
        world.emitGameEvent((Entity)player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }
}
