package com.bex.bexPack.util;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Getters
{
	/**
	 * 
	 * Unusual Candy
	 * @return unusualCandy as itemStack (pixelmon- rare candy)
	 */
	public static ItemStack getCandy()
	{
		ItemStack candyBase = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "pixelmon:rare_candy").get());
		List<Text> lore = new ArrayList<Text>();
		lore.add(Text.of("Causes a pixelmon to lose a level"));
		candyBase.offer(Keys.DISPLAY_NAME,Text.of(TextColors.BLUE,"Unusual Candy"));
		candyBase.offer(Keys.ITEM_LORE,lore);
		return candyBase;
	}
	
	/**
	 * 
	 * Rare Candy
	 * @return rareCandy as itemStack (pixelmon- rare candy)
	 */
	public static ItemStack getCandyRare()
	{
		ItemStack candyBase = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "pixelmon:rare_candy").get());
		return candyBase;
	}

	/**
	 * 
	 * Incubator
	 * @return incubator as itemStack (pixelmon- lucky egg)
	 */
	public static ItemStack getIncubator()
	{
		ItemStack egg = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "pixelmon:lucky_egg").get());
		List<Text> lore = new ArrayList<Text>();
		lore.add(Text.of("Incubate an egg in your party"));
		/*1.7*/	//lore.add(Text.of("Walk a few steps to finish up the hatching process"));
		lore.add(Text.of("/pcan incubate <slotNo>"));
		egg.offer(Keys.DISPLAY_NAME,Text.of(TextColors.BLUE,"Incubator"));
		egg.offer(Keys.ITEM_LORE,lore);
		return egg;	
	}
	
	/**
	 * 
	 * ruler
	 * @return ruler as itemStack (clock)
	 */
	public static ItemStack getRuler()
	{
		ItemStack ruler = ItemStack.of(ItemTypes.CLOCK);
		ruler.offer(Keys.DISPLAY_NAME,Text.of("ruler"));
		return ruler;
	}
	
	/**
	 * 
	 * flightSuit
	 * @return flightSuit as itemStack (elytra)
	 */
	public static ItemStack getElytra()
	{
		ItemStack elytra = ItemStack.of(ItemTypes.ELYTRA);
		List<Text> lore = new ArrayList<Text>();
		lore.add(Text.of("Flight"));
		/*1.7*/	//lore.add(Text.of("Walk a few steps to finish up the hatching process"));

		elytra.offer(Keys.DISPLAY_NAME,Text.of(TextColors.BLUE,"Wings"));
		elytra.offer(Keys.ITEM_LORE,lore);
		return elytra;
	}
	
	/**
	 * check of the player is sneaking
	 * @param p player
	 * @return bool is the player sneaking
	 */
	public static boolean isSneaking(Player p) 
	{
		return p.get(Keys.IS_SNEAKING).orElse(false);
	}

	public static ItemStack getDrink(String id)
	{
		ItemStack drink = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "minecraft:potion").get());
		List<Text> lore = new ArrayList<Text>();
		
		String drinkName = AlcoholProcessor.getString(id, "name");
		String drinkLore = AlcoholProcessor.getString(id, "lore");

		lore.add(Text.of("Alcoholic Beverage"));
		lore.add(Text.of(id));
		lore.add(Text.of(drinkLore));
		drink.offer(Keys.DISPLAY_NAME,Text.of(TextColors.RED,drinkName));
		drink.offer(Keys.ITEM_LORE,lore);
		return drink;
	}
}
