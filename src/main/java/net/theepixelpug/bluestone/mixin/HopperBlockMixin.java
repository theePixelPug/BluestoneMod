package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin {
    private void updateEnabled(World world, BlockPos pos, BlockState state) {
        boolean bl;
        boolean bl2 = bl = !(world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos));
        if (bl != state.get(HopperBlock.ENABLED)) {
            world.setBlockState(pos, (BlockState)state.with(HopperBlock.ENABLED, bl), Block.NO_REDRAW);
        }
    }
}
