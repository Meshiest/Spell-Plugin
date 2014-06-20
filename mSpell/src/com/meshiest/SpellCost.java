package com.meshiest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.inventory.ItemStack;

public class SpellCost {

	public ItemStack[] items;
	public String body;
	public Spell[] spells;

	public SpellCost(Spell[] spells, ItemStack[] items, String body) {
		this.items = items;
		this.body = body;
		this.spells = spells;
	}

	public static Packet parseContent(String body) {
		body = body.replace("[^\"\\w;&!:,*]", "");
		String[] components = body.split(";");
		int len = components.length;
		while (len > 0 && components[len - 1].length() < 1) {
			len--;
		}
		if (len < 1)
			return new Packet(false, "not enough components");

		String[] names = new String[len], durations = new String[len];
		int[] powers = new int[len];
		String[][] arguments = new String[len][];
		for (int i = 0; i < len; i++) {
			String part = components[i];
			String name, duration;
			String[] argument;
			int power;
			Matcher match = Pattern.compile("(?<=\").*(?=\")").matcher(part);

			if (match.groupCount() > 1)
				return new Packet(false, "more than one name");
			else if (!match.find()) {

				return new Packet(false, "invalid name");
			} else
				name = match.group(0);
			match = Pattern.compile(
					"(?<=&)((([1-9]\\d?[Mm])|([1-9]\\d?[Ss])){1,2}|!)")
					.matcher(part); // match
			// for
			// duration
			if (!match.find()) {

				return new Packet(false, "bad duration");
			} else {
				duration = match.group(0);
			}

			match = Pattern.compile("(?<=:).*(?=:)").matcher(part);

			if (!match.find()) {
				argument = null;
			} else {
				argument = match.group(0).split(",");
			}
			
			match = Pattern.compile("(?<=\\*)[1-9]\\d*").matcher(part);

			if (!match.find()) {
				power = 0;
			} else {
				power = Integer.parseInt(match.group(0));
			}
			
			

			boolean isSpell = Spell.isSpell(name
					+ (duration.equals("!") ? "!" : "&") + (power==0?"":"*"));
			if (!isSpell)
				return new Packet(false, "spell \"" + name
						+ "\""+(duration.equals("!") ? "!" : "&") + (power==0?"":"*")+" does not exist");
			names[i] = name;
			durations[i] = duration;
			arguments[i] = argument;
			powers[i] = power;
		}
		return new Packet(names, durations, arguments, powers);
	}

	public static int intDuration(String duration) {
		if (duration.equals("!"))
			return 0;
		int len = 0;
		Matcher match = Pattern.compile("([1-9]\\d?(?=[Mm]))")
				.matcher(duration);
		if (match.find())
			len += Integer.parseInt(match.group(0)) * 60;
		match = Pattern.compile("([1-9]\\d?(?=[Ss]))").matcher(duration);
		if (match.find())
			len += Integer.parseInt(match.group(0));
		return len;
	}

	public static Packet createSpells(Packet packet) {
		int len = packet.name.length;
		for (int i = 0; i < len; i++) {
			int durInt = packet.durationI[i], power = packet.power[i];
			String name = packet.name[i], durStr = packet.duration[i], nName = name.toLowerCase()
					+ (durInt == 0 ? "!" : "&");

			if (!Spell.effects.containsKey(nName)) {
				packet.work = false;
				packet.error = "invalid spell name";
				return packet;
			}

			Effect effect = Spell.effects.get(nName);

			ItemStack[] stack = effect.getCost(durInt, power);

			packet.spell[i] = new Spell(effect, durInt, power, stack);

		}
		return packet;
	}

}
