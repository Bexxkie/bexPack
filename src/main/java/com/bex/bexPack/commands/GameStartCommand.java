package com.bex.bexPack.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;

public class GameStartCommand 
{

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Start a game"))
			.permission("bex.game.admin")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("target"))),
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("allowedGuesses"))),
					GenericArguments.optional(GenericArguments.integer(Text.of("timeLimit")), 60))
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{

					String target = args.<String>getOne("target").get().toLowerCase();
					int guesses = args.<Integer>getOne("allowedGuesses").get();
					int timeLimit = args.<Integer>getOne("timeLimit").get();
					if(!PixelCandy.gameTargetWordAndTime.isEmpty())
					{
						Messenger.sendMessage(src, "There is already a game in progress");
						return CommandResult.success();
					}
					if(timeLimit < 10)
					{
						Messenger.sendMessage(src, "Minumum time limit is 10s, set to 10s");
						timeLimit = 10;
					}
					if(timeLimit > 1200)
					{
						Messenger.sendMessage(src, "Maximum time limit is 120s, set to 1200s");
						timeLimit = 1200;
					}
					if(guesses < 1)
					{
						Messenger.sendMessage(src, "Minumum guess is 1, set to 1");
						guesses=1;
					}
					if(guesses > 10)
					{
						Messenger.sendMessage(src, "Maximum guess is 10, set to 10");
						guesses=1;
					}
					//PixelCandy.gameTime=timeLimit;
					//PixelCandy.gameTargetWord=target;
					PixelCandy.gameTargetWordAndTime.put(target, timeLimit);
					PixelCandy.gameGuesses.clear();
					for(Player p :Sponge.getServer().getOnlinePlayers())
					{
						PixelCandy.gameGuesses.put(p, guesses);
					}
					Messenger.broadcastMessage(((Player)src).getName()+" Started a game. Allowed guesses: "+guesses+". Time limit: "+timeLimit+" seconds", TextColors.GOLD);
					Messenger.broadcastMessage("use /pcan g <yourGuess> to play", TextColors.GOLD);

					return CommandResult.success();
				}
			})
			.build();


	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
