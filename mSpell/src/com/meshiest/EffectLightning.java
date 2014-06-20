package com.meshiest;

import java.util.TimerTask;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

@SuppressWarnings("deprecation")
public class EffectLightning implements Effect {

	public void doEffect(final Player player, int time, int power) {
		Block block = player.getTargetBlock(null, 50).getRelative(BlockFace.UP);
		if (block != null)
			player.getWorld().strikeLightning(block.getLocation());
	}

	public ItemStack[] getCost(int time, int power) {
		ItemStack sulphur = new ItemStack(Material.GOLD_SWORD, 1), snowball = new ItemStack(
				Material.DIAMOND, 1);
		ItemStack[] cost = { sulphur, snowball };
		return cost;
	}

	public boolean canDoEffect(Player player) {
		Block block = player.getTargetBlock(null, 50).getRelative(BlockFace.UP);
		return block != null;
	}

	public String getName() {
		return "Creates lightning where you are looking";
	}
}
