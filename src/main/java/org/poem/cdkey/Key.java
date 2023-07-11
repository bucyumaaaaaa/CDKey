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
    public long EndTime = -1;
    public boolean Disposable = false;

    public Key(String key) {
        Key = key;
    }

    public String isOK(){
        if (Key == null || Key.isEmpty()) return "字符串未设置，也许是你搞了什么奇葩，请cancel后重试";
        if (Name == null || Name.isEmpty()) return "名字未设置，请使用/setname 设置名称";
        if (Items == null) return "奖品未设置，请使用/additem 添加奖品";
        if (EndTime < 0) return "未设置时间，请使用/settime 设置兑换码限时";
        return null;
    }
}

