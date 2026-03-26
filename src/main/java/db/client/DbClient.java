package db.client;

import config.ConfigReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbClient {

    private static ThreadLocal<Connection> connectionTL = new ThreadLocal<>();

    public static Connection getConnection() {
        if (connectionTL.get() == null) {
            try {
                Class.forName(ConfigReader.get("db.driver"));
                Connection connection = DriverManager.getConnection(
                        ConfigReader.get("db.url"),
                        ConfigReader.get("db.username"),
                        ConfigReader.get("db.password")
                );
                connectionTL.set(connection);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create DB connection", e);
            }
        }
        return connectionTL.get();
    }

    public static void close() {
        try {
            if (connectionTL.get() != null) {
                connectionTL.get().close();
                connectionTL.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
