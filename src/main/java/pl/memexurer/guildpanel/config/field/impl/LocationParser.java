package pl.memexurer.guildpanel.config.field.impl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pl.memexurer.guildpanel.config.field.CustomConfigurationParser;

public class LocationParser implements CustomConfigurationParser<String, Location> {
    private static final LocationParser INSTANCE = new LocationParser();

    @Override
    public Location parse(String obj) {
        if (obj == null) return null;
        String[] splittedString = obj.split(",");
        return new Location(Bukkit.getWorld(splittedString[0]), Integer.parseInt(splittedString[1]), Integer.parseInt(splittedString[2]), Integer.parseInt(splittedString[3]));
    }

    @Override
    public String convert(Location obj) {
        if (obj == null) return null;
        return obj.getWorld().getName() + "," + obj.getBlockX() + "," + obj.getBlockY() + "," + obj.getBlockZ();
    }

    public static LocationParser getInstance() {
        return INSTANCE;
    }
}
