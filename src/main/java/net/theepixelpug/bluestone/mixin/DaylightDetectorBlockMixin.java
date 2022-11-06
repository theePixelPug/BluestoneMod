package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.DaylightDetectorBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DaylightDetectorBlock.class)
public abstract class DaylightDetectorBlockMixin extends BlockWithEntity {
    protected DaylightDetectorBlockMixin(Settings settings) {
        super(settings);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(DaylightDetectorBlock.POWER);
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }
}
