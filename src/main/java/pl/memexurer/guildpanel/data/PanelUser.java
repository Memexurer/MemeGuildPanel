package pl.memexurer.guildpanel.data;

import pl.memexurer.guildpanel.GuildPanelPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PanelUser {
    private UUID playerUniqueId;
    private Map<PanelPermission, Boolean> permissions = new LinkedHashMap<>();

    private boolean needInsert;
    private boolean needUpdate;

    public PanelUser(UUID uniqueId) {
        this.playerUniqueId = uniqueId;
        this.permissions.putAll(GuildPanelPlugin.getPluginInstance().getPluginConfiguration().defaultPermissions);
        this.needInsert = true;
    }

    public PanelUser(ResultSet set) {
        try {
            this.playerUniqueId = UUID.fromString(set.getString("PlayerUniqueId"));
            String permissionString = set.getString("PlayerPermissions");

            int index = 0;
            for (PanelPermission permission : PanelPermission.values())
                permissions.put(permission, permissionString.charAt(index++) == '1');
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void resetPermissions() {
        this.permissions.clear();
        this.permissions.putAll(GuildPanelPlugin.getPluginInstance().getPluginConfiguration().defaultPermissions);
        this.needUpdate = true;
    }

    public boolean isAllowed(PanelPermission permission) {
        return this.permissions.get(permission);
    }

    public void togglePermission(PanelPermission permission) {
        this.needUpdate = true;
        this.permissions.computeIfPresent(permission, (a, b) -> !b);
    }

    private String getPermissionString() {
        return permissions.values().stream().map(b -> b ? "1" : "0").collect(Collectors.joining());
    }

    public Set<Map.Entry<PanelPermission, Boolean>> getPermissions() {
        return permissions.entrySet();
    }


    public void insert(PreparedStatement statement) throws Exception {
        statement.setString(1, playerUniqueId.toString());
        statement.setString(2, getPermissionString());
        statement.addBatch();
        this.needInsert = false;
        this.needUpdate = false;
    }

    public void update(PreparedStatement statement) throws Exception {
        statement.setString(1, getPermissionString());
        statement.setString(2, playerUniqueId.toString());
        statement.addBatch();
        this.needUpdate = false;
    }

    public UUID getUniqueId() {
        return playerUniqueId;
    }

    public boolean isNeedInsert() {
        return needInsert;
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }
}
