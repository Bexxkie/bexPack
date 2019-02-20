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

public class SpawnEggCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawn an incubator"))
			.permission("bex.candies.spawn")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
					GenericArguments.optional(GenericArguments.integer(Text.of("amount")), 1))
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
					}
					if(src.hasPermission("bex.candies.spawn")||isBex==true)
					{
						ItemStack incubator = PixelCandy.getIncubator();
						Player pt = args.<Player>getOne("player").get();
						int ct = args.<Integer>getOne("amount").get();
						incubator.setQuantity(ct);
						pt.getInventory().offer(incubator);

						//if(src instanceof Player) 
						//{
						//	Player p = (Player) src;
						//	p.getInventory().offer(incubator);
						//}
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
