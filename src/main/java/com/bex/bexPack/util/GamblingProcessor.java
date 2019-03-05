package com.bex.bexPack.util;

import java.io.File;
import java.io.IOException;

import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

@Plugin(id="pcangamble", name="pcangamble")
public class GamblingProcessor
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
				configurationLoader.save(configurationNode);
			}
			configurationNode = configurationLoader.load();
		}catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	@Listener
	public void reload(GameReloadEvent event) throws IOException, ObjectMappingException {
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
	
	public static CommentedConfigurationNode getRoot() {
		return configurationNode;
	}
	
	public static String getString(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getString();
	}
	
	
	public static int getInt(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getInt();
	}
	
	public static Boolean getBool(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getBoolean();
	}
}