package net.theepixelpug.bluestone.interfaces;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public interface WorldViewMixinInterface {
    default int getStrongBluestonePower(BlockPos pos, Direction direction) {
        return 0;
    }
}
