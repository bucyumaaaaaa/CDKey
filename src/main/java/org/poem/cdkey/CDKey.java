package org.poem.cdkey;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.util.Timer;
import java.util.TimerTask;

public final class CDKey extends JavaPlugin {

    public static Key key;

    public static void checkIsOutdated() {
        File[] files;
        SKey sk;
        files = new File("plugins\\CDKey\\").listFiles();
        assert files != null;
        for (File m : files) {
            String name = m.getName();
            String extension = name.lastIndexOf(".") == -1 ? "" : name.substring(name.lastIndexOf(".") + 1);
            if (extension.equals("cdk"))//判断后缀是不是cdk;
            {
                ObjectInputStream in;
                try {
                    in = new ObjectInputStream(Files.newInputStream(m.toPath()));
                    sk = (SKey) in.readObject();
                    if (sk.EndTime == 0) continue;
                    if (System.currentTimeMillis() >= sk.EndTime) {
                        Files.deleteIfExists(m.toPath());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }


        }
    }

    @Override
    public void onEnable() {
        //noinspection DataFlowIssue
        Bukkit.getPluginCommand("cdk").setExecutor(new cdk());
        //noinspection DataFlowIssue
        Bukkit.getPluginCommand("cdkgui").setExecutor(new cdkgui());
        saveDefaultConfig();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                checkIsOutdated();
            }
        }, getConfig().getInt("checkIsOutdatedDelay"));

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("newcdk")) {
            if (args.length == 0) {
                String keyString = RandomStringUtils.randomAlphanumeric(10);
                sender.sendMessage("§e" + keyString);
                sender.sendMessage("兑换码字符串如上,接下来请设置兑换码属性，除了 一次性 属性默认为否，其它必填，若时间为0，则视为永久保存");
                key = new Key(keyString);
                return true;
            } else sender.sendMessage("参数错误");
        }
        return false;
    }

    @Override
    public void onDisable() {

    }
}
