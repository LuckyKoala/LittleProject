package telnetchat.command;

import telnetchat.client.Client;
import telnetchat.client.UserRegistry;

public class MsgCommand implements Command {
    public static final String[] MSG_LABELS = new String[]{
            "m"
    };

    @Override
    public boolean canHandle(String label) {
        for (String msgLabel : MSG_LABELS) {
            if(msgLabel.equalsIgnoreCase(label)) return true;
        }

        return false;
    }

    @Override
    public boolean processInput(Client client, String label, String[] args) {
        if(canHandle(label)) {
            if (args.length >= 2) {
                String userName = args[0];
                if (userName.equals(client.getUser().getName())
                        || userName.equals(UserRegistry.GUEST.getName())) {
                    client.systemMessage("不能给自己或游客发私信");
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        stringBuilder.append(args[i])
                                .append(" ");
                    }
                    client.getRoom().msg(client, userName, stringBuilder.toString());
                }
            } else {
                client.println("命令参数不足，/m username message...");
            }

            return true;
        }

        return false;
    }
}
