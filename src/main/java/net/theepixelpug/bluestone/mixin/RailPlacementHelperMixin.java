package net.theepixelpug.bluestone.mixin;

import net.minecraft.block.RailPlacementHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RailPlacementHelper.class)
public interface RailPlacementHelperMixin {
    @Invoker("getNeighborCount")
    int invokeGetNeighborCount();
}
