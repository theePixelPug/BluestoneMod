package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.RodBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockMixin extends RodBlock implements Waterloggable {
    public LightningRodBlockMixin(Settings settings) {
        super(settings);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(LightningRodBlock.POWERED) != false ? 15 : 0;
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(LightningRodBlock.POWERED).booleanValue() && state.get(FACING) == direction) {
            return 15;
        }
        return 0;
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }
}
