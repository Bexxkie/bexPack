package com.bex.bexPack.commands;

import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class BaseCommand 
{
	CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("PixelCandy's base command"))
			.child(new SpawnCommand().getCommandSpec(), "spawnCandy")
			.child(new ConvertCommand().getCommandSpec(), "convertCandy")
			.child(new SpawnEggCommand().getCommandSpec(), "spawnEgg")
			//.child(new CheckStepsCommand().getCommandSpec(), "eggSteps")
			.child(new IncubateCommand().getCommandSpec(), "incubate")
			.child(new ModLevelCommand().getCommandSpec(), "setLevel")
			.child(new ItemRainCommand().getCommandSpec(), "itemRain")
			.child(new BunnyHatCommand().getCommandSpec(), "bunnyHat")
			.child(new ParrotHatCommand().getCommandSpec(), "parrotHat")
			.child(new SpawnWingsCommand().getCommandSpec(), "spawnWings")
			.child(new ToggleHat().getCommandSpec(), "togglehat")
			.child(new ClearHatCommand().getCommandSpec(), "clearHat")
			.child(new InfoCommand().getCommandSpec(), "info")
			.child(new PokeRain().getCommandSpec(), "curse")
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
}
