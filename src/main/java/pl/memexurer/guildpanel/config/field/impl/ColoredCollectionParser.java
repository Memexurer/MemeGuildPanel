package pl.memexurer.guildpanel.config.field.impl;

import pl.memexurer.guildpanel.config.field.CustomConfigurationParser;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ColoredCollectionParser implements CustomConfigurationParser<List<String>, List<String>> {
    @Override
    public List<String> parse(List<String> obj) {
        return obj.stream().map(ChatUtil::fixColor).collect(Collectors.toList());
    }

    @Override
    public List<String> convert(List<String> obj) {
        return obj.stream().map(str -> str.replace("\u00A7", "&")).collect(Collectors.toList());
    }
}
