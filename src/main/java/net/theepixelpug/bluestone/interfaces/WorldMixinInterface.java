package net.theepixelpug.bluestone.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface WorldMixinInterface {
    default int getReceivedStrongBluestonePower(BlockPos pos) {
        return 0;
    }

    default boolean isEmittingBluestonePower(BlockPos pos, Direction direction) {
        return false;
    }

    default int getEmittedBluestonePower(BlockPos pos, Direction direction) {
        return 0;
    }

    default boolean isReceivingBluestonePower(BlockPos pos) {
        return false;
    }

    default int getReceivedBluestonePower(BlockPos pos) {
        return 0;
    }
}
