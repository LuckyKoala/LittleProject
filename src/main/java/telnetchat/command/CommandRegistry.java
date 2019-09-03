package telnetchat.command;

import telnetchat.client.Client;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private static Map<String, Command> commandMap = new HashMap<>();

    private static final Command UNHANDLED_COMMAND = new Command() {
        @Override
        public boolean canHandle(String label) {
            return true;
        }

        @Override
        public boolean processInput(Client client, String label, String[] args) {
            client.println("命令不存在");
            return true;
        }
    };

    static {
        HelpCommand helpCommand = new HelpCommand();
        for (String helpLabel : HelpCommand.HELP_LABELS) {
            register(helpLabel, helpCommand);
        }

        UserCommand userCommand = new UserCommand();
        for (String userLabel : UserCommand.USER_LABELS) {
            register(userLabel, userCommand);
        }

        RoomCommand roomCommand = new RoomCommand();
        for (String roomLabel : RoomCommand.ROOM_LABELS) {
            register(roomLabel, roomCommand);
        }

        InfoCommand infoCommand = new InfoCommand();
        for (String infoLabel : InfoCommand.INFO_LABELS) {
            register(infoLabel, infoCommand);
        }

        MsgCommand msgCommand = new MsgCommand();
        for (String msgLabel : MsgCommand.MSG_LABELS) {
            register(msgLabel, msgCommand);
        }
    }

    private static volatile CommandRegistry instance;

    public static CommandRegistry getInstance() {
        if(instance == null) {
            //Double check singleton pattern with volatile variable 'instance'
            //  this will work after JDK 1.4
            //  according to link here:
            //    http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
            synchronized (CommandRegistry.class) {
                if(instance == null) {
                    instance = new CommandRegistry();
                }
            }
        }

        return instance;
    }

    private static boolean register(String cmdLabel, Command command) {
        if(commandMap.containsKey(cmdLabel)) {
            return false;
        }

        commandMap.put(cmdLabel, command);
        return true;
    }

    public boolean processInput(Client client, String label, String[] args) {
        return commandMap.getOrDefault(label, UNHANDLED_COMMAND)
                .processInput(client, label, args);
    }
}
