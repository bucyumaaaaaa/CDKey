package org.poem.cdkey;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.poem.cdkey.CDKey.key;

public class cdk implements CommandExecutor, TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cdk")) {
            switch (args[0]) {
                case "additem":
                    if (args.length > 1) {
                        sender.sendMessage("参数错误");
                        return false;
                    }
                    Player player = (Player) sender;
                    ItemStack item = player.getInventory().getItemInMainHand();
                    sender.sendMessage("手中物品为" + item + "，将被作为兑换码奖励");
                    key.Items.add(item);
                    return true;
                case "settime":
                    int days = Integer.parseInt(args[1]);
                    if (args.length != 2 || days > 365 || days < 0) {
                        sender.sendMessage("参数错误");
                        return false;
                    }
                    key.EndTime = System.currentTimeMillis() + days * 86400000L;
                    sender.sendMessage("设置成功！兑换码时间为"+days+"天（共"+ key.EndTime + "毫秒）");
                    return true;
                case "setname":
                    if (args.length != 2) {
                        sender.sendMessage("参数错误");
                        return false;
                    }
                    key.Name = args[1];
                    sender.sendMessage("设置成功！兑换码名字为：" + key.Name);
                    return true;
                case "setdisposable":
                    if (args.length == 2 || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("true")) {
                        key.Disposable = Boolean.parseBoolean(args[1]);
                        sender.sendMessage("设置成功！兑换码一次性属性为" + key.Disposable);
                        return true;
                    }
                    sender.sendMessage("参数错误");
                    return false;
                case "finish":
                    try {
                        File file = new File(key.Key + ".cdk");
                        if (!file.exists()) {
                            file.createNewFile();// 创建目标文件
                        }
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(Files.newOutputStream(Paths.get(file.toURI())));
                        SKey sKey = new SKey(key);
                        objectOutputStream.writeObject(sKey);
                        objectOutputStream.flush();
                        objectOutputStream.close();
                        sender.sendMessage("兑换码创建完成！已经存入插件文件夹作为.cdk文件");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return true;
                case "cancel":
                    if (args.length > 1) {
                        sender.sendMessage("参数错误");
                        return false;
                    }
                    key = null;
                    break;
                case "use":
                    try {
                        if (args.length != 2) {
                            sender.sendMessage("参数错误");
                            return false;
                        }

                        File[] files = new File("plugins\\CDKey\\").listFiles();
                        Key k = null;
                        SKey sk = null;
                        for (File m : files) {
                            if (m.isFile())                                //m为文件时
                            {
                                String name = m.getName();
                                String extension = name.lastIndexOf(".") == -1 ? "" : name.substring(name.lastIndexOf(".") + 1);
                                if (extension.equals("cdk"))//判断后缀是不是cdk;
                                {
                                    ObjectInputStream in = new ObjectInputStream(Files.newInputStream(m.toPath()));
                                    sk = (SKey) in.readObject();
                                    k = new Key(sk.Key);

                                    if (!Objects.equals(k.Key, args[1])) {
                                        sender.sendMessage("找不到该兑换码！");
                                        in.close();
                                        return true;
                                    }
                                }
                            }
                        }

                        k.Disposable = sk.Disposable;
                        k.EndTime = sk.EndTime;
                        k.Name = sk.Name;
                        for (String s :
                                sk.Items) {
                            k.Items.add(SKey.itemStackDeserialize(s));
                        }

                        Player p = (Player) sender;
                        for (ItemStack i :
                                k.Items) {
                            Item gitem = p.getWorld().spawn(p.getLocation(), Item.class);
                            gitem.setItemStack(i);
                        }
                        if (k.Disposable) {
                            Files.deleteIfExists(Paths.get(k.Key + ".cdk"));
                        }
                        sender.sendMessage("兑换成功！奖励已以掉落物形式出现在你脚下，请注意背包空闲！");
                        return true;
                    } catch (Exception e) {
                        sender.sendMessage("找不到该兑换码！");
                        return true;
                    }
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
