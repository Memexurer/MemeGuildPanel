package pl.memexurer.guildpanel.listener;

import net.dzikoysk.funnyguilds.FunnyGuilds;
import net.dzikoysk.funnyguilds.basic.guild.Region;
import net.dzikoysk.funnyguilds.basic.guild.RegionUtils;
import net.dzikoysk.funnyguilds.basic.user.User;
import net.dzikoysk.funnyguilds.data.configs.PluginConfiguration;
import net.dzikoysk.funnyguilds.event.guild.member.GuildMemberJoinEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.memexurer.guildpanel.data.PanelPermission;
import pl.memexurer.guildpanel.data.PanelUserData;

public class PanelActionListener implements Listener {
    private final PanelUserData panelUserData;
    private final PluginConfiguration.Commands commands;

    public PanelActionListener(PanelUserData panelUserData) {
        this.panelUserData = panelUserData;
        this.commands = FunnyGuilds.getInstance().getPluginConfiguration().commands;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material eventMaterial = event.getBlock().getType();
        if (eventMaterial == Material.BEACON && isNotAllowed(event.getPlayer(), event.getBlock().getLocation(), PanelPermission.BEACON))
            event.setCancelled(true);
        else if (isNotAllowed(event.getPlayer(), event.getBlock().getLocation(), PanelPermission.BLOCK_BREAK))
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Material eventMaterial = event.getBlockPlaced().getType();
        if (eventMaterial == Material.TNT && isNotAllowed(event.getPlayer(), event.getBlockPlaced().getLocation(), PanelPermission.TNT_PLACEMENT))
            event.setCancelled(true);
        else if (eventMaterial == Material.BEACON && isNotAllowed(event.getPlayer(), event.getBlock().getLocation(), PanelPermission.BEACON))
            event.setCancelled(true);
        else if (isNotAllowed(event.getPlayer(), event.getBlockPlaced().getLocation(), PanelPermission.BLOCK_PLACE))
            event.setCancelled(true);
    }

    @EventHandler
    public void onFluidPlace(PlayerBucketEmptyEvent event) {
        if (isNotAllowed(event.getPlayer(), event.getBlockClicked().getLocation(), PanelPermission.FLUID_PLACEMENT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onFluidBreak(PlayerBucketFillEvent event) {
        if (isNotAllowed(event.getPlayer(), event.getBlockClicked().getLocation(), PanelPermission.FLUID_PLACEMENT))
            event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String commandName = e.getMessage().split(" ")[0].substring(1);

        User sender = User.get(e.getPlayer());
        if (isCommand(commandName, commands.kick) && isAllowed(e.getPlayer(), PanelPermission.MEMBER_KICK) ||
                isCommand(commandName, commands.invite) && isAllowed(e.getPlayer(), PanelPermission.MEMBER_INVITE) ||
                isCommand(commandName, commands.validity) && isAllowed(e.getPlayer(), PanelPermission.GUILD_PROLONG)) {
            e.setCancelled(true);

            User previousOwner = sender.getGuild().getOwner();
            try {
                sender.getGuild().setOwner(sender);
                Bukkit.dispatchCommand(e.getPlayer(), e.getMessage().substring(1));
            } finally {
                sender.getGuild().setOwner(previousOwner);
            }
            return;
        }

        if (commandName.equalsIgnoreCase("tpaccept") && isNotAllowed(e.getPlayer(), e.getPlayer().getLocation(), PanelPermission.TELEPORTATION_USE))
            e.setCancelled(true);
    }

    private boolean isCommand(String commandName, PluginConfiguration.Commands.FunnyCommand command) {
        return commandName.equalsIgnoreCase(command.name) || command.aliases.stream().anyMatch(str -> str.equalsIgnoreCase(commandName));
    }

    @EventHandler
    public void onGuildJoin(GuildMemberJoinEvent e) {
        panelUserData.reset(e.getMember().getUUID());
    }

    private boolean isAllowed(Player player, PanelPermission permission) {
        User user = User.get(player);

        if (user.getGuild() == null || user.isDeputy() || user.isOwner()) return false;
        return !panelUserData.isAllowed(player, permission);
    }

    private boolean isNotAllowed(Player player, Location location, PanelPermission permission) {
        User user = User.get(player);
        Region region = RegionUtils.getAt(location);

        if (user.getGuild() == null || region == null || !user.getGuild().equals(region.getGuild())) return false;
        if (user.isDeputy() || user.isOwner()) return false;

        if (panelUserData.isAllowed(player, permission)) {
            player.sendMessage(permission.getMessage());
            return true;
        }

        return false;
    }

}
