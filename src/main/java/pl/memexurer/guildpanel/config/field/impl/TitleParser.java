package pl.memexurer.guildpanel.config.field.impl;

import org.bukkit.configuration.ConfigurationSection;
import pl.memexurer.guildpanel.config.field.CustomConfigurationParser;
import pl.memexurer.guildpanel.util.chat.Title;

import java.util.HashMap;
import java.util.Map;

public class TitleParser implements CustomConfigurationParser<Object, Title> {
    @Override
    public Title parse(Object obj) {
        ConfigurationSection section = (ConfigurationSection) obj;
        return new Title(section.getString("up"), section.getString("down"));
    }

    @Override
    public Object convert(Title obj) {
        Map<String, Object> map = new HashMap<>();
        map.put("up", obj.getTitle());
        map.put("down", obj.getSubtitle());
        return map;
    }
}
