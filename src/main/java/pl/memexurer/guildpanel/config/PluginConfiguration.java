package pl.memexurer.guildpanel.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.guildpanel.config.field.ConfigurationParserType;

import java.lang.reflect.Field;

public class PluginConfiguration extends DataConfiguration {

    public PluginConfiguration(JavaPlugin plugin, String fileName) {
        super(plugin, fileName);
    }

    public PluginConfiguration(Plugin plugin) {
        super(plugin, "config.yml");
    }

    protected void loadConfiguration(String sectionName) {
        if (super.configuration == null)
            super.loadConfiguration();

        this.loadConfiguration(super.getConfiguration().getConfigurationSection(sectionName));
    }

    public void loadConfiguration() {
        loadConfiguration("");
    }

    @SuppressWarnings("unchecked")
    private void loadConfiguration(ConfigurationSection section) {
        for (Field f : getClass().getFields()) {
            if (!f.isAnnotationPresent(ConfigAnnotation.class)) continue;
            try {
                ConfigAnnotation annotation = f.getAnnotation(ConfigAnnotation.class);
                Object val = section.get(annotation.path());
                if (annotation.parserType() != ConfigurationParserType.DEFAULT)
                    val = annotation.parserType().getParserType().parse(val);
                if (val == null) {
                    //    System.out.println("Wystapil blad podczas ladowania " + getClass().getName() + "#" + f.getName());
                    continue;
                }

                f.set(this, val);
            } catch (Exception e) {
                System.out.println("Wystapil blad podczas ladowania " + getClass().getName() + "#" + f.getName());
                e.printStackTrace();
            }
        }
    }

    public void loadDataConfiguration() {
        super.loadConfiguration();
    }

    @SuppressWarnings("unchecked")
    public void saveConfiguration() {
        for (Field f : getClass().getFields()) {
            if (!f.isAnnotationPresent(ConfigAnnotation.class)) continue;
            try {
                ConfigAnnotation annotation = f.getAnnotation(ConfigAnnotation.class);
                Object val = f.get(this);
                if (val == null) continue;

                if (annotation.parserType() != ConfigurationParserType.DEFAULT)
                    val = annotation.parserType().getParserType().convert(val);

                if (val != null)
                    getConfiguration().set(annotation.path(), val);
            } catch (Exception e) {
                System.out.println("Wystapil blad podczas zapisywania " + getClass().getName() + "#" + f.getName());
                e.printStackTrace();
            }
        }

        super.saveConfiguration();
    }

}
