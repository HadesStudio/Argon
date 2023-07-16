package dev.rage.argon.util.config;

import java.util.Map;

public interface Serializable<T> {

    Map<String, Object> serialize();

    void deserialize(Map<String, Object> map);
}
