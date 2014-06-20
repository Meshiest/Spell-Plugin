package com.meshiest;

import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EffectChicken implements Effect {
	public void doEffect(final Player player, int time, int power) {
		if (power == 0)
			power = 1;
		for (int i = 0; i < power; i++)
			player.getWorld().spawnEntity(player.getLocation(),
					EntityType.CHICKEN);
	}

	public ItemStack[] getCost(int time, int power) {
		if (power == 0)
			power = 1;
		ItemStack feather = new ItemStack(Material.FEATHER, power), bone = new ItemStack(
				Material.BONE, power), chicken = new ItemStack(
				Material.RAW_CHICKEN, power), egg = new ItemStack(Material.EGG,
				power);
		ItemStack[] cost = { feather, bone, chicken, egg };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		return true;
	}

	public String getName() {
		return "Creates a chicken";
	}
}
