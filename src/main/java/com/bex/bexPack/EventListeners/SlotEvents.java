package com.bex.bexPack.EventListeners;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import com.flowpowered.math.vector.Vector3d;

public class SlotEvents
{	
	@Listener
	public void onUseSlots(InteractBlockEvent.Secondary.MainHand e, @Root Player p) {
		BlockState blockState = e.getTargetBlock().getState();
		//System.out.println("clicked on "+blockState.getType().getName());
		if (blockState.getType().getName().equals("minecraft:stone_button")) {
			Vector3d point = e.getInteractionPoint().get();
			if (checkIfSlots(p.getWorld(), point)) {
				//System.out.println("Valid slot machine");
			}
		}
	}
	
	public Boolean checkIfSlots(World world, Vector3d point) {
		Location<World> buttonLoc = new Location<World>(world, point);
		Location<World> loc;
		double x = buttonLoc.getX();
		double y = buttonLoc.getY();
		double z = buttonLoc.getZ();
		
		//System.out.println("button location: ("+x+","+y+","+z+")");
		
		BlockState block = buttonLoc.getBlock();
		if (block.getType().getName().equals("minecraft:stone_button")) {
			loc = new Location<World>(world, new Vector3d(x-1,y,z));
			if (loc.getBlock().getType().getName().equals("minecraft:concrete"))
				return getSlotsMultiblock(loc, "EW");
			
			loc = new Location<World>(world, new Vector3d(x+1,y,z));
			if (loc.getBlock().getType().getName().equals("minecraft:concrete"))
				return getSlotsMultiblock(loc, "EW");
			
			loc = new Location<World>(world, new Vector3d(x,y,z-1));
			if (loc.getBlock().getType().getName().equals("minecraft:concret1001e"))
				return getSlotsMultiblock(loc, "NS");
			
			loc = new Location<World>(world, new Vector3d(x,y,z+1));
			if (loc.getBlock().getType().getName().equals("minecraft:concrete"))
				return getSlotsMultiblock(loc, "NS");
			
			//System.out.println("Concrete not found");
		}
		return false;
	}
	
	public Boolean getSlotsMultiblock(Location<World> loc, String direction) {
		Location<World> leverLoc;
		Boolean lever = false;
		
		//System.out.println("concrete found");
		
		//If the slot is facing North or South, then the lever is either on the East or West side
		if (direction.equals("NS")) {
			leverLoc = loc.copy();
			
			leverLoc = leverLoc.add(1, 0, 0);
			if (leverLoc.getBlock().getType().getName().equals("minecraft:lever") && !lever)
				lever = true;
			
			leverLoc = leverLoc.add(-2, 0, 0);
			if (leverLoc.getBlock().getType().getName().equals("minecraft:lever") && !lever)
				lever = true;
			
			if (!lever)
				return false;
		}
		
		//If the slot is facing East or West, then the lever is either on the North or South side
		if (direction.equals("EW")) {
			leverLoc = loc.copy();
			
			leverLoc = leverLoc.add(0, 0, 1);
			if (leverLoc.getBlock().getType().getName().equals("minecraft:lever") && !lever)
				lever = true;
			
			leverLoc = leverLoc.add(0, 0, -2);
			if (leverLoc.getBlock().getType().getName().equals("minecraft:lever") && !lever)
				lever = true;
			
			if (!lever)
				return false;
		}
		
		//System.out.println("lever found");
		
		loc = loc.add(0,1,0);
		if (!loc.getBlock().getType().getName().equals("minecraft:carpet"))
			return false;
		//System.out.println("carpet found");
		
		loc = loc.add(0,-2,0);
		if (!loc.getBlock().getType().getName().equals("minecraft:anvil"))
			return false;
		//System.out.println("anvil found");
		
		loc = loc.add(0,-1,0);
		if (!loc.getBlock().getType().getName().equals("minecraft:obsidian"))
			return false;
		//System.out.println("obsidian found");
		
		return true;
	}
}