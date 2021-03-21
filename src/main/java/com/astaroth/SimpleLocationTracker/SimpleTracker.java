package com.astaroth.SimpleLocationTracker;

import com.astaroth.SimpleLocationTracker.command.ReloadCommand;
import com.astaroth.SimpleLocationTracker.data.IData;
import com.astaroth.SimpleLocationTracker.task.TrackerTask;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.openhft.hashing.LongHashFunction;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SimpleTracker {

    private IData data;
    private JavaPlugin plugin;
    private BukkitTask trackerTask;

    public Config config = null;
    public final List<String> logs = new ArrayList<>();
    public final List<Command> registeredCommands = new ArrayList<>();

    public void initialize(JavaPlugin plugin, IData data){
        if (data == null || this.plugin == null){
            this.plugin = plugin;
            this.data = data;
        }
        loadConfig();
        this.data.initialize(this);
        this.trackerTask =  new TrackerTask(this).runTaskTimer(plugin, 0, 20L * config.getTracker().getUpdateInterval());
        this.registeredCommands.addAll(Collections.singleton(new ReloadCommand(this, "streload")));
        if (config.getTracker().isEnableCommand()){
            enableCommand();
        }
    }

    public void enableCommand(){
        registeredCommands.forEach(this::registerCommand);
    }

    public void disableCommand(){
        registeredCommands.forEach(this::unregisterCommand);
    }

    public void loadConfig(){
        File conf = new File(plugin.getDataFolder(), "SimpleTracker.yml");
        if (!conf.exists()) {
            conf.getParentFile().mkdirs();
            plugin.saveResource("SimpleTracker.yml", false);
        }
        ObjectMapper obj = new ObjectMapper(new YAMLFactory());
        try {
            this.config = obj.readValue(new File(plugin.getDataFolder(), "SimpleTracker.yml"), Config.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown(boolean restart){
        trackerTask.cancel();
        disableCommand();
        data.insert(logs).thenRun(() -> {
            logs.clear();
            data.shutdown();
            if (restart) initialize(plugin, data);
        });
    }

    public void registerCommand(Command command) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register("SimpleTracker", command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void unregisterCommand(Command command) {
        try {
            PluginManager pluginManager = Bukkit.getPluginManager();
            Class<? extends PluginManager> pluginManagerClass = pluginManager.getClass();
            Field commandMapField = pluginManagerClass.getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap simpleCommandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
            Field knownCommandsField = simpleCommandMap.getClass().getSuperclass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            HashMap<String, Command> knownCommands = (HashMap<String, Command>) knownCommandsField.get(simpleCommandMap);
            commandMapField.setAccessible(false);
            knownCommandsField.setAccessible(false);
            knownCommands.remove(command.getName());
            command.getAliases().forEach(knownCommands::remove);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Void> update(){
        return data.insert(logs);
    }

    public static String hash(String in) throws NoSuchAlgorithmException {
        return Long.toHexString(LongHashFunction.xx(69).hashBytes(in.getBytes(StandardCharsets.UTF_8)));
    }

}
