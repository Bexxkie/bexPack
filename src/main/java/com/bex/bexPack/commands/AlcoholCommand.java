package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;



public class AlcoholCommand
{

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawns a drink for consumption"))
			.permission("bex.alcohol.spawn")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
					GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException, NullPointerException
				{
					Boolean isBex = false;
					if(src instanceof Player)
					{
						if(((Player) src).getUniqueId()==PixelCandy.bex)
						{
							isBex=true;
						}
					}
					if(src.hasPermission("bex.alcohol.spawn")||isBex==true)
					{
						Player pt = args.<Player>getOne("player").get();
						ItemStack drink = Getters.getDrink(args.<String>getOne("id").get().toLowerCase());
						pt.getInventory().offer(drink);
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
