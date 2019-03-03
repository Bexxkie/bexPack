package com.bex.bexPack.EventListeners;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.GameModeData;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.DropItemEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackComparators;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;

import com.bex.bexPack.main.PixelCandy;
import com.bex.bexPack.util.Getters;
import com.flowpowered.math.vector.Vector3d;

public class WorldEvents 
{
	//TODO finish up the ruler and test it. (watch with name 'ruler')
	/**
	 * 
	 * this is for the ruler
	 * @param e blockClicked
	 * @param p Player (only calls if the source is a player)
	 */
	@SuppressWarnings("rawtypes")
	@Listener
	public void useRulerMainHand(InteractBlockEvent.Secondary.MainHand  e, @Root Player p)
	{
		//may need a try catch, dunno.

		//make sure the dude has the ruler before shit
		ItemStack _i=p.getItemInHand(HandTypes.MAIN_HAND).get();

		int isRuler = ItemStackComparators.ITEM_DATA_IGNORE_DAMAGE.compare(Getters.getRuler(),_i);

		if(isRuler!=0)
		{
			// does not have a ruler, return
			return;
		}
		//get targetBlock location
		Location loc = e.getTargetBlock().getLocation().get();
		if(PixelCandy.rulerMap.containsKey(p)) 
		{
			//doshit
			//the player already has a location stored, so this SHOULD be the second click, 
			Location _a = PixelCandy.rulerMap.get(p);
			Location _b = loc;
			Double _d = _a.getPosition().distance(_b.getPosition());
			//convert to int for block count
			int d = _d.intValue()+1;

			//send distance to player
			Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
					.append(Text.builder("Distance ").color(TextColors.BLUE)
							.append(Text.builder(d+" ").color(TextColors.YELLOW)
									.append(Text.builder("blocks.").color(TextColors.RED)
											.build()).build()).build()).build();
			//send the message remove the player from rulerMap and return
			p.sendMessage(msg);
			PixelCandy.rulerMap.remove(p);
			return;
		}
		//assume this is the first click.
		ParticleEffect effect = ParticleEffect.builder()
				.type(ParticleTypes.PORTAL)
				.quantity(10)
				.velocity(Vector3d.from(.5,1.5,.5))
				.build();
		p.getLocation().getExtent().spawnParticles(effect, loc.getPosition().add(.5,.5,.5),1);

		Text msg = Text.builder("[bPack] ").color(TextColors.LIGHT_PURPLE)
				.append(Text.builder("First point selected").color(TextColors.BLUE)
						.build()).build();
		//send the message add the player to rulerMap and return
		p.sendMessage(msg);
		PixelCandy.rulerMap.put(p,loc);
	}

	/**
	 * this is to make cake and healers drop when broken
	 * @param e blockBreak
	 * @param p Player (only calls if the source is a player)
	 */
	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Listener
	public void blockBreakEvent(ChangeBlockEvent.Break e, @Root Player p)
	{
		GameModeData data = p.getGameModeData();
		GameMode gm = data.get(Keys.GAME_MODE).get();
		if(!gm.equals(GameModes.CREATIVE))
		{
			BlockSnapshot bs = e.getTransactions().get(0).getOriginal();
			BlockType h = Sponge.getGame().getRegistry().getType(BlockType.class,"pixelmon:healer").get();
			BlockType c = BlockTypes.CAKE;
			BlockType b = bs.getState().getType();
			//System.out.println(b);
			if(b==h)
			{	
				ItemStack im = ItemStack.of(Sponge.getGame().getRegistry().getType(ItemType.class, "pixelmon:healer").get());
				im.setQuantity(1);
				p.getInventory().offer(im);
			}
			if(b==c)
			{
				ItemStack im = ItemStack.of(ItemTypes.CAKE);
				im.setQuantity(1);
				p.getInventory().offer(im);
			}
			if(!PixelCandy.enchantingBlockMap.isEmpty())
			{
				Location l = bs.getLocation().get();
				if(PixelCandy.enchantingBlockMap.containsKey(l))
				{
					e.setCancelled(true);
				}
			}

		}
	}


	@Listener
	public void itemDropEvent(DropItemEvent.Destruct e)
	{
		Optional<BlockSnapshot> bs = e.getCause().getContext().get(EventContextKeys.BLOCK_HIT);

		try{
			BlockType bss = bs.get().getState().getType();
			BlockType h = Sponge.getGame().getRegistry().getType(BlockType.class,"pixelmon:healer").get();
			if(bss==h)
			{
				e.setCancelled(true);
			}
		}catch (NoSuchElementException ex)
		{
			return;
		}
	}




}
