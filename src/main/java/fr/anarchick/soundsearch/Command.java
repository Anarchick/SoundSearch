package fr.anarchick.soundsearch;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;

import fr.anarchick.soundsearch.Engine.Group;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Command implements CommandExecutor {
	
	final private static String EMPTY = new String();
	final private static String PLAY = ""+'\u25B6';
	final private static String FAVORITE = ""+'\u2764';
	
	private String[] args = null;
	private Player player;
	

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if (sender instanceof Player) {
            this.player = (Player) sender;
            this.args = args;
            
            if (getArg(0) == EMPTY) {
            	print(null, null, 1);
            	
            } else if (getArg(0).equals("tool")) {
    				Material material = player.getInventory().getItemInMainHand().getType();
    				Group group = (material.isBlock()) ? Group.BLOCK : Group.ITEM;
    				print(material.name(), group, 1);
    				
            } else if (getArg(0).equals("favorite")) {
            	if (!getArg(1).equals(EMPTY)) {
            		String soundName = getArg(1);
            		if (Engine.getSound(soundName) != null) {
            			Favorite.addOrRemove(soundName, player);
            		}
            	}
            	
            } else if (getArg(0).equals("play")) {
				if (!getArg(1).equals(EMPTY) && !getArg(2).equals(EMPTY)) {
					float pitch = 1f;
					try {
						pitch = Float.valueOf(getArg(2));
					} catch (Exception e) {}
					play(getArg(1), pitch);
					return true;
				}
				
			} else if (getArg(0).equals("page")) {
				int page;
				try {
					page = Integer.valueOf(getArg(3));
					print(getArg(1), Engine.getGroup(getArg(2)), page);
				} catch (Exception e) {}

			} else {
        		String groupName = getArg(1).toUpperCase();
        		Group group = Engine.getGroup(groupName);
        		print(getArg(0), group, 1);
            }

        }
		return true;
	}
	
	@Nonnull
	private String getArg(int i) {
		if (this.args == null) return EMPTY;
		if (this.args.length > i) {
			return this.args[i];
		} else {
			return EMPTY;
		}
	}
	
	private void play(String soundName, float pitch) {
		Sound sound = Engine.getSound(soundName);
		player.stopAllSounds();
		player.playSound(player, sound, SoundCategory.MASTER, 10f, pitch);
	}
	
	private void print(@Nullable String regex, @Nullable Group group, int page) {
		List<String> content;
		int maxPage;
		if (regex == null && group == null) {
			content = Favorite.get();
			maxPage = Math.floorDiv(content.size(), 18) +1;
			this.player.sendMessage(""+ChatColor.GOLD+ChatColor.BOLD+"'FAVORITES' from page "+page+"/"+maxPage);
		} else {
			content = Engine.search(regex, group);
			maxPage = Math.floorDiv(content.size(), 18) +1;
			this.player.sendMessage(""+ChatColor.GOLD+ChatColor.BOLD+"'"+regex+"' in group '"+group.name()+"' from page "+page+"/"+maxPage);
		}
		
		final List<String> sounds = new LinkedList<>();
		int offset = (page -1) * 18;
		for (int i = offset; i < 18+offset; i++) {
			if (content.size() > i) {
				sounds.add(content.get(i));
			}
		}
		
		Spigot player = this.player.spigot();
		for (String sound : sounds) {
			
			TextComponent message = new TextComponent("- ");
			
			float p = 0.0f;
			ChatColor[] colors = {ChatColor.DARK_RED , ChatColor.RED , ChatColor.GOLD , ChatColor.GREEN , ChatColor.DARK_GREEN};
			for (ChatColor color : colors) {
				String shortPitch = String.format(Locale.US, "%.1f ", p);
				TextComponent pitch = new TextComponent(PLAY);
				pitch.setColor(color);
				pitch.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/soundsearch play "+sound+" "+shortPitch));
				pitch.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(sound+" Pitch="+shortPitch)));
				message.addExtra(pitch);
				p += 0.5f;
			}
			
			TextComponent favorite = new TextComponent(" "+ChatColor.LIGHT_PURPLE+FAVORITE+" ");
			favorite.setColor(ChatColor.DARK_AQUA);
			favorite.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/soundsearch favorite "+sound));
			message.addExtra(favorite);
			
			TextComponent soundName = new TextComponent(sound);
			soundName.setColor(ChatColor.AQUA);
			soundName.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, sound));
			soundName.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(sound)));
			message.addExtra(soundName);

			player.sendMessage(message);
		}
		if (maxPage > 1 && page < maxPage) {
			TextComponent next = new TextComponent("NEXT PAGE");
			next.setColor(ChatColor.GOLD);
			next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/soundsearch page "+regex+" "+group+" "+(++page)));
			player.sendMessage(next);
		}
	}
	
}
