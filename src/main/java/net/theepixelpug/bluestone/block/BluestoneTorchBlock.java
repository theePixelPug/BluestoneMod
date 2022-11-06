package net.theepixelpug.bluestone.block;

import com.google.common.collect.Lists;
import net.minecraft.block.*;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import net.theepixelpug.bluestone.particle.BluestoneDustParticleEffect;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class BluestoneTorchBlock extends TorchBlock {
    public static final BooleanProperty LIT;
    private static final Map<BlockView, List<BluestoneTorchBlock.BurnoutEntry>> BURNOUT_MAP;
    public static final int field_31227 = 60;
    public static final int field_31228 = 8;
    public static final int field_31229 = 160;
    private static final int SCHEDULED_TICK_DELAY = 2;

    public BluestoneTorchBlock(AbstractBlock.Settings settings) {
        super(settings, BluestoneDustParticleEffect.BLUESTONE);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(LIT, true));
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        Direction[] var6 = Direction.values();
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            world.updateNeighborsAlways(pos.offset(direction), this);
        }

    }

    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!moved) {
            Direction[] var6 = Direction.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                Direction direction = var6[var8];
                world.updateNeighborsAlways(pos.offset(direction), this);
            }

        }
    }

    public int getWeakBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return (Boolean)state.get(LIT) && Direction.UP != direction ? 15 : 0;
    }

    protected boolean shouldUnpower(World world, BlockPos pos, BlockState state) {
        return ((WorldMixinInterface)world).isEmittingBluestonePower(pos.down(), Direction.DOWN);
    }

    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean bl = this.shouldUnpower(world, pos, state);
        List<BluestoneTorchBlock.BurnoutEntry> list = (List)BURNOUT_MAP.get(world);

        while(list != null && !list.isEmpty() && world.getTime() - ((BluestoneTorchBlock.BurnoutEntry)list.get(0)).time > 60L) {
            list.remove(0);
        }

        if ((Boolean)state.get(LIT)) {
            if (bl) {
                world.setBlockState(pos, (BlockState)state.with(LIT, false), 3);
                if (isBurnedOut(world, pos, true)) {
                    world.syncWorldEvent(1502, pos, 0);
                    world.createAndScheduleBlockTick(pos, world.getBlockState(pos).getBlock(), 160);
                }
            }
        } else if (!bl && !isBurnedOut(world, pos, false)) {
            world.setBlockState(pos, (BlockState)state.with(LIT, true), 3);
        }

    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if ((Boolean)state.get(LIT) == this.shouldUnpower(world, pos, state) && !world.getBlockTickScheduler().isTicking(pos, this)) {
            world.createAndScheduleBlockTick(pos, this, 2);
        }

    }

    public int getStrongBluestonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return direction == Direction.DOWN ? ((AbstractBlockStateMixinInterface)state).getWeakBluestonePower(world, pos, direction) : 0;
    }

    public boolean emitsBluestonePower(BlockState state) {
        return true;
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if ((Boolean)state.get(LIT)) {
            double d = (double)pos.getX() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            double e = (double)pos.getY() + 0.7 + (random.nextDouble() - 0.5) * 0.2;
            double f = (double)pos.getZ() + 0.5 + (random.nextDouble() - 0.5) * 0.2;
            world.addParticle(this.particle, d, e, f, 0.0, 0.0, 0.0);
        }
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{LIT});
    }

    private static boolean isBurnedOut(World world, BlockPos pos, boolean addNew) {
        List<BluestoneTorchBlock.BurnoutEntry> list = (List)BURNOUT_MAP.computeIfAbsent(world, (worldx) -> {
            return Lists.newArrayList();
        });
        if (addNew) {
            list.add(new BluestoneTorchBlock.BurnoutEntry(pos.toImmutable(), world.getTime()));
        }

        int i = 0;

        for(int j = 0; j < list.size(); ++j) {
            BluestoneTorchBlock.BurnoutEntry burnoutEntry = (BluestoneTorchBlock.BurnoutEntry)list.get(j);
            if (burnoutEntry.pos.equals(pos)) {
                ++i;
                if (i >= 8) {
                    return true;
                }
            }
        }

        return false;
    }

    static {
        LIT = Properties.LIT;
        BURNOUT_MAP = new WeakHashMap();
    }

    public static class BurnoutEntry {
        final BlockPos pos;
        final long time;

        public BurnoutEntry(BlockPos pos, long time) {
            this.pos = pos;
            this.time = time;
        }
    }
}
