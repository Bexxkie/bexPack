package com.bex.bexPack.EventListeners;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class LeaveJoinEvents 
{
	/**
	 * PLAYER JOIN EVENT
	 */
	@Listener
	public void playerJoinEvent(ClientConnectionEvent.Join e)
	{
		//add player to onlineplayers list (can also use isOnline())
		Player p = e.getTargetEntity();
		PixelCandy.pList.add(p);

		ItemStack wingsuit = Getters.getElytra();
		ItemStack equip = p.getEquipped(EquipmentTypes.CHESTPLATE).get();
		int isWingsuit = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(wingsuit,equip);
		//System.out.println("changeInventoryEvent");
		GameModeData data = p.getGameModeData();
		GameMode gm = data.get(Keys.GAME_MODE).get();
		if(gm.equals(GameModes.CREATIVE))
		{
			return;
		}
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
		} else {
			//System.out.println("isWingsuit");
			p.offer(Keys.CAN_FLY, true);
			if(!PixelCandy.pFly.containsKey(p)) 
			{
				PixelCandy.pFly.put(p, true);
			}
		}

	}
	/**
	 * PLAYER LEAVE EVENT
	 */
	@SuppressWarnings("rawtypes")
	@Listener
	public void playerLeaveEvent(ClientConnectionEvent.Disconnect e)
	{
		//Cleanup anything that needs to be. remove players from list, clean up entities and shit
		Player p = e.getTargetEntity();
		if(PixelCandy.pList.contains(p)) 
		{
			PixelCandy.pList.remove(p);	
		}
		//
		//newStyle,moveEntity
		//
		if(PixelCandy.ride==false)
		{
			if(PixelCandy.bunnyMap2.containsKey(p)) 
			{
				Entity ent = PixelCandy.bunnyMap2.get(p);
				Boolean isPK = ent.getClass().equals(EntityPixelmon.class);
				if(isPK==false)
				{
					ent.remove();
				}
				ent.offer(Keys.HAS_GRAVITY,true);
				ent.offer(Keys.INVULNERABLE,false);
				PixelCandy.bunnyMap2.remove(p);
			}
		}
		//
		//Clear curse entities
		//
		if(PixelCandy.curseMap.containsKey(p)) 
		{
			HashMap<Entity, Integer> ls = PixelCandy.curseMap.get(p);
			try {
				for(Entity ent:ls.keySet())
				{

					ent.remove();
					ls.remove(ent);
				}
			}catch(ConcurrentModificationException |NullPointerException ex) {}	

		}
		//
		//clear enchant table shit
		//
		if(PixelCandy.enchantingBlockMap.containsKey(p))
		{
			HashMap<Location, BlockType> tmp = PixelCandy.enchantingBlockMap.get(p);
			for(Location loc :tmp.keySet())
			{
				loc.setBlockType(tmp.get(loc));
			}
			
			PixelCandy.enchantingBlockMap.remove(p);
		}
	}	

}
