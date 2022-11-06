package net.theepixelpug.bluestone.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import net.theepixelpug.bluestone.state.property.ModProperties;
import org.jetbrains.annotations.Nullable;

public class BluestoneNoteBlock extends Block {
    public static final EnumProperty<Instrument> INSTRUMENT;
    public static final BooleanProperty BLUESTONE_POWERED;
    public static final IntProperty NOTE;

    public BluestoneNoteBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(INSTRUMENT, Instrument.HARP)).with(NOTE, 0)).with(BLUESTONE_POWERED, false));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(INSTRUMENT, Instrument.fromBlockState(ctx.getWorld().getBlockState(ctx.getBlockPos().down())));
    }

    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        return direction == Direction.DOWN ? (BlockState)state.with(INSTRUMENT, Instrument.fromBlockState(neighborState)) : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        if (bl != (Boolean)state.get(BLUESTONE_POWERED)) {
            if (bl) {
                this.playNote((Entity)null, world, pos);
            }

            world.setBlockState(pos, (BlockState)state.with(BLUESTONE_POWERED, bl), 3);
        }

    }

    private void playNote(@Nullable Entity entity, World world, BlockPos pos) {
        if (world.getBlockState(pos.up()).isAir()) {
            world.addSyncedBlockEvent(pos, this, 0, 0);
            world.emitGameEvent(entity, GameEvent.NOTE_BLOCK_PLAY, pos);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            state = (BlockState)state.cycle(NOTE);
            world.setBlockState(pos, state, 3);
            this.playNote(player, world, pos);
            player.incrementStat(Stats.TUNE_NOTEBLOCK);
            return ActionResult.CONSUME;
        }
    }

    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            this.playNote(player, world, pos);
            player.incrementStat(Stats.PLAY_NOTEBLOCK);
        }
    }

    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        int i = (Integer)state.get(NOTE);
        float f = (float)Math.pow(2.0, (double)(i - 12) / 12.0);
        world.playSound((PlayerEntity)null, pos, ((Instrument)state.get(INSTRUMENT)).getSound(), SoundCategory.RECORDS, 3.0F, f);
        world.addParticle(ParticleTypes.NOTE, (double)pos.getX() + 0.5, (double)pos.getY() + 1.2, (double)pos.getZ() + 0.5, (double)i / 24.0, 0.0, 0.0);
        return true;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{INSTRUMENT, BLUESTONE_POWERED, NOTE});
    }

    static {
        INSTRUMENT = Properties.INSTRUMENT;
        BLUESTONE_POWERED = ModProperties.BLUESTONE_POWERED;
        NOTE = Properties.NOTE;
    }
}
