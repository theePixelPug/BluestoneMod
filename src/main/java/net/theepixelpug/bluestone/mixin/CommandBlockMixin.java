package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandBlock.class)
public abstract class CommandBlockMixin extends BlockWithEntity implements OperatorBlock {
    private final boolean auto;

    public CommandBlockMixin(AbstractBlock.Settings settings, boolean auto) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(CommandBlock.FACING, Direction.NORTH)).with(CommandBlock.CONDITIONAL, false));
        this.auto = auto;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isClient) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof CommandBlockBlockEntity)) {
            return;
        }
        CommandBlockBlockEntity commandBlockBlockEntity = (CommandBlockBlockEntity)blockEntity;
        boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        boolean bl2 = commandBlockBlockEntity.isPowered();
        commandBlockBlockEntity.setPowered(bl);
        if (bl2 || commandBlockBlockEntity.isAuto() || commandBlockBlockEntity.getCommandBlockType() == CommandBlockBlockEntity.Type.SEQUENCE) {
            return;
        }
        if (bl) {
            commandBlockBlockEntity.updateConditionMet();
            world.createAndScheduleBlockTick(pos, this, 1);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof CommandBlockBlockEntity)) {
            return;
        }
        CommandBlockBlockEntity commandBlockBlockEntity = (CommandBlockBlockEntity)blockEntity;
        CommandBlockExecutor commandBlockExecutor = commandBlockBlockEntity.getCommandExecutor();
        if (itemStack.hasCustomName()) {
            commandBlockExecutor.setCustomName(itemStack.getName());
        }
        if (!world.isClient) {
            if (BlockItem.getBlockEntityNbt(itemStack) == null) {
                commandBlockExecutor.setTrackOutput(world.getGameRules().getBoolean(GameRules.SEND_COMMAND_FEEDBACK));
                commandBlockBlockEntity.setAuto(this.auto);
            }
            if (commandBlockBlockEntity.getCommandBlockType() == CommandBlockBlockEntity.Type.SEQUENCE) {
                boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
                commandBlockBlockEntity.setPowered(bl);
            }
        }
    }
}
