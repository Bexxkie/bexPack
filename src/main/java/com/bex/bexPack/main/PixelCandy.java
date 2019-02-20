package com.bex.bexPack.main;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.CollideEntityEvent;
import org.spongepowered.api.event.entity.InteractEntityEvent;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.item.inventory.equipment.EquipmentTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import com.bex.bexPack.commands.BaseCommand;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;

import net.minecraft.entity.player.EntityPlayer;


@Plugin (name = PixelCandy.NAME,
id = "pixelcandy",
version = PixelCandy.VERSION,
authors = PixelCandy.AUTHOR,
description = PixelCandy.DESC,
dependencies = {@Dependency(id = "pixelmon")})
public class PixelCandy 
{
	public static final String NAME = "bexPack";
	public static final String VERSION = "1.1.8b";
	public static final String AUTHOR = "Bexxkie";
	public static final String DESC = "This does a bunch of shit...";
	@Inject
	private PluginContainer pluginContainer;
	public static PixelCandy INSTANCE;
	/* Player, timeToRain      */		public static HashMap<Player,Integer> rainTime = new HashMap<Player,Integer>();
	/* Player, flightEnabled   */		public static HashMap<Player,Boolean> pFly = new HashMap<Player,Boolean>();
	/* depricated              */	  //public static HashMap<Player,List<Entity>> bunnyMap = new HashMap<Player, List<Entity>>();
	/* Player, entityAsHat     */		public static HashMap<Player,Entity> bunnyMap2 = new HashMap<Player, Entity>();
	/* onlinePlayers 		   */		public static List<Player> pList = new ArrayList<Player>();
	/* entityItemRain          */		public static List<Entity> blockMap = new ArrayList<Entity>();
	/* entityHat  Toggle       */		public static List<Player> bMapTog = new ArrayList<Player>();
	/* player, entity,time     */		public static HashMap<Player,HashMap<Entity,Integer>> curseMap = new HashMap<Player,HashMap<Entity,Integer>>();
	/* Player, itemToRain      */		public static HashMap<Player,ItemStack> ItemRainMap = new HashMap<Player,ItemStack>();
	/* cooldown by player	   */		public static HashMap<Player,Integer> Cooldowns = new HashMap<Player, Integer>();
	@SuppressWarnings("rawtypes")
	/* player, target, loc 	   */					public static HashMap<Player,HashMap<Integer,Location>> rulerMap = new HashMap<Player,HashMap<Integer,Location>>();
	/* for repeating tasks     */	Task.Builder tb = Task.builder();
	/* hat testing (keep false)*/	public static Boolean ride = false;
	public static UUID bex = UUID.fromString("7c4958de-7a27-4b58-ac97-947142459d76");

	
	@Listener
	public void onServerStart(GameInitializationEvent e)
	{
		Sponge.getCommandManager().register(this, new BaseCommand().getCommandSpec(), "pixelcandy", "pcan");
	}

	@Listener
	public void onServerStarted(GameStartedServerEvent e) 
	{
		//init the repeating tasks.
		repeatingTask();
	}

	@Listener
	public void playerJoinEvent(ClientConnectionEvent.Join e)
	{
		//add player to onlineplayers list (can also use isOnline())
		Player p = e.getTargetEntity();
		pList.add(p);
	}

	@Listener
	public void playerLeaveEvent(ClientConnectionEvent.Disconnect e)
	{
		//Cleanup anything that needs to be. remove players from list, clean up entities and shit
		Player p = e.getTargetEntity();
		if(pList.contains(p)) 
		{
			pList.remove(p);	
		}
		//
		//newStyle,moveEntity
		//
		if(ride==false)
		{
			if(bunnyMap2.containsKey(p)) 
			{
				Entity ent = bunnyMap2.get(p);
				Boolean isPK = ent.getClass().equals(EntityPixelmon.class);
				if(isPK==false)
				{
					ent.remove();
				}
				ent.offer(Keys.HAS_GRAVITY,true);
				ent.offer(Keys.INVULNERABLE,false);
				bunnyMap2.remove(p);
			}
		}
		//
		//oldStyle,passenger
		//
		/*
		if(ride==true&&bunnyMap.containsKey(p)) 
		{
			List<Entity> el = PixelCandy.bunnyMap.get(p);
			Entity e1 = el.get(1);
			Entity e2 = el.get(0);
			e2.remove();
			e1.offer(Keys.INVULNERABLE,false);
			if(!e1.getClass().equals(EntityPixelmon.class))
			{
				e1.remove();
			}
			bunnyMap.remove(p);
		}
		 */
		//
		//Clear curse entities
		//
		if(curseMap.containsKey(p)) {
			HashMap<Entity, Integer> ls = curseMap.get(p);
			try {
				try {
					for(Entity ent:ls.keySet())
					{

						ent.remove();
						ls.remove(ent);
					}
				}catch(ConcurrentModificationException ex) {}	
			}catch(NullPointerException ex) {}
		}
		//
		//bMap clear
		//

	}	
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

