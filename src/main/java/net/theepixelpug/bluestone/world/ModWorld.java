package net.theepixelpug.bluestone.world;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

public abstract class ModWorld implements WorldAccess, AutoCloseable {
    public int getReceivedStrongRedstonePower(BlockPos pos) {
        int i = 0;
        i = Math.max(i, this.getStrongRedstonePower(pos.down(), Direction.DOWN));
        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, this.getStrongRedstonePower(pos.up(), Direction.UP));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, this.getStrongRedstonePower(pos.north(), Direction.NORTH));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, this.getStrongRedstonePower(pos.south(), Direction.SOUTH));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, this.getStrongRedstonePower(pos.west(), Direction.WEST));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, this.getStrongRedstonePower(pos.east(), Direction.EAST));
                            return i >= 15 ? i : i;
                        }
                    }
                }
            }
        }
    }

    public boolean isEmittingBluestonePower(BlockPos pos, Direction direction) {
        return this.getEmittedBluestonePower(pos, direction) > 0;
    }

    public int getEmittedBluestonePower(BlockPos pos, Direction direction) {
        BlockState blockState = this.getBlockState(pos);
        int i = blockState.getWeakRedstonePower(this, pos, direction);
        return blockState.isSolidBlock(this, pos) ? Math.max(i, this.getReceivedStrongRedstonePower(pos)) : i;
    }
}
