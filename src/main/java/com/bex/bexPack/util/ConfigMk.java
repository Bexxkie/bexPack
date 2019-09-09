package com.bex.bexPack.util;

import java.io.IOException;

import com.bex.bexPack.main.PixelCandy;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class ConfigMk 
{
	public CommentedConfigurationNode getRoot() {
		return PixelCandy.configurationNode;
	}
	
	public String getString(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getString();
	}
	
	
	public int getInt(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getInt();
	}
	
	public Boolean getBool(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getBoolean();
	}
	/**
	 * 
	 * @param path node.id.s
	 * @param value whatever you want to apply
	 */
	public void save(String path,Object value)
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
	
	public Double getDouble(String node, String id, String s) {
		CommentedConfigurationNode root = getRoot();
		return root.getNode(node, id, s).getDouble();
	}

}
