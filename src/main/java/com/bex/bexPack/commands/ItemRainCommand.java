package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;

public class ItemRainCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Shower roses over a player"))
			.permission("bex.fun.rain")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
					GenericArguments.optional(GenericArguments.integer(Text.of("time")), 10))
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
					if(src.hasPermission("bex.fun.rain")||isBex==true)
					{
						Player p = (Player)src;
						Player pt = args.<Player>getOne("player").get();
						int time = args.<Integer>getOne("time").get();

						if(PixelCandy.Cooldowns.containsKey(pt)&&!src.hasPermission("bex.fun.override"))
						{
							int remT = PixelCandy.Cooldowns.get(pt);
							
							Messenger.sendComplexMessage(src, "This player is under cooldown, seconds remaining:", TextColors.RED, remT+"", TextColors.YELLOW);
							//Text msg = Text.builder(Getters.getPrefix().toString())
							//		.append(Text.builder("This player cannot be targeted for another ").color(TextColors.RED)
							//				.append(Text.builder(remT+" ").color(TextColors.YELLOW)
							//						.append(Text.builder("seconds.").color(TextColors.RED)
							//								.build()).build()).build()).build();
							//src.sendMessage(msg);
							return CommandResult.success();
						}	
						if(pt.isOnline()==false)
						{
							Messenger.sendMessage(src, "Player must be online", TextColors.RED);
							//Text msg = Text.builder(Getters.getPrefix().toString())
							//		.append(Text.builder("Player must be online").color(TextColors.RED)
							//				.build()).build();
							//src.sendMessage(msg);
						}
						if(!src.hasPermission("bex.fun.override"))
						{
							PixelCandy.Cooldowns.put(pt, 30);
						}
						if(time > 15)
						{time = 15;}

						if(time <1)
						{time=1;}

						ItemStack inHand = p.getItemInHand(HandTypes.MAIN_HAND).get();

						if(!inHand.equals(ItemStack.of(ItemTypes.AIR)))
						{
							ItemStack iRain = inHand.copy();
							iRain.offer(Keys.DISPLAY_NAME,Text.of("bex.item.rain"));
							PixelCandy.ItemRainMap.put(pt, iRain);
						}
						//Player p = (Player) src;
						PixelCandy.rainTime.put(pt, time);
						return CommandResult.success();
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}	

}

