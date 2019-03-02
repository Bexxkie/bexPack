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
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;

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
		ItemStack wingsuit = Getters.getElytra();
		ItemStack equip = p.getEquipped(EquipmentTypes.CHESTPLATE).get();
		int isWingsuit = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(wingsuit,equip);
		//System.out.println("changeInventoryEvent");
		if(isWingsuit!=0)
		{
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
