package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.text.Text;

import com.bex.bexPack.main.PixelCandy;



public class ConvertCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Convert a piece of candy"))
			.permission("bex.candies.convert")
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

						if(src.hasPermission("bex.candies.convert")||isBex==true)
						{
							ItemStack candyBase = PixelCandy.getCandy();
							ItemStack candyRare = PixelCandy.getCandyRare();

							Player p = (Player) src;
							ItemStack im = p.getItemInHand(HandTypes.MAIN_HAND).get();
							int q = im.getQuantity();
							System.out.println("convert");
							if(ItemStackComparators.IGNORE_SIZE.compare(im,candyRare)==0)
							{
								candyBase.setQuantity(q);
								p.setItemInHand(HandTypes.MAIN_HAND, candyBase);
							}
							else if (ItemStackComparators.IGNORE_SIZE.compare(im,candyBase)==0)
							{
								candyRare.setQuantity(q);
								p.setItemInHand(HandTypes.MAIN_HAND, candyRare);
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
