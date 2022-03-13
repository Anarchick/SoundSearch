package fr.anarchick.soundsearch;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static Main instance;
	
	public static final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
	
	@Override
	public void onEnable() {
		if(instance != null)
            throw new IllegalStateException("Plugin initialized twice.");
		instance = this;
		this.getCommand("soundsearch").setExecutor(new Command());
	}
	
}
