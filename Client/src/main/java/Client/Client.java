package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String ADDRESS = "localhost";
    private static final int PORT = 8189;

    private static Socket socket;
    private static DataInputStream in;
    private static DataOutputStream out;
    private static Scanner sc;

    public static void main(String[] args) {

        try {
            socket = new Socket(ADDRESS, PORT);

            System.out.println("Сервер подключен!");
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

                System.out.println("Сервер пишет: " + msgServer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Сервер отключен!\nКлиент отключен!");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
