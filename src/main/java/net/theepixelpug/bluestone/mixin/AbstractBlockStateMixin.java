package net.theepixelpug.bluestone.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.theepixelpug.bluestone.interfaces.AbstractBlockMixinInterface;
import net.theepixelpug.bluestone.interfaces.AbstractBlockStateMixinInterface;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin extends State<Block, BlockState> implements AbstractBlockStateMixinInterface {
    protected AbstractBlockStateMixin(Block owner, ImmutableMap<Property<?>, Comparable<?>> entries, MapCodec<BlockState> codec) {
        super(owner, entries, codec);
    }

    public Block getBlock() {
        return (Block)this.owner;
    }

    public abstract BlockState asBlockState();

    @Override
    public boolean emitsBluestonePower() {
        return ((AbstractBlockMixinInterface)this.getBlock()).emitsBluestonePower(this.asBlockState());
    }

    @Override
    public int getWeakBluestonePower(BlockView world, BlockPos pos, Direction direction) {
        return ((AbstractBlockMixinInterface)this.getBlock()).getWeakBluestonePower(this.asBlockState(), world, pos, direction);
    }

    @Override
    public int getStrongBluestonePower(BlockView world, BlockPos pos, Direction direction) {
        return ((AbstractBlockMixinInterface)this.getBlock()).getStrongBluestonePower(this.asBlockState(), world, pos, direction);
    }
}
