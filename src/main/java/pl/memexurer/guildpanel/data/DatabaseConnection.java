package pl.memexurer.guildpanel.data;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private HikariDataSource hikariDataSource;
    private Connection conn;

    public DatabaseConnection(String ip, String databaseName, String user, String password, int port) {
        hikariDataSource = new HikariDataSource();
        hikariDataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        hikariDataSource.addDataSourceProperty("serverName", ip);
        hikariDataSource.addDataSourceProperty("port", port);
        hikariDataSource.addDataSourceProperty("databaseName", databaseName);
        hikariDataSource.addDataSourceProperty("user", user);
        hikariDataSource.addDataSourceProperty("password", password);
        hikariDataSource.addDataSourceProperty("autoReconnect", true);
        try {
            conn = hikariDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void shutdown() {
        if (hikariDataSource != null)
            hikariDataSource.close();
    }
}
