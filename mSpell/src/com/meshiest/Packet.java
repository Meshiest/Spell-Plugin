package com.meshiest;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Packet {

	public boolean work;
	public String error;
	public String[] duration, name;
	public String[][] argument;
	public int[] durationI, power;
	public Spell[] spell;

	public Packet(boolean work, String error) {
		this.work = work;
		this.error = error;
	}

	public Packet(String[] name, String[] duration, String[][] argument, int[] power) {
		this.duration = duration;
		this.durationI = new int[name.length];
		for (int i = 0; i < duration.length; i++) {
			durationI[i] = SpellCost.intDuration(duration[i]);
		}
		this.name = name;
		this.argument = argument;
		this.power = power;
		this.spell = new Spell[name.length];
		this.work = true;
	}
	
	public boolean canCast(Player player) {
		for(int i=0;i<spell.length; i++)
			if(!spell[i].canCast(player))
				return false;
		return true;
	}
	
	public void cast(Player player) {
		for(int i=0;i<spell.length; i++)
			spell[i].cast(player);
	}
}