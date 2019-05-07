package com.bex.bexPack.util;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.entity.weather.Lightning;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.flowpowered.math.vector.Vector3d;


public class Schedulars 
{
	public static void run()
	{
		//ITEMRAIN/CURSE/COOLDOWN MANAGEMENT (1s)
		PixelCandy.tb.interval(1, TimeUnit.SECONDS);
		PixelCandy.tb.execute(new Runnable()
		{
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() 
			{
				//ItemRain
				if(PixelCandy.rainTime.isEmpty()==false)
				{

					for(Player p:PixelCandy.rainTime.keySet())
					{
						int t = PixelCandy.rainTime.get(p); 
						t--;
						if(t <1)
						{
							PixelCandy.rainTime.remove(p);
						}
						PixelCandy.rainTime.replace(p, t);
						PixelCandy.spawnItem(p);
						//spawnItem(p);
						//spawnItem(p);
					}
				}
				//curseControl+Cleanup
				if(PixelCandy.curseMap.isEmpty()==false)
				{
					try {
						for(Player p:PixelCandy.curseMap.keySet())
						{
							HashMap<Entity, Integer> emp = PixelCandy.curseMap.get(p);
							if(emp.isEmpty()==true)
							{
								PixelCandy.curseMap.remove(p);
							}
							for(Entity e: emp.keySet())
							{
								int t = emp.get(e);
								t=t-1;
								Location loc = p.getLocation();
								loc=loc.add(RandomNum.rNum(-3,3),RandomNum.rNum(2,8),RandomNum.rNum(-3,3));
								e.setLocation(loc);
								emp.replace(e, t);
								if(t<1)
								{
									Entity enl = p.getWorld().createEntity(EntityTypes.LIGHTNING,e.getLocation().getPosition());
									Lightning lt = (Lightning) enl;
									e.getWorld().spawnEntity(lt);

									e.remove();
									emp.remove(e);
								}
							}
						}
					}
					catch(ConcurrentModificationException e)
					{}
				}
				//ItemRainCleanup
				if(PixelCandy.blockMap.isEmpty()==false&&PixelCandy.rainTime.isEmpty())
				{
					try
					{
						for(Entity b:PixelCandy.blockMap)
						{
							b.remove();
							PixelCandy.blockMap.remove(b);
						}
					}
					catch(ConcurrentModificationException e)
					{}
				}
				//Cooldowns
				for(Player p:PixelCandy.Cooldowns.keySet())
				{
					int remT = PixelCandy.Cooldowns.get(p);
					if(remT<1)
					{
						PixelCandy.Cooldowns.remove(p);
					}
					remT=remT-1;
					PixelCandy.Cooldowns.replace(p, remT);
				}
				//GameTimer
				if(PixelCandy.gameTargetWordAndTime.isEmpty()==false)
				{
					for(String _s : PixelCandy.gameTargetWordAndTime.keySet())
					{
						int _t = PixelCandy.gameTargetWordAndTime.get(_s);
						//System.out.println(_t);
						if(_t<=0)
						{
							//gameOver
							Messenger.broadcastComplexMessage("Bad luck, no one guessed the word. It was ", TextColors.RED, _s.toString(), TextColors.YELLOW);
							PixelCandy.gameTargetWordAndTime.clear();
							PixelCandy.gameGuesses.clear();
						}
						else 
						{
							_t = _t-1;
							PixelCandy.gameTargetWordAndTime.replace(_s, _t);
						}
					}

				}
			}

		}).name("bp-itemRain_t.1s").submit(PixelCandy.INSTANCE);
		//flight drain 0.5xp/s (4s)
		PixelCandy.tb.interval(4, TimeUnit.SECONDS);
		PixelCandy.tb.execute(new Runnable()
		{
			public void run()
			{
				if(PixelCandy.pFly.isEmpty()==false)
				{
					for(Player p:PixelCandy.pFly.keySet())
					{
						GameModeData data = p.getGameModeData();
						GameMode gm = data.get(Keys.GAME_MODE).get();
						if(gm.equals(GameModes.SURVIVAL)||gm.equals(GameModes.ADVENTURE))
						{
							int xp = p.get(Keys.TOTAL_EXPERIENCE).get();
							if(p.get(Keys.IS_FLYING).get())
							{
								xp=xp-2;
								p.offer(Keys.TOTAL_EXPERIENCE,xp);
							}
						}
					}
				}
			}
		}).name("bp-elytraExpHandler_t.4s").submit(PixelCandy.INSTANCE);

		//flight (.5s)
		PixelCandy.tb.interval(500, TimeUnit.MILLISECONDS);
		PixelCandy.tb.execute(new Runnable()
		{
			public void run()
			{
				//FlightDetections
				if(PixelCandy.pFly.isEmpty()==false)
				{
					for(Player p:PixelCandy.pFly.keySet())
					{
						if(!p.hasPermission("bex.wings.ignore"))
						{
							GameModeData data = p.getGameModeData();
							GameMode gm = data.get(Keys.GAME_MODE).get();
							if(gm.equals(GameModes.SURVIVAL))
							{

								int xp = p.get(Keys.TOTAL_EXPERIENCE).get();
								if(p.get(Keys.IS_FLYING).get())
								{
									if(xp<=1 || PixelCandy.pFly.get(p)==false)
									{
										p.offer(Keys.IS_FLYING, false);
										p.offer(Keys.CAN_FLY, false);
									}
								}
							}
						}
					}
				}
			}
		}).name("bp-elytraFlightHandler_t.500ms").submit(PixelCandy.INSTANCE);
		//playerListenHelper (5s)
		PixelCandy.tb.interval(250, TimeUnit.MILLISECONDS);
		PixelCandy.tb.execute(new Runnable()
		{
			@SuppressWarnings("rawtypes")
			public void run()
			{
				if(!PixelCandy.rulerMap.isEmpty())
				{
					for(Player p: PixelCandy.rulerMap.keySet())
					{
						Location loc = PixelCandy.rulerMap.get(p);
						ParticleEffect effect = ParticleEffect.builder()
								.type(ParticleTypes.PORTAL)
								.quantity(10)
								.velocity(Vector3d.from(.5,1.5,.5))
								.build();
						p.getLocation().getExtent().spawnParticles(effect, loc.getPosition().add(.5,.5,.5),2);
					}
				}
			}
		}).name("bp-particleHelper_t.500ms").submit(PixelCandy.INSTANCE);
	}

}
