package telnetchat.command;

import telnetchat.client.Client;

public class InfoCommand implements Command {
    public static final String[] INFO_LABELS = new String[]{
            "i",
            "info"
    };

    @Override
    public boolean canHandle(String label) {
        for (String infoLabel : INFO_LABELS) {
            if(infoLabel.equalsIgnoreCase(label)) return true;
        }

        return false;
    }

    @Override
    public boolean processInput(Client client, String label, String[] args) {
        if(canHandle(label)) {
            String[] infos = new String[]{
                    "=============================",
                    "用户名: " + client.getUser().getName(),
                    "房间名: " + client.getRoom().getName(),
                    "============================="
            };
            for (String info : infos) {
                client.println(info);
            }

            return true;
        }

        return false;
    }
}
