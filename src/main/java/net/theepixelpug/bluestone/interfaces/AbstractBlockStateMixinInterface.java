package net.theepixelpug.bluestone.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public interface AbstractBlockStateMixinInterface {
    default boolean emitsBluestonePower() {
        return false;
    }

    public default int getWeakBluestonePower(BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    default int getStrongBluestonePower(BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }
}
