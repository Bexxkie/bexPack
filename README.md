------------------------------------------
# BEXPACK
- Authors: Bexxkie, Dr. Stupid
- Current Version: 1.2.2
------------------------------------------

## TODO
- [x] make the enchanting thing
- [ ] swear jar \(?)
- [ ] move all the math stuff to a single class
------------------------------------------

## JUMP
- [Commands]#(COMMANDS)
- [Permissions]#(PERMISSIONS)
- [Changelog]#(CHANGELOG)
------------------------------------------		 
 
## COMMANDS
	- /pcan spawnCandy
		- bex.candies.spawn
		- Spawn unusual candy
	- /pcan convertCandy
		- bex.candies.convert
		- convert candy types (rare <-> unusual)
	- /pcan spawnEgg
		- bex.candies.spawn
		- spawn an incubator
	- /pcan spawnDrink
		- bex.alcohol.spawn
	- /pcan incubate
		- bex.egg.incubate
		- incubate an egg
	- /pcan itemRain <player>
		- bex.fun.rain
		- rain roses on a player for 10 seconds
	- /pcan setLevel <slot> <level>
		- bex.hax.lvl
	- /pcan hat <entity> <type>
		- bex.fun.hat
	- /pcan togglehat
		- bex.fun.hat
	- /pcan clearhat
		- bex.fun.hat
	- /pcan info
		- bex.dev
	- /pcan enchant
		- bex.util.enchant
------------------------------------------
## PERMISSIONS
	- bex.candies.spawn
	- bex.candies.convert
	- bex.alcohol.spawn
	- bex.egg.incubate
	- bex.fun.rain
	- bex.hax.lvl
	- bex.fun.hat
	- bex.util.enchant
	- bex.dev
	- bex.fun.superhats

------------------------------------------ 		
## DEBUG COMMANDS: (DISABLED IN RELEASE)
	- /pcan eggSteps
		- bex.egg.steps
		- displays current eggCycle of <slot> to console
------------------------------------------
## CHANGELOG
------------------------------------------	
### Ver_1.2.3
	- renamed AlcoholProcessor.java to GamblingProcessor.java
	- renamed the "alchol" plugin to "pcangamble"
		-this means that the config location is now <server root>/config/pcangamble/pcangamble.conf
	-added the ability to reload the config without having to restart the server
		-literally just /sponge plugins reload
	-changed the get functions in GamblingProcessor.java to require more details, allowing the config file to be more detailed
	-condensed the EventListener imports in PixelCandy.java to just import the whole directory instead of just one directory at a time
	-changed README.md in such a way that Dr. Stupid's IDE would stop yelling at him
------------------------------------------

### Ver_1.2.2
	- added enchant command
		- /pcan enchant
		- bex.util.enchant
	- changed cooldown [60s -> 30s]
	- added override permission to cooldowns
		- bex.fun.override
------------------------------------------
	
### Ver_1.2.1
	- updated ruler
		- includes clicked block
		- /pcan spawnruler <player> <count>
		- added particle marker to first block
	- updated flight detection
	- fixed not being able to select slot 6 in modLevel command 
------------------------------------------
  
### Ver_1.2.0
	- Added Dr. Stupid as an author
	- Added Alcohol Gambling
		- /pcan spawnDrink <player name> <drink id>
		- config location located in <server root directory>/config/alcohol/alcohol.conf
		- added onConsumeDrink event handler
		- permission: bex.alcohol.spawn
	- Added IndexOutOfBoundsException to itemDoublecheck event handler
	- Updated build paths for Pixelmon 7.0.3
------------------------------------------
 
### Ver_1.1.9
	- updated Eventhandlers
	- removed commands
		- /pcan bunnyHat
		- /pcan parrotHat
	- added commands
		- /pcan hat <type> [1-5] 
			- type = bunny, parrot
------------------------------------------	
 	
### Ver_1.1.8
	- updated ItemRain
		- /pcan itemRain <player> <?time=10>
		- uses item in hand as target item
			- if hand is empty, uses roseblock 
		- Maximum time = 15s
		- reduced spawn calls (1 from 3)
		- implimented cooldown (60s)
	- updated curse
		- reduced quantity to 10 from 15
		- implimented cooldown (60s)
------------------------------------------

### Ver_1.1.7
	- updated ItemRain
		-/pcan itemRain <player> <?item> <?time>
			- item [heart,heart2] blank==roseBlock
			- time [time in seconds] defaults to 10s
	- new Commands
		- /pcan curse <player>
			- is this really a curse?
			- bex.fun.superRain
------------------------------------------ 
### Ver_1.1.6b
	- hat changes (applies to superHats and hats)
		- hats now have INVULNERABLE tag
		- fixed logging off not restoring gravity to hats
------------------------------------------
### Ver_1.1.6
	- Commands updated
		- Player requirements are now enforced
			- /pcan convetCandy
			- /pcan incubate <slot>
			- /pcan convetCandy
			- /pcan modLevel <slot> <level>
			- /pcan bunnyHat <type>
			- /pcan parrotHat <type>
			- /pcan clearHat <type>
			- /pcan toggleHat <type>
			- /pcan eggSteps <slot> (!DEBUG COMMAND!)
	- Arguments to commands updated
	  	- /pcan spawnCandy <player> <?count=1> 
	  	- /pcan spawnEgg <player> <?count=1>
	  	- /pcan spawnWings <player>
	  	  		
	- Added info command
		- /pcan info
			- bex.dev
			- returns version info
------------------------------------------

### Ver_1.1.5
	  - superHats changes
		- only pokemon can be worn, and you must own them
		- added toggle command for selecting a hat
			- /pcan togglehat
				- bex.fun.superhats
		- added clear command
			- /pcan clearhat
				- bex.fun.superhats
------------------------------------------  	
 
### Ver_1.1.4
	 - Rewrote SuperHat
		- disables gravity on entity and updates location relative to players location
		- no longer blocks targeting
		- Sneak+RMB to pick a hat
		- fixed hats not being removed or updated in some instances
	 - added option to remove hat
		- /pcan bunnyhat 0
		- /pcan parrothat 0
------------------------------------------
 	
### Ver_1.1.2
	 - Added SuperHat
		- bex.fun.superhats
	 - updated wings
------------------------------------------
 
### Ver_1.1.1
	- Added BunnyHat
		- /pcan bunnyHat <type>
	- Added ParrotHat
		- /pcan parrotHat <type>
	 - Added Permission nodes
		- bex.fun.hat
------------------------------------------

### Ver_1.1.0
	 - Added flower rain
		- /pcan itemRain <player>
	 - Added level hacks
		- /pcan setLevel <slot> <level>
	 - Added flight when wearing elytra
		- drains .5xp/s while flying 
	 - Added permission nodes
		- bex.fun.rain
		- bex.hax.lvl
------------------------------------------

### Ver_1.0.1
	 - minor bug fixes
		- incubator will not be used when target egg is <= 2 egg steps.
		- fixed permission node type mismatch
------------------------------------------
 
### Ver_1.0.0
	- Created Changelog File
		- Added 'Incubator'
			- removed 'use incubator to incubate', replaced with command
			- /pcan incubate <slot>
			- /pcan spawnegg
	- added permission nodes
		- bex.egg.steps
		- bex.egg.incubate
-------------------------------------------------------------------------
