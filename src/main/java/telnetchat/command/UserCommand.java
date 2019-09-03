package telnetchat.command;

import telnetchat.client.Client;
import telnetchat.client.User;
import telnetchat.client.UserRegistry;
import telnetchat.util.HashUtils;

public class UserCommand implements Command {
    public static final String[] USER_LABELS = new String[]{
            "register",
            "login",
            "logout"
    };

    @Override
    public boolean canHandle(String label) {
        for (String userLabel : USER_LABELS) {
            if(userLabel.equalsIgnoreCase(label)) return true;
        }

        return false;
    }

    @Override
    public boolean processInput(Client client, String label, String[] args) {
        switch (label) {
            case "register":
                if(args.length < 2) {
                    client.println("参数不足，/register username password");
                } else {
                    String username = args[0];
                    String passwordHash = HashUtils.md5(args[1]);
                    boolean success = UserRegistry.getInstance().register(username, passwordHash);

                    if(success) {
                        client.println("成功注册用户 " + username);
                    } else {
                        client.println("用户名已存在");
                    }
                }
                break;

            case "login":
                if(args.length < 2) {
                    client.println("参数不足，/login username password");
                } else {
                    String username = args[0];
                    String passwordHash = HashUtils.md5(args[1]);
                    User user = UserRegistry.getInstance().login(username, passwordHash);

                    if(UserRegistry.GUEST != user) {
                        client.setUser(user);
                        client.println("成功登入用户 " + username);
                        return true;
                    } else {
                        client.println("用户名密码不对应，请检查后重试");
                    }
                }
                break;

            case "logout":
                if(UserRegistry.GUEST != client.getUser()) {
                    client.setUser(UserRegistry.GUEST);
                    client.println("已注销");
                } else {
                    client.println("当前已处于注销状态");
                }
                break;

            default:
                return false;
        }

        return true;
    }
}
