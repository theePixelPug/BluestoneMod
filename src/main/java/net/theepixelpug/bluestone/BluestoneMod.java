package net.theepixelpug.bluestone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.theepixelpug.bluestone.block.ModBlocks;
import net.theepixelpug.bluestone.block.dispenser.BluestoneDispenserBehavior;
import net.theepixelpug.bluestone.block.entity.ModBlockEnities;
import net.theepixelpug.bluestone.item.ModItems;
import net.theepixelpug.bluestone.world.gen.ModOreGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BluestoneMod implements ModInitializer {
	public static final String MOD_ID = "bluestone";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();

		BluestoneDispenserBehavior.registerDefaults();

		ModBlockEnities.registerBlockEntities();

		ModOreGeneration.generateOres();

		BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER, ModItems.BLUESTONE, Potions.MUNDANE);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.NIGHT_VISION, ModItems.BLUESTONE, Potions.LONG_NIGHT_VISION);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.INVISIBILITY, ModItems.BLUESTONE, Potions.LONG_INVISIBILITY);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.FIRE_RESISTANCE, ModItems.BLUESTONE, Potions.LONG_FIRE_RESISTANCE);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.LEAPING, ModItems.BLUESTONE, Potions.LONG_LEAPING);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.SLOWNESS, ModItems.BLUESTONE, Potions.LONG_SLOWNESS);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.TURTLE_MASTER, ModItems.BLUESTONE, Potions.LONG_TURTLE_MASTER);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.SWIFTNESS, ModItems.BLUESTONE, Potions.LONG_SWIFTNESS);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.WATER_BREATHING, ModItems.BLUESTONE, Potions.LONG_WATER_BREATHING);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.POISON, ModItems.BLUESTONE, Potions.LONG_POISON);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.REGENERATION, ModItems.BLUESTONE, Potions.LONG_REGENERATION);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.STRENGTH, ModItems.BLUESTONE, Potions.LONG_STRENGTH);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.WEAKNESS, ModItems.BLUESTONE, Potions.LONG_WEAKNESS);
		BrewingRecipeRegistry.registerPotionRecipe(Potions.SLOW_FALLING, ModItems.BLUESTONE, Potions.LONG_SLOW_FALLING);

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1, factories -> {
			factories.add(new TradeOffers.SellItemFactory(ModItems.BLUESTONE, 1, 2, 1));
		});
	}
}
