package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractRailBlock.class)
public abstract class AbstractRailBlockMixin extends Block implements Waterloggable {
    public AbstractRailBlockMixin(Settings settings, boolean forbidCurves) {
        super(settings);
        this.forbidCurves = forbidCurves;
    }

    @Shadow
    private final boolean forbidCurves;

    @Shadow
    public abstract Property<RailShape> getShapeProperty();


    public BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClient) {
            return state;
        }
        RailShape railShape = state.get(this.getShapeProperty());
        return new RailPlacementHelper(world, pos, state).updateBlockState((world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos)), forceUpdate, railShape).getBlockState();
    }

    @Shadow
    protected BlockState updateCurves(BlockState state, World world, BlockPos pos, boolean notify) {
        state = this.updateBlockState(world, pos, state, true);
        if (this.forbidCurves) {
            world.updateNeighbor(state, pos, this, pos, notify);
        }
        return state;
    }

    @Shadow
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (oldState.isOf(state.getBlock())) {
            return;
        }
        this.updateCurves(state, world, pos, notify);
    }
}
