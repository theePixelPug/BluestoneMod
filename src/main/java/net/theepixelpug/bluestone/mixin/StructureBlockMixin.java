package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(StructureBlock.class)
public abstract class StructureBlockMixin extends BlockWithEntity implements OperatorBlock {
    protected StructureBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!(world instanceof ServerWorld)) {
            return;
        }
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (!(blockEntity instanceof StructureBlockBlockEntity)) {
            return;
        }
        StructureBlockBlockEntity structureBlockBlockEntity = (StructureBlockBlockEntity)blockEntity;
        boolean bl = world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos);
        boolean bl2 = structureBlockBlockEntity.isPowered();
        if (bl && !bl2) {
            structureBlockBlockEntity.setPowered(true);
            this.doAction((ServerWorld)world, structureBlockBlockEntity);
        } else if (!bl && bl2) {
            structureBlockBlockEntity.setPowered(false);
        }
    }

    private void doAction(ServerWorld world, StructureBlockBlockEntity blockEntity) {
        switch (blockEntity.getMode()) {
            case SAVE: {
                blockEntity.saveStructure(false);
                break;
            }
            case LOAD: {
                blockEntity.loadStructure(world, false);
                break;
            }
            case CORNER: {
                blockEntity.unloadStructure();
                break;
            }
            case DATA: {
                break;
            }
        }
    }
}
