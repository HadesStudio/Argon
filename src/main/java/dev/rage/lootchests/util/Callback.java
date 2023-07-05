package dev.rage.lootchests.util;

import java.io.Serializable;

public interface Callback<T> extends Serializable {
	
    void callback(T data);
}