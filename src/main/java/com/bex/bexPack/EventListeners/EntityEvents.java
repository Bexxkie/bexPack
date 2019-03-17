package com.bex.bexPack.EventListeners;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;
import com.flowpowered.math.vector.Vector3d;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;

import net.minecraft.entity.player.EntityPlayer;

public class EntityEvents 
{
	/**
	 * 
	 * player use on entity (usually an animal or something)
	 * @param e Entity clicked on
	 * @param p player executed event
	 */
	@Listener
	public void candyUseHatEvent(InteractEntityEvent.Secondary.MainHand e, @Root Player p)
	{
		ItemStack candy = Getters.getCandy();
		Entity ent = e.getTargetEntity();
		if(ent.getClass().equals(EntityPixelmon.class))
		{

			EntityPixelmon	ep = ((EntityPixelmon) ent);
			if(!ep.hasOwner())
			{return;}

			EntityPlayer enp = (EntityPlayer) p;
			ItemStack im = p.getItemInHand(HandTypes.MAIN_HAND).get();
			int b = ItemStackComparators.IGNORE_SIZE.compare(im, candy);
			if(b==0 && ep.belongsTo(enp))
			{
				Level lv = ep.getLvl();
				int _x = lv.getLevel()-2;

				if(ep.getLvl().getLevel() <= 1)
				{
					_x = lv.getLevel()-1;
					Text msg =Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
							append(Text.builder("You cannot lower your pokemon anymore.").color(TextColors.RED).build()).build();
					p.sendMessage(msg);

					candy.setQuantity(1);
					p.getInventory().offer(candy);
				}
				ep.getLvl().setLevel(_x);
				ep.updateStats();
			}
			//***************************************************
			// EntityHats
			//***************************************************
			if(!PixelCandy.bMapTog.contains(p)) 
			{return;}

			if(Getters.isSneaking(p)==false)
			{return;}

			if(p.hasPermission("bex.fun.superhats"))
			{
				if(PixelCandy.ride==false)
				{
					if(ent==PixelCandy.bunnyMap2.get(p)) 
					{
						Entity en = PixelCandy.bunnyMap2.get(p);
						Boolean isHatPK = en.getClass().equals(EntityPixelmon.class);
						ent.offer(Keys.HAS_GRAVITY,true);
						ent.offer(Keys.INVULNERABLE,false);

						if(isHatPK==false)
						{
							en.remove();
						}
						PixelCandy.bunnyMap2.remove(p);
						return;
					}
					if(ep.belongsTo((EntityPlayer)p)==true)
					{

						if(PixelCandy.bunnyMap2.containsKey(p)) 
						{
							Entity en = PixelCandy.bunnyMap2.get(p);
							Boolean isHatPK = en.getClass().equals(EntityPixelmon.class);

							en.offer(Keys.HAS_GRAVITY,true);
							ent.offer(Keys.HAS_GRAVITY,false);
							ent.offer(Keys.INVULNERABLE,true);
							if(isHatPK==false)
							{
								en.remove();
							}
							PixelCandy.bunnyMap2.remove(p);
							PixelCandy.bunnyMap2.put(p,ent);
							return;
						}
						ent.offer(Keys.HAS_GRAVITY,false);
						ent.offer(Keys.INVULNERABLE,true);
						PixelCandy.bunnyMap2.put(p,ent);
						return;
					}
					return;
				}
			}

		}
	}
	/**
	 * this is for updating the location of the entityHat 
	 * @param e moveEvent
	 * @param p Player (only calls moveEvent if source is a player)
	 */
	//TODO add the Disguise stuff
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listener
	public void moveEvent(MoveEntityEvent e, @Root Player p)
	{
		if(PixelCandy.disguiseMap.containsKey(p))
		{
			Entity ent = (Entity)PixelCandy.disguiseMap.get(p);
			if(!ent.isLoaded())
			{
				PixelCandy.disguiseMap.remove(p);
				return;
			}
			Location loc = p.getLocation();
			Vector3d rot = p.getRotation();
			ent.setRotation(rot);
			ent.setLocation(loc);
		}
		if(PixelCandy.bunnyMap2.containsKey(p))
		{
			Entity ent = PixelCandy.bunnyMap2.get(p);
			//This is so you cant try and wear something your sitting on (would send you to space)
			if(ent.getPassengers().contains(p))
			{
				ent.offer(Keys.HAS_GRAVITY,true);
				ent.offer(Keys.INVULNERABLE,false);
				PixelCandy.bunnyMap2.remove(p);
				return;
			}

			Location loc = p.getLocation();
			Vector3d rot = p.getHeadRotation();
			loc=loc.add(0,2,0);
			//ent.offer(Keys.HAS_GRAVITY,false);
			//ent.offer(Keys.INVULNERABLE,true);
			ent.setRotation(rot);
			ent.setLocation(loc);
		}
	}
}