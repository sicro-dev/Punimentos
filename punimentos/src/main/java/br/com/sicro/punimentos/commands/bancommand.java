package br.com.sicro.punimentos.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class bancommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("cban")) {
            if (args.length < 2) {
                sender.sendMessage("Uso incorreto! Use /cban <Player> <Motivo>");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("O jogador não está online ou não existe.");
                return false;
            }

            String reason = "";
            for (int i = 1; i < args.length; i++) {
                reason += args[i] + " ";
            }

            // Banir o jogador
            target.kickPlayer("Você foi banido permanentemente. Motivo: " + reason);
            Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).addBan(target.getName(), reason, null, sender.getName());



            sender.sendMessage("O jogador " + target.getName() + " foi banido permanentemente por " + reason);
            return true;
        }
        return false;
    }
}
