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
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class ParrotHatCommand 
{

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Spawn an incubator"))
			.permission("bex.fun.hat")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("type"))))
			.executor(new CommandExecutor() {

				@SuppressWarnings("rawtypes")
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

						if(src.hasPermission("bex.fun.hat")||isBex==true)
						{

							Player p = (Player) src;
							int rt = args.<Integer>getOne("type").get();

							//removeEntityHats
							if(rt==0)
							{

								if(PixelCandy.ride==false)
								{

									Entity ent = PixelCandy.bunnyMap2.get(p);
									if(!ent.getClass().equals(EntityPixelmon.class))
									{
										ent.remove();
									}
									ent.offer(Keys.HAS_GRAVITY,true);
									ent.offer(Keys.INVULNERABLE,false);
									PixelCandy.bunnyMap2.remove(p);
									return CommandResult.success();
								}
								//
								//old,passenger
								//
								/*
								if(PixelCandy.bunnyMap.containsKey(p))
								{
									List<Entity> el = PixelCandy.bunnyMap.get(p);
									if(el.get(0).getClass().equals(EntityPixelmon.class))
									{
										el.get(0).offer(Keys.INVULNERABLE,false);
										el.get(1).remove();
									}
									if(el.get(1).getClass().equals(EntityPixelmon.class))
									{
										el.get(1).offer(Keys.INVULNERABLE,false);
										el.get(0).remove();
									}
									else
									{
										el.get(0).remove();
										el.get(1).remove();
									}
									PixelCandy.bunnyMap.remove(p);
									return CommandResult.success();
							
								}*/
							}
							if(rt<1||rt>5)
							{
								Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
										append(Text.builder("Type must be between 1-5").color(TextColors.RED).build()).build();
								p.sendMessage(msg);
								return CommandResult.success();
							}

							//
							//new
							//
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

							//
							//old
							//
							/*
							if(PixelCandy.ride==true&&PixelCandy.bunnyMap.containsKey(p))
							{
								List<Entity> el = PixelCandy.bunnyMap.get(p);
								el.get(0).remove();
								el.get(1).remove();
								PixelCandy.bunnyMap.remove(p);
							}
							PotionEffect pt = PotionEffect.builder()
									.potionType(PotionEffectTypes.INVISIBILITY)
									.duration(Integer.MAX_VALUE)
									.amplifier(5)
									.build();
							*/
							Location loc = p.getLocation();
							Entity e = p.getWorld().createEntity(EntityTypes.PARROT, p.getLocation().getPosition());
							//Entity e2 = p.getWorld().createEntity(EntityTypes.PARROT, p.getLocation().getPosition());

							//PotionEffectData effects = e2.getOrCreate(PotionEffectData.class).get();
							ParrotVariant t = getType(rt);
							//effects.addElement(pt);
							//e2.offer(Keys.AGE,Integer.MIN_VALUE);
							//e2.offer(effects);
							//e2.offer(Keys.INVULNERABLE,true);
							e.offer(Keys.PARROT_VARIANT,t);
							e.offer(Keys.INVULNERABLE,true);
							loc.spawnEntity(e);
							
							//
							//new
							//
							if(PixelCandy.ride==false)
							{
								e.offer(Keys.HAS_GRAVITY,false);
								e.offer(Keys.INVULNERABLE,true);
								PixelCandy.bunnyMap2.put(p, e);
							}
							//
							//old
							//
							/*
							if(PixelCandy.ride==true)
							{
								loc.spawnEntity(e2);
								e2.addPassenger(e);
								p.addPassenger(e2);
								List<Entity> le = new ArrayList<Entity>();
								le.add(e2);
								le.add(e);
								PixelCandy.bunnyMap.remove(p);
								PixelCandy.bunnyMap.put(p,le);
							}
							*/
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
	public ParrotVariant getType(int i)
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
		}
		return ParrotVariants.RED;	
	}

}
