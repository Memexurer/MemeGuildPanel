package pl.memexurer.guildpanel.data;

import net.dzikoysk.funnyguilds.basic.user.User;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PanelUserData {
    private final Map<UUID, PanelUser> userMap = new HashMap<>();
    private final DatabaseConnection databaseConnection;

    public PanelUserData(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean isAllowed(Player player, PanelPermission permission) {
        PanelUser user = userMap.get(player.getUniqueId());
        if (user == null) return true;

        return user.isAllowed(permission);
    }

    public PanelUser getOrCreate(User user) {
        return userMap.computeIfAbsent(user.getUUID(), PanelUser::new);
    }

    public void load() throws Exception {
        try (Statement statement = databaseConnection.getConnection().createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `panel_permissions` (PlayerUniqueId varchar(36), PlayerPermissions varchar(" + PanelPermission.values().length + 1 + "))");
            ResultSet rs = statement.executeQuery("SELECT * FROM `panel_permissions`");
            while (rs.next()) {
                PanelUser user = new PanelUser(rs);
                userMap.put(user.getUniqueId(), user);
            }
        }
    }

    public void save() throws Exception {
        PreparedStatement insertStatement = databaseConnection.getConnection().prepareStatement("INSERT INTO `panel_permissions` VALUES (?, ?)");
        PreparedStatement updateStatement = databaseConnection.getConnection().prepareStatement("UPDATE `panel_permissions` SET PlayerPermissions=? WHERE PlayerUniqueId=?");
        for (PanelUser user : userMap.values())
            if (user.isNeedInsert())
                user.insert(insertStatement);
            else if (user.isNeedUpdate())
                user.update(updateStatement);

        insertStatement.executeBatch();
        updateStatement.executeBatch();
    }

    public void reset(UUID uuid) {
        PanelUser user = userMap.get(uuid);
        if(user == null) return;

        user.resetPermissions();
    }
}
