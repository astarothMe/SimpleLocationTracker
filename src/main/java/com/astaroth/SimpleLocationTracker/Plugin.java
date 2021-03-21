package com.astaroth.SimpleLocationTracker;

import com.astaroth.SimpleLocationTracker.data.DataMySQL;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private SimpleTracker simpleTracker;

    @Override
    public void onEnable() {
        if (simpleTracker == null) {
            this.simpleTracker = new SimpleTracker();
        }
        simpleTracker.initialize(this, new DataMySQL());
    }

    @Override
    public void onDisable() {
        simpleTracker.shutdown(false);
    }
}
