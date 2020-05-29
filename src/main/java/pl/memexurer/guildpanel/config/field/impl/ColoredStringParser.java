package pl.memexurer.guildpanel.config.field.impl;

import pl.memexurer.guildpanel.config.field.CustomConfigurationParser;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

public class ColoredStringParser implements CustomConfigurationParser<String, String> {
    @Override
    public String parse(String obj) {
        if (obj == null) return null;
        return ChatUtil.fixColor(obj);
    }

    @Override
    public String convert(String obj) {
        return obj.replace("\u00A7", "&");
    }
}
