package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RailBlock.class)
public abstract class RailBlockMixin extends AbstractRailBlock {
    protected RailBlockMixin(boolean forbidCurves, Settings settings) {
        super(forbidCurves, settings);
    }

    @Override
    public void updateBlockState(BlockState state, World world, BlockPos pos, Block neighbor) {
        if ((neighbor.getDefaultState().emitsRedstonePower() || ((AbstractBlockStateMixinInterface)neighbor.getDefaultState()).emitsBluestonePower()) && ((RailPlacementHelperMixin)new RailPlacementHelper(world, pos, state)).invokeGetNeighborCount() == 3) {
            this.updateBlockState(world, pos, state, false);
        }
    }
}
