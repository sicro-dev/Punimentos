package br.com.sicro.punimentos.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class mutecommand implements CommandExecutor {
    private Map<UUID, Boolean> mutedPlayers = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mute")) {
            if (!(sender instanceof Player) || sender.hasPermission("punimentos.mute")) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Uso incorreto! Use /mute <Player> <Motivo>");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    sender.sendMessage(ChatColor.RED + "O jogador não está online ou não existe.");
                    return false;
                }

                UUID targetUUID = target.getUniqueId();
                String reason = "";
                for (int i = 1; i < args.length; i++) {
                    reason += args[i] + " ";
                }

                mutedPlayers.put(targetUUID, true);

                target.sendMessage(ChatColor.RED + "Você foi silenciado! Motivo: " + reason);
                sender.sendMessage(ChatColor.GREEN + "O jogador " + target.getName() + " foi silenciado por " + reason);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return false;
            }
        }
        return false;
    }

    public boolean isMuted(UUID playerUUID) {
        return mutedPlayers.containsKey(playerUUID) && mutedPlayers.get(playerUUID);
    }


    public void handleChatEvent(Player sender, String message, boolean cancelled) {
        if (cancelled) return;
        UUID senderUUID = sender.getUniqueId();
        if (isMuted(senderUUID)) {
            sender.sendMessage(ChatColor.RED + "Você está silenciado e não pode enviar mensagens no chat.");
            cancelled = true;
        }
    }
}
