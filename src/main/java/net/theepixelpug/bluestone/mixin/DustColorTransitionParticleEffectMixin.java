package net.theepixelpug.bluestone.mixin;

import net.minecraft.particle.DustColorTransitionParticleEffect;
import net.minecraft.particle.DustParticleEffect;
import net.theepixelpug.bluestone.particle.BluestoneDustParticleEffect;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DustColorTransitionParticleEffect.class)
public abstract class DustColorTransitionParticleEffectMixin {
    private static final DustColorTransitionParticleEffect DEFAULT = new DustColorTransitionParticleEffect(DustColorTransitionParticleEffect.SCULK_BLUE, BluestoneDustParticleEffect.BLUE, 1.0f);
}
