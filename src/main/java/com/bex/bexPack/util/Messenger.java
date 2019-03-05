package com.bex.bexPack.util;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;



public class Messenger
{
	

	/**
	 * Construct a message and send it to the entire server
	 * @param src command source, usually a player
	 * @param message message in full
	 * @param color message color TextColors.<color>
	 */
	public static void broadcastMessage(String message,TextColor color)
	{
		Text msg = Text.builder("[pcan] ").color(TextColors.LIGHT_PURPLE).
				append(Text.builder(message).color(color).build()).build();
		
		Sponge.getServer().getBroadcastChannel().send(msg);
	}
	/**
	 * Construct a 2 part message and send it to the entire server
	 * @param src = command source, usually a player
	 * @param message1 = first part of message, colored by color1 
	 * @param color1 = TextColors.<color>
	 * @param message2 = second part of message, colored by color2
	 * @param color2 = TextColors.<color>
	 */
	public static void broadcastComplexMessage(String message1,TextColor color1,String message2,TextColor color2)
	{
		Text msg = Text.builder("[pcan] ").color(TextColors.LIGHT_PURPLE).
				append(Text.builder(message1).color(color1).append(Text.builder(message2).color(color2)
						.build()).build()).build();
		Sponge.getServer().getBroadcastChannel().send(msg);
	}
	/**
	 * Construct a message and send it to the player
	 * @param src command source, usually a player
	 * @param message message in full
	 * @param color message color TextColors.<color>
	 */
	public static void sendMessage(CommandSource src, String message,TextColor color)
	{
		Text msg = Text.builder("[pcan] ").color(TextColors.LIGHT_PURPLE).
				append(Text.builder(message).color(color).build()).build();
		src.sendMessage(msg);
	}
	/**
	 * Construct a 2 part message and send it to the player
	 * @param src = command source, usually a player
	 * @param message1 = first part of message, colored by color1 
	 * @param color1 = TextColors.<color>
	 * @param message2 = second part of message, colored by color2
	 * @param color2 = TextColors.<color>
	 */
	public static void sendComplexMessage(CommandSource src,String message1,TextColor color1,String message2,TextColor color2)
	{
		Text msg = Text.builder("[pcan] ").color(TextColors.LIGHT_PURPLE).
				append(Text.builder(message1).color(color1).append(Text.builder(message2).color(color2)
						.build()).build()).build();
		src.sendMessage(msg);
	}
	/**
	 * returns "You must be a player to run this command"
	 * @param src command source
	 */
	public static void sendMessageNotPlayer(CommandSource src)
	{
		Text msg = Text.builder("[pcan] ").color(TextColors.LIGHT_PURPLE).
				append(Text.builder("You must be a player to run this command").color(TextColors.RED).build()).build();
		src.sendMessage(msg);
	}

}
