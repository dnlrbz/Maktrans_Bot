package BotDB;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDBManager implements UserDAO {


    private User extractUserFromResultSet(ResultSet rs) throws SQLException {

        String id = rs.getString("user_id");
        String bot_state = rs.getString("bot_state");
        String phone_number = rs.getString("phone_number");
        String city = rs.getString("city");
        User user = new User(id, bot_state, phone_number, city);

        return user;
    }

    @Override
    public User getUserById(String uid) {
        Connection connection = ConnectionBotDB.getConnection();
        User user = null;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE user_id="+uid);

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
            }

            connection.close();
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<User> getAllUsers() {
        Connection connection = ConnectionBotDB.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            Set users = new HashSet();
            while(rs.next())
            {
                User user = extractUserFromResultSet(rs);
                users.add(user);
            }

            connection.close();
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insertUser(User user) {

        Connection connection = ConnectionBotDB.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?)");
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getState());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getCity());
            int i = ps.executeUpdate();
            connection.close();
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUser(User user) {
        Connection connection = ConnectionBotDB.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE users SET bot_state=?, phone_number=?, city=? WHERE user_id=?");
            ps.setString(1, user.getState());
            ps.setString(2, user.getPhoneNumber());
            ps.setString(3, user.getCity());
            ps.setString(4, user.getUserId());


            int i = ps.executeUpdate();
            connection.close();
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUser(String uid) {
        Connection connection = ConnectionBotDB.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM users WHERE user_id=" + uid);
            connection.close();
            if(i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean userExists(String uid) {
        Connection connection = ConnectionBotDB.getConnection();
        boolean exists = false;

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet result = null;
            result = stmt.executeQuery("select * from users where user_id=" + uid);
            exists = result.isBeforeFirst();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
