package com.bex.bexPack.commands;

import java.util.HashMap;
import java.util.Random;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.RabbitType;
import org.spongepowered.api.data.type.RabbitTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.RandomNum;

@SuppressWarnings("unused")
public class PokeRain 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Rain pokemon"))
			.permission("bex.fun.superRain")
			.arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))))
			.executor(new CommandExecutor() {

				@SuppressWarnings({ "rawtypes", "unchecked" })
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
					if(src.hasPermission("bex.fun.superRain")||isBex==true)
					{
						Player pt = args.<Player>getOne("player").get();

						if(PixelCandy.Cooldowns.containsKey(pt))
						{
							int remT = PixelCandy.Cooldowns.get(pt);
							Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
									.append(Text.builder("This player cannot be targeted for another ").color(TextColors.RED)
											.append(Text.builder(remT+" ").color(TextColors.YELLOW)
													.append(Text.builder("seconds.").color(TextColors.RED)
															.build()).build()).build()).build();
							src.sendMessage(msg);
							return CommandResult.success();
						}
						if(pt.isOnline()==false)
						{
							Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
									.append(Text.builder("Player must be online").color(TextColors.RED)
															.build()).build();
							src.sendMessage(msg);
						}
						PixelCandy.Cooldowns.put(pt, 60);	
						HashMap<Entity,Integer> elist = new HashMap<Entity,Integer>();
						for(int x = 0;x < 10;++x)
						{
							
							Location loc = pt.getLocation();
							Entity ent = pt.getWorld().createEntity(EntityTypes.RABBIT, pt.getLocation().getPosition());
							int tp = RandomNum.rNum(1, 6);
							RabbitType t = getType(tp);
							//RabbitType t = RabbitTypes.KILLER;
							ent.offer(Keys.RABBIT_TYPE, t);
							ent.offer(Keys.ANGRY,true);
							ent.offer(Keys.INVULNERABLE,true);
							elist.put(ent,10);
							PixelCandy.curseMap.put(pt,elist);
							loc=loc.add(RandomNum.rNum(-3,3),RandomNum.rNum(2,8),RandomNum.rNum(-3,3));
							loc.spawnEntity(ent);
							ent.setLocation(loc);
						}
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}

	public RabbitType getType(int i)
	{
		switch(i)
		{
		case 1: 
			return RabbitTypes.BLACK;
		case 2: 
			return RabbitTypes.BLACK_AND_WHITE;
		case 3: 
			return RabbitTypes.BROWN;
		case 4: 
			return RabbitTypes.GOLD;
		case 5: 
			return RabbitTypes.SALT_AND_PEPPER;
		case 6: 
			return RabbitTypes.WHITE;
		}
		return RabbitTypes.BROWN;	


	}
}
