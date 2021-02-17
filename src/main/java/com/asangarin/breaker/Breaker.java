package com.asangarin.breaker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.asangarin.breaker.api.NMSHandler;
import com.asangarin.breaker.command.BreakerCommand;
import com.asangarin.breaker.command.BreakerTabComplete;
import com.asangarin.breaker.core.BreakingCore;
import com.asangarin.breaker.legacy.DefaultManager;
import com.asangarin.breaker.legacy.LegacyManager;
import com.asangarin.breaker.legacy.VersionManager;
import com.asangarin.breaker.manager.ConfigManager;
import com.asangarin.breaker.manager.Database;
import com.asangarin.breaker.manager.StatesManager;
import com.asangarin.breaker.manager.SystemManager;
import com.asangarin.breaker.manager.TriggerManager;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

public class Breaker extends JavaPlugin
{
	public static Breaker plugin;
	private final List<String> compat = new ArrayList<>();
	
	public Settings settings;
	public ProtocolManager protocol;
	public StatesManager states;
	public TriggerManager triggers;
	public Database database;
	public SystemManager system;
	public NMSHandler nms;
	public VersionManager legacy;
	
	private ConfigManager config;
	public BreakingCore core;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Override
    public void onEnable() {
        plugin = this;
		new Metrics(this);

		// If default directory does not exist, create it.
		File f = new File(plugin.getDataFolder() + "/");
		if(!f.exists())
			f.mkdir();

		// Check if config.yml is already generated, if not, generate all the other default files.
		if(!new File(this.getDataFolder(), "config.yml").exists()) {
			File folder = new File(this.getDataFolder(), "/blockconfigs/");
			if(!folder.exists()) {
				if(folder.mkdir()) {
					File sampleFile1 = new File(this.getDataFolder(), "/blockconfigs/STONE_BLOCKS.yml");
					File sampleFile2 = new File(this.getDataFolder(), "/blockconfigs/WOODEN_BLOCKS.yml");
					if(sampleFile1.exists()) return;
					if(sampleFile2.exists()) return;
					InputStream input1 = getClass().getResourceAsStream("/default/blockconfigs/STONE_BLOCKS.yml");
					InputStream input2 = getClass().getResourceAsStream("/default/blockconfigs/WOODEN_BLOCKS.yml");

					try {
						byte[] buffer1 = new byte[input1.available()];
						input1.read(buffer1);
						byte[] buffer2 = new byte[input2.available()];
						input2.read(buffer2);

						OutputStream output1 = new FileOutputStream(sampleFile1);
						output1.write(buffer1);
						OutputStream output2 = new FileOutputStream(sampleFile2);
						output2.write(buffer2);
					} catch(IOException io) {
						io.printStackTrace();
					}
				} else {
					warn("Couldn't create blockconfigs folder...!");
				}
			}
		}
        this.saveDefaultConfig();
        settings = new Settings(this.getConfig().getConfigurationSection("settings"));
        
		this.getCommand("breaker").setExecutor(new BreakerCommand());
		this.getCommand("breaker").setTabCompleter(new BreakerTabComplete());
        
        protocol = ProtocolLibrary.getProtocolManager();
		try {
			//Bind NMS handler to running version
			nms = (NMSHandler) Class.forName("com.asangarin.breaker.nms.NMSHandler_" + Bukkit.getServer().getClass()
					.getPackage().getName().replace(".", ",").split(",")[3].substring(1)).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			warn("Couldn't find NMS class for version: '" + Bukkit.getServer().getClass()
				.getPackage().getName().replace(".", ",").split(",")[3].substring(1) + "'!");
			warn("Are you using a supported version of Minecraft?");
			Bukkit.getPluginManager().disablePlugin(plugin);
		}

		if(nms.getVersion().equals("1.12_R1"))
			legacy = new LegacyManager();
		else
			legacy = new DefaultManager();
		
		for(String plugin : new String[]{"MMOItems", "MMOCore", "WorldGuard", "PlaceholderAPI"})
			if(Bukkit.getPluginManager().isPluginEnabled(plugin))
				compat.add(plugin);
		
        states = new StatesManager();
        triggers = new TriggerManager();
        database = new Database();
        system = new SystemManager();
        config = new ConfigManager();
        core = new BreakingCore();
        
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        onReload();
    }

	public boolean isLoaded(String p)
	{ return compat.contains(p); }

	public void onReload() {
		reloadConfig();
		config.reload();
		log("Breaker reloaded!");
	}

	public static void log(String m)
	{ plugin.getLogger().info(m); }
	public static void warn(String m)
	{ plugin.getLogger().warning(m); } 
	public static void debug(String m, int depth) {
		if(plugin.getConfig().getBoolean("debug.enabled") && plugin.getConfig().getInt("debug.depth") >= depth)
			plugin.getLogger().info("[Debug] " + m);
	}
}
