package com.astaroth.SimpleLocationTracker.data;

import com.astaroth.SimpleLocationTracker.SimpleTracker;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DataMySQL implements IData {

    private HikariDataSource dataSource;

    @Override
    public void initialize(SimpleTracker simpleTracker) {
        var config = simpleTracker.config.getMysql();
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(config.getJdbcURL());
        dataSource.setUsername(config.getUsername());
        if (config.getPassword() != null) dataSource.setPassword(config.getPassword());
        dataSource.setMaxLifetime(config.getMaxLifetime());
        dataSource.setMaximumPoolSize(config.getMaximumPoolSize());
        dataSource.setPoolName("SLTPool");
        dataSource.addDataSourceProperty("cachePrepStmts", "true");
        dataSource.addDataSourceProperty("prepStmtCacheSize", "250");
        dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        CompletableFuture.runAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement prepareStatement = c.prepareStatement("CREATE TABLE IF NOT EXISTS simple_tracker (hash VARCHAR(16) NOT NULL, epoch BIGINT, uuid VARCHAR(36), x INT, z INT, world VARCHAR(32), PRIMARY KEY (hash))")){
                prepareStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Void> insert(List<String> logs) {
       return CompletableFuture.runAsync(() -> {
            try (Connection c = dataSource.getConnection();
                 PreparedStatement prepareStatement = c.prepareStatement(
                         "INSERT IGNORE INTO simple_tracker (hash, epoch ,uuid, x, z, world) VALUES " + String.join(", ", logs) + " ON DUPLICATE KEY UPDATE hash=hash"
                 )){
                if (!logs.isEmpty()) prepareStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void shutdown() {
        dataSource.close();
        dataSource = null;
    }

}
