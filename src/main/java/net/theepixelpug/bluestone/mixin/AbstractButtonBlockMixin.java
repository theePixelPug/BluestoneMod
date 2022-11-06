package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractButtonBlock.class)
public abstract class AbstractButtonBlockMixin extends WallMountedBlock {
    public AbstractButtonBlockMixin(Settings settings) {
        super(settings);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(AbstractButtonBlock.POWERED) != false ? 15 : 0;
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(AbstractButtonBlock.POWERED).booleanValue() && AbstractButtonBlock.getDirection(state) == direction) {
            return 15;
        }
        return 0;
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }
}
