package telnetchat.client;

import lombok.Getter;
import lombok.Setter;
import telnetchat.command.CommandRegistry;
import telnetchat.room.Room;
import telnetchat.room.RoomRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Client implements Runnable {
    private static final String SPACE = " ";

    @Getter
    private boolean valid;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    @Setter
    @Getter
    private User user;

    @Getter
    @Setter
    private Room room;

    public Client(Socket socket) {
        this.socket = socket;
        this.valid = true;
        this.user = UserRegistry.GUEST;
        this.room = RoomRegistry.LOBBY;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            RoomRegistry.LOBBY.addClient(this);

            systemMessage("请使用'/register username password'命令进行注册");
            systemMessage("请使用'/login username password'命令进行登录");

            String message;
            while(valid && (message = readLine()) != null) {
                if(message.length() > 1 && message.charAt(0) == '/') {
                    //Command case:
                    //  "/cmd param1 param2 ..."

                    //Split command label and params
                    String[] rawStrings = message.split(SPACE);
                    String label = rawStrings[0].substring(1); //remove preceding "/"
                    String[] params = new String[rawStrings.length-1];
                    if(rawStrings.length > 1) {
                        System.arraycopy(rawStrings, 1, params, 0, rawStrings.length-1);
                    }

                    //Pass label and params to command handler
                    boolean handled = CommandRegistry.getInstance().processInput(this, label, params);
                    if(!handled) {
                        systemMessage("不存在对应的命令");
                    }
                } else {
                    //Message case:
                    //  "message1 message2 ..."
                    if (getUser() != UserRegistry.GUEST) {
                        getRoom().broadcast(this, message);
                    } else {
                        systemMessage("请使用'/login username password'命令进行登录");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void systemMessage(String message) {
        println("{System} " + message);
    }

    public void println(String message) {
        out.println(message);
    }

    public String readLine() throws IOException {
        out.print("--> ");
        out.flush();
        return in.readLine();
    }

    public void close() {
        this.valid = false;

        if(out != null) {
            out.close();
        }

        if(in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "用户名： " + user.getName() +
                "\n所处聊天室： " + room.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return socket.equals(client.socket) &&
                user.equals(client.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, user);
    }
}
