package com.bex.bexPack.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.FallingBlock;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
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
import com.bex.bexPack.util.FxHandler;
import com.bex.bexPack.util.Schedulars;
import com.google.inject.Inject;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;


@Plugin (name = PixelCandy.NAME,
id = "pixelcandy",
version = PixelCandy.VERSION,
authors = PixelCandy.AUTHOR,
description = PixelCandy.DESC,
dependencies = {@Dependency(id = "pixelmon")})
public class PixelCandy 
{
	public static final String NAME = "pixelCandy";
	public static final String VERSION = "1.2.4i";
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
	public static HashMap<Player, EntityPixelmon> disguiseMap= new HashMap<Player, EntityPixelmon>();
	public static HashMap<Player,Integer> gameGuesses = new HashMap<Player,Integer>();
	public static HashMap<String,Integer> gameTargetWordAndTime = new HashMap<String,Integer>();
	@SuppressWarnings("rawtypes")
	public static HashMap<Player,HashMap<Location,BlockType>> enchantingBlockMap = new HashMap<Player, HashMap<Location,BlockType>>();
	@SuppressWarnings("rawtypes")
	public static HashMap<Player,Location> rulerMap = new HashMap<Player, Location>();
	//public static ArrayList<Player> particleList = new ArrayList<Player>();
	public static HashMap<Player, ArrayList<Object>> particleMap = new HashMap<Player, ArrayList<Object>>();
	public static Boolean ride = false;
	public static UUID bex = UUID.fromString("7c4958de-7a27-4b58-ac97-947142459d76");
	public static Task.Builder tb = Task.builder();

	@Inject
	@DefaultConfig(sharedRoot = false)
	public File configuration = null;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	public static ConfigurationLoader<CommentedConfigurationNode> configurationLoader = null;
	
	public static CommentedConfigurationNode configurationNode = null;
	
	@Listener
	public void preInit(GamePreInitializationEvent e) throws IOException, ObjectMappingException
	{
		try {
			if (!configuration.exists()) {
				configuration.createNewFile();
				configurationNode = configurationLoader.load();
				configurationLoader.save(configurationNode);
			}
			configurationNode = configurationLoader.load();
		}catch (IOException exc) {
			exc.printStackTrace();
		}
	}
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
		Sponge.getEventManager().registerListeners(this, new SlotEvents());
		FxHandler.initMap();
	}


	@Listener
	public void onServerStarted(GameStartedServerEvent e) 
	{
		Schedulars.run();
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
}



