package com.meshiest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Spell {
	public static HashMap<String, Effect> effects;

	public Effect effect;
	public ItemStack[] cost;
	public int time, power;

	public Spell(Effect effect, int duration, int power, ItemStack[] cost) {
		this.effect = effect;
		this.cost = cost;
		this.power = power;
		this.time = duration;
	}

	public static void init() {
		effects = new HashMap<String, Effect>();
		effects.put("fly&", new EffectFly());
		effects.put("primum volatum&", effects.get("fly&")); // the first flight

		effects.put("speed&", new EffectSpeed());
		effects.put("speed&*", new EffectSpeed());
		effects.put("inchoare celeritate&", effects.get("speed&")); // initiate
																	// speed
		effects.put("inchoare celeritate&*", effects.get("speed&*"));

		effects.put("chicken!", new EffectChicken());
		effects.put("chicken!*", new EffectChicken());
		effects.put("partum a gallinaceo!", effects.get("chicken!")); // create
																		// a
																		// chicken
		effects.put("partum a gallinaceo!*", effects.get("chicken!*"));

		effects.put("fireball!", new EffectFireBall());
		effects.put("fireball!*", new EffectFireBall());
		effects.put("sphaera ignis!", effects.get("fireball!")); // ball of fire
		effects.put("sphaera ignis!*", effects.get("fireball!*"));

		effects.put("lightning!", new EffectLightning());
		effects.put("facit fulgura!", effects.get("lightning!")); // make bright

		effects.put("absorb!", new EffectAbsorbHealth());
		effects.put("absorb!*", new EffectAbsorbHealth());
		effects.put("salutem accipe!", effects.get("absorb!")); // take health
		effects.put("salutem accipe!*", effects.get("absorb!"));
	}

	public static boolean isSpell(String name) {
		return effects.containsKey(name);
	}

	public ItemStack[] getCost() {
		return null;
	}

	public boolean canCast(Player player) {
		PlayerInventory inv = player.getInventory();
		ArrayList<Integer> used = new ArrayList<Integer>();
		for (int i = 0; i < cost.length; i++) {
			if (!effect.canDoEffect(player)) {
				player.sendMessage(ChatColor.RED
						+ "You can't use this spell right now");
				return false;
			}
			if (used.contains(i))
				continue;
			ItemStack stack = cost[i];
			int amount = stack.getAmount();
			for (int j = 0; j < cost.length; j++) {
				if (j != i && cost[i].getType().equals(cost[j].getType())
						&& !used.contains(j)) {
					amount += cost[j].getAmount();
					used.add(j);
				}
			}
			if (!inv.contains(stack.getType(), amount)) {
				player.sendMessage(ChatColor.RED + "You don't have " + amount
						+ " " + stack.getType().toString());
				return false;
			}
		}
		return true;
	}

	public void cast(Player player) {
		PlayerInventory inv = player.getInventory();
		ArrayList<Integer> used = new ArrayList<Integer>();
		for (int i = 0; i < cost.length; i++) {
			if (used.contains(i))
				continue;
			ItemStack stack = cost[i];
			int amount = stack.getAmount();
			for (int j = 0; j < cost.length; j++) {
				if (j != i && cost[i].getType().equals(cost[j].getType())
						&& !used.contains(j)) {
					amount += cost[j].getAmount();
					used.add(j);
				}
			}
			inv.removeItem(new ItemStack(stack.getType(), amount));
		}
		this.effect.doEffect(player, time, power);
	}

}
