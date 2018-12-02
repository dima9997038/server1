package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    // ��������� ������ �������
    private Server server;
    // ��������� ���������
    private PrintWriter outMessage;
    // �������� ��������
    private Scanner inMessage;
    private static final String HOST = "localhost";
    private static final int PORT = 3443;
    // ���������� �����
    private Socket clientSocket = null;
    // ���������� ������� � ����, ��������� ����
    private static int clients_count = 0;




    // �����������, ������� ��������� ���������� ����� � ������
    public ClientHandler(Socket socket, Server server) {
        try {
            clients_count++;
            this.server = server;
            this.clientSocket = socket;
            this.outMessage = new PrintWriter(socket.getOutputStream());
            this.inMessage = new Scanner(socket.getInputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    // �������������� ����� run(), ������� ���������� �����
    // �� �������� new Thread(client).start();

    public void run() {
        try {

            while (true) {
                if (inMessage.hasNext()) {
                    String login = inMessage.nextLine();
                    // �������� ������ � ���� ������
                    if (Db.checkLogin(login)) {
                        // ������ ���������� ���������
                        server.sendMessageToAllClients("New client!");
                        server.sendMessageToAllClients("Clients in chat = " + clients_count);
                        break;
                    }
                    else {
                        System.out.println("Login "+login);
                        server.sendMessageToAllClients("Wrong login");
                    }

                }

            }

            while (true) {
                // ���� �� ������� ������ ���������
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    // ���� ������ ���������� ������ ���������, �� ���� ����������� �
                    // ������ ������� �� ����
                    if (clientMessage.equalsIgnoreCase("##session##end##")) {
                        break;
                    }
                    // ������� � ������� ��������� (��� �����)
                    System.out.println(clientMessage);
                    System.out.println(this.toString());
                    // ���������� ������ ��������� ���� ��������
                    server.sendMessageToAllClients(clientMessage);
                }
                // ������������� ���������� ������ �� 100 ��
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }
    // ���������� ���������
    public void sendMsg(String msg) {
        try {
            outMessage.println(msg);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // ������ ������� �� ����
    public void close() {
        // ������� ������� �� ������
        server.removeClient(this);
        clients_count--;
        server.sendMessageToAllClients("Clients in chat = " + clients_count);
    }
}
