package uriExamples.caracter.or.object.socket.practice.Reminder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMultiThread extends Thread {

    private boolean isActive = true;
    private static int numberClientConnected = 0;

    public static void main(String[] args) {
        new ServerMultiThread().start();
    }


    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(1478);
            while (isActive) {
                System.out.println("Waiting Client for connection...");
                Socket socket = server.accept();
                numberClientConnected ++;
                new Conversation(socket, numberClientConnected).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class Conversation extends Thread {
        private Socket socket;
        private int number;

        public Conversation(Socket socket, int number) {
            this.socket = socket;
            this.number = number;
        }

        public void run() {
            try {
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                String ipAddress = socket.getRemoteSocketAddress().toString();
                pw.println("Welcome Your a the " + number + " client.");
                System.out.println("Connection of  the "+number+" client. with IP Address : " + ipAddress);
                while (isActive) {
                    String request = br.readLine();
                    String response = "Length = " + request.length();
                    pw.println(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
