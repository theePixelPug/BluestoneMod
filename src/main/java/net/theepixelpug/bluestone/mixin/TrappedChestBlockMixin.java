package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;

@Mixin(TrappedChestBlock.class)
public abstract class TrappedChestBlockMixin extends ChestBlock {
    public TrappedChestBlockMixin(Settings settings, Supplier<BlockEntityType<? extends ChestBlockEntity>> supplier) {
        super(settings, supplier);
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return MathHelper.clamp(ChestBlockEntity.getPlayersLookingInChestCount(world, pos), 0, 15);
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (direction == Direction.UP) {
            return ((AbstractBlockStateMixinInterface)state).getWeakBluestonePower(world, pos, direction);
        }
        return 0;
    }
}
