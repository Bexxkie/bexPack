package com.bex.bexPack.util;

import java.io.IOException;

import com.bex.bexPack.main.PixelCandy;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigMk 
{
	public static CommentedConfigurationNode getRoot() {
		return PixelCandy.configurationNode;
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
	/**
	 * 
	 * @param path node.id.s
	 * @param value whatever you want to apply
	 */
	public static void save(String path,Object value)
	{
		CommentedConfigurationNode root = getRoot();
		root.getNode(path).setValue(true);
		try {
			PixelCandy.configurationLoader.save(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Double getDouble(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getDouble();
	}

}
