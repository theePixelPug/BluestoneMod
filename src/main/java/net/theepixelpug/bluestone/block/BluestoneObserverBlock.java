package net.theepixelpug.bluestone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import net.theepixelpug.bluestone.state.property.ModProperties;

public class BluestoneObserverBlock extends FacingBlock {
    public static final BooleanProperty BLUESTONE_POWERED;

    public BluestoneObserverBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.SOUTH)).with(BLUESTONE_POWERED, false));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING, BLUESTONE_POWERED});
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if ((Boolean)state.get(BLUESTONE_POWERED)) {
            world.setBlockState(pos, (BlockState)state.with(BLUESTONE_POWERED, false), 2);
        } else {
            world.setBlockState(pos, (BlockState)state.with(BLUESTONE_POWERED, true), 2);
            world.createAndScheduleBlockTick(pos, this, 2);
        }

        this.updateNeighbors(world, pos, state);
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(FACING) == direction && !(Boolean)state.get(BLUESTONE_POWERED)) {
            this.scheduleTick(world, pos);
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    private void scheduleTick(WorldAccess world, BlockPos pos) {
        if (!world.isClient() && !world.getBlockTickScheduler().isQueued(pos, this)) {
            world.createAndScheduleBlockTick(pos, this, 2);
        }

    }

    protected void updateNeighbors(World world, BlockPos pos, BlockState state) {
        Direction direction = (Direction)state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        world.updateNeighbor(blockPos, this, pos);
        world.updateNeighborsExcept(blockPos, this, direction);
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return ((AbstractBlockStateMixinInterface)state).getWeakBluestonePower(world, pos, direction);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (Boolean)state.get(BLUESTONE_POWERED) && state.get(FACING) == direction ? 15 : 0;
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!state.isOf(oldState.getBlock())) {
            if (!world.isClient() && (Boolean)state.get(BLUESTONE_POWERED) && !world.getBlockTickScheduler().isQueued(pos, this)) {
                BlockState blockState = (BlockState)state.with(BLUESTONE_POWERED, false);
                world.setBlockState(pos, blockState, 18);
                this.updateNeighbors(world, pos, blockState);
            }

        }
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            if (!world.isClient && (Boolean)state.get(BLUESTONE_POWERED) && world.getBlockTickScheduler().isQueued(pos, this)) {
                this.updateNeighbors(world, pos, (BlockState)state.with(BLUESTONE_POWERED, false));
            }

        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite().getOpposite());
    }

    static {
        BLUESTONE_POWERED = ModProperties.BLUESTONE_POWERED;
    }
}
