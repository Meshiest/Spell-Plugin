package com.meshiest;

import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EffectFireBall implements Effect {

	public void doEffect(final Player player, int time, int power) {
		if (power == 0)
			power = 1;
		Location loc = player
				.getEyeLocation()
				.toVector()
				.add(player.getLocation().getDirection().multiply(power * 5))
				.toLocation(player.getWorld(), player.getLocation().getYaw(),
						player.getLocation().getPitch());
		Fireball fireball = player.getWorld().spawn(loc, Fireball.class);

	}

	public ItemStack[] getCost(int time, int power) {
		if (power == 0)
			power = 1;
		ItemStack sulphur = new ItemStack(Material.SULPHUR, 3 * power), snowball = new ItemStack(
				Material.SNOW_BALL, 1);
		ItemStack[] cost = { sulphur, snowball };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		return true;
	}

	public String getName() {
		return "Creates a fireball";
	}
}
