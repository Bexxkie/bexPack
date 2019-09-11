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
import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.FxHandler;
import com.bex.bexPack.util.Messenger;

public class FxBaseCommand 
{
	
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Fx base command"))
			.permission("bex.fun.fx")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("optA"))),
					GenericArguments.optional(GenericArguments.integer(Text.of("optB")),"none"))
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

						if(src.hasPermission("bex.fun.fx")||isBex==true)
						{
							Player p = (Player)src;
							String oa = args.<String>getOne("optA").get().toLowerCase();
							String ob = args.<String>getOne("optB").get().toLowerCase();
							
							if(oa == "toggle")
							{
								//dostuff
								Boolean b = Boolean.valueOf(ob);
								
								
							}
							if(oa == "set")
							{
								//Check if type is in particleList
								if(FxHandler.ParticleList.containsKey(ob.toUpperCase()))
								{
									//set type to type entered
									
									
								}
								
							}
							
						}
					}
					else
					{
						Messenger.sendMessageNotPlayer(src);
						//src.sendMessage(Text.of(Getters.getPrefix()+"You must be a player to run this command"));
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}

}
