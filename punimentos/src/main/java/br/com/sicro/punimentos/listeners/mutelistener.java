package br.com.sicro.punimentos.listeners;

import br.com.sicro.punimentos.commands.mutecommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class mutelistener implements Listener {
    private final mutecommand muteCommand;

    public mutelistener(mutecommand muteCommand) {
        this.muteCommand = muteCommand;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (muteCommand.isMuted(playerUUID)) {
            player.sendMessage(ChatColor.RED + "Você está silenciado e não pode enviar mensagens no chat.");
            event.setCancelled(true);
        }
    }
}
