package br.com.sicro.punimentos;

import br.com.sicro.punimentos.commands.*;
import br.com.sicro.punimentos.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        mutecommand muteCommand = new mutecommand();
        System.out.println("Plugin de Punimento inicializado!");
        this.getCommand("cban").setExecutor(new bancommand());
        this.getCommand("unban").setExecutor(new unbancommand());
        this.getCommand("mute").setExecutor(muteCommand);
        this.getCommand("ckick").setExecutor(new kickcommand());
        this.getCommand("tempban").setExecutor(new tempbancommand());
        this.getCommand("tempmute").setExecutor(new tempmutecommand(this));
        this.getServer().getPluginManager().registerEvents(new banlistener(), this);
        this.getServer().getPluginManager().registerEvents(new mutelistener(muteCommand), this); // Corrigido aqui
    }

    @Override
    public void onDisable() {
    }
}
