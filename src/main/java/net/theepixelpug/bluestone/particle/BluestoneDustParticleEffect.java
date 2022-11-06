package net.theepixelpug.bluestone.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.*;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class BluestoneDustParticleEffect extends AbstractDustParticleEffect {
    public static final Vec3f BLUE = new Vec3f(Vec3d.unpackRgb(0x0000FF));
    public static final DustParticleEffect BLUESTONE;
    public static final Codec<DustParticleEffect> CODEC;
    public static final ParticleEffect.Factory<DustParticleEffect> PARAMETERS_FACTORY;

    public BluestoneDustParticleEffect(Vec3f vec3f, float f) {
        super(vec3f, f);
    }

    public ParticleType<DustParticleEffect> getType() {
        return ParticleTypes.DUST;
    }

    static {
        BLUESTONE = new DustParticleEffect(BLUE, 1.0F);
        CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Vec3f.CODEC.fieldOf("color").forGetter((effect) -> {
                return effect.getColor();
            }), Codec.FLOAT.fieldOf("scale").forGetter((effect) -> {
                return effect.getScale();
            })).apply(instance, DustParticleEffect::new);
        });
        PARAMETERS_FACTORY = new ParticleEffect.Factory<DustParticleEffect>() {
            public DustParticleEffect read(ParticleType<DustParticleEffect> particleType, StringReader stringReader) throws CommandSyntaxException {
                Vec3f vec3f = AbstractDustParticleEffect.readColor(stringReader);
                stringReader.expect(' ');
                float f = stringReader.readFloat();
                return new DustParticleEffect(vec3f, f);
            }

            public DustParticleEffect read(ParticleType<DustParticleEffect> particleType, PacketByteBuf packetByteBuf) {
                return new DustParticleEffect(AbstractDustParticleEffect.readColor(packetByteBuf), packetByteBuf.readFloat());
            }
        };
    }
}
