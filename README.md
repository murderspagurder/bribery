# Bribery

Bribery is a mod for Fabric and Neoforge that implements a Village Bribery System. This was primarily 
designed because I found the concept of bribery existing in Minecraft was humorous, but also because I 
could see this having value in RP-style games.

Players can attempt to bribe entities (currently villagers and golems) by sneaking and left-clicking
them while holding a stack of currency, which currently includes iron ingots, gold ingots, emeralds, 
diamonds, and netherite ingots. A bribed entity does not pay attention to negative gossip or reputation 
events or target the player. When attempting to bribe an entity, there is a chance that the entity will 
reject the bribe; this will cause severe negative gossip and possibly aggression towards the player if 
the entity is neutral.

When an entity accepts a bribe, they also have a chance of becoming an extortionist. Extortionists will 
periodically blackmail the player, threatening to spread negative gossip or aggro the player of they do
not provide sufficient funds within the time limit.

Currency types, chances/modifiers, cooldowns and more can be fine-tuned in `config/bribery.json`.
Additionally, thanks to MidnightLib, this can be configured in realtime using the config screen 
(key bindable), Mod Menu, or the `/midnightconfig bribery` command.

Compatibility with Guard Villagers is planned in the future.

Special thanks to:
- KikuGie: Stonecutter and other gradle utils, project layout role model
- Motschen: MidnightLib
