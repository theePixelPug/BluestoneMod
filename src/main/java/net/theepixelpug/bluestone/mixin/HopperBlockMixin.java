package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.HopperBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin extends BlockWithEntity {
    protected HopperBlockMixin(Settings settings) {
        super(settings);
    }

    public void updateEnabled(World world, BlockPos pos, BlockState state) {
        boolean bl = !(world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos));
        if (bl != state.get(HopperBlock.ENABLED)) {
            world.setBlockState(pos, state.with(HopperBlock.ENABLED, bl), Block.NO_REDRAW);
        }
    }

    @Shadow
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        this.updateEnabled(world, pos, state);
    }

    @Shadow
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        this.updateEnabled(world, pos, state);
    }
}
