package telnetchat.room;

import telnetchat.client.Client;

import java.util.HashMap;
import java.util.Map;

public class RoomRegistry {
    private Map<String, Room> roomMap = new HashMap<>(16);
    public static final Room LOBBY = new Room("大厅", 64);

    private static volatile RoomRegistry instance;

    public static RoomRegistry getInstance() {
        if(instance == null) {
            //Double check singleton pattern with volatile variable 'instance'
            //  this will work after JDK 1.4
            //  according to link here:
            //    http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
            synchronized (RoomRegistry.class) {
                if(instance == null) {
                    instance = new RoomRegistry();
                }
            }
        }

        return instance;
    }

    public boolean create(String roomName, int limit) {
        if(roomMap.containsKey(roomName)) {
            return false;
        }

        roomMap.put(roomName, new Room(roomName, limit));
        return true;
    }

    public boolean remove(String roomName) {
        Room room = roomMap.remove(roomName);
        if(room != null) {
            room.invalidate();
            return true;
        } else {
            return false;
        }
    }

    public boolean join(String roomName, Client client) {
        Room room = roomMap.get(roomName);
        if(room != null) {
            boolean success = room.addClient(client);
            if(success) {
                client.setRoom(room);
            }

            return success;
        } else {
            return false;
        }
    }

    public boolean leave(Client client) {
        boolean success = client.getRoom().delClient(client);
        if(success) {
            if (client.getRoom() != LOBBY) {
                client.setRoom(LOBBY);
            } else {
                client.close();
            }
        }

        return success;
    }
}
