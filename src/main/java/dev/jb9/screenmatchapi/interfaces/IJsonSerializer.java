package dev.jb9.screenmatchapi.interfaces;

public interface IJsonSerializer {
    <T> T deserialize(String json, Class<T> objectClass);
}
