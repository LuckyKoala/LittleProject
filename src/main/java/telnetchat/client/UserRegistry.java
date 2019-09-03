package telnetchat.client;

import java.util.HashMap;
import java.util.Map;

public class UserRegistry {
    private static Map<String, User> userMap = new HashMap<>(16);
    public static final User GUEST = new User("Guest", "Guest", false);

    private static volatile UserRegistry instance;

    public static UserRegistry getInstance() {
        if(instance == null) {
            //Double check singleton pattern with volatile variable 'instance'
            //  this will work after JDK 1.4
            //  according to link here:
            //    http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
            synchronized (UserRegistry.class) {
                if(instance == null) {
                    instance = new UserRegistry();
                }
            }
        }

        return instance;
    }

    public boolean register(String userName, String passwordHash) {
        if(userMap.containsKey(userName)) {
            return false;
        }

        userMap.put(userName, new User(userName, passwordHash, false));
        return true;
    }

    public User login(String username, String passwordHash) {
        User user = userMap.getOrDefault(username, GUEST);
        if(user.getPasswordHash().equals(passwordHash)) {
            return user;
        } else {
            return GUEST;
        }
    }
}
