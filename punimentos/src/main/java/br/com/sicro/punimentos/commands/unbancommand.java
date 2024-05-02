package br.com.sicro.punimentos.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class unbancommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("unban")) {
            if (args.length != 1) {
                sender.sendMessage("Uso incorreto! Use /unban <Player>");
                return false;
            }

            String playerName = args[0];


            if (Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).isBanned(playerName)) {
                Bukkit.getServer().getBanList(org.bukkit.BanList.Type.NAME).pardon(playerName);
                sender.sendMessage("O jogador " + playerName + " foi desbanido com sucesso.");
                return true;
            }

            sender.sendMessage("O jogador " + playerName + " não está banido.");
            return false;
        }
        return false;
    }
}
