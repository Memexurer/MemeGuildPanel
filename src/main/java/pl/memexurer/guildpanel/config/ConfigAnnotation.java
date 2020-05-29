package pl.memexurer.guildpanel.config;

import pl.memexurer.guildpanel.config.field.ConfigurationParserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigAnnotation {
    String path();

    String comment() default "";

    ConfigurationParserType parserType() default ConfigurationParserType.DEFAULT;
}
