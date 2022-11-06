package net.theepixelpug.bluestone.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.TickPriority;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldViewMixinInterface;
import net.theepixelpug.bluestone.state.property.ModProperties;

public abstract class AbstractPowerGateBlock extends HorizontalFacingBlock {
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);
    public static final BooleanProperty RED_TO_BLUE_POWERED;
    public static final BooleanProperty BLUE_TO_RED_POWERED;

    protected AbstractPowerGateBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return hasTopRim(world, pos.down());
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean bl = state.get(RED_TO_BLUE_POWERED);
        boolean bl2 = state.get(BLUE_TO_RED_POWERED);
        boolean bl3 = this.hasRedstonePower(world, pos, state);
        boolean bl4 = this.hasBluestonePower(world, pos, state);
        if (bl && !bl3) {
            world.setBlockState(pos, state.with(RED_TO_BLUE_POWERED, false), 2);
        } else if (!bl && bl3) {
            world.setBlockState(pos, state.with(RED_TO_BLUE_POWERED, true), 2);
        } else if (bl2 && !bl4) {
            world.setBlockState(pos, state.with(BLUE_TO_RED_POWERED, false), 2);
        } else if (!bl2 && bl4) {
            world.setBlockState(pos, state.with(BLUE_TO_RED_POWERED, true), 2);
        } else if (!bl3 && !bl4) {
            world.createAndScheduleBlockTick(pos, this, 0, TickPriority.VERY_HIGH);
        }
    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return ((AbstractBlockStateMixinInterface)state).getWeakBluestonePower(world, pos, direction);
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (!(Boolean)state.get(RED_TO_BLUE_POWERED)) {
            return 0;
        } else {
            return state.get(FACING) == direction ? this.getOutputLevel(world, pos, state) : 0;
        }
    }

    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.getWeakRedstonePower(world, pos, direction);
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (!(Boolean)state.get(BLUE_TO_RED_POWERED)) {
            return 0;
        } else {
            return state.get(FACING) == direction ? this.getOutputLevel(world, pos, state) : 0;
        }
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (state.canPlaceAt(world, pos)) {
            this.updatePowered(world, pos, state);
        } else {
            BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
            dropStacks(state, world, pos, blockEntity);
            world.removeBlock(pos, false);
            Direction[] var8 = Direction.values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                Direction direction = var8[var10];
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

        }
    }

    protected void updatePowered(World world, BlockPos pos, BlockState state) {
        boolean bl = state.get(RED_TO_BLUE_POWERED) || state.get(BLUE_TO_RED_POWERED);
        boolean bl2 = this.hasPower(world, pos, state);
        if (bl != bl2 && !world.getBlockTickScheduler().isTicking(pos, this)) {
            TickPriority tickPriority = TickPriority.HIGH;
            if (this.isTargetNotAligned(world, pos, state)) {
                tickPriority = TickPriority.EXTREMELY_HIGH;
            } else if (bl) {
                tickPriority = TickPriority.VERY_HIGH;
            }

            world.createAndScheduleBlockTick(pos, this, 0, tickPriority);
        }

    }

    protected boolean hasPower(World world, BlockPos pos, BlockState state) {
        return (this.getRedstonePower(world, pos, state) + this.getBluestonePower(world, pos, state)) > 0;
    }

    protected boolean hasRedstonePower(World world, BlockPos pos, BlockState state) {
        return this.getRedstonePower(world, pos, state) > 0;
    }

    protected int getRedstonePower(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        int i = world.getEmittedRedstonePower(blockPos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockState = world.getBlockState(blockPos);
            return Math.max(i, blockState.isOf(Blocks.REDSTONE_WIRE) ? blockState.get(RedstoneWireBlock.POWER) : 0);
        }
    }

    protected boolean hasBluestonePower(World world, BlockPos pos, BlockState state) {
        return this.getBluestonePower(world, pos, state) > 0;
    }

    protected int getBluestonePower(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction);
        int i = ((WorldMixinInterface)world).getEmittedBluestonePower(blockPos, direction);
        if (i >= 15) {
            return i;
        } else {
            BlockState blockState = world.getBlockState(blockPos);
            return Math.max(i, blockState.isOf(ModBlocks.BLUESTONE_WIRE) ? blockState.get(BluestoneWireBlock.BLUESTONE_POWER) : 0);
        }
    }

    protected int getMaxRedstoneInputLevelSides(WorldView world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        Direction direction2 = direction.rotateYClockwise();
        Direction direction3 = direction.rotateYCounterclockwise();
        return Math.max(this.getRedstoneInputLevel(world, pos.offset(direction2), direction2), this.getRedstoneInputLevel(world, pos.offset(direction3), direction3));
    }

    protected int getRedstoneInputLevel(WorldView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos);
        if (this.isValidInput(blockState)) {
            if (blockState.isOf(Blocks.REDSTONE_BLOCK)) {
                return 15;
            } else {
                return blockState.isOf(Blocks.REDSTONE_WIRE) ? blockState.get(RedstoneWireBlock.POWER) : world.getStrongRedstonePower(pos, dir);
            }
        } else {
            return 0;
        }
    }

    protected int getMaxBluestoneInputLevelSides(WorldView world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        Direction direction2 = direction.rotateYClockwise();
        Direction direction3 = direction.rotateYCounterclockwise();
        return Math.max(this.getBluestoneInputLevel(world, pos.offset(direction2), direction2), this.getBluestoneInputLevel(world, pos.offset(direction3), direction3));
    }

    protected int getBluestoneInputLevel(WorldView world, BlockPos pos, Direction dir) {
        BlockState blockState = world.getBlockState(pos);
        if (this.isValidInput(blockState)) {
            if (blockState.isOf(ModBlocks.BLUESTONE_BLOCK)) {
                return 15;
            } else {
                return blockState.isOf(ModBlocks.BLUESTONE_WIRE) ? blockState.get(BluestoneWireBlock.BLUESTONE_POWER) : ((WorldViewMixinInterface)world).getStrongBluestonePower(pos, dir);
            }
        } else {
            return 0;
        }
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }

    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (this.hasPower(world, pos, state)) {
            world.createAndScheduleBlockTick(pos, this, 1);
        }

    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        this.updateTarget(world, pos, state);
    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved && !state.isOf(newState.getBlock())) {
            super.onStateReplaced(state, world, pos, newState, moved);
            this.updateTarget(world, pos, state);
        }
    }

    protected void updateTarget(World world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        world.updateNeighbor(blockPos, this, pos);
        world.updateNeighborsExcept(blockPos, this, direction);
    }

    protected boolean isValidInput(BlockState state) {
        return ((AbstractBlockStateMixinInterface)state).emitsBluestonePower() || state.emitsRedstonePower();
    }

    protected int getOutputLevel(BlockView world, BlockPos pos, BlockState state) {
        return 15;
    }

    public static boolean isPowerGate(BlockState state) {
        return state.getBlock() instanceof AbstractPowerGateBlock;
    }

    public boolean isTargetNotAligned(BlockView world, BlockPos pos, BlockState state) {
        Direction direction = state.get(FACING).getOpposite();
        BlockState blockState = world.getBlockState(pos.offset(direction));
        return isPowerGate(blockState) && blockState.get(FACING) != direction;
    }

    static {
        RED_TO_BLUE_POWERED = ModProperties.RED_TO_BLUE_POWERED;
        BLUE_TO_RED_POWERED = ModProperties.BLUE_TO_RED_POWERED;
    }
}
