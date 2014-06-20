package com.meshiest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Core extends JavaPlugin implements Listener {

	public Core plugin = this;

	public static Timer timer = new Timer(true);

	public static void main(String[] args) {

	}

	public void onEnable() {

		log("Starting up");

		Spell.init();

		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {
		log("Shutting down");
	}

	public void log(String s) {
		getLogger().info(s);
	}

	public boolean onCommand(CommandSender send, Command cmd, String label,
			String[] args) {
		/*
		 * if (cmd.getName().equals("fly")) { if (args.length != 1) {
		 * send.sendMessage("Invalid arguments, requires one"); return false; }
		 * else if (send instanceof Player) { Player player = (Player) send;
		 * plugin.flyToggle(player, Integer.parseInt(args[0])); return true; } }
		 * else
		 */if (cmd.getName().equals("compile")) {
			if (send instanceof Player) {
				Player player = (Player) send;
				boolean hasBook = player.getItemInHand().getType()
						.equals(Material.WRITTEN_BOOK)
						|| player.getItemInHand().getType()
								.equals(Material.BOOK_AND_QUILL);

				if (!hasBook) {
					player.sendMessage(ChatColor.YELLOW
							+ "You aren't holding a book");
					return true;
				}
				ItemStack st = player.getItemInHand();
				BookMeta m = (BookMeta) st.getItemMeta();
				if (player.getItemInHand().getType()
						.equals(Material.WRITTEN_BOOK)
						&& !m.getTitle().substring(0, 1).equals("$")) {
					player.sendMessage(ChatColor.YELLOW
							+ "That isn't a spell book");
					return true;
				}

				String body = "";
				for (String page : m.getPages()) {
					body += page;
				}
				Packet packet = SpellCost.parseContent(body);
				if (!packet.work) {
					player.sendMessage(ChatColor.YELLOW + "Error"
							+ ChatColor.WHITE + ": " + packet.error);
				} else {
					packet = SpellCost.createSpells(packet);
					player.sendMessage(ChatColor.YELLOW + "Components"
							+ ChatColor.WHITE + ": ");
					for (int i = 0; i < packet.name.length; i++) {
						String name = packet.name[i], duration = packet.duration[i], cost = "";
						ItemStack[] stack = packet.spell[i].cost;
						ArrayList<Integer> used = new ArrayList<Integer>();
						for (int k = 0; k < stack.length; k++) {

							if (used.contains(k))
								continue;
							ItemStack s = stack[k];
							int amount = s.getAmount();
							for (int j = 0; j < stack.length; j++) {
								if (j != k
										&& stack[k].getType().equals(
												stack[j].getType())
										&& !used.contains(j)) {
									amount += stack[j].getAmount();
									used.add(j);
								}
							}
							cost += s.getType().toString() + " (" + amount
									+ ")\n";
						}
						player.sendMessage((i + 1) + ". " + ChatColor.GREEN
								+ name + ChatColor.WHITE + "("
								+ ChatColor.YELLOW + duration + ChatColor.WHITE
								+ ") *" + ChatColor.RED + packet.power[i]
								+ ChatColor.WHITE + " => \n" + ChatColor.YELLOW
								+ cost.trim());
					}

				}
				return true;
			}
		} else if (cmd.getName().equals("spells")) {
			if (args.length != 1) {
				return false;
			}
			Set<String> spells = Spell.effects.keySet();
			int count = (Integer.parseInt(args[0]) - 1) % (spells.size() / 5);
			send.sendMessage("Key:" + ChatColor.GREEN
					+ " !=instant, &=duration, *=power, :=argument\n"
					+ ChatColor.YELLOW + "Page " + (count + 1) + "/"
					+ (spells.size() / 5));
			ArrayList<String> sorted = new ArrayList<String>();
			for (String spell : spells) {
				sorted.add(spell);
			}
			Collections.sort(sorted);
			count *= 5;
			for (int i = 0, end = (count + 5 > spells.size() ? spells.size() - 5
					: 5); i < end; i++) {
				send.sendMessage((i + count + 1) + " - "
						+ sorted.get(i + count));
			}
			return true;
		} else if (cmd.getName().equals("spell")) {
			if (args.length != 1) {
				return false;
			}
			args[0] = args[0].toLowerCase();
			if (!Spell.effects.keySet().contains(args[0])) {
				send.sendMessage("\"" + ChatColor.YELLOW + args[0]
						+ ChatColor.WHITE + "\" is not a valid spell");
				for (String spell : Spell.effects.keySet()) {
					if (spell.startsWith(args[0]))
						send.sendMessage(ChatColor.RED + spell);
				}
				return true;
			}
			Effect effect = Spell.effects.get(args[0]);
			send.sendMessage(ChatColor.YELLOW + args[0] + ":\n"
					+ ChatColor.WHITE + effect.getName());
			return true;
		}
		return false;
	}

	public void flyToggle(final Player player, int time) {
		Spell.effects.get("fly&").doEffect(player, time, 0);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent e) {
		// log("Action: "+e.getAction().name() + " " +
		// Action.LEFT_CLICK_BLOCK.name() + " " + Action.LEFT_CLICK_AIR.name());
		if (!e.getAction().equals(Action.LEFT_CLICK_AIR)
				&& !e.getAction().equals(Action.LEFT_CLICK_BLOCK)
				&& !e.getAction().equals(Action.PHYSICAL))
			return;

		Player player = e.getPlayer();

		boolean hasBook = player.getItemInHand().getType()
				.equals(Material.WRITTEN_BOOK);

		if (!hasBook)
			return;

		ItemStack st = player.getItemInHand();
		BookMeta m = (BookMeta) st.getItemMeta();
		if (!m.getTitle().substring(0, 1).equals("$"))
			return;

		String body = "";
		for (String page : m.getPages()) {
			body += page;
		}
		// log("compiling book");
		Packet packet = SpellCost.parseContent(body);
		if (!packet.work) {
			player.sendMessage(ChatColor.YELLOW + "Error" + ChatColor.WHITE
					+ ": " + packet.error);
		} else {
			packet = SpellCost.createSpells(packet);
			if (!packet.work) {
				player.sendMessage(ChatColor.YELLOW + "Error" + ChatColor.WHITE
						+ ": " + packet.error);
			} else {
				if (!packet.canCast(player)) {
					player.sendMessage(ChatColor.RED + "Could not cast spell");
				} else {
					player.sendMessage(ChatColor.GREEN + "Casting spell; "
							+ ChatColor.YELLOW + packet.name.length + " effect"
							+ (packet.name.length == 1 ? "" : "s"));
					packet.cast(player);
				}
			}
		}
	}

	public static void schedule(TimerTask task, long delay) {
		timer.schedule(task, delay);
	}

}
