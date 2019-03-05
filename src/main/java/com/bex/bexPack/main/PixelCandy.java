package com.bex.bexPack.main;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.bex.bexPack.EventListeners.*;
import com.bex.bexPack.commands.BaseCommand;
import com.bex.bexPack.util.RandomNum;
import com.flowpowered.math.vector.Vector3d;
import com.google.inject.Inject;


@Plugin (name = PixelCandy.NAME,
id = "pixelcandy",
version = PixelCandy.VERSION,
authors = PixelCandy.AUTHOR,
description = PixelCandy.DESC,
dependencies = {@Dependency(id = "pixelmon")})
public class PixelCandy 
{
	public static final String NAME = "bexPack";
	public static final String VERSION = "1.2.3";
	public static final String AUTHOR = "Bexxkie, Dr. Stupid";
	public static final String DESC = "This does a bunch of shit...";
	@Inject
	private PluginContainer pluginContainer;
	public static PixelCandy INSTANCE;
	/* Player, timeToRain      */		public static HashMap<Player,Integer> rainTime = new HashMap<Player,Integer>();
	/* Player, flightEnabled   */		public static HashMap<Player,Boolean> pFly = new HashMap<Player,Boolean>();
	/* Player, entityAsHat     */		public static HashMap<Player,Entity> bunnyMap2 = new HashMap<Player, Entity>();
	/* onlinePlayers 		   */		public static List<Player> pList = new ArrayList<Player>();
	/* entityItemRain          */		public static List<Entity> blockMap = new ArrayList<Entity>();
	/* entityHat  Toggle       */		public static List<Player> bMapTog = new ArrayList<Player>();
	/* flightSuit list	       */		public static List<Player> flightSuitList = new ArrayList<Player>();
	/* player, entity,time     */		public static HashMap<Player,HashMap<Entity,Integer>> curseMap = new HashMap<Player,HashMap<Entity,Integer>>();
	/* Player, itemToRain      */		public static HashMap<Player,ItemStack> ItemRainMap = new HashMap<Player,ItemStack>();
	/* cooldown by player	   */		public static HashMap<Player,Integer> Cooldowns = new HashMap<Player, Integer>();
	@SuppressWarnings("rawtypes")
	public static HashMap<Player,HashMap<Location,BlockType>> enchantingBlockMap = new HashMap<Player, HashMap<Location,BlockType>>();
	@SuppressWarnings("rawtypes")
	public static HashMap<Player,Location> rulerMap = new HashMap<Player, Location>();
	public static Boolean ride = false;
	public static UUID bex = UUID.fromString("7c4958de-7a27-4b58-ac97-947142459d76");
	Task.Builder tb = Task.builder();

	/**
	 * SERVER INIT EVENTS
	 */
	@Listener
	public void onServerStart(GameInitializationEvent e)
	{
		INSTANCE = this;
		Sponge.getCommandManager().register(this, new BaseCommand().getCommandSpec(), "pixelcandy", "pcan");
		//Register the listeners
		Sponge.getEventManager().registerListeners(this, new EntityEvents());
		Sponge.getEventManager().registerListeners(this, new InventoryEvents());
		Sponge.getEventManager().registerListeners(this, new LeaveJoinEvents());
		Sponge.getEventManager().registerListeners(this, new WorldEvents());
		Sponge.getEventManager().registerListeners(this, new AlcoholEvents());
		
	}


	@Listener
	public void onServerStarted(GameStartedServerEvent e) 
	{
		run();
	}
	public PluginContainer getPluginContainer() 
	{
		return this.pluginContainer;
	}

