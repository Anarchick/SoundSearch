package fr.anarchick.soundsearch;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Sound;

public class Engine {
	
	public enum Group {
		ALL, AMBIENT, BLOCK, ENTITY, ITEM, MUSIC, UI; // WEATHER, ENCHANT, EVENT, PARTICLE;
		
		private List<String> content = new LinkedList<>();
		
		private void addContent(final String content) {
			this.content.add(content);
		}
		
		private void lock() {
			this.content = List.of(this.content.toArray(new String[0]));
		}
		
		public List<String> getContent() {
			return this.content;
		}
	}
	
	static {
		for (Sound sound : Sound.values()) {
			String soundName = sound.name();
			List<String> groups = RegexUtils.getRegexGroup("([A-Z]+)", soundName, 1);
			try {
				Group group = Group.valueOf(groups.get(0));
				group.addContent(soundName);
			} catch (Exception e) {}
			Group.ALL.addContent(soundName);
		}
		for (Group group : Group.values()) {
			group.lock();
		}
	}
	
	private static String retentionRegex;
	private static Group retentionGroup;
	private static List<String> retentionContent;

	public static List<String> search(String regex, final Group group) {
		regex = regex.toUpperCase();
		if (regex.equals(retentionRegex) && group.equals(retentionGroup)) {
			return retentionContent;
		}
		retentionRegex = regex;
		retentionGroup = group;
		List<String> matchers = new LinkedList<>();
		for (String soundName : group.getContent()) {
			if (RegexUtils.regexMatch(regex, soundName)) {
				matchers.add(soundName);
			}
		}
		retentionContent = List.of(matchers.toArray(new String[0]));
		return retentionContent;
	}
	
	@Nullable
	public static Sound getSound(final String soundName) {
		Sound sound = null;
		try {
			sound = Sound.valueOf(soundName);
		} catch (Exception e) {}
		return sound;
	}
	
	@Nonnull
	public static Group getGroup(final String groupName) {
		Group group = Group.ALL;
		try {
			group = Group.valueOf(groupName);
		} catch (Exception e) {}
		return group;
	}
	
}
