package net.theepixelpug.bluestone.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface AbstractBlockMixinInterface {
    @Deprecated
    default int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Deprecated
    default int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Deprecated
    default boolean emitsBluestonePower(BlockState state) {
        return false;
    }
}
