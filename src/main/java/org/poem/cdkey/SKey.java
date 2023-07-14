package org.poem.cdkey;

import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.math.BigDecimal;

public class SKey implements Serializable {
    private static final long serialVersionUID = 1110583069336424217L;
    public final String Key;
    public String Name;
    public String[] Items;
    public long EndTime;
    public boolean Disposable;
    public BigDecimal Money;

    public SKey(Key key) {
        Key = key.Key;

        Name = key.Name;

        Items = new String[key.Items.size()];
        for (int i = 0; i < key.Items.size(); i++) {
            Items[i] = itemStackSerialize(key.Items.get(i));
        }

        EndTime = key.EndTime;

        Disposable = key.Disposable;

        Money = key.Money;
    }

    private static String itemStackSerialize(ItemStack itemStack) {
        YamlConfiguration yml = new YamlConfiguration();
        yml.set("item", itemStack);
        return yml.saveToString();
    }

    public static ItemStack itemStackDeserialize(String str) {
        YamlConfiguration yml = new YamlConfiguration();
        ItemStack item;
        try {
            yml.loadFromString(str);
            item = yml.getItemStack("item");
        } catch (InvalidConfigurationException ex) {
            item = new ItemStack(Material.AIR, 1);
        }
        return item;
    }
}
