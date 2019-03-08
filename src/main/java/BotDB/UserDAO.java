package BotDB;


import java.util.Set;

public interface UserDAO {
    User getUserById(String uid);
    Set<User> getAllUsers();

    boolean insertUser(User user);

    boolean updateUser(User user);

    boolean deleteUser(String uid);
}
