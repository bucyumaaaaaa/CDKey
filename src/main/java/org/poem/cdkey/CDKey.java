package org.poem.cdkey;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class CDKey extends JavaPlugin {

    public static Key key;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(Bukkit.getPluginCommand("cdk")).setExecutor(new cdk());
        saveDefaultConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("newcdk")) {
            if (args.length > 0) {
                sender.sendMessage("参数错误");
                return false;
            }
            String keyString = RandomStringUtils.randomAlphanumeric(10);
            sender.sendMessage("§e" + keyString);
            sender.sendMessage("兑换码字符串如上,接下来请设置兑换码属性，除了 一次性 属性默认为否，其它必填");
            key = new Key(keyString);
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
