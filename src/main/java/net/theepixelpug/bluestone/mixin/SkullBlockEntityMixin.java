package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.theepixelpug.bluestone.interfaces.WorldMixinInterface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SkullBlockEntity.class)
public abstract class SkullBlockEntityMixin extends BlockEntity {
    public SkullBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * @author theePixelPug
     * @reason Dragon Head must chomp with bluestone!
     */
    @Overwrite
    public static void tick(World world, BlockPos pos, BlockState state, SkullBlockEntity blockEntity) {
        if (world.isReceivingRedstonePower(pos) || ((WorldMixinInterface)world).isReceivingBluestonePower(pos)) {
            ((SkullBlockEntityPowerAccessorMixin)blockEntity).setPowered(true);
            int tp = (((SkullBlockEntityPowerAccessorMixin) blockEntity).getTicksPowered());
            ((SkullBlockEntityPowerAccessorMixin) blockEntity).setTicksPowered(++tp);
        } else {
            ((SkullBlockEntityPowerAccessorMixin)blockEntity).setPowered(false);
        }
    }
}
