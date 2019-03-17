package com.bex.bexPack.commands;

import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;

import net.minecraft.entity.Entity;


public class DisguiseCommand 
{
	//TODO add a check for shiny (optional arg with seperate perm)
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("toggle hat selections"))
			.permission("bex.disguise.disguise")
			.child(new DisguiseClearCommand().getCommandSpec(), "clear")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))),
					GenericArguments.optional(GenericArguments.string(Text.of("shiny")),"false"))
			.executor(new CommandExecutor() {

				@Override
				public CommandResult execute(CommandSource src, CommandContext args) throws CommandException 
				{
					if(src instanceof Player==false)
					{
						Messenger.sendMessageNotPlayer(src);
						return CommandResult.success();
					}
					String name = args.<String>getOne("name").get();
					Boolean shiny = Boolean.valueOf(args.<String>getOne("shiny").get());
					List<String> nameList = EnumSpecies.getNameList();
					Player p = (Player)src;
					if(!nameList.stream().anyMatch(name::equalsIgnoreCase))
					{
						return CommandResult.success();
					}
					Pokemon ent = Pixelmon.pokemonFactory.create(EnumSpecies.getFromNameAnyCase(name));
					if(src.hasPermission("bex.disguise.shiny"))
					{
						ent.setShiny(shiny);
					}
					EntityPixelmon ep = ent.getOrSpawnPixelmon((Entity) p);
					//ep.setInvisible(false);
					org.spongepowered.api.entity.Entity e = (org.spongepowered.api.entity.Entity) ep;
					e.offer(Keys.INVULNERABLE,true);
					e.offer(Keys.AI_ENABLED,false);
					p.offer(Keys.VANISH,true);
					p.offer(Keys.INVISIBLE,true);
					p.offer(Keys.VANISH_PREVENTS_TARGETING,true);
					p.offer(Keys.VANISH_IGNORES_COLLISION,true);
					

					if(PixelCandy.disguiseMap.containsKey(p)) 
					{
						PixelCandy.disguiseMap.replace(p, ep);
					}
					else 
					{	
						PixelCandy.disguiseMap.put(p, ep);
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}

}
