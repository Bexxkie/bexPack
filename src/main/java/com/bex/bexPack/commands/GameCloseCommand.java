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

public class GameCloseCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("stop a game"))
			.permission("bex.game.admin")
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					if(!PixelCandy.gameTargetWordAndTime.isEmpty())
					{
						PixelCandy.gameTargetWordAndTime.clear();
						PixelCandy.gameGuesses.clear();
						Messenger.sendMessage(src, "Stopped the game in progress");
						Messenger.broadcastMessage("Game was cancelled by "+((Player)src).getName(), TextColors.RED);
					}
					Messenger.sendMessage(src, "No game in progress");
					return CommandResult.success();
				}
			})
			.build();


	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
