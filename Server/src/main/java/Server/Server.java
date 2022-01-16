package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private static ServerSocket server;
    private static Socket socket;
    private static final int PORT = 8189;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner sc;


    public static void main(String[] args) {

        try {
            server = new ServerSocket(PORT);
            System.out.println("Сервер подключен!");
            socket = server.accept();
            System.out.println("Клиент подключен!");
            System.out.println("Введите текст сообщения:");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            sc = new Scanner(System.in);

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
//                        System.out.println("Введите текст сообщения:");
                        String str = sc.nextLine();
                        out.writeUTF(str);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
            thread.setDaemon(true);
            thread.start();

            while (true) {
                String msgServer = in.readUTF();

                if (msgServer.equals("/end")) {
                    out.writeUTF("/end");
                    break;
                }

                System.out.println("Клиент пишет: " + msgServer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Клиент отключен!\nСервер отключен!");
            try {
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}