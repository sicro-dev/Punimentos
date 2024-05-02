package br.com.sicro.punimentos.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class tempmutecommand implements CommandExecutor {
    private Map<UUID, Long> mutedPlayers = new HashMap<>();
    private Plugin plugin;

    public tempmutecommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tempmute")) {
            if (!sender.hasPermission("punimentos.tempmute")) {
                sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return false;
            }

            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Uso incorreto! Use /tempmute <Player> <Motivo> <Tempo>");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "O jogador não está online ou não existe.");
                return false;
            }

            long duration = parseTime(args[args.length - 1]);
            if (duration == -1) {
                sender.sendMessage(ChatColor.RED + "Tempo inválido! Use um formato válido (Ex: 10m, 2h, 3d).");
                return false;
            }

            String reason = "";
            for (int i = 1; i < args.length - 1; i++) {
                reason += args[i] + " ";
            }

            mutedPlayers.put(target.getUniqueId(), System.currentTimeMillis() + duration);
            target.sendMessage(ChatColor.RED + "Você foi temporariamente silenciado! Motivo: " + reason + " | Expira em: " + formatDuration(duration));
            sender.sendMessage(ChatColor.GREEN + "O jogador " + target.getName() + " foi temporariamente silenciado por " + formatDuration(duration) + ".");

            muteChat(target, duration);

            return true;
        }
        return false;
    }

    private long parseTime(String input) {
        char unit = input.charAt(input.length() - 1);
        if (Character.isLetter(unit)) {
            int time;
            try {
                time = Integer.parseInt(input.substring(0, input.length() - 1));
            } catch (NumberFormatException e) {
                return -1;
            }
            switch (unit) {
                case 'm':
                    return time * 60000;
                case 'h':
                    return time * 3600000;
                case 'd':
                    return time * 86400000;
            }
        }
        return -1;
    }

    private String formatDuration(long milliseconds) {
        long days = milliseconds / 86400000;
        milliseconds %= 86400000;
        long hours = milliseconds / 3600000;
        milliseconds %= 3600000;
        long minutes = milliseconds / 60000;
        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append(" dias ");
        }
        if (hours > 0) {
            builder.append(hours).append(" horas ");
        }
        if (minutes > 0) {
            builder.append(minutes).append(" minutos ");
        }
        return builder.toString().trim();
    }

    private void muteChat(Player player, long duration) {
        mutedPlayers.put(player.getUniqueId(), System.currentTimeMillis() + duration);
        player.sendMessage(ChatColor.RED + "Você foi temporariamente silenciado! Expira em: " + formatDuration(duration));
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerChat(PlayerChatEvent event) {
                if (event.getPlayer().equals(player)) {
                    if (System.currentTimeMillis() < mutedPlayers.getOrDefault(player.getUniqueId(), 0L)) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "Você está temporariamente silenciado e não pode enviar mensagens no chat.");
                    } else {
                        mutedPlayers.remove(player.getUniqueId());
                        HandlerList.unregisterAll(this);
                        player.sendMessage(ChatColor.GREEN + "Seu silenciamento temporário expirou e você pode enviar mensagens novamente.");
                    }
                }
            }
        }, plugin);
    }
}