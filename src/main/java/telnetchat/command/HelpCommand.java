package telnetchat.command;

import telnetchat.client.Client;

public class HelpCommand implements Command {
    public static final String[] HELP_LABELS = new String[]{
            "?",
            "help"
    };

    private static final String[] HELP_MESSAGES = new String[]{
            "[帮助信息]",
            "\t/? 查看命令帮助",
            "\t/i 查看当前信息",
            "\t/m username message... 私信",
            "\t/register username password 新用户注册",
            "\t/login username password 用户登录",
            "\t/logout 用户注销",
            "\t/create roomname limit 创建聊天室",
            "\t/remove roomname 移除聊天室",
            "\t/join roomname 加入聊天室",
            "\t/list 列出当前聊天室的在线人员",
            "\t/leave 离开聊天室"
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
                client.systemMessage(message);
            }

            return true;
        }

        return false;
    }
}
