package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;

public class DisguiseClearCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("toggle hat selections"))
			.permission("bex.disguise.disguise")
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					if(src instanceof Player==false)
					{
						Messenger.sendMessageNotPlayer(src);
						return CommandResult.success();
					}
					Player p = (Player)src;
					if(PixelCandy.disguiseMap.containsKey(p))
					{
						Entity ent = (Entity) PixelCandy.disguiseMap.get(p);
						ent.remove();
						p.offer(Keys.VANISH,false);
						p.offer(Keys.INVISIBLE,false);
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
