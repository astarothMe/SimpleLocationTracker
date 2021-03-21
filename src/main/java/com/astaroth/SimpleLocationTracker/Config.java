package com.astaroth.SimpleLocationTracker;

public class Config {

    private MySQL mysql;
    private Tracker tracker;

    public Config(MySQL mySQL, Tracker tracker) {
        this.mysql = mySQL;
        this.tracker = tracker;
    }

    public Config(){

    }

    public MySQL getMysql() {
        return mysql;
    }

    public void setMysql(MySQL mysql) {
        this.mysql = mysql;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    @Override
    public String toString() {
        return "Config{" +
                "mysql=" + mysql +
                ", tracker=" + tracker +
                '}';
    }

    public static class MySQL {

        private String jdbcURL;
        private String username;
        private String password;

        private int maxLifetime;
        private int maximumPoolSize;

        public MySQL(String jdbcURL, String username, String password, int maxLifetime, int idleTimeout, int maximumPoolSize) {
            this.jdbcURL = jdbcURL;
            this.username = username;
            this.password = password;
            this.maxLifetime = maxLifetime;
            this.maximumPoolSize = maximumPoolSize;
        }

        public MySQL(){

        }

        public String getJdbcURL() {
            return jdbcURL;
        }

        public void setJdbcURL(String jdbcURL) {
            this.jdbcURL = jdbcURL;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getMaxLifetime() {
            return maxLifetime;
        }

        public void setMaxLifetime(int maxLifetime) {
            this.maxLifetime = maxLifetime;
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public void setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
        }

        @Override
        public String toString() {
            return "MySQL{" +
                    "jdbcURL='" + jdbcURL + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", maxLifetime=" + maxLifetime +
                    ", maximumPoolSize=" + maximumPoolSize +
                    '}';
        }
    }

    public static class Tracker {

        private int updateInterval;
        private int updateCount;
        private boolean enableCommand;

        public Tracker(int updateInterval, int updateCount, boolean enableCommand) {
            this.updateInterval = updateInterval;
            this.updateCount = updateCount;
            this.enableCommand = enableCommand;
        }

        public Tracker(){

        }

        public int getUpdateInterval() {
            return updateInterval;
        }

        public void setUpdateInterval(int updateInterval) {
            this.updateInterval = updateInterval;
        }

        public int getUpdateCount() {
            return updateCount;
        }

        public void setUpdateCount(int updateCount) {
            this.updateCount = updateCount;
        }

        public boolean isEnableCommand() {
            return enableCommand;
        }

        public void setEnableCommand(boolean enableCommand) {
            this.enableCommand = enableCommand;
        }

        @Override
        public String toString() {
            return "Tracker{" +
                    "updateInterval=" + updateInterval +
                    ", updateCount=" + updateCount +
                    ", enableCommand=" + enableCommand +
                    '}';
        }
    }
}
