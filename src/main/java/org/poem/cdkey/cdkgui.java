package org.poem.cdkey;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import org.jetbrains.annotations.NotNull;

public class cdkgui implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("cdkgui")){

            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())){
                    FloodgatePlayer floodgatePlayer = FloodgateApi.getInstance().getPlayer(player.getUniqueId());
                    floodgatePlayer.sendForm(CustomForm.builder()
                            .input("兑换码")
                            .validResultHandler((customForm, customFormResponse) -> {
                                String cdk = customFormResponse.asInput(0);
                                player.chat("/cdk use " + cdk);
                            }));
                }
            } else {
                sender.sendMessage("控制台/java版不可执行！");
                return true;
            }

        }
        return false;
    }
}
