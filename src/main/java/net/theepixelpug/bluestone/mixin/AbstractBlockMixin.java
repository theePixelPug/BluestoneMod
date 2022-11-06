package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.theepixelpug.bluestone.interfaces.AbstractBlockMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin implements AbstractBlockMixinInterface {
    @Override
    @Deprecated
    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    @Deprecated
    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return 0;
    }

    @Override
    @Deprecated
    public boolean emitsBluestonePower(BlockState state) {
        return false;
    }
}
