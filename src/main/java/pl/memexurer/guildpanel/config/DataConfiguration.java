package pl.memexurer.guildpanel.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class DataConfiguration {
    protected YamlConfiguration configuration;
    private File file;

    public DataConfiguration(Plugin plugin, String fileName) {
        fileName = (fileName.endsWith(".yml") ? fileName : fileName + ".yml");
        this.file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            if (plugin.getResource(fileName) != null) plugin.saveResource(fileName, false);
            else if (!file.exists()) {
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadConfiguration() {
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }

    public void saveConfiguration() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
