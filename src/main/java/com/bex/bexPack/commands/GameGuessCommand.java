package com.bex.bexPack.commands;

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

public class GameGuessCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("participate in a game"))
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("guessWord"))))
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					if(src instanceof Player==false)
					{
						Messenger.sendMessageNotPlayer(src);
						return CommandResult.success();
					}
					if(PixelCandy.gameTargetWordAndTime.isEmpty()||PixelCandy.gameGuesses.isEmpty())
					{
						Messenger.sendMessage(src, "No game in progress", TextColors.RED);
						return CommandResult.success();
					}
					if(!PixelCandy.gameGuesses.containsKey((Player)src))
					{
						Messenger.sendMessage(src, "You are out of guesses", TextColors.RED);
						return CommandResult.success();
					}

					Player p = (Player)src;
					String guessWord = args.<String>getOne("guessWord").get().trim().toLowerCase();
					//String target = PixelCandy.gameTargetWord;
					Object[] target = PixelCandy.gameTargetWordAndTime.keySet().toArray();
					String tar = target[0].toString().trim().toLowerCase();
					int remainingGuesses = PixelCandy.gameGuesses.get(p);
					int isSame = tar.compareTo(guessWord);
					if(isSame==0)
					{	
						String gMess = "Won with "+remainingGuesses+" guesses left with ";
						if(remainingGuesses == 1)
						{
							gMess = "Won on their last guess with ";
						}
						Messenger.broadcastComplexMessage(p.getName()+ gMess, TextColors.GOLD, guessWord, TextColors.GREEN);
						PixelCandy.gameGuesses.clear();
						PixelCandy.gameTargetWordAndTime.clear();
					}
					else
					{
						remainingGuesses=remainingGuesses-1;
						PixelCandy.gameGuesses.replace(p, remainingGuesses);
						if(remainingGuesses <=0)
						{
							Messenger.sendMessage(src, "That was your last guess, thanks for playing ", TextColors.RED);
							PixelCandy.gameGuesses.remove(p);
						}
						else
						{
							Messenger.sendComplexMessage(src, "Incorrect guess, remaining ", TextColors.RED, remainingGuesses+"", TextColors.YELLOW);
						}

					}
					return CommandResult.success();
				}
			})
			.build();


	public CommandSpec getCommandSpec() {
		return commandSpec;
	}


}
