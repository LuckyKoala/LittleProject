package telnetchat;

import telnetchat.client.Client;
import telnetchat.client.UserRegistry;
import telnetchat.room.RoomRegistry;
import telnetchat.util.HashUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实现一个 telnet 版本的聊天服务器，主要有以下需求。
 *
 * 每个客户端可以用使用telnet ip:port的方式连接到服务器上。
 * 新连接需要用用户名和密码登录，如果没有，则需要注册一个。
 * 然后可以选择一个聊天室加入聊天。
 * 管理员有权创建或删除聊天室，普通人员只有加入、退出、查询聊天室的权力。
 * 聊天室需要有人数限制，每个人发出来的话，其它所有的人都要能看得到。
 */
public class Launcher {
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    static {
        //内置管理员及用户
        UserRegistry.getInstance().register("admin", HashUtils.md5("adminpass"), true);
        UserRegistry.getInstance().register("john", HashUtils.md5("johnpass"));
        UserRegistry.getInstance().register("kentucky", HashUtils.md5("kentuckypass"));
        //内置房间
        RoomRegistry.getInstance().create("BagEnd", 1);
        RoomRegistry.getInstance().create("Tech", 5);
        RoomRegistry.getInstance().create("Life", 10);
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(23);

        while(true) {
            Socket clientSocket = serverSocket.accept();
            //多余的连接请求会进入队列
            executor.submit(new Client(clientSocket));
        }
    }
}
