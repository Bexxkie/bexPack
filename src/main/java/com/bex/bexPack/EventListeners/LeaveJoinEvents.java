package com.bex.bexPack.EventListeners;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import com.bex.bexPack.main.PixelCandy;
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
	}
	/**
	 * PLAYER LEAVE EVENT
	 */
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
		if(PixelCandy.curseMap.containsKey(p)) {
			HashMap<Entity, Integer> ls = PixelCandy.curseMap.get(p);
			try {
				for(Entity ent:ls.keySet())
				{

					ent.remove();
					ls.remove(ent);
				}
			}catch(ConcurrentModificationException |NullPointerException ex) {}	

		}
	}	

}
