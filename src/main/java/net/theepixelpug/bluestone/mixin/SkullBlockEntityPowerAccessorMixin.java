package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.entity.SkullBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SkullBlockEntity.class)
public interface SkullBlockEntityPowerAccessorMixin {
    @Accessor("powered")
    void setPowered(boolean powered);

    @Accessor("ticksPowered")
    void setTicksPowered(int ticksPowered);

    @Accessor
    int getTicksPowered();
}
