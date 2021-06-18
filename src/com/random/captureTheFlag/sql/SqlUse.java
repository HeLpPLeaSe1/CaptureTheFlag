package com.random.captureTheFlag.sql;

import com.random.captureTheFlag.Capture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlUse {

    public static boolean isUserExists(UUID uuid) {
        final PreparedStatement ps = Capture.getInstance().getSql().read("SELECT uuid FROM ctf_stats WHERE uuid = ?");
        try {
            ps.setString(1, uuid.toString());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static int getStat(UUID uuid, String stat) {
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("SELECT " + stat + " FROM ctf_stats WHERE uuid = ?");
            try {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(stat);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public static void incrStat(UUID uuid, String stat) {
        final int value = getStat(uuid, stat) + 1;
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("UPDATE ctf_stats SET " + stat + " = ? WHERE uuid = ?");
            try {
                ps.setInt(1, value);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void setStat(UUID uuid, String stat, int value) {
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("UPDATE ctf_stats SET " + stat + " = ? WHERE uuid = ?");
            try {
                ps.setInt(1, value);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getPlace(int place, String stat) {
        int id = 0;
        final PreparedStatement ps = Capture.getInstance().getSql().read("SELECT uuid FROM ctf_stats ORDER BY " + stat + " DESC");
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if(++id == place) {
                    return rs.getString("uuid");
                }
            }
            rs.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void updateName(UUID uuid, String name) {
        final String oldName = getName(uuid);
        if(!oldName.equals(name)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("UPDATE ctf_stats SET name = ? WHERE uuid = ?");
            try {
                ps.setString(1, name);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getName(UUID uuid) {
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("SELECT name FROM ctf_stats WHERE uuid = ?");
            try {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getKillMsg(UUID uuid) {
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("SELECT killmsg FROM ctf_stats WHERE uuid = ?");
            try {
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("killmsg");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void setKillMsg(UUID uuid, String message) {
        if (isUserExists(uuid)) {
            final PreparedStatement ps = Capture.getInstance().getSql().read("UPDATE ctf_stats SET killmsg = ? WHERE uuid = ?");
            try {
                ps.setString(1, message);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static void insertUser(UUID uuid, String name) {
        final PreparedStatement ps = Capture.getInstance().getSql().read("INSERT INTO ctf_stats (uuid,name,kills,wins,captured,deaths,killmsg,losses,gamesplayed,winstreak,flagslost) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        try {
            ps.setString(1, uuid.toString());
            ps.setString(2, name);
            ps.setInt(3, 0);
            ps.setInt(4, 0);
            ps.setInt(5, 0);
            ps.setInt(6, 0);
            ps.setString(7, " §7was killed by ");
            ps.setInt(8, 0);
            ps.setInt(9, 0);
            ps.setInt(10, 0);
            ps.setInt(11, 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
