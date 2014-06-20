package com.meshiest;

import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class EffectSpeed implements Effect {
	
	public void doEffect(final Player player, int time, int power) {
		if(power == 0)
			power = 1;
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,time*20,power));

	}

	public ItemStack[] getCost(int time, int power) {
		if(power == 0)
			power = 1;
		ItemStack sugar = new ItemStack(Material.SUGAR);
		sugar.setAmount(time + (int)Math.pow(2, power)+2);
		ItemStack[] cost = { sugar };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		return !player.hasPotionEffect(PotionEffectType.SPEED);
	}
	
	public String getName() {
		return "Increases your speed for a duration of time";
	}
}
