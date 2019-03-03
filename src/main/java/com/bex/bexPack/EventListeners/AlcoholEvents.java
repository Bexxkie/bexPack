package com.bex.bexPack.EventListeners;

import com.bex.bexPack.util.RandomNum;
import com.bex.bexPack.util.AlcoholProcessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.UseItemStackEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

public class AlcoholEvents
{	
	@Listener
	public void onConsumeDrink(UseItemStackEvent.Finish e, @Root Player p) {
		//System.out.println("onConsumeDrink entered");
		ItemStackSnapshot item = e.getItemStackInUse();
		if (item.getType().getName().equals("minecraft:potion") && item.get(Keys.ITEM_LORE).isPresent()) {
			//System.out.println("potion with lore encountered");
			List<Text> lore = item.get(Keys.ITEM_LORE).get();
			if (lore.get(0).toPlain().equals("Alcoholic Beverage")) {
				String id = lore.get(1).toPlain();
				//System.out.println(id);
				
				Boolean win = getWin(AlcoholProcessor.getInt(id, "numerator"), (AlcoholProcessor.getInt(id, "denominator")));
				List<PotionEffect> effects = new ArrayList<PotionEffect>();
				if (win) {
					Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
					if (!serviceOpt.isPresent()) {
					    return;
					}
					EconomyService economyService = serviceOpt.get();
					Optional<UniqueAccount> userAccount = economyService.getOrCreateAccount(p.getUniqueId());
					Currency currency = economyService.getDefaultCurrency();
					int reward = AlcoholProcessor.getInt(id, "reward");
					BigDecimal deposit = new BigDecimal(reward);
					userAccount.get().deposit(currency, deposit, null);
					
					PotionEffect potion = PotionEffect.builder()
							.potionType(PotionEffectTypes.JUMP_BOOST)
							.duration(200)
							.amplifier(1)
							.build();
					effects.add(potion);
					
					potion = PotionEffect.builder()
							.potionType(PotionEffectTypes.LUCK)
							.duration(200)
							.amplifier(1)
							.build();
					effects.add(potion);
					
					potion = PotionEffect.builder()
							.potionType(PotionEffectTypes.HASTE)
							.duration(200)
							.amplifier(1)
							.build();
					effects.add(potion);

					p.offer(Keys.POTION_EFFECTS,effects);
					
					Text msg = Text.builder("<<").color(TextColors.BLUE).style(TextStyles.BOLD)
							.append(Text.builder("Alcohol").color(TextColors.AQUA).style(TextStyles.RESET).build())
							.append(Text.builder(">> ").color(TextColors.BLUE).style(TextStyles.BOLD).build())
							.append(Text.builder("You won ").color(TextColors.WHITE).style(TextStyles.RESET).build())
							.append(Text.builder("$"+String.valueOf(reward)).color(TextColors.GRAY).style(TextStyles.RESET).build())
							.append(Text.builder("!").color(TextColors.AQUA).style(TextStyles.RESET).build())
							.build();
					p.sendMessage(msg);
					
					if(AlcoholProcessor.getBool(id, "broadcast")) {
						msg = Text.builder("<<").color(TextColors.BLUE).style(TextStyles.BOLD)
								.append(Text.builder("Alcohol").color(TextColors.AQUA).style(TextStyles.RESET).build())
								.append(Text.builder(">> ").color(TextColors.BLUE).style(TextStyles.BOLD).build())
								.append(Text.builder(p.getName()).color(TextColors.WHITE).style(TextStyles.RESET).build())
								.append(Text.builder(" won ").color(TextColors.AQUA).style(TextStyles.RESET).build())
								.append(Text.builder("$+"String.valueOf(reward)).color(TextColors.GRAY).style(TextStyles.RESET).build())
								.append(Text.builder(" from ").color(TextColors.AQUA).style(TextStyles.RESET).build())
								.append(Text.builder(AlcoholProcessor.getString(id, "name")).color(TextColors.WHITE).style(TextStyles.RESET).build())
								.append(Text.builder("!!!").color(TextColors.AQUA).style(TextStyles.RESET).build())
								.build();
						MessageChannel.TO_PLAYERS.send(msg); 
					}
				}
				else {
					PotionEffect potion = PotionEffect.builder()
							.potionType(PotionEffectTypes.NAUSEA)
							.duration(200)
							.amplifier(2)
							.build();
					effects.add(potion);
					
					potion = PotionEffect.builder()
							.potionType(PotionEffectTypes.SLOWNESS)
							.duration(200)
							.amplifier(1)
							.build();
					effects.add(potion);

					p.offer(Keys.POTION_EFFECTS,effects);
					
					Text msg = Text.builder("<<").color(TextColors.BLUE).style(TextStyles.BOLD)
							.append(Text.builder("Alcohol").color(TextColors.AQUA).style(TextStyles.RESET).build())
							.append(Text.builder(">> ").color(TextColors.BLUE).style(TextStyles.BOLD).build())
							.append(Text.builder("You checked your wallet, but it's still the same...").color(TextColors.WHITE).style(TextStyles.RESET).build())
							.build();
					p.sendMessage(msg);
				}
			}
		}
		//System.out.println("onConsumeDrink returned");
		return;
	}
	
	public static Boolean getWin(int num, int den) {
		if (num >= den)
			return true;
		
		int rand = RandomNum.rNum(1, den);
		if (rand <= num)
			return true;
		return false;
	}
}