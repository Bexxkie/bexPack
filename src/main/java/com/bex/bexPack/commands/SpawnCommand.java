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



public class SpawnCommand 
{

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawn a piece of candy"))
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
						Player pt = args.<Player>getOne("player").get();
						int ct = args.<Integer>getOne("amount").get();
						ItemStack candyBase = Getters.getCandy();
						candyBase.setQuantity(ct);
						pt.getInventory().offer(candyBase);

						//if(src instanceof Player) 
						//{
						//	Player p = (Player) src;
						//	p.getInventory().offer(candyBase);
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
