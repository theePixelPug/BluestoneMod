package net.theepixelpug.bluestone.block.dispenser;

import com.mojang.logging.LogUtils;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.*;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.theepixelpug.bluestone.block.BluestoneDispenserBlock;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public interface BluestoneDispenserBehavior {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final net.minecraft.block.dispenser.DispenserBehavior NOOP = (pointer, stack) -> stack;

    public ItemStack dispense(BlockPointer var1, ItemStack var2);

    public static void registerDefaults() {
        BluestoneDispenserBlock.registerBehavior(Items.ARROW, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ArrowEntity arrowEntity = new ArrowEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.TIPPED_ARROW, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                ArrowEntity arrowEntity = new ArrowEntity(world, position.getX(), position.getY(), position.getZ());
                arrowEntity.initFromStack(stack);
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return arrowEntity;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.SPECTRAL_ARROW, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                SpectralArrowEntity persistentProjectileEntity = new SpectralArrowEntity(world, position.getX(), position.getY(), position.getZ());
                persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
                return persistentProjectileEntity;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.EGG, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new EggEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.SNOWBALL, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new SnowballEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.EXPERIENCE_BOTTLE, new ProjectileDispenserBehavior(){

            @Override
            protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                return Util.make(new ExperienceBottleEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
            }

            @Override
            protected float getVariation() {
                return super.getVariation() * 0.5f;
            }

            @Override
            protected float getForce() {
                return super.getForce() * 1.25f;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.SPLASH_POTION, new net.minecraft.block.dispenser.DispenserBehavior(){

            @Override
            public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
                return new ProjectileDispenserBehavior(){

                    @Override
                    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                        return Util.make(new PotionEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
                    }

                    @Override
                    protected float getVariation() {
                        return super.getVariation() * 0.5f;
                    }

                    @Override
                    protected float getForce() {
                        return super.getForce() * 1.25f;
                    }
                }.dispense(blockPointer, itemStack);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.LINGERING_POTION, new net.minecraft.block.dispenser.DispenserBehavior(){

            @Override
            public ItemStack dispense(BlockPointer blockPointer, ItemStack itemStack) {
                return new ProjectileDispenserBehavior(){

                    @Override
                    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
                        return Util.make(new PotionEntity(world, position.getX(), position.getY(), position.getZ()), entity -> entity.setItem(stack));
                    }

                    @Override
                    protected float getVariation() {
                        return super.getVariation() * 0.5f;
                    }

                    @Override
                    protected float getForce() {
                        return super.getForce() * 1.25f;
                    }
                }.dispense(blockPointer, itemStack);
            }
        });
        ItemDispenserBehavior itemDispenserBehavior = new ItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem)stack.getItem()).getEntityType(stack.getNbt());
                try {
                    entityType.spawnFromItemStack(pointer.getWorld(), stack, null, pointer.getPos().offset(direction), SpawnReason.DISPENSER, direction != Direction.UP, false);
                }
                catch (Exception exception) {
                    LOGGER.error("Error while dispensing spawn egg from dispenser at {}", (Object)pointer.getPos(), (Object)exception);
                    return ItemStack.EMPTY;
                }
                stack.decrement(1);
                pointer.getWorld().emitGameEvent(null, GameEvent.ENTITY_PLACE, pointer.getPos());
                return stack;
            }
        };
        for (SpawnEggItem spawnEggItem : SpawnEggItem.getAll()) {
            BluestoneDispenserBlock.registerBehavior(spawnEggItem, itemDispenserBehavior);
        }
        BluestoneDispenserBlock.registerBehavior(Items.ARMOR_STAND, new ItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                BlockPos blockPos = pointer.getPos().offset(direction);
                ServerWorld world = pointer.getWorld();
                ArmorStandEntity armorStandEntity = new ArmorStandEntity(world, (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5);
                EntityType.loadFromEntityNbt(world, null, armorStandEntity, stack.getNbt());
                armorStandEntity.setYaw(direction.asRotation());
                world.spawnEntity(armorStandEntity);
                stack.decrement(1);
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.SADDLE, new FallibleItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                List<LivingEntity> list = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(blockPos), entity -> {
                    if (entity instanceof Saddleable) {
                        Saddleable saddleable = (Saddleable)((Object)entity);
                        return !saddleable.isSaddled() && saddleable.canBeSaddled();
                    }
                    return false;
                });
                if (!list.isEmpty()) {
                    ((Saddleable)((Object)list.get(0))).saddle(SoundCategory.BLOCKS);
                    stack.decrement(1);
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        FallibleItemDispenserBehavior itemDispenserBehavior2 = new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                List<AbstractHorseEntity> list = pointer.getWorld().getEntitiesByClass(AbstractHorseEntity.class, new Box(blockPos), entity -> entity.isAlive() && entity.hasArmorSlot());
                for (AbstractHorseEntity abstractHorseEntity : list) {
                    if (!abstractHorseEntity.isHorseArmor(stack) || abstractHorseEntity.hasArmorInSlot() || !abstractHorseEntity.isTame()) continue;
                    abstractHorseEntity.getStackReference(401).set(stack.split(1));
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(pointer, stack);
            }
        };
        BluestoneDispenserBlock.registerBehavior(Items.LEATHER_HORSE_ARMOR, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.IRON_HORSE_ARMOR, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.GOLDEN_HORSE_ARMOR, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.DIAMOND_HORSE_ARMOR, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.WHITE_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.ORANGE_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.CYAN_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.BLUE_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.BROWN_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.BLACK_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.GRAY_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.GREEN_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.LIGHT_BLUE_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.LIGHT_GRAY_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.LIME_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.MAGENTA_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.PINK_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.PURPLE_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.RED_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.YELLOW_CARPET, itemDispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.CHEST, new FallibleItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                List<AbstractDonkeyEntity> list = pointer.getWorld().getEntitiesByClass(AbstractDonkeyEntity.class, new Box(blockPos), entity -> entity.isAlive() && !entity.hasChest());
                for (AbstractDonkeyEntity abstractDonkeyEntity : list) {
                    if (!abstractDonkeyEntity.isTame() || !abstractDonkeyEntity.getStackReference(499).set(stack)) continue;
                    stack.decrement(1);
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.FIREWORK_ROCKET, new ItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity((World)pointer.getWorld(), stack, pointer.getX(), pointer.getY(), pointer.getX(), true);
                net.minecraft.block.dispenser.DispenserBehavior.setEntityPosition(pointer, fireworkRocketEntity, direction);
                fireworkRocketEntity.setVelocity(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ(), 0.5f, 1.0f);
                pointer.getWorld().spawnEntity(fireworkRocketEntity);
                stack.decrement(1);
                return stack;
            }

            @Override
            protected void playSound(BlockPointer pointer) {
                pointer.getWorld().syncWorldEvent(WorldEvents.FIREWORK_ROCKET_SHOOTS, pointer.getPos(), 0);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.FIRE_CHARGE, new ItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                Position position = BluestoneDispenserBlock.getOutputLocation(pointer);
                double d = position.getX() + (double)((float)direction.getOffsetX() * 0.3f);
                double e = position.getY() + (double)((float)direction.getOffsetY() * 0.3f);
                double f = position.getZ() + (double)((float)direction.getOffsetZ() * 0.3f);
                ServerWorld world = pointer.getWorld();
                Random random = world.random;
                double g = random.nextTriangular(direction.getOffsetX(), 0.11485000000000001);
                double h = random.nextTriangular(direction.getOffsetY(), 0.11485000000000001);
                double i = random.nextTriangular(direction.getOffsetZ(), 0.11485000000000001);
                SmallFireballEntity smallFireballEntity = new SmallFireballEntity(world, d, e, f, g, h, i);
                world.spawnEntity(Util.make(smallFireballEntity, entity -> entity.setItem(stack)));
                stack.decrement(1);
                return stack;
            }

            @Override
            protected void playSound(BlockPointer pointer) {
                pointer.getWorld().syncWorldEvent(WorldEvents.BLAZE_SHOOTS, pointer.getPos(), 0);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.OAK_BOAT, new BoatDispenserBehavior(BoatEntity.Type.OAK));
        BluestoneDispenserBlock.registerBehavior(Items.SPRUCE_BOAT, new BoatDispenserBehavior(BoatEntity.Type.SPRUCE));
        BluestoneDispenserBlock.registerBehavior(Items.BIRCH_BOAT, new BoatDispenserBehavior(BoatEntity.Type.BIRCH));
        BluestoneDispenserBlock.registerBehavior(Items.JUNGLE_BOAT, new BoatDispenserBehavior(BoatEntity.Type.JUNGLE));
        BluestoneDispenserBlock.registerBehavior(Items.DARK_OAK_BOAT, new BoatDispenserBehavior(BoatEntity.Type.DARK_OAK));
        BluestoneDispenserBlock.registerBehavior(Items.ACACIA_BOAT, new BoatDispenserBehavior(BoatEntity.Type.ACACIA));
        BluestoneDispenserBlock.registerBehavior(Items.MANGROVE_BOAT, new BoatDispenserBehavior(BoatEntity.Type.MANGROVE));
        BluestoneDispenserBlock.registerBehavior(Items.OAK_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.OAK, true));
        BluestoneDispenserBlock.registerBehavior(Items.SPRUCE_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.SPRUCE, true));
        BluestoneDispenserBlock.registerBehavior(Items.BIRCH_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.BIRCH, true));
        BluestoneDispenserBlock.registerBehavior(Items.JUNGLE_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.JUNGLE, true));
        BluestoneDispenserBlock.registerBehavior(Items.DARK_OAK_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.DARK_OAK, true));
        BluestoneDispenserBlock.registerBehavior(Items.ACACIA_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.ACACIA, true));
        BluestoneDispenserBlock.registerBehavior(Items.MANGROVE_CHEST_BOAT, new BoatDispenserBehavior(BoatEntity.Type.MANGROVE, true));
        ItemDispenserBehavior dispenserBehavior = new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                FluidModificationItem fluidModificationItem = (FluidModificationItem)((Object)stack.getItem());
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                ServerWorld world = pointer.getWorld();
                if (fluidModificationItem.placeFluid(null, world, blockPos, null)) {
                    fluidModificationItem.onEmptied(null, world, stack, blockPos);
                    return new ItemStack(Items.BUCKET);
                }
                return this.fallbackBehavior.dispense(pointer, stack);
            }
        };
        BluestoneDispenserBlock.registerBehavior(Items.LAVA_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.WATER_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.POWDER_SNOW_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.SALMON_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.COD_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.PUFFERFISH_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.TROPICAL_FISH_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.AXOLOTL_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.TADPOLE_BUCKET, dispenserBehavior);
        BluestoneDispenserBlock.registerBehavior(Items.BUCKET, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ItemStack itemStack;
                BlockPos blockPos;
                ServerWorld worldAccess = pointer.getWorld();
                BlockState blockState = worldAccess.getBlockState(blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING)));
                Block block = blockState.getBlock();
                if (block instanceof FluidDrainable) {
                    itemStack = ((FluidDrainable)((Object)block)).tryDrainFluid(worldAccess, blockPos, blockState);
                    if (itemStack.isEmpty()) {
                        return super.dispenseSilently(pointer, stack);
                    }
                } else {
                    return super.dispenseSilently(pointer, stack);
                }
                worldAccess.emitGameEvent(null, GameEvent.FLUID_PICKUP, blockPos);
                Item item = itemStack.getItem();
                stack.decrement(1);
                if (stack.isEmpty()) {
                    return new ItemStack(item);
                }
                if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(new ItemStack(item)) < 0) {
                    this.fallbackBehavior.dispense(pointer, new ItemStack(item));
                }
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.FLINT_AND_STEEL, new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld world = pointer.getWorld();
                this.setSuccess(true);
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                BlockPos blockPos = pointer.getPos().offset(direction);
                BlockState blockState = world.getBlockState(blockPos);
                if (AbstractFireBlock.canPlaceAt(world, blockPos, direction)) {
                    world.setBlockState(blockPos, AbstractFireBlock.getState(world, blockPos));
                    world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                } else if (CampfireBlock.canBeLit(blockState) || CandleBlock.canBeLit(blockState) || CandleCakeBlock.canBeLit(blockState)) {
                    world.setBlockState(blockPos, (BlockState)blockState.with(Properties.LIT, true));
                    world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, blockPos);
                } else if (blockState.getBlock() instanceof TntBlock) {
                    TntBlock.primeTnt(world, blockPos);
                    world.removeBlock(blockPos, false);
                } else {
                    this.setSuccess(false);
                }
                if (this.isSuccess() && stack.damage(1, world.random, null)) {
                    stack.setCount(0);
                }
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.BONE_MEAL, new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(true);
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                if (BoneMealItem.useOnFertilizable(stack, world, blockPos) || BoneMealItem.useOnGround(stack, world, blockPos, null)) {
                    if (!world.isClient) {
                        world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                    }
                } else {
                    this.setSuccess(false);
                }
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Blocks.TNT, new ItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                TntEntity tntEntity = new TntEntity(world, (double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5, null);
                world.spawnEntity(tntEntity);
                world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
                world.emitGameEvent(null, GameEvent.ENTITY_PLACE, blockPos);
                stack.decrement(1);
                return stack;
            }
        });
        FallibleItemDispenserBehavior dispenserBehavior2 = new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
                return stack;
            }
        };
        BluestoneDispenserBlock.registerBehavior(Items.CREEPER_HEAD, dispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.ZOMBIE_HEAD, dispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.DRAGON_HEAD, dispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.SKELETON_SKULL, dispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.PLAYER_HEAD, dispenserBehavior2);
        BluestoneDispenserBlock.registerBehavior(Items.WITHER_SKELETON_SKULL, new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld world = pointer.getWorld();
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                BlockPos blockPos = pointer.getPos().offset(direction);
                if (world.isAir(blockPos) && WitherSkullBlock.canDispense(world, blockPos, stack)) {
                    world.setBlockState(blockPos, (BlockState)Blocks.WITHER_SKELETON_SKULL.getDefaultState().with(SkullBlock.ROTATION, direction.getAxis() == Direction.Axis.Y ? 0 : direction.getOpposite().getHorizontal() * 4), Block.NOTIFY_ALL);
                    world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    BlockEntity blockEntity = world.getBlockEntity(blockPos);
                    if (blockEntity instanceof SkullBlockEntity) {
                        WitherSkullBlock.onPlaced(world, blockPos, (SkullBlockEntity)blockEntity);
                    }
                    stack.decrement(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
                }
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Blocks.CARVED_PUMPKIN, new FallibleItemDispenserBehavior(){

            @Override
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                ServerWorld world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                CarvedPumpkinBlock carvedPumpkinBlock = (CarvedPumpkinBlock)Blocks.CARVED_PUMPKIN;
                if (world.isAir(blockPos) && carvedPumpkinBlock.canDispense(world, blockPos)) {
                    if (!world.isClient) {
                        world.setBlockState(blockPos, carvedPumpkinBlock.getDefaultState(), Block.NOTIFY_ALL);
                        world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    }
                    stack.decrement(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
                }
                return stack;
            }
        });
        BluestoneDispenserBlock.registerBehavior(Blocks.SHULKER_BOX.asItem(), new BlockPlacementDispenserBehavior());
        for (DyeColor dyeColor : DyeColor.values()) {
            BluestoneDispenserBlock.registerBehavior(ShulkerBoxBlock.get(dyeColor).asItem(), new BlockPlacementDispenserBehavior());
        }
        BluestoneDispenserBlock.registerBehavior(Items.GLASS_BOTTLE.asItem(), new FallibleItemDispenserBehavior(){
            private final ItemDispenserBehavior fallbackBehavior = new ItemDispenserBehavior();

            private ItemStack tryPutFilledBottle(BlockPointer pointer, ItemStack emptyBottleStack, ItemStack filledBottleStack) {
                emptyBottleStack.decrement(1);
                if (emptyBottleStack.isEmpty()) {
                    pointer.getWorld().emitGameEvent(null, GameEvent.FLUID_PICKUP, pointer.getPos());
                    return filledBottleStack.copy();
                }
                if (((DispenserBlockEntity)pointer.getBlockEntity()).addToFirstFreeSlot(filledBottleStack.copy()) < 0) {
                    this.fallbackBehavior.dispense(pointer, filledBottleStack.copy());
                }
                return emptyBottleStack;
            }

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                this.setSuccess(false);
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                BlockState blockState = serverWorld.getBlockState(blockPos);
                if (blockState.isIn(BlockTags.BEEHIVES, state -> state.contains(BeehiveBlock.HONEY_LEVEL) && state.getBlock() instanceof BeehiveBlock) && blockState.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    ((BeehiveBlock)blockState.getBlock()).takeHoney(serverWorld, blockState, blockPos, null, BeehiveBlockEntity.BeeState.BEE_RELEASED);
                    this.setSuccess(true);
                    return this.tryPutFilledBottle(pointer, stack, new ItemStack(Items.HONEY_BOTTLE));
                }
                if (serverWorld.getFluidState(blockPos).isIn(FluidTags.WATER)) {
                    this.setSuccess(true);
                    return this.tryPutFilledBottle(pointer, stack, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER));
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.GLOWSTONE, new FallibleItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                Direction direction = pointer.getBlockState().get(BluestoneDispenserBlock.FACING);
                BlockPos blockPos = pointer.getPos().offset(direction);
                ServerWorld world = pointer.getWorld();
                BlockState blockState = world.getBlockState(blockPos);
                this.setSuccess(true);
                if (blockState.isOf(Blocks.RESPAWN_ANCHOR)) {
                    if (blockState.get(RespawnAnchorBlock.CHARGES) != 4) {
                        RespawnAnchorBlock.charge(world, blockPos, blockState);
                        stack.decrement(1);
                    } else {
                        this.setSuccess(false);
                    }
                    return stack;
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.SHEARS.asItem(), new ShearsDispenserBehavior());
        BluestoneDispenserBlock.registerBehavior(Items.HONEYCOMB, new FallibleItemDispenserBehavior(){

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                ServerWorld world = pointer.getWorld();
                BlockState blockState = world.getBlockState(blockPos);
                Optional<BlockState> optional = HoneycombItem.getWaxedState(blockState);
                if (optional.isPresent()) {
                    world.setBlockState(blockPos, optional.get());
                    world.syncWorldEvent(WorldEvents.BLOCK_WAXED, blockPos, 0);
                    stack.decrement(1);
                    this.setSuccess(true);
                    return stack;
                }
                return super.dispenseSilently(pointer, stack);
            }
        });
        BluestoneDispenserBlock.registerBehavior(Items.POTION, new ItemDispenserBehavior(){
            private final ItemDispenserBehavior fallback = new ItemDispenserBehavior();

            @Override
            public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                if (PotionUtil.getPotion(stack) != Potions.WATER) {
                    return this.fallback.dispense(pointer, stack);
                }
                ServerWorld serverWorld = pointer.getWorld();
                BlockPos blockPos = pointer.getPos();
                BlockPos blockPos2 = pointer.getPos().offset(pointer.getBlockState().get(BluestoneDispenserBlock.FACING));
                if (serverWorld.getBlockState(blockPos2).isIn(BlockTags.CONVERTABLE_TO_MUD)) {
                    if (!serverWorld.isClient) {
                        for (int i = 0; i < 5; ++i) {
                            serverWorld.spawnParticles(ParticleTypes.SPLASH, (double)blockPos.getX() + serverWorld.random.nextDouble(), blockPos.getY() + 1, (double)blockPos.getZ() + serverWorld.random.nextDouble(), 1, 0.0, 0.0, 0.0, 1.0);
                        }
                    }
                    serverWorld.playSound(null, blockPos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
                    serverWorld.emitGameEvent(null, GameEvent.FLUID_PLACE, blockPos);
                    serverWorld.setBlockState(blockPos2, Blocks.MUD.getDefaultState());
                    return new ItemStack(Items.GLASS_BOTTLE);
                }
                return this.fallback.dispense(pointer, stack);
            }
        });
    }

    public static void setEntityPosition(BlockPointer pointer, Entity entity, Direction direction) {
        entity.setPosition(pointer.getX() + (double)direction.getOffsetX() * (0.5000099999997474 - (double)entity.getWidth() / 2.0), pointer.getY() + (double)direction.getOffsetY() * (0.5000099999997474 - (double)entity.getHeight() / 2.0) - (double)entity.getHeight() / 2.0, pointer.getZ() + (double)direction.getOffsetZ() * (0.5000099999997474 - (double)entity.getWidth() / 2.0));
    }
}
