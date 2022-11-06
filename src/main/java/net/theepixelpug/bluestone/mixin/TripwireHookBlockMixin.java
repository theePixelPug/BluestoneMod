package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireHookBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TripwireHookBlock.class)
public abstract class TripwireHookBlockMixin extends Block {
    public TripwireHookBlockMixin(Settings settings) {
        super(settings);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(TripwireHookBlock.POWERED) != false ? 15 : 0;
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (!state.get(TripwireHookBlock.POWERED).booleanValue()) {
            return 0;
        }
        if (state.get(TripwireHookBlock.FACING) == direction) {
            return 15;
        }
        return 0;
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }
}
