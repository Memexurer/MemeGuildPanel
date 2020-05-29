package pl.memexurer.guildpanel;

import net.dzikoysk.funnyguilds.basic.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.memexurer.guildpanel.config.impl.GuildPanelConfiguration;
import pl.memexurer.guildpanel.data.DatabaseConnection;
import pl.memexurer.guildpanel.data.PanelUserData;
import pl.memexurer.guildpanel.gui.impl.PanelGui;
import pl.memexurer.guildpanel.listener.InventoryActionListener;
import pl.memexurer.guildpanel.listener.PanelActionListener;
import pl.memexurer.guildpanel.util.chat.ChatUtil;

public final class GuildPanelPlugin extends JavaPlugin {
    private static GuildPanelPlugin PLUGIN_INSTANCE;

    private DatabaseConnection databaseConnection;
    private PanelUserData panelUserData;
    private GuildPanelConfiguration pluginConfiguration;

    public static GuildPanelPlugin getPluginInstance() {
        return PLUGIN_INSTANCE;
    }

    @Override
    public void onEnable() {
        GuildPanelPlugin.PLUGIN_INSTANCE = this;

        this.pluginConfiguration = new GuildPanelConfiguration(this);
        this.pluginConfiguration.loadConfiguration();

        this.databaseConnection = new DatabaseConnection(pluginConfiguration.databaseAddress, pluginConfiguration.databaseName, pluginConfiguration.databaseUser, pluginConfiguration.databasePassword, pluginConfiguration.databasePort);

        this.panelUserData = new PanelUserData(databaseConnection);
        try {
            this.panelUserData.load(); //no bo async nie mozna XD?
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getServer().getPluginManager().registerEvents(new PanelActionListener(panelUserData), this);
        this.getServer().getPluginManager().registerEvents(new InventoryActionListener(), this);

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            try {
                panelUserData.save();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0L, 20L * 60L * 5L);

        //XD
    }

    @Override
    public void onDisable() {
        try {
            this.panelUserData.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.databaseConnection.shutdown();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Ta komenda jest dostepna tylko dla graczy.");
            return true;
        }

        User user = User.get((Player) sender);
        if (user.getGuild() == null) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Nie posiadasz gildii!"));
            return true;
        }

        if (!user.isDeputy() && !user.isOwner()) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Ta komenda jest dostepna tylko dla zalozyciela i zastepcy gildii."));
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Poprawne uzycie: /" + label + " (nick gracza)"));
            return true;
        }

        User argUser = User.get(args[0]);
        if (argUser == null) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Nie znaleziono gracza."));
            return true;
        }

        if (argUser.getGuild() == null) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Gracz nie posiada gildii."));
            return true;
        }

        if (!argUser.getGuild().equals(user.getGuild())) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Gracz nie znajduje sie w twojej gildii."));
            return true;
        }

        if(argUser.isDeputy() || argUser.isOwner()) {
            sender.sendMessage(ChatUtil.fixColor("&4Blad: &7Nie mozesz zarzadzac permisjami tego gracza."));
            return true;
        }

        PanelGui.openInventory((Player) sender, argUser.getName(), panelUserData.getOrCreate(argUser));
        return true;
    }

    public GuildPanelConfiguration getPluginConfiguration() {
        return pluginConfiguration;
    }
}
