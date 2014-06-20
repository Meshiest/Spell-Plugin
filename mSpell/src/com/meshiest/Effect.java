package com.meshiest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Effect {

	public void doEffect(Player player, int time, int power);
	public boolean canDoEffect(Player player);
	public ItemStack[] getCost(int time, int power);
	public String getName();
	
}
