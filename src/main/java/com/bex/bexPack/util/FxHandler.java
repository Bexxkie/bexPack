package com.bex.bexPack.util;

import org.spongepowered.api.effect.Viewer;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.living.player.Player;
import com.flowpowered.math.vector.Vector3d;

public class FxHandler 
{
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
	
	
}
