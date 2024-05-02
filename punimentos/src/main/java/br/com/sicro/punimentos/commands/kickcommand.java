package br.com.sicro.punimentos.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class kickcommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ckick")) {
            if (!sender.hasPermission("seuplugin.kick")) {
                sender.sendMessage(ChatColor.RED + "Você não tem permissão para usar este comando.");
                return false;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Uso incorreto! Use /ckick <Player> [Motivo]");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                sender.sendMessage(ChatColor.RED + "O jogador não está online ou não existe.");
                return false;
            }

            String reason = "Sem motivo especificado.";
            if (args.length > 1) {
                StringBuilder reasonBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reasonBuilder.append(args[i]).append(" ");
                }
                reason = reasonBuilder.toString().trim();
            }

            target.kickPlayer("Você foi kickado do servidor. Motivo: " + reason);
            sender.sendMessage(ChatColor.GREEN + "O jogador " + target.getName() + " foi kickado do servidor.");
            return true;
        }
        return false;
    }
}
