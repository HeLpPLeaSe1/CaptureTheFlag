package com.random.captureTheFlag.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class Sql {

    private final SqlSetting setting;
    private Connection con;

    public Sql(SqlSetting setting) {
        this.setting = setting;
    }

    public void connect() throws SQLException {
        rawConnect();
        write("CREATE TABLE IF NOT EXISTS ctfstats(uuid VARCHAR(64), name VARCHAR(32), kills INT(64), wins INT(64), captured INT(64))");
        write("CREATE TABLE IF NOT EXISTS levels(uuid VARCHAR(64), name VARCHAR(32), kills INT(64), wins INT(64), captured INT(64))");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(!con.isValid(1)) {
                        rawConnect();
                    }
                } catch (Exception ex) {
                    try {
                        rawConnect();
                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        }, 30000, 30000);
    }

    public void rawConnect() throws SQLException {
        rawConnect(0);
    }

    public void rawConnect(int fails) throws SQLException {
        if (!isConnected()) {
            try {
                Properties connectionProps = new Properties();
                connectionProps.put("user", this.setting.getUser());
                if(!this.setting.getPassword().equalsIgnoreCase("/")) {
                    connectionProps.put("password", this.setting.getPassword());
                }
                this.con = DriverManager.getConnection(
                        "jdbc:mysql://" + this.setting.getHost() + ":" + this.setting.getPort() + "/" + this.setting.getDatabase() + "?autoReconnect=true", connectionProps);
            } catch (SQLException ex) {
                ex.printStackTrace();
                if(++fails == 5) {
                    throw ex;
                }
                rawConnect(fails);
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                this.con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        try {
            return this.con != null && this.con.isValid(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Connection getConnection() {
        if(this.con == null) {
            return null;
        }
        try {
            if(this.con.isValid(1)) {
                return this.con;
            }
            rawConnect();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return this.con;
    }

    public void write(String query) {
        PreparedStatement ps = null;
        try {
            ps = getConnection().prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }

    public PreparedStatement read(String query) {
        Connection connection = getConnection();
        if(connection == null) {
            return null;
        }
        try {
            return connection.prepareStatement(query);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

}
