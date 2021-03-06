package com.dreamsecurity.shopface.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public Module jsonMapperJava8DateTimeModule() {
        SimpleModule module = new SimpleModule();

        module.addSerializer(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public void serialize(
                    LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(value));
            }
        });

        module.addSerializer(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public void serialize(
                    LocalTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format(value));
            }
        });

        module.addSerializer(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
            @Override
            public void serialize(
                    LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(value));
            }
        });

        module.addDeserializer(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(
                    JsonParser p, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
                return LocalDateTime.parse(p.getValueAsString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }
        });

        return module;
    }
}
