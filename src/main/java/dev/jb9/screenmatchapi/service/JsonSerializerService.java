package dev.jb9.screenmatchapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jb9.screenmatchapi.exception.JsonSerializerException;

public class JsonSerializerService {
    public <T> T deserialize(String json, Class<T> objectClass) {
        try {
            return new ObjectMapper().readValue(json, objectClass);
        } catch (JsonProcessingException exception) {
            throw new JsonSerializerException(
                    "Couldn't deserialize %s due to %s".formatted(json, exception.getMessage())
            );
        }
    }
}
