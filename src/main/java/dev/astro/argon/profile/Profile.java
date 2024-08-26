package dev.astro.argon.profile;

import dev.astro.argon.util.config.Serializable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class Profile implements Serializable<Profile> {

    private final UUID id;

    private Map<UUID, Integer> storedKeys;
    private Map<UUID, Integer> virtualKeys;

    public Profile(UUID id) {
        this.id = id;
        this.storedKeys = new HashMap<>();
        this.virtualKeys = new HashMap<>();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("stored-keys", storedKeys);
        map.put("virtual-keys", virtualKeys);

        return map;
    }

    @Override
    public void deserialize(Map<String, Object> map) {
        storedKeys = (Map<UUID, Integer>) map.get("stored-keys");
        virtualKeys = (Map<UUID, Integer>) map.get("virtual-keys");
    }

    public void addStoredKey(UUID id, int amount) {
        if (storedKeys.containsKey(id)) storedKeys.put(id, storedKeys.get(id) + amount);

        storedKeys.put(id, amount);
    }

    public void removeStoredKey(UUID id, int amount) {
        if (storedKeys.containsKey(id)) {
            storedKeys.put(id, storedKeys.get(id) - amount);
        }
    }

    public void addVirtualKey(UUID id, int amount) {
        if (virtualKeys.containsKey(id)) virtualKeys.put(id, virtualKeys.get(id) + amount);

        virtualKeys.put(id, amount);
    }

    public void removeVirtualKey(UUID id, int amount) {
        if (virtualKeys.containsKey(id)) {
            virtualKeys.put(id, virtualKeys.get(id) - amount);
        }
    }
}
