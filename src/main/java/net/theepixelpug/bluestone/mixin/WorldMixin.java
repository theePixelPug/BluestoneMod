package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.theepixelpug.bluestone.interfaces.AbstractBlockMixinInterface;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldViewMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable, WorldMixinInterface {

    @Override
    public int getReceivedStrongBluestonePower(BlockPos pos) {
        int i = 0;
        i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.down(), Direction.DOWN));
        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.up(), Direction.UP));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.north(), Direction.NORTH));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.south(), Direction.SOUTH));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.west(), Direction.WEST));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, ((WorldViewMixinInterface)this).getStrongBluestonePower(pos.east(), Direction.EAST));
                            return i >= 15 ? i : i;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isEmittingBluestonePower(BlockPos pos, Direction direction) {
        return this.getEmittedBluestonePower(pos, direction) > 0;
    }

    @Override
    public int getEmittedBluestonePower(BlockPos pos, Direction direction) {
        BlockState blockState = this.getBlockState(pos);
        int i = ((AbstractBlockStateMixinInterface)blockState).getWeakBluestonePower(this, pos, direction);
        return blockState.isSolidBlock(this, pos) ? Math.max(i, this.getReceivedStrongBluestonePower(pos)) : i;
    }

    @Override
    public boolean isReceivingBluestonePower(BlockPos pos) {
        if (this.getEmittedBluestonePower(pos.down(), Direction.DOWN) > 0) {
            return true;
        } else if (this.getEmittedBluestonePower(pos.up(), Direction.UP) > 0) {
            return true;
        } else if (this.getEmittedBluestonePower(pos.north(), Direction.NORTH) > 0) {
            return true;
        } else if (this.getEmittedBluestonePower(pos.south(), Direction.SOUTH) > 0) {
            return true;
        } else if (this.getEmittedBluestonePower(pos.west(), Direction.WEST) > 0) {
            return true;
        } else {
            return this.getEmittedBluestonePower(pos.east(), Direction.EAST) > 0;
        }
    }

    @Override
    public int getReceivedBluestonePower(BlockPos pos) {
        int i = 0;
        Direction[] var3 = DIRECTIONS;
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Direction direction = var3[var5];
            int j = this.getEmittedBluestonePower(pos.offset(direction), direction);
            if (j >= 15) {
                return 15;
            }

            if (j > i) {
                i = j;
            }
        }

        return i;
    }

    private static final Direction[] DIRECTIONS = Direction.values();
}
