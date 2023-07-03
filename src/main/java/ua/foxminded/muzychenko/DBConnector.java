package ua.foxminded.muzychenko;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnector {

    private final HikariDataSource dataSource;

    public DBConnector(String fileConfigName) {
        HikariConfig hikariConfig = new HikariConfig(fileConfigName);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public DBConnector(String jdbcUrl, String userName, String password) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(userName);
        hikariConfig.setPassword(password);
        dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}