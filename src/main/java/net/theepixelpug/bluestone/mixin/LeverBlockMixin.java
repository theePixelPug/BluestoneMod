package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.WallMountedBlock;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.theepixelpug.bluestone.particle.PurpleDustParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin extends WallMountedBlock {
    public LeverBlockMixin(Settings settings) {
        super(settings);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(LeverBlock.POWERED) != false ? 15 : 0;
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (state.get(LeverBlock.POWERED).booleanValue() && LeverBlock.getDirection(state) == direction) {
            return 15;
        }
        return 0;
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }

    /**
     * @author theePixelPug
     * @reason Purple particles go brrr
     */
    @Overwrite
    public static void spawnParticles(BlockState state, WorldAccess world, BlockPos pos, float alpha) {
        Direction direction = state.get(FACING).getOpposite();
        Direction direction2 = LeverBlock.getDirection(state).getOpposite();
        double d = (double)pos.getX() + 0.5 + 0.1 * (double)direction.getOffsetX() + 0.2 * (double)direction2.getOffsetX();
        double e = (double)pos.getY() + 0.5 + 0.1 * (double)direction.getOffsetY() + 0.2 * (double)direction2.getOffsetY();
        double f = (double)pos.getZ() + 0.5 + 0.1 * (double)direction.getOffsetZ() + 0.2 * (double)direction2.getOffsetZ();
        world.addParticle(new DustParticleEffect(PurpleDustParticleEffect.PURPLE, alpha), d, e, f, 0.0, 0.0, 0.0);
    }
}
