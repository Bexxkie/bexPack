package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;

public class ToggleHat 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("toggle hat selections"))
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

							if(PixelCandy.bMapTog.contains(p))
							{
								PixelCandy.bMapTog.remove(p);
								Messenger.sendComplexMessage(src, "hat equips",TextColors.GREEN, "disabled", TextColors.YELLOW);
								//Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
								//		append(Text.builder("hat equips disabled").color(TextColors.GREEN).build()).build();
								//p.sendMessage(msg);
							}
							else 
							{
								PixelCandy.bMapTog.add(p);
								Messenger.sendComplexMessage(src, "hat equips",TextColors.GREEN, "enabbled", TextColors.YELLOW);
								//Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
								//		append(Text.builder("hat equips enabled").color(TextColors.GREEN).build()).build();
								//p.sendMessage(msg);

							}
						}
					}
					else
					{
						Messenger.sendMessageNotPlayer(src);
						//src.sendMessage(Text.of("[bPack] You must be a player to run this command"));
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}

}
