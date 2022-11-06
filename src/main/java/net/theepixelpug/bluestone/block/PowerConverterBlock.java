package net.theepixelpug.bluestone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.particle.PurpleDustParticleEffect;

public class PowerConverterBlock extends AbstractPowerGateBlock {

    public PowerConverterBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(RED_TO_BLUE_POWERED, false).with(BLUE_TO_RED_POWERED, false));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = super.getPlacementState(ctx);
        return blockState;
    }

    protected boolean isValidInput(BlockState state) {
        return isPowerGate(state);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(RED_TO_BLUE_POWERED) || state.get(BLUE_TO_RED_POWERED)) {
            Direction direction = state.get(FACING);
            double d = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double e = (double)pos.getY() + 0.4 + (random.nextDouble() - 0.5) * 0.2;
            double f = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            float g = -5.0F;
            if (random.nextBoolean()) {
                g = (float)(8 - 1);
            }

            g /= 16.0F;
            double h = g * (float)direction.getOffsetX();
            double i = g * (float)direction.getOffsetZ();
            world.addParticle(PurpleDustParticleEffect.DEFAULT, d + h, e, f + i, 0.0, 0.0, 0.0);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, RED_TO_BLUE_POWERED, BLUE_TO_RED_POWERED});
    }
}
