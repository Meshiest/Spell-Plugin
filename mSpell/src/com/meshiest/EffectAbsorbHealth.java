package com.meshiest;

import java.util.List;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EffectAbsorbHealth implements Effect {
	public void doEffect(final Player player, int time, int power) {
		if (power == 0)
			power = 1;
		player.getLineOfSight(null, 20);
		List<Entity> near = player.getNearbyEntities(20, 20, 20);
		for (int i = 0; i < near.size(); i++) {
			Entity e = near.get(i);
			if (e instanceof LivingEntity && !(e instanceof Player)) {
				LivingEntity l = (LivingEntity) e;
				if (player.hasLineOfSight(l)) {
					l.damage((double) power);
					double health = ((CraftPlayer) player).getHandle()
							.getHealth();
					player.setHealth((health + power > 20 ? 20 : health + power));
					return;
				}
			}
		}
	}

	public ItemStack[] getCost(int time, int power) {
		if (power == 0)
			power = 1;
		ItemStack apple = new ItemStack(Material.APPLE), spiderEye = new ItemStack(
				Material.SPIDER_EYE, power);
		ItemStack[] cost = { apple, spiderEye };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		//Bukkit.getLogger().info("Health " + player.getHealthScale());
		if (((CraftPlayer) player).getHandle().getHealth() >= 20)
			return false;
		player.getLineOfSight(null, 20);
		List<Entity> near = player.getNearbyEntities(20, 20, 20);
		for (int i = 0; i < near.size(); i++) {
			Entity e = near.get(i);
			if (e instanceof LivingEntity && !(e instanceof Player)) {
				LivingEntity l = (LivingEntity) e;
				if (player.hasLineOfSight(l)) {
					return true;
				}
			}
		}
		return true;
	}
	
	public String getName() {
		return "Absorbs the health of a living entity in line of sight within 20 blocks away";
	}
}