	/****************************************************************
	/
	/
	/****************************************************************
	 * 
	 * used for itemRain and stuff
	 * @param p player
	 */
	public static void spawnItem(Player p)
	{
		Boolean isItem = ItemRainMap.containsKey(p);
		Location<World> loc = p.getLocation();
		int x = (int) (Math.random() * ((2 - -2) + 1));

		if(isItem==false)
		{
			loc = loc.add(x, 3+x, x);
			Extent extent = loc.getExtent();
			Entity ent = extent.createEntity(EntityTypes.FALLING_BLOCK, loc.getBlockPosition());
			FallingBlock block = (FallingBlock) ent;
			BlockState blockState = BlockState.builder().blockType(BlockTypes.RED_FLOWER).build();
			block.offer(Keys.CAN_PLACE_AS_BLOCK, false);
			block.offer(Keys.CAN_DROP_AS_ITEM, false);
			block.offer(Keys.FALL_TIME, 1);
			block.offer(Keys.FALLING_BLOCK_STATE, blockState);
			blockMap.add(block);
			loc.spawnEntity(block);
		}
		else
		{
			loc = loc.add(x, 5+x, x);
			Extent extent = loc.getExtent();
			ItemStack im = ItemRainMap.get(p);
			Entity ent = extent.createEntity(EntityTypes.ITEM, loc.getBlockPosition());
			ent.offer(Keys.REPRESENTED_ITEM, im.createSnapshot());
			blockMap.add(ent);
			loc.spawnEntity(ent);
		}
	}
	public void run()
	{
		//ITEMRAIN/CURSE/COOLDOWN MANAGEMENT (1s)
		tb.interval(1, TimeUnit.SECONDS);
		tb.execute(new Runnable()
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
								if(t<1)
								{
									e.remove();
									emp.remove(e);
								}
								Location loc = p.getLocation();
								loc=loc.add(RandomNum.rNum(-3,3),RandomNum.rNum(2,8),RandomNum.rNum(-3,3));
								e.setLocation(loc);
								emp.replace(e, t);
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
			}
		}).name("bp-itemRain_t.1s").submit(this);
		//flight drain 0.5xp/s (4s)
		tb.interval(4, TimeUnit.SECONDS);
		tb.execute(new Runnable()
		{
			public void run()
			{
				if(PixelCandy.pFly.isEmpty()==false)
				{
					for(Player p:PixelCandy.pFly.keySet())
					{
						GameModeData data = p.getGameModeData();
						GameMode gm = data.get(Keys.GAME_MODE).get();
						if(gm.equals(GameModes.SURVIVAL))
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
		}).name("bp-elytraExpHandler_t.4s").submit(this);

		//flight (.5s)
		tb.interval(500, TimeUnit.MILLISECONDS);
		tb.execute(new Runnable()
		{
			public void run()
			{
				//FlightDetections
				if(PixelCandy.pFly.isEmpty()==false)
				{
					for(Player p:PixelCandy.pFly.keySet())
					{
						GameModeData data = p.getGameModeData();
						GameMode gm = data.get(Keys.GAME_MODE).get();
						if(gm.equals(GameModes.SURVIVAL))
						{

							int xp = p.get(Keys.TOTAL_EXPERIENCE).get();
							if(p.get(Keys.IS_FLYING).get())
							{
								if(xp<1 || PixelCandy.pFly.get(p)==false)
								{
									p.offer(Keys.IS_FLYING, false);
								}
							}
						}
					}
				}
			}
		}).name("bp-elytraFlightHandler_t.500ms").submit(this);
		//EntityHat Helper (.1s)
		tb.interval(100, TimeUnit.MILLISECONDS);
		tb.execute(new Runnable()
		{
			Boolean _b =false;
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run()
			{
				if(PixelCandy.ride==false&&_b==false)
				{
					return;
				}
				//
				//new Style, moveEntity
				//
				if(PixelCandy.ride==false&&_b==true)
				{
					for(Player p:PixelCandy.bunnyMap2.keySet())
					{
						Entity ent = PixelCandy.bunnyMap2.get(p);
						if(ent.getPassengers()!=null)
						{
							ent.offer(Keys.HAS_GRAVITY,true);
							ent.offer(Keys.INVULNERABLE,false);
							PixelCandy.bunnyMap2.remove(p);
							return;
						}
						if(ent.isLoaded())
						{
							Location loc = p.getLocation();
							loc=loc.add(0,2,0);
							ent.offer(Keys.HAS_GRAVITY,false);
							ent.offer(Keys.INVULNERABLE,true);
							ent.setLocation(loc);

							return;
						}

						PixelCandy.bunnyMap2.remove(p);
					}	
					return;
				}
			}
		}).name("bp-entityHatHelper_t.100ms").submit(this);
		//playerListenHelper (5s)
		/*
		tb.interval(5, TimeUnit.SECONDS);
		tb.execute(new Runnable()
		{
			public void run() 
			{
				if(PixelCandy.pList.isEmpty()==false)
				{
					for(Player p:PixelCandy.pList)
					{
						//ItemType it = p.getEquipped(EquipmentTypes.CHESTPLATE).get().getType();
						ItemStack is = p.getEquipped(EquipmentTypes.CHESTPLATE).get();
						ItemStack wings = Getters.getElytra();
						int b = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(is, wings);

						if(b==0)
						{
							if(!PixelCandy.pFly.containsKey(p))
							{
								p.offer(Keys.CAN_FLY,true);
								p.offer(Keys.IS_FLYING,true);
								PixelCandy.pFly.put(p,true);
							}
						}
						else
						{
							if(PixelCandy.pFly.containsKey(p))
							{
								p.offer(Keys.CAN_FLY,false);
								p.offer(Keys.IS_FLYING,false);
								PixelCandy.pFly.remove(p);
							}
						}
					}
				}
			}
		}).name("bp-playerListener_t.5s").submit(this);
		 */
		tb.interval(500, TimeUnit.MILLISECONDS);
		tb.execute(new Runnable()
		{
			@SuppressWarnings("rawtypes")
			public void run()
			{
				if(!rulerMap.isEmpty())
				{
					for(Player p: PixelCandy.rulerMap.keySet())
					{
						Location loc = rulerMap.get(p);
						ParticleEffect effect = ParticleEffect.builder()
								.type(ParticleTypes.PORTAL)
								.quantity(10)
								.velocity(Vector3d.from(.5,1.5,.5))
								.build();
						p.getLocation().getExtent().spawnParticles(effect, loc.getPosition().add(.5,.5,.5),2);
					}
				}
			}
		}).name("bp-particleHelper_t.500ms").submit(this);
	}
}



