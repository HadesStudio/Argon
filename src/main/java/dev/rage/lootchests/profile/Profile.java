package dev.rage.lootchests.profile;

import dev.rage.lootchests.util.config.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class Profile implements Serializable<Profile> {

    private final UUID id;
    // +527441019720
    private Map<UUID, Integer> storedKeys = new HashMap<>();
    private Map<UUID, Integer> virtualKeys = new HashMap<>();

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
        if (storedKeys.containsKey(id)) storedKeys.put(id, storedKeys.get(id) + 1);

        storedKeys.put(id, 1);
    }

    public void removeStoredKey(UUID id, int amount) {
        if (storedKeys.containsKey(id)) {
            storedKeys.put(id, storedKeys.get(id) - amount);
        }
    }

    public void addVirtualKey(UUID id, int amount) {
        if (virtualKeys.containsKey(id)) virtualKeys.put(id, virtualKeys.get(id) + 1);

        virtualKeys.put(id, 1);
    }

    public void removeVirtualKey(UUID id, int amount) {
        if (virtualKeys.containsKey(id)) {
            virtualKeys.put(id, virtualKeys.get(id) - amount);
        }
    }
}
