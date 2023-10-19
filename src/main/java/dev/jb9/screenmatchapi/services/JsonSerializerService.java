package dev.jb9.screenmatchapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jb9.screenmatchapi.exceptions.JsonSerializerException;
import dev.jb9.screenmatchapi.interfaces.IJsonSerializer;

public class JsonSerializerService implements IJsonSerializer {
    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T deserialize(String json, Class<T> objectClass) {
        try {
            return mapper.readValue(json, objectClass);
        } catch (JsonProcessingException exception) {
            throw new JsonSerializerException(
                    "Couldn't deserialize %s due to %s".formatted(json, exception.getMessage())
            );
        }
    }
}
