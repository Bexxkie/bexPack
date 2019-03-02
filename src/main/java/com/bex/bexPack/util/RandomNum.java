package com.bex.bexPack.util;

import java.util.Random;
//TODO Change to math class or some shit, put all the math stuff here, like random num and shit.
public class RandomNum 
{
	/**
	 * 
	 * @param min integer must be smaller than max
	 * @param max integer must be larger than min
	 * @return random int between min/max
	 */
	public static int rNum(int min, int max) 
	{

		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
}
