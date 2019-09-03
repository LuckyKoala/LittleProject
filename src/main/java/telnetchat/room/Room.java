package telnetchat.room;

import lombok.Getter;
import telnetchat.client.Client;
import telnetchat.client.UserRegistry;

import java.util.HashSet;
import java.util.Set;

public class Room {
    @Getter
    private boolean valid;
    @Getter
    private final String name;
    private int limit;
    private Set<Client> clientSet;

    public Room(String name, int limit) {
        this.valid = true;
        this.name = name;
        this.limit = limit;
        this.clientSet = new HashSet<>(limit);
    }

    public boolean addClient(Client client) {
        if(clientSet.contains(client) || clientSet.size() > limit) {
            return false;
        }

        clientSet.add(client);
        return true;
    }

    public boolean delClient(Client client) {
        return clientSet.remove(client);
    }

    public void broadcast(Client sender, String message) {
        String formattedMessage = "[" + sender.getUser().getName() + "] " + message;
        sender.println("正在发送广播# " + formattedMessage);
        clientSet.stream()
                .filter(Client::isValid)
                .filter(client -> client.getUser() != UserRegistry.GUEST)
                .filter(client -> !(client.equals(sender)))
                .forEach(client -> client.println(formattedMessage));
    }

    public void msg(Client sender, String userName, String message) {
        //FIXME 应检查私信对象是否在线 是否存在
        String formattedMessage = String.format("[%s -> %s]", sender.getUser().getName(), userName) + message;
        sender.println("正在发送私信# " + formattedMessage);
        clientSet.stream()
                .filter(client -> client.getUser().getName().equals(userName))
                .findFirst()
                .ifPresent(client -> client.println(formattedMessage));
    }

    public void invalidate() {
        clientSet.forEach(this::delClient);
        this.valid = false;
    }

    public String intel() {
        return "=================" +
                "房间信息" +
                "名称: " + name +
                String.format("人数: %d/%d", clientSet.size(), limit) +
                "=================";
    }
}
