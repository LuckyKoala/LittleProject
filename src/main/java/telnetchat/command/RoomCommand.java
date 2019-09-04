package telnetchat.command;

import telnetchat.client.Client;
import telnetchat.room.RoomRegistry;

public class RoomCommand implements Command {
    public static final String[] ROOM_LABELS = new String[]{
            "create",
            "remove",
            "join",
            "list",
            "leave"
    };


    @Override
    public boolean canHandle(String label) {
        for (String roomLabel : ROOM_LABELS) {
            if(roomLabel.equalsIgnoreCase(label)) return true;
        }

        return false;
    }

    @Override
    public boolean processInput(Client client, String label, String[] args) {
        switch (label) {
            case "create":
                if (client.getUser().isAdmin()) {
                    if(args.length < 2) {
                        client.println("参数不足，/create roomname limit");
                    } else {
                        String roomName = args[0];
                        int limit = Integer.parseInt(args[1]);
                        boolean success = RoomRegistry.getInstance()
                                .create(roomName, limit);

                        if(success) {
                            client.println("成功创建聊天室 " + roomName);
                        } else {
                            client.println("房间名已存在");
                        }
                    }
                } else {
                    client.systemMessage("只有管理员才能执行本操作");
                }

                break;

            case "remove":
                if(client.getUser().isAdmin()) {
                    if(args.length < 1) {
                        client.println("参数不足，/remove roomname");
                    } else {
                        String roomName = args[0];
                        boolean success = RoomRegistry.getInstance()
                                .remove(roomName);

                        if(success) {
                            client.println("成功移除聊天室 " + roomName);
                        } else {
                            client.println("房间名不存在");
                        }
                    }
                } else {
                    client.systemMessage("只有管理员才能执行本操作");
                }

                break;

            case "join":
                if(args.length < 1) {
                    client.println("参数不足，/join roomname");
                } else {
                    String roomName = args[0];
                    boolean success = RoomRegistry.getInstance().join(roomName, client);

                    if(success) {
                        client.println("成功加入聊天室 " + roomName);
                    } else {
                        client.println("指定聊天室不存在或是人数已满");
                    }
                }
                break;

            case "list":
                client.getRoom().listOnlineClients(client);
                break;

            case "leave":
                boolean success = RoomRegistry.getInstance().leave(client);
                if(success) {
                    client.println("已离开聊天室, 返回大厅");
                } else {
                    client.println("聊天室已关闭");
                }
                break;

            default:
                return false;
        }

        return true;
    }
}
