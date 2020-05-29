package pl.memexurer.guildpanel.config.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import pl.memexurer.guildpanel.config.ConfigAnnotation;
import pl.memexurer.guildpanel.config.PluginConfiguration;
import pl.memexurer.guildpanel.data.PanelPermission;

import java.util.Map;
import java.util.stream.Collectors;

public class GuildPanelConfiguration extends PluginConfiguration {
    @ConfigAnnotation(path = "database.ip", comment = "ip bazy danych")
    public String databaseAddress;
    @ConfigAnnotation(path = "database.name", comment = "nazwa bazy danych")
    public String databaseName;
    @ConfigAnnotation(path = "database.user", comment = "nazwa uzytkownika")
    public String databaseUser;
    @ConfigAnnotation(path = "database.password", comment = "haslo")
    public String databasePassword;
    @ConfigAnnotation(path = "database.port", comment = "port do bazy danych")
    public int databasePort;

    public Map<PanelPermission, Boolean> defaultPermissions;

    public GuildPanelConfiguration(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void loadConfiguration() {
        super.loadConfiguration();

        ConfigurationSection permissionEntrySection = configuration.getConfigurationSection("permissions");
        this.defaultPermissions = permissionEntrySection.getKeys(false).stream()
                .map(permissionEntrySection::getConfigurationSection)
                .peek(section -> PanelPermission.byName(section.getName()).setMessage(section.getString("message")))
                .collect(Collectors.toMap(section -> PanelPermission.byName(section.getName()), section -> section.getBoolean("default")));
    }

}
