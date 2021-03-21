package com.astaroth.SimpleLocationTracker.command;

import com.astaroth.SimpleLocationTracker.SimpleTracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ReloadCommand extends Command {

    private final SimpleTracker tracker;

    public ReloadCommand(SimpleTracker tracker, @NotNull String name) {
        super(name);
        this.setDescription("SimpleTracker Reload Command");
        this.setAliases(Collections.emptyList());
        this.setPermission("simpletracker.command.reload");
        this.tracker = tracker;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender.hasPermission("simpletracker.command.reload")){
            sender.sendMessage("Restarting plugin...");
            tracker.shutdown(true);
            return true;
        }
        return false;
    }
}
