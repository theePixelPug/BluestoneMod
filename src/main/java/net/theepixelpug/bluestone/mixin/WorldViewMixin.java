package net.theepixelpug.bluestone.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.CollisionView;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.source.BiomeAccess;
import net.theepixelpug.bluestone.interfaces.AbstractBlockMixinInterface;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldViewMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldView.class)
public interface WorldViewMixin extends BiomeAccess.Storage, CollisionView, BlockRenderView, WorldViewMixinInterface {
    @Override
    default int getStrongBluestonePower(BlockPos pos, Direction direction) {
        return ((AbstractBlockStateMixinInterface)this.getBlockState(pos)).getStrongBluestonePower(this, pos, direction);
    }
}
