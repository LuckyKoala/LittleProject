package telnetchat.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserRegistry {
    private static Map<String, User> userMap = new HashMap<>(16);
    private static Set<String> userInUseSet = new HashSet<>(16);
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
        return register(userName, passwordHash, false);
    }

    public boolean register(String userName, String passwordHash, boolean isAdmin) {
        if(userMap.containsKey(userName)) {
            return false;
        }

        userMap.put(userName, new User(userName, passwordHash, isAdmin));
        return true;
    }

    public void login(Client client, String username, String passwordHash) {
        User user = userMap.getOrDefault(username, GUEST);
        if(user.getPasswordHash().equals(passwordHash)) {
            if(userInUseSet.contains(username)) {
                client.systemMessage("该用户已登录");
            } else {
                userInUseSet.add(username);
                client.setUser(user);
                client.systemMessage("登录成功");
            }
        } else {
            client.systemMessage("用户名密码错误");
        }
    }

    public void logout(Client client) {
        if(UserRegistry.GUEST != client.getUser()) {
            String userName = client.getUser().getName();
            client.setUser(UserRegistry.GUEST);
            userInUseSet.remove(userName);
            client.systemMessage("已注销");
        } else {
            client.systemMessage("未登录，无需注销");
        }
    }

    public boolean userInUse(String userName) {
        return userInUseSet.contains(userName);
    }
}
