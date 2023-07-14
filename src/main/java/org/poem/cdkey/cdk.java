package org.poem.cdkey;

import me.yic.xconomy.api.XConomyAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.poem.cdkey.CDKey.key;

public class cdk implements CommandExecutor, TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cdk")) {
            try {
                switch (args[0]) {
                    case "additem":
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码再进行设置");
                            return true;
                        }
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
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码再进行设置");
                            return true;
                        }
                        double days = Double.parseDouble(args[1]);
                        if (args.length != 2 || days > 365 || days < 0) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        key.EndTime = (long) (System.currentTimeMillis() + days * 86400000L);
                        sender.sendMessage("设置成功！兑换码时间为" + days + "天");
                        return true;
                    case "setname":
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码再进行设置");
                            return true;
                        }
                        if (args.length != 2) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        key.Name = args[1];
                        sender.sendMessage("设置成功！兑换码名字为：" + key.Name);
                        return true;
                    case "setdisposable":
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码再进行设置");
                            return true;
                        }
                        if (args.length == 2 || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("true")) {
                            key.Disposable = Boolean.parseBoolean(args[1]);
                            sender.sendMessage("设置成功！兑换码一次性属性为" + key.Disposable);
                            return true;
                        }
                        sender.sendMessage("参数错误");
                        return false;
                    case "setmoney":
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码再进行设置");
                            return true;
                        }
                        if (args.length != 2) {
                            sender.sendMessage("参数错误");
                            return false;
                        }
                        key.Money = BigDecimal.valueOf(Long.parseLong(args[1]));
                        return true;
                    case "finish":
                        if (key == null) {
                            sender.sendMessage("未创建兑换码！请使用/newcdk创建兑换码设置后再保存");
                            return true;
                        }
                        String error = key.isOK();
                        if (error != null) {
                            sender.sendMessage(error);
                            return true;
                        }
                        try {
                            File file = new File("plugins\\CDKey\\" + key.Key + ".cdk");
                            if (!file.exists()) {
                                //noinspection ResultOfMethodCallIgnored
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
                            assert files != null;
                            for (File m : files) {

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


                            assert k != null;
                            k.Disposable = sk.Disposable;
                            k.EndTime = sk.EndTime;
                            k.Name = sk.Name;
                            k.Money = sk.Money;
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
                            XConomyAPI xcapi = new XConomyAPI();
                            xcapi.changePlayerBalance(((Player) sender).getUniqueId(), sender.getName(), k.Money, true);
                            if (k.Disposable) {
                                Files.deleteIfExists(Paths.get("plugins\\CDKey\\" + k.Key + ".cdk"));
                            }
                            sender.sendMessage("兑换成功！实物奖励已以掉落物形式出现在你脚下，请注意背包空闲！");
                            return true;
                        } catch (Exception e) {
                            sender.sendMessage("找不到该兑换码！");
                            return true;
                        }
                    case "delete":
                        try {
                            Files.deleteIfExists(Paths.get("plugins\\CDKey\\" + args[1] + ".cdk"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }
            }catch (Exception e){
                sender.sendMessage("发生内部错误，这多半是你参数写错的问题，请检查参数");
                return false;
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            list.add("additem");
            list.add("settime");
            list.add("setname");
            list.add("setdisposable");
            list.add("finish");
            list.add("cancel");
            list.add("use");
            list.add("delete");
            list.add("setmoney");
            return list;
        }
        if (args[0].equals("setdisposable")) {
            List<String> list = new ArrayList<>();
            list.add("true");
            list.add("false");
            return list;
        }
        return null;
    }
}
