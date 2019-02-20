package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;

public class ClearHatCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawn a set of wings"))
			.permission("bex.fun.superhats")
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					Boolean isBex = false;
					if(src instanceof Player)
					{
						if(((Player) src).getUniqueId()==PixelCandy.bex)
						{
							isBex=true;
						}

						if(src.hasPermission("bex.fun.superhats")||isBex==true)
						{
							Player p = (Player)src;
							if(PixelCandy.bunnyMap2.containsKey(p))
							{
								PixelCandy.bunnyMap2.get(p).offer(Keys.HAS_GRAVITY,true);
								PixelCandy.bunnyMap2.remove(p);
								Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
										append(Text.builder("hat cleared").color(TextColors.GREEN).build()).build();
								p.sendMessage(msg);
							}
							else 
							{
								Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
										append(Text.builder("You arent wearing a hat").color(TextColors.GREEN).build()).build();
								p.sendMessage(msg);
							}
						}
					}
					else
					{
						src.sendMessage(Text.of("[bPack] You must be a player to run this command"));
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
