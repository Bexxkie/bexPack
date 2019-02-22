package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.ParrotVariant;
import org.spongepowered.api.data.type.ParrotVariants;
import org.spongepowered.api.data.type.RabbitType;
import org.spongepowered.api.data.type.RabbitTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class HatCommand 
{
	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawn an incubator"))
			.permission("bex.fun.hat")
			.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("entity"))),
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("type"))))
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					Boolean isBex = false;
					if(src instanceof Player)
					{
						if(((Player) src).getUniqueId()==PixelCandy.bex)
						{isBex=true;}


						if(src.hasPermission("bex.fun.hat")||isBex==true)
						{
							Player p = (Player) src;
							String type = args.<String>getOne("entity").get();
							int rt = args.<Integer>getOne("type").get();

							switch(type)
							{
							case "bunny":
								bunnyHat(rt,p);
								return CommandResult.success();
							case "parrot":
								parrotHat(rt,p);
								return CommandResult.success();
							default:
								src.sendMessage(Text.of("[bPack] Must choose a type and skin, [bunny,parrot] [1-5]"));
								return CommandResult.success();
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

	@SuppressWarnings("rawtypes")
	public void bunnyHat(int rt,Player p)
	{
		if(rt==0)
		{
			//
			//new
			//
			if(PixelCandy.ride==false)
			{
				if(PixelCandy.bunnyMap2.containsKey(p)) {

					Entity ent = PixelCandy.bunnyMap2.get(p);
					if(!ent.getClass().equals(EntityPixelmon.class))
					{
						ent.remove();
					}
					ent.offer(Keys.HAS_GRAVITY,true);
					ent.offer(Keys.INVULNERABLE,false);
					PixelCandy.bunnyMap2.remove(p);
					return;
				}
			}
		}
		if(rt<1||rt>5)
		{
			Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
					append(Text.builder("Type must be between 1-6").color(TextColors.RED).build()).build();
			p.sendMessage(msg);
			return;
		}
		if(PixelCandy.ride==false&&PixelCandy.bunnyMap2.containsKey(p))
		{
			Entity ent = PixelCandy.bunnyMap2.get(p);
			if(!ent.getClass().equals(EntityPixelmon.class))
			{
				ent.remove();
			}
			ent.offer(Keys.HAS_GRAVITY,true);
			ent.offer(Keys.INVULNERABLE,false);
			PixelCandy.bunnyMap2.remove(p);
		}

		RabbitType t = getBunnyType(rt);
		Location loc = p.getLocation();
		Entity e = p.getWorld().createEntity(EntityTypes.RABBIT, p.getLocation().getPosition());

		e.offer(Keys.RABBIT_TYPE, t);
		e.offer(Keys.INVULNERABLE,true);
		loc.spawnEntity(e);
		if(PixelCandy.ride==false)
		{
			e.offer(Keys.HAS_GRAVITY,false);
			e.offer(Keys.INVULNERABLE,true);
			PixelCandy.bunnyMap2.put(p, e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void parrotHat(int rt,Player p)
	{
		if(rt==0)
		{

			if(PixelCandy.ride==false)
			{
				if(PixelCandy.bunnyMap2.containsKey(p)) {
					Entity ent = PixelCandy.bunnyMap2.get(p);
					if(!ent.getClass().equals(EntityPixelmon.class))
					{
						ent.remove();
					}
					ent.offer(Keys.HAS_GRAVITY,true);
					ent.offer(Keys.INVULNERABLE,false);
					PixelCandy.bunnyMap2.remove(p);
					return;
				}
			}

		}
		if(rt<1||rt>5)
		{
			Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
					append(Text.builder("Type must be between 1-5").color(TextColors.RED).build()).build();
			p.sendMessage(msg);
			return;
		}

		if(PixelCandy.ride==false&&PixelCandy.bunnyMap2.containsKey(p))
		{
			Entity ent = PixelCandy.bunnyMap2.get(p);
			if(!ent.getClass().equals(EntityPixelmon.class))
			{
				ent.remove();
			}
			ent.offer(Keys.HAS_GRAVITY,true);
			ent.offer(Keys.INVULNERABLE,false);
			PixelCandy.bunnyMap2.remove(p);
		}

		Location loc = p.getLocation();
		Entity e = p.getWorld().createEntity(EntityTypes.PARROT, p.getLocation().getPosition());
		ParrotVariant t = getParrotType(rt);

		e.offer(Keys.PARROT_VARIANT,t);
		e.offer(Keys.INVULNERABLE,true);
		loc.spawnEntity(e);

		if(PixelCandy.ride==false)
		{
			e.offer(Keys.HAS_GRAVITY,false);
			e.offer(Keys.INVULNERABLE,true);
			PixelCandy.bunnyMap2.put(p, e);
		}
	}

	public ParrotVariant getParrotType(int i)
	{
		switch(i)
		{
		case 1: 
			return ParrotVariants.BLUE;
		case 2: 
			return ParrotVariants.CYAN;
		case 3: 
			return ParrotVariants.GRAY;
		case 4: 
			return ParrotVariants.GREEN;
		case 5: 
			return ParrotVariants.RED;
		default:
			return ParrotVariants.RED;
		}	
	}

	public RabbitType getBunnyType(int i)
	{
		switch(i)
		{
		case 1: 
			return RabbitTypes.BLACK;
		case 2: 
			return RabbitTypes.BLACK_AND_WHITE;
		case 3: 
			return RabbitTypes.GOLD;
		case 4: 
			return RabbitTypes.SALT_AND_PEPPER;
		case 5: 
			return RabbitTypes.WHITE;
		default:
			return RabbitTypes.BROWN;
		}	


	}
}
