package com.bex.bexPack.util;

import java.util.ArrayList;
import java.util.HashMap;

import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

import com.bex.bexPack.main.PixelCandy;
import com.flowpowered.math.vector.Vector3d;

public class FxHandler 
{
	public final static HashMap<String,ParticleType> ParticleList= new HashMap<String,ParticleType>();
	private ConfigMk config = new ConfigMk();
	
	public void initMap()
	{
		ParticleList.put("AMBIENT_MOB_SPELL", ParticleTypes.AMBIENT_MOB_SPELL);
		ParticleList.put("ANGRY_VILLAGER", ParticleTypes.ANGRY_VILLAGER);
		ParticleList.put("BARRIER", ParticleTypes.BARRIER);
		ParticleList.put("BLOCK_CRACK", ParticleTypes.BLOCK_CRACK);
		ParticleList.put("BLOCK_DUST", ParticleTypes.BLOCK_DUST);
		ParticleList.put("BREAK_BLOCK", ParticleTypes.BREAK_BLOCK);
		ParticleList.put("CLOUD", ParticleTypes.CLOUD);
		ParticleList.put("CRITICAL_HIT", ParticleTypes.CRITICAL_HIT);
		ParticleList.put("DAMAGE_INDICATOR", ParticleTypes.DAMAGE_INDICATOR);
		ParticleList.put("DRAGON_BREATH", ParticleTypes.DRAGON_BREATH);
		ParticleList.put("DRAGON_BREATH_ATTACK", ParticleTypes.DRAGON_BREATH_ATTACK);
		ParticleList.put("DRIP_LAVA", ParticleTypes.DRIP_LAVA);
		ParticleList.put("DRIP_WATER", ParticleTypes.DRIP_WATER);
		ParticleList.put("ENCHANTING_GLYPHS", ParticleTypes.ENCHANTING_GLYPHS);
		ParticleList.put("END_ROD", ParticleTypes.END_ROD);
		ParticleList.put("ENDER_TELEPORT", ParticleTypes.ENDER_TELEPORT);
		ParticleList.put("EXPLOSION", ParticleTypes.EXPLOSION);
		ParticleList.put("FALLING_DUST", ParticleTypes.FALLING_DUST);
		ParticleList.put("FERTILIZER", ParticleTypes.FERTILIZER);
		ParticleList.put("FIRE_SMOKE", ParticleTypes.FIRE_SMOKE);
		ParticleList.put("FIREWORKS", ParticleTypes.FIREWORKS);
		ParticleList.put("FIREWORKS_SPARK", ParticleTypes.FIREWORKS_SPARK);
		ParticleList.put("FLAME", ParticleTypes.FLAME);
		ParticleList.put("FOOTSTEP", ParticleTypes.FOOTSTEP);
		ParticleList.put("GUARDIAN_APPEARANCE", ParticleTypes.GUARDIAN_APPEARANCE);
		ParticleList.put("HAPPY_VILLAGER", ParticleTypes.HAPPY_VILLAGER);
		ParticleList.put("HEART", ParticleTypes.HEART);
		ParticleList.put("HUGE_EXPLOSION", ParticleTypes.HUGE_EXPLOSION);
		ParticleList.put("INSTANT_SPELL", ParticleTypes.INSTANT_SPELL);
		ParticleList.put("ITEM_CRACK", ParticleTypes.ITEM_CRACK);
		ParticleList.put("LARGE_EXPLOSION", ParticleTypes.LARGE_EXPLOSION);
		ParticleList.put("LARGE_SMOKE", ParticleTypes.LARGE_SMOKE);
		ParticleList.put("LAVA", ParticleTypes.LAVA);
		ParticleList.put("MAGIC_CRITICAL_HIT", ParticleTypes.MAGIC_CRITICAL_HIT);
		ParticleList.put("MOB_SPELL", ParticleTypes.MOB_SPELL);
		ParticleList.put("MOBSPAWNER_FLAMES", ParticleTypes.MOBSPAWNER_FLAMES);
		ParticleList.put("NOTE", ParticleTypes.NOTE);
		ParticleList.put("PORTAL", ParticleTypes.PORTAL);
		ParticleList.put("REDSTONE_DUST", ParticleTypes.REDSTONE_DUST);
		ParticleList.put("SLIME", ParticleTypes.SLIME);
		ParticleList.put("SMOKE", ParticleTypes.SMOKE);
		ParticleList.put("SNOW_SHOVEL", ParticleTypes.SNOW_SHOVEL);
		ParticleList.put("SNOWBALL", ParticleTypes.SNOWBALL);
		ParticleList.put("SPELL", ParticleTypes.SPELL);
		ParticleList.put("SPLASH_POTION", ParticleTypes.SPLASH_POTION);
		ParticleList.put("SUSPENDED", ParticleTypes.SUSPENDED);
		ParticleList.put("SUSPENDED_DEPTH", ParticleTypes.SUSPENDED_DEPTH);
		ParticleList.put("SWEEP_ATTACK", ParticleTypes.SWEEP_ATTACK);
		ParticleList.put("TOWN_AURA", ParticleTypes.TOWN_AURA);
		ParticleList.put("WATER_BUBBLE", ParticleTypes.WATER_BUBBLE);
		ParticleList.put("WATER_DROP", ParticleTypes.WATER_DROP);
		ParticleList.put("WATER_SPLASH", ParticleTypes.WATER_SPLASH);
		ParticleList.put("WATER_WAKE", ParticleTypes.WATER_WAKE);
		ParticleList.put("WITCH_SPELL", ParticleTypes.WITCH_SPELL);

	}
	

