package com.bex.bexPack.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
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

public class CheckStepsCommand {

	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("Checks eggCycle (DEBUG COMMAND)"))
			.permission("bex.egg.steps")
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

						if(src.hasPermission("bex.egg.steps")||isBex==true)
						{
							Player p = (Player) src;
							EntityPlayerMP ep = PixelmonPlayerUtils.getUniquePlayerStartingWith(p.getName());

							PartyStorage party = Pixelmon.storageManager.getParty(ep);

							//PlayerStorage party = PixelmonStorage.pokeBallManager.getPlayerStorage(ep).get();
							//int[] pk = party.getIDFromPosition(0);
							//EntityPixelmon epix = party.getPokemon(pk, (World) p.getWorld());

							Pokemon pk = party.get(0);
							//if(epix.isEgg)
							if(pk.isEgg())
							{
								//System.out.println("steps: "+epix.eggCycles);
								System.out.println("steps: "+pk.getEggSteps());

							}
						}
					}
					else
					{
						Messenger.sendMessageNotPlayer(src);
						//src.sendMessage(Text.of(Getters.getPrefix()+" You must be a player to run this command"));
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}

}
