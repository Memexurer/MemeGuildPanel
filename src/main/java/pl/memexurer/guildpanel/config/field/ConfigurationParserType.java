package pl.memexurer.guildpanel.config.field;

import pl.memexurer.guildpanel.config.field.impl.ColoredCollectionParser;
import pl.memexurer.guildpanel.config.field.impl.ColoredStringParser;
import pl.memexurer.guildpanel.config.field.impl.LocationParser;
import pl.memexurer.guildpanel.config.field.impl.TitleParser;

public enum ConfigurationParserType {
    DEFAULT(null),
    COLORED_STRING(new ColoredStringParser()),
    COLLECTION_COLOR_STRING(new ColoredCollectionParser()),
    LOCATION_STRING(new LocationParser()),
    TITLE(new TitleParser());

    private CustomConfigurationParser parserType;

    ConfigurationParserType(CustomConfigurationParser parserType) {
        this.parserType = parserType;
    }

    public CustomConfigurationParser getParserType() {
        return parserType;
    }
}
