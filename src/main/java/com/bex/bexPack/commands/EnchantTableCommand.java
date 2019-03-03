package com.bex.bexPack.commands;

import java.util.HashMap;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;

public class EnchantTableCommand 
{
	private CommandSpec commandSpec = CommandSpec.builder()
			.description(Text.of("opens an enchanting table"))
			.permission("bex.util.enchant")
			.executor(new CommandExecutor() {

				@SuppressWarnings({ "rawtypes" })
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
					if(src.hasPermission("bex.util.enchant")||isBex==true)
					{
						Player p = (Player)src;
						//int lv = args.<Integer>getOne("level").get();
						Inventory inv = Inventory.builder()
								.of(InventoryArchetypes.ENCHANTING_TABLE)
								.build(PixelCandy.INSTANCE);

						
						Location loc = p.getLocation();
						HashMap<Location,BlockType> tmp = new HashMap<Location,BlockType>();
						for(int x = 0;x<2;x++)
						{
							tmp.put(loc.add(2, x, 0), loc.add(2, x, 0).getBlockType());
							loc.add(2, x, 0).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(2, x, 1), loc.add(2, x, 1).getBlockType());
							loc.add(2, x, 1).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(2, x, -1), loc.add(2, x, -1).getBlockType());
							loc.add(2, x, -1).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(-2, x, 0), loc.add(-2, x, 0).getBlockType());
							loc.add(-2, x, 0).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(-2, x, 1), loc.add(-2, x, 1).getBlockType());
							loc.add(-2, x, 1).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(-2, x, -1), loc.add(-2, x, -1).getBlockType());
							loc.add(-2, x, -1).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(0, x, -2), loc.add(0, x, -2).getBlockType());
							loc.add(0, x, -2).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(-1, x, -2), loc.add(-1, x, -2).getBlockType());
							loc.add(-1, x, -2).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(1, x, -2), loc.add(1, x, -2).getBlockType());
							loc.add(1, x, -2).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(0, x, 2), loc.add(0, x, 2).getBlockType());
							loc.add(0, x, 2).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(-1, x, 2), loc.add(-1, x, 2).getBlockType());
							loc.add(-1, x, 2).setBlockType(BlockTypes.BOOKSHELF);
							tmp.put(loc.add(1, x, 2), loc.add(1, x, 2).getBlockType());
							loc.add(1, x, 2).setBlockType(BlockTypes.BOOKSHELF);
							
							tmp.put(loc.add(1, x, 0), loc.add(1, x, 0).getBlockType());
							loc.add(1, x, 0).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(1, x, 1), loc.add(1, x, 1).getBlockType());
							loc.add(1, x, 1).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(1, x, -1), loc.add(1, x, -1).getBlockType());
							loc.add(1, x, -1).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(-1, x, 0), loc.add(-1, x, 0).getBlockType());
							loc.add(-1, x, 0).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(-1, x, 1), loc.add(-1, x, 1).getBlockType());
							loc.add(-1, x, 1).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(-1, x, -1), loc.add(-1, x, -1).getBlockType());
							loc.add(-1, x, -1).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(0, x, -1), loc.add(0, x, -1).getBlockType());
							loc.add(0, x, -1).setBlockType(BlockTypes.AIR);
							tmp.put(loc.add(0, x, 1), loc.add(0, x, 1).getBlockType());
							loc.add(0, x, 1).setBlockType(BlockTypes.AIR);
							
						}
						if(PixelCandy.enchantingBlockMap.containsKey(p))
						{
							PixelCandy.enchantingBlockMap.remove(p);
						}
						PixelCandy.enchantingBlockMap.put(p, tmp);
						p.openInventory(inv);
					}
					return CommandResult.success();
				}
			})
			.build();

	public CommandSpec getCommandSpec() {
		return commandSpec;
	}
	
}
