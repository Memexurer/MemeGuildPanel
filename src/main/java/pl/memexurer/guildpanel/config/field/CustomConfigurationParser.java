package pl.memexurer.guildpanel.config.field;

public interface CustomConfigurationParser<K, T> {
    T parse(K obj);

    K convert(T obj);
}
