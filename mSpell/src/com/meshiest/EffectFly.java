package com.meshiest;

import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class EffectFly implements Effect {
	public void doEffect(final Player player, int time, int power) {
		player.setAllowFlight(true);
		player.setFlying(true);
		
		player.sendMessage("Started Flying");
		TimerTask task = new TimerTask() {
			final Player p = player;

			public void run() {
				if (p != null) {
					p.setAllowFlight(false);
					p.setFlying(false);
					p.sendMessage("Stopped Flying");
				}
			}
		};
		Core.schedule(task, time*1000);
	}

	public ItemStack[] getCost(int time, int power) {
		ItemStack feather = new ItemStack(Material.FEATHER);
		feather.setAmount(time + 4);
		ItemStack[] cost = { feather };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		return !player.getAllowFlight();
	}
	
	public String getName() {
		return "The ability to fly for a duration of time";
	}
}
