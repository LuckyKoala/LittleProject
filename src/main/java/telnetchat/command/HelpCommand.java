package telnetchat.command;

import telnetchat.client.Client;

public class HelpCommand implements Command {
    public static final String[] HELP_LABELS = new String[]{
            "?",
            "help"
    };

    private static final String[] HELP_MESSAGES = new String[]{
            "[Command help]",
            "/? 查看命令帮助",
            "/i 查看当前信息",
            "/m username message... 私信",
            "/register username password 新用户注册",
            "/login username password 用户登录",
            "/logout 用户注销",
            "/create roomname limit 创建聊天室",
            "/join roomname 加入聊天室",
            "/leave 离开聊天室"
    };

    @Override
    public boolean canHandle(String label) {
        for (String helpLabel : HELP_LABELS) {
            if(helpLabel.equalsIgnoreCase(label)) return true;
        }

        return false;
    }

    @Override
    public boolean processInput(Client client, String label, String[] args) {
        if(canHandle(label)) {
            for (String message : HELP_MESSAGES) {
                client.println(message);
            }

            return true;
        }

        return false;
    }
}
