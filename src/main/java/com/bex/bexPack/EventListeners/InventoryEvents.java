package com.bex.bexPack.EventListeners;

import java.util.HashMap;
import java.util.NoSuchElementException;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;



public class InventoryEvents 
{
	/**
	 * this shit should be for stoping the itemRain item from being picked up by players. 
	 * @param e pickupItemEvent
	 */
	@Listener
	public void itemPickupEvent(ChangeInventoryEvent.Pickup e)
	{
		try {
			for (SlotTransaction _tr : e.getTransactions())
			{
				ItemStack _im = _tr.getFinal().createStack();

				Text _in = _im.get(Keys.DISPLAY_NAME).get();
				Boolean isRain = _in.equals(Text.of("bex.item.rain"));
				if(isRain==true)
				{
					e.setCancelled(true);
				}
			}
		}catch(NoSuchElementException | NullPointerException ex) {}
	}
	/**
	 * if a player somehow picks up an item that they shouldnt it will be deleted
	 * @param e 
	 */
	@Listener
	public void itemDoublecheck(ClickInventoryEvent e, @Root Player p)
	{
		try {
			ItemStackSnapshot _im = e.getTransactions().get(0).getOriginal();
			Slot s = e.getTransactions().get(0).getSlot();
			//System.out.println(_im);
			//Transaction<ItemStackSnapshot> _tr =e.getCursorTransaction();
			//ItemStack _im = _tr.getFinal().createStack();
			//ItemStack i =_im.createStack();
			Text _in = _im.get(Keys.DISPLAY_NAME).get();
			Boolean isRain = _in.equals(Text.of("bex.item.rain"));
			if(isRain==true)
			{
				e.setCancelled(true);
				s.set(ItemStack.of(ItemTypes.AIR));
			}
		}catch(NoSuchElementException | NullPointerException | IndexOutOfBoundsException ex) {}

	}
	
	@Listener
	public void wingSuitEquip(ChangeInventoryEvent e, @Root Player p)
	{
		equipWingsuit(p);
	}
	
	@SuppressWarnings("rawtypes")
	@Listener
	public void closeEnchantmentTableEvent(InteractInventoryEvent.Close e,@Root Player p)
	{
		if(e.getTargetInventory().getArchetype().equals(InventoryArchetypes.ENCHANTING_TABLE))
		{
			if(PixelCandy.enchantingBlockMap.isEmpty())
			{
				return;
			}
			Location loc = p.getLocation();
			loc = loc.add(2,0,0);
			if(PixelCandy.enchantingBlockMap.containsKey(p))
			{
				HashMap<Location,BlockType> tmp = PixelCandy.enchantingBlockMap.get(p);
				for(Location _l : tmp.keySet())
				{
					_l.setBlockType(tmp.get(_l),BlockChangeFlags.NONE);
				}
				PixelCandy.enchantingBlockMap.remove(p);
			}

		}
	}
	//was testing using 2 methods of detection. but event not implemented in 1.12 cause fuck you.
	public void equipWingsuit(Player p)
	{
		GameModeData data = p.getGameModeData();
		GameMode gm = data.get(Keys.GAME_MODE).get();
		if(!gm.equals(GameModes.SURVIVAL))
		{
			return;
		}
		ItemStack wingsuit = Getters.getElytra();
		ItemStack equip = p.getEquipped(EquipmentTypes.CHESTPLATE).get();
		int isWingsuit = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(wingsuit,equip);
		//System.out.println("changeInventoryEvent");
		if(equip.isEmpty())
		{
			p.offer(Keys.CAN_FLY,false);
			p.offer(Keys.IS_FLYING,false);
		}
		if(isWingsuit!=0)
		{
			if(p.hasPermission("bex.wings.ignore"))
			{
				return;
			}
			//System.out.println("notWingSuit");
			if(PixelCandy.pFly.containsKey(p)) 
			{
				PixelCandy.pFly.put(p, true);
			}
			p.offer(Keys.CAN_FLY, false);
			p.offer(Keys.IS_FLYING, false);
			return;
		}
		//System.out.println("isWingsuit");
		p.offer(Keys.CAN_FLY, true);
		if(PixelCandy.pFly.containsKey(p)) {return;}
		PixelCandy.pFly.put(p, true);
	}

}