	//Gonna need config stuff, probably something like <player, effect, vel, radius>
	/**
	 * spawn particle effect at players location
	 * 
	 * @param p
	 * @param effect
	 * @param vel
	 * @param rad
	 */
	public void sendFxToPlayer(Player p, ParticleType effect, Vector3d vel,int rad)
	{

		ParticleEffect fx = ParticleEffect.builder()
				.type(effect)
				.velocity(vel)
				.build();
		Vector3d loc = p.getLocation().getPosition();

		((Viewer)p).spawnParticles(fx, loc, rad);
	}
	//
	public void buildFX(Player p)
	{
		boolean b = checkVal(p);
		if(b==false)
		{
			messenger.
		}
		ArrayList<Object> o = PixelCandy.particleMap.get(p);
		ParticleType effect = (ParticleType) o.get(0);
		Vector3d vel = (Vector3d) o.get(1);
		int rad = (int) o.get(2);
		sendFxToPlayer(p, effect, vel, rad);
	}
	// Check if the particle map is valid
	public boolean checkVal(Player p)
	{
		ArrayList<Object> o = PixelCandy.particleMap.get(p);
		if(o.isEmpty())
		{
			return buildMap(p,o);
			
		}
		// this block checks if the particle has been changed, in which case update the map
		if (o.get(0) instanceof ParticleType)
		{
			if((ParticleType) o.get(0) != getEffectType(p)) 
			{
				return buildMap(p, o);
				
			}
		}
		return true;
	}
	// Populate player particle map
	public boolean buildMap(Player p, ArrayList<Object> o)
	{
		o.add(0,getEffectType(p));
		if(o.get(0)==null)
		{
			return false;
		}
		o.add(1,getVel(p));
		o.add(2,getRad(p));
		//save the particle map
		PixelCandy.particleMap.replace(p, o);
		return true;
		
	}
	// get particle rad (how far should the particles spawn)
	public int getRad(Player p)
	{
		int rad = config.getInt(p.getUniqueId().toString(), "particle", "radius");
		return rad;
	}
	// get velocity of particles (how much should the particles move)
	public Vector3d getVel(Player p)
	{
		Double velx = config.getDouble(p.getUniqueId().toString(), "particle", "vx");
		Double vely = config.getDouble(p.getUniqueId().toString(), "particle", "vy");
		Double velz = config.getDouble(p.getUniqueId().toString(), "particle", "vz");
		Vector3d vel = new Vector3d(velx,vely,velz);
		return vel;
	}
	// get particle type (what particle are we spawning)
	public ParticleType getEffectType(Player p)
	{
		//ParticleType fx;

		String particleName = config.getString(p.getUniqueId().toString(), "particle", "effect");
		if(ParticleList.containsKey(particleName.toUpperCase()))
		{
			ParticleType fx = ParticleList.get(particleName.toUpperCase());
			return fx;
		}
		return null;
	}


}