	public PluginContainer getPluginContainer() 
	{
		return this.pluginContainer;
	}

	public void repeatingTask()
	{
		//ITEMRAIN/CURSE/COOLDOWN MANAGEMENT (1s)
		tb.interval(1, TimeUnit.SECONDS);
		tb.execute(new Runnable()
		{
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() 
			{
				//ItemRain
				if(rainTime.isEmpty()==false)
				{

					for(Player p:rainTime.keySet())
					{
						int t = rainTime.get(p); 
						t--;
						if(t <1)
						{
							rainTime.remove(p);
						}
						rainTime.replace(p, t);
						spawnItem(p);
						//spawnItem(p);
						//spawnItem(p);
					}
				}
				//curseControl+Cleanup
				if(curseMap.isEmpty()==false)
				{
					try {
						for(Player p:curseMap.keySet())
						{
							HashMap<Entity, Integer> emp = curseMap.get(p);
							if(emp.isEmpty()==true)
							{
								curseMap.remove(p);
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
								loc=loc.add(rNum(-3,3),rNum(2,8),rNum(-3,3));
								e.setLocation(loc);
								emp.replace(e, t);
							}
						}
					}
					catch(ConcurrentModificationException e)
					{}
				}
				//ItemRainCleanup
				if(blockMap.isEmpty()==false&&rainTime.isEmpty())
				{
					try
					{
						for(Entity b:blockMap)
						{
							b.remove();
							blockMap.remove(b);
						}
					}
					catch(ConcurrentModificationException e)
					{}
				}

				//Cooldowns
				for(Player p:Cooldowns.keySet())
				{
					int remT = Cooldowns.get(p);
					if(remT<1)
					{
						Cooldowns.remove(p);
					}
					remT=remT-1;
					Cooldowns.replace(p, remT);
				}
			}
		}).name("bp-itemRain_t.1s").submit(this);
		//flight drain 0.5xp/s (4s)
		tb.interval(4, TimeUnit.SECONDS);
		tb.execute(new Runnable()
		{
			public void run()
			{
				if(pFly.isEmpty()==false)
				{
					for(Player p:pFly.keySet())
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
		}).name("bp-elytraExpHandler_t.4s").submit(this);

		//flight (.5s)
		tb.interval(500, TimeUnit.MILLISECONDS);
		tb.execute(new Runnable()
		{
			public void run()
			{
				//FlightDetections
				if(pFly.isEmpty()==false)
				{
					for(Player p:pFly.keySet())
					{
						int xp = p.get(Keys.TOTAL_EXPERIENCE).get();
						if(p.get(Keys.IS_FLYING).get())
						{
							if(xp<1 || pFly.get(p)==false)
							{
								p.offer(Keys.IS_FLYING, false);
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
				if(ride==false&&_b==false)
				{
					return;
				}
				//
				//new Style, moveEntity
				//
				if(ride==false&&_b==true)
				{
					for(Player p:bunnyMap2.keySet())
					{
						Entity ent = bunnyMap2.get(p);
						if(ent.getPassengers()!=null)
						{
							ent.offer(Keys.HAS_GRAVITY,true);
							ent.offer(Keys.INVULNERABLE,false);
							bunnyMap2.remove(p);
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

						bunnyMap2.remove(p);
					}	
					return;
				}
				//
				//old Style, passengers
				//
				/*
				for(Player p:bunnyMap.keySet())
				{
					List<Entity> el = PixelCandy.bunnyMap.get(p);
					Entity e1 = el.get(1);
					Entity e2 = el.get(0);
					if(!p.getPassengers().contains(e2))
					{
						//System.out.println("killingHats");
						if(e2.getClass().equals(EntityPixelmon.class))
						{
							e2.offer(Keys.INVULNERABLE,false);
							e1.remove();
						}else{
							e1.remove();
							e2.remove();
						}
						if(e1.getClass().equals(EntityPixelmon.class))
						{
							e1.offer(Keys.INVULNERABLE,false);
							e2.remove();
							bunnyMap.remove(p);
						}else{
							e2.remove();
							e1.remove();
						}
						bunnyMap.remove(p);
					}
					else if(!e2.getPassengers().contains(e1))
					{
						//System.out.println("killingHats");
						if(e2.getClass().equals(EntityPixelmon.class))
						{
							e2.offer(Keys.INVULNERABLE,false);
							e1.remove();
						}else{
							e1.remove();
							e2.remove();
						}
						if(e1.getClass().equals(EntityPixelmon.class))
						{
							e1.offer(Keys.INVULNERABLE,false);
							e2.remove();
							bunnyMap.remove(p);
						}else{
							e2.remove();
							e1.remove();
						}
						bunnyMap.remove(p);
					}
				}
				 */
			}
		}).name("bp-entityHatHelper_t.100ms").submit(this);
		//playerListenHelper (5s)
		tb.interval(5, TimeUnit.SECONDS);
		tb.execute(new Runnable()
		{
			public void run() 
			{
				if(pList.isEmpty()==false)
				{
					for(Player p:pList)
					{
						//ItemType it = p.getEquipped(EquipmentTypes.CHESTPLATE).get().getType();
						ItemStack is = p.getEquipped(EquipmentTypes.CHESTPLATE).get();
						ItemStack wings = getElytra();
						int b = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(is, wings);

						if(b==0)
						{
							if(!pFly.containsKey(p))
							{
								p.offer(Keys.CAN_FLY,true);
								p.offer(Keys.IS_FLYING,true);
								pFly.put(p,true);
							}
						}
						else
						{
							if(pFly.containsKey(p))
							{
								p.offer(Keys.CAN_FLY,false);
								p.offer(Keys.IS_FLYING,false);
								pFly.remove(p);
							}
						}
					}
				}
			}
		}).name("bp-playerListener_t.5s").submit(this);
	}

	/**
	 * 
	 * @param min integer must be smaller than max
	 * @param max integer must be larger than min
	 * @return random int between min/max
	 */
	private static int rNum(int min, int max) 
	{

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	/**
	 * 
	 * used for itemRain and stuff
	 * @param p player
	 */
	public void spawnItem(Player p)
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
	/**
	 * 
	 * player use on entity (usually an animal or something)
	 * @param e Entity clicked on
	 * @param p player executed event
	 */
	@Listener
	public void useCandyEvent(InteractEntityEvent.Secondary.MainHand e, @Root Player p)
	{
		ItemStack candy = PixelCandy.getCandy();
		Entity ent = e.getTargetEntity();
		//((EntityPixelmon) ent).getPixelmonWrapper().getPlayerOwner();
		if(ent.getClass().equals(EntityPixelmon.class))
		{
			EntityPixelmon	ep = ((EntityPixelmon) ent);
			if(!ep.hasOwner())
			{
				return;	
			}
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
		}
	}

	/**
	 * check of the player is sneaking
	 * @param p player
	 * @return bool is the player sneaking
	 */
	public boolean isSneaking(Player p) 
	{
		return p.get(Keys.IS_SNEAKING).orElse(false);
	}
	@Listener
	public void entityCollide(CollideEntityEvent.Impact e, @Root Player p)
	{
		List<Entity> en = e.getEntities();
		Entity ent = bunnyMap2.get(p);
		if(en.contains(ent))
		{
			e.setCancelled(true);
		}
	}
	/**
	 * 
	 * @param e pickupItemEvent
	 */
	@Listener
	public void itemPickupEvent(ChangeInventoryEvent.Pickup e)
	{
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
	}

	/**
	 * for the entityHat
	 * @param e moveEvent
	 * @param p Player (only calls moveEvent if source is a player)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listener
	public void moveEvent(MoveEntityEvent e, @Root Player p)
	{
		if(ride==true)
		{
			return;
		}
		if(bunnyMap2.containsKey(p)) 
		{
			Entity ent = bunnyMap2.get(p);
			if(ent.getPassengers().contains(p))
			{
				ent.offer(Keys.HAS_GRAVITY,true);
				ent.offer(Keys.INVULNERABLE,false);
				bunnyMap2.remove(p);
				return;
			}

			Location loc = p.getLocation();
			loc=loc.add(0,2,0);
			//ent.offer(Keys.HAS_GRAVITY,false);
			//ent.offer(Keys.INVULNERABLE,true);
			ent.setLocation(loc);
		}
	}
	//TODO finish up the ruler and test it. (watch with name 'ruler')
	/**
	 * 
	 * this is for the ruler
	 * @param e blockClicked
	 * @param p Player (only calls if the source is a player)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Listener
	public void blockClickEvent(InteractBlockEvent.Secondary.MainHand  e, @Root Player p)
	{
		Location loc = e.getTargetBlock().getLocation().get();
		HashMap<Integer,Location> l= new HashMap<Integer,Location>();
		if(rulerMap.containsKey(p))
		{
			l = rulerMap.get(p);
			if(l.containsKey(1))
			{
				l.clear();
				return;
			}
			if(l.containsKey(0))
			{
				//getDistance
				Location a = l.get(0);
				Location b = l.get(1);
				double d = a.getPosition().distance(b.getPosition());
				Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
						.append(Text.builder("Distance ").color(TextColors.BLUE)
						.append(Text.builder(d+" ").color(TextColors.YELLOW)
						.append(Text.builder("seconds.").color(TextColors.RED)
						.build()).build()).build()).build();
				p.sendMessage(msg);
				return;
			}

		}
		l.put(0, loc);
		rulerMap.put(p, l);
	}
	/**
	 * 
	 * this is part of the entityHats stuff
	 * @param e interactEvent
	 * @param p Player (only calls if the source is a player)
	 */
	@Listener
	public void entityUseEvent(InteractEntityEvent.Secondary.MainHand e, @Root Player p)
	{
		//System.out.println(e.getTargetEntity());
		if(!bMapTog.contains(p)) 
		{
			return;
		}
		if(isSneaking(p)==false)
		{
			return;
		}
		if(p.hasPermission("bex.fun.superhats"))
		{

			Entity ent = e.getTargetEntity();
			//
			//New Style, moveEntity
			//

			if(ride==false)
			{
				Boolean isTargetPK = ent.getClass().equals(EntityPixelmon.class);
				//Boolean isPlayer = ent

				if(isTargetPK==false)
				{
					return;
				}
				EntityPixelmon ep = (EntityPixelmon) ent;
				if(ent==bunnyMap2.get(p)) 
				{
					Entity en = bunnyMap2.get(p);
					Boolean isHatPK = en.getClass().equals(EntityPixelmon.class);
					ent.offer(Keys.HAS_GRAVITY,true);
					ent.offer(Keys.INVULNERABLE,false);

					if(isHatPK==false)
					{
						en.remove();
					}
					bunnyMap2.remove(p);
					return;
				}

				if(ep.hasOwner()==false)
				{
					return;
				}

				if(ep.hasOwner()==true&&ep.belongsTo((EntityPlayer) p)==false)
				{
					return;
				}

				if(ep.hasOwner()==true&&ep.belongsTo((EntityPlayer)p)==true)
				{

					if(bunnyMap2.containsKey(p)) 
					{
						Entity en = bunnyMap2.get(p);
						Boolean isHatPK = en.getClass().equals(EntityPixelmon.class);

						en.offer(Keys.HAS_GRAVITY,true);
						ent.offer(Keys.HAS_GRAVITY,false);
						ent.offer(Keys.INVULNERABLE,true);
						if(isHatPK==false)
						{
							en.remove();
						}
						bunnyMap2.remove(p);
						bunnyMap2.put(p,ent);
						return;
					}
					ent.offer(Keys.HAS_GRAVITY,false);
					ent.offer(Keys.INVULNERABLE,true);
					bunnyMap2.put(p,ent);
					return;
				}
				return;
			}

			//
			//Old style, entity passengers
			//
			/*
			if(bunnyMap.containsKey(p)) 
			{

				//System.out.println("changing target");
				List<Entity> el = PixelCandy.bunnyMap.get(p);
				Entity e1 = el.get(1);
				Entity e2 = el.get(0);
				if(p.getPassengers().contains(e2)==true)
				{
					//System.out.println("Passengers e2");
					if(e1==ent)
					{
						//System.out.println("ent==e1");
						e1.offer(Keys.INVULNERABLE,false);
						e2.remove();
						bunnyMap.remove(p);
						return;
					}
					if(e2==ent)
					{

						//System.out.println("ent==e2");
						e1.offer(Keys.INVULNERABLE,false);
						e2.remove();
						bunnyMap.remove(p);
						return;
					}
				}
				else
				{
					//System.out.println("notPassengers");
					if(e1.getClass().equals(EntityPixelmon.class))
					{
						//System.out.println("e1 = pixelmon");
						e1.offer(Keys.INVULNERABLE,false);
						bunnyMap.remove(p);
						return;
					}
					else if(e1.getClass().equals(EntityPixelmon.class)==false)
					{
						//System.out.println("e1 != pixelmon");
						e1.remove();
						bunnyMap.remove(p);
						return;
					}
					if(e2.getClass().equals(EntityPixelmon.class))
					{
						//System.out.println("e2 = pixelmon");
						e2.offer(Keys.INVULNERABLE,false);
						bunnyMap.remove(p);
						return;
					}else if(e2.getClass().equals(EntityPixelmon.class)==false)
					{
						//System.out.println("e1 != pixelmon");
						e2.remove();
						bunnyMap.remove(p);
						return;
					}

				}
			}
			
			Location loc = p.getLocation();
			Entity e1 = ent;
			Entity e2 = p.getWorld().createEntity(EntityTypes.RABBIT, p.getLocation().getPosition());
			if(!ent.getClass().equals(EntityPixelmon.class))
			{
				e1 = p.getWorld().createEntity(ent.getType(),p.getLocation().getPosition());
			}
			PotionEffect pt = PotionEffect.builder()
					.potionType(PotionEffectTypes.INVISIBILITY)
					.duration(Integer.MAX_VALUE)
					.amplifier(5)
					.build();
			PotionEffectData effects = e2.getOrCreate(PotionEffectData.class).get();
			effects.addElement(pt);
			e2.offer(Keys.AGE,Integer.MIN_VALUE);
			e2.offer(effects);
			e2.offer(Keys.INVULNERABLE,true);
			e1.offer(Keys.INVULNERABLE,true);
			if(!ent.getClass().equals(EntityPixelmon.class))
			{
				loc.spawnEntity(e1);
			}
			loc.spawnEntity(e2);
			e2.addPassenger(e1);
			p.addPassenger(e2);
			List<Entity> le = new ArrayList<Entity>();
			le.add(e2);
			le.add(e1);
			bunnyMap.remove(p);
			bunnyMap.put(p, le);
		*/
		}

	}
	/**
	 * this is to make cake and healers drop when broken
	 * @param e blockBreak
	 * @param p Player (only calls if the source is a player)
	 */
	@Listener
	public void blockBreakEvent(ChangeBlockEvent.Break e, @Root Player p)
	{
		GameModeData data = p.getGameModeData();
		GameMode gm = data.get(Keys.GAME_MODE).get();
		if(gm.equals(GameModes.CREATIVE))
		{
			return;
		}
		BlockSnapshot bs = e.getTransactions().get(0).getOriginal();
		BlockType h = Sponge.getGame().getRegistry().getType(BlockType.class,"pixelmon:healer").get();
		BlockType c = BlockTypes.CAKE;
		BlockType b = bs.getState().getType();
		//System.out.println(b);
		if(b==h)
		{	
			ItemStack im = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "pixelmon:healer").get());
			im.setQuantity(1);
			p.getInventory().offer(im);
		}
		if(b==c)
		{
			ItemStack im = ItemStack.of(ItemTypes.CAKE);
			im.setQuantity(1);
			p.getInventory().offer(im);
		}
	}

	@Listener
	public void itemDropEvent(DropItemEvent.Destruct e)
	{
		Optional<BlockSnapshot> bs = e.getCause().getContext().get(EventContextKeys.BLOCK_HIT);

		try{
			BlockType bss = bs.get().getState().getType();
			BlockType h = Sponge.getGame().getRegistry().getType(BlockType.class,"pixelmon:healer").get();
			if(bss==h)
			{
				e.setCancelled(true);
			}
		}catch (NoSuchElementException ex)
		{
			return;
		}
	}


}



