package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;
import com.pixelmonmod.pixelmon.Pixelmon;
//import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
//import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
//import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;

import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.world.World;

public class IncubateCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("incubate command"))
			.permission("bex.egg.incubate")
			.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("slot"))))
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

						if(src.hasPermission("bex.egg.incubate")||isBex==true)
						{
							Player p = (Player) src;
							int slot = args.<Integer>getOne("slot").get();
							ItemStack i = p.getItemInHand(HandTypes.MAIN_HAND).get();
							ItemStack egg = Getters.getIncubator();
							int b = ItemStackComparators.IGNORE_SIZE.compare(i, egg);
							if(slot<=0||slot>6)
							{
								Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
										append(Text.builder("You must choose a valid slot [1-6]").color(TextColors.RED).build()).build();
								p.sendMessage(msg);
								return CommandResult.success();
							}
							if(b==0)
							{
								slot=slot-1;
								EntityPlayerMP ep = PixelmonPlayerUtils.getUniquePlayerStartingWith(p.getName());
								//PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(ep).get();
								PartyStorage party = Pixelmon.storageManager.getParty(ep);
								try
								{
									//int[] pk = party.getIDFromPosition(slot);
									Pokemon pk = party.get(slot);
									//EntityPixelmon epix = party.getPokemon(pk, (World) p.getWorld());
									//if(epix.isEgg)
									if(pk.isEgg())
									{
										//if(epix.eggCycles<=2)
										if(pk.getEggCycles()<=2)
										{
											Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE).
													append(Text.builder("Incubation would have almost no affect on this egg, show it some love").color(TextColors.GREEN).build()).build();
											p.sendMessage(msg);
										}
										else // egg cycles at acceptable level
										{
											//epix.eggCycles=epix.eggCycles-epix.eggCycles;
											//epix.updateStats();
											pk.setEggCycles(pk.getEggCycles()-pk.getEggCycles());
											pk.hatchEgg();
											if(i.getQuantity()==1)
											{
												p.setItemInHand(HandTypes.MAIN_HAND, null);
											}
											else if(i.getQuantity()>1)
											{
												i.setQuantity(i.getQuantity()-1);
												p.setItemInHand(HandTypes.MAIN_HAND, i);
											}
											Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
													//		.append(Text.builder("Pokemon successfully incubated, go for a short walk to finish the process").color(TextColors.GREEN).build()).build();
													/*1.7 */					.append(Text.builder("Pokemon successfully incubated").color(TextColors.GREEN).build()).build();
											p.sendMessage(msg);
										}
									}
									else	//not an egg
									{
										Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
												.append(Text.builder("There is no egg in this slot").color(TextColors.RED).build()).build();
										p.sendMessage(msg);
										return CommandResult.success();
									}
								}
								catch(NullPointerException ex)
								{
									Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
											.append(Text.builder("There is no egg in this slot").color(TextColors.RED).build()).build();
									p.sendMessage(msg);
									return CommandResult.success();
								}
							}
							else //no incubator 
							{
								Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
										.append(Text.builder("You must be holding an incubator to do this").color(TextColors.RED).build()).build();
								p.sendMessage(msg);
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
