package fr.anarchick.soundsearch;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;

public class Favorite {
	
	final private static List<String> favorite = new LinkedList<>();
	
	public static List<String> get() {
		return favorite;
	}

	public static void addOrRemove(String soundName, Player player) {
		if (favorite.contains(soundName)) {
			favorite.remove(soundName);
			player.sendTitle(soundName, "remove from favorites", 0, 20, 0);
		} else {
			favorite.add(soundName);
			player.sendTitle(soundName, "add to favorites", 0, 20, 0);
		}
	}
	
}
