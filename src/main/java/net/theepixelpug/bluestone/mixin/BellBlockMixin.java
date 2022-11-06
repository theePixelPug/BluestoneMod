package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BellBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BellBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Attachment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BellBlock.class)
public abstract class BellBlockMixin extends BlockWithEntity {
    protected BellBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        if (bl != state.get(BellBlock.POWERED)) {
            if (bl) {
                this.ring(world, pos, null);
            }
            world.setBlockState(pos, (BlockState)state.with(BellBlock.POWERED, bl), Block.NOTIFY_ALL);
        }
    }

    public boolean ring(World world, BlockState state, BlockHitResult hitResult, @Nullable PlayerEntity player, boolean checkHitPos) {
        boolean bl;
        Direction direction = hitResult.getSide();
        BlockPos blockPos = hitResult.getBlockPos();
        boolean bl2 = bl = !checkHitPos || this.isPointOnBell(state, direction, hitResult.getPos().y - (double)blockPos.getY());
        if (bl) {
            boolean bl22 = this.ring(player, world, blockPos, direction);
            if (bl22 && player != null) {
                player.incrementStat(Stats.BELL_RING);
            }
            return true;
        }
        return false;
    }

    private boolean isPointOnBell(BlockState state, Direction side, double y) {
        if (side.getAxis() == Direction.Axis.Y || y > (double)0.8124f) {
            return false;
        }
        Direction direction = state.get(BellBlock.FACING);
        Attachment attachment = state.get(BellBlock.ATTACHMENT);
        switch (attachment) {
            case FLOOR: {
                return direction.getAxis() == side.getAxis();
            }
            case SINGLE_WALL:
            case DOUBLE_WALL: {
                return direction.getAxis() != side.getAxis();
            }
            case CEILING: {
                return true;
            }
        }
        return false;
    }

    public boolean ring(World world, BlockPos pos, @Nullable Direction direction) {
        return this.ring(null, world, pos, direction);
    }

    public boolean ring(@Nullable Entity entity, World world, BlockPos pos, @Nullable Direction direction) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!world.isClient && blockEntity instanceof BellBlockEntity) {
            if (direction == null) {
                direction = world.getBlockState(pos).get(BellBlock.FACING);
            }
            ((BellBlockEntity)blockEntity).activate(direction);
            world.playSound(null, pos, SoundEvents.BLOCK_BELL_USE, SoundCategory.BLOCKS, 2.0f, 1.0f);
            world.emitGameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
            return true;
        }
        return false;
    }
}
