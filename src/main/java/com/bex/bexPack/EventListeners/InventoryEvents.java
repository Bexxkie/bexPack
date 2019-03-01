package com.bex.bexPack.EventListeners;

import java.util.NoSuchElementException;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;

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
	public void itemDoublecheck(ClickInventoryEvent e,@Root Player p)
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
	
	
}
