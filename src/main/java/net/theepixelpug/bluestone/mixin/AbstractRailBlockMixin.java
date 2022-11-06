package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.block.enums.RailShape;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractRailBlock.class)
public abstract class AbstractRailBlockMixin extends Block implements Waterloggable {
    public AbstractRailBlockMixin(Settings settings) {
        super(settings);
    }

    public BlockState updateBlockState(World world, BlockPos pos, BlockState state, boolean forceUpdate) {
        if (world.isClient) {
            return state;
        }
        RailShape railShape = state.get(this.getShapeProperty());
        return new RailPlacementHelper(world, pos, state).updateBlockState((world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos)), forceUpdate, railShape).getBlockState();
    }

    public abstract Property<RailShape> getShapeProperty();
}
