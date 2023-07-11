package org.poem.cdkey;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Key implements Serializable {
    private static final long serialVersionUID = -6582464251162268760L;
    public final String Key;
    public String Name;
    public List<ItemStack> Items = new ArrayList<>();
    public long EndTime;
    public boolean Disposable = false;

    public Key(String key) {
        Key = key;
    }
}

