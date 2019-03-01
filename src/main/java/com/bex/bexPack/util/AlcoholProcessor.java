package com.bex.bexPack.util;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Plugin(id="alcohol", name="Alcohol")
public class AlcoholProcessor
{	
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	public File configuration = null;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	public ConfigurationLoader<CommentedConfigurationNode> configurationLoader = null;
	
	public static CommentedConfigurationNode configurationNode = null;
	
	@Listener
	public void preInit(GamePreInitializationEvent e) throws IOException, ObjectMappingException
	{
		try {
			if (!configuration.exists()) {
				configuration.createNewFile();
				configurationNode = configurationLoader.load();
				
				/*
				configurationNode.setComment("Formula for calculating expected profit:");
				configurationNode.setComment("\t((chance of winning)*(reward - cost)) + ((1 - chance of winning)*(-cost))");
				configurationNode.setComment("\tEx: For a potion whose cost is 1000, reward is 7500,000 and chance of winning is 1/8192:");
				configurationNode.setComment("\t((1/8192)*(7500000 - 1000)) + ((1 - (1/8192))*(-1000)) = -84.47");
				configurationNode.setComment("\tTherefore, the player is expected to make a -84.47 profit on the shiny drink");
				
				configurationNode.getNode("drink", "doublenothing", "name").setValue("Double or Nothing");
				configurationNode.getNode("drink", "doublenothing", "lore").setValue("50% chance to double the amount you spent");
				configurationNode.getNode("drink", "doublenothing", "cost").setValue(1000);
				configurationNode.getNode("drink", "doublenothing", "reward").setValue(2000);
				configurationNode.getNode("drink", "doublenothing", "numerator").setValue(50);
				configurationNode.getNode("drink", "doublenothing", "denominator").setValue(100);
				configurationNode.getNode("drink", "doublenothing", "broadcast").setValue(false);
				
				configurationNode.getNode("drink", "shiny", "name").setValue("Shiny Beverage");
				configurationNode.getNode("drink", "shiny", "lore").setValue("1/8192 chance to get 14,336,000!");
				configurationNode.getNode("drink", "shiny", "cost").setValue(1750);
				configurationNode.getNode("drink", "shiny", "reward").setValue(14336000);
				configurationNode.getNode("drink", "shiny", "numerator").setValue(1);
				configurationNode.getNode("drink", "shiny", "denominator").setValue(8192);
				configurationNode.getNode("drink", "shiny", "broadcast").setValue(true);
				*/
				
				configurationLoader.save(configurationNode);
			}
			configurationNode = configurationLoader.load();
		}catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	public static CommentedConfigurationNode getRoot() {
		return configurationNode;
	}
	
	public static String getString(String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode("drink", id, s).getString();
	}
	
	
	public static int getInt(String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode("drink", id, s).getInt();
	}
	
	public static Boolean getBool(String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode("drink", id, s).getBoolean();
	}
}