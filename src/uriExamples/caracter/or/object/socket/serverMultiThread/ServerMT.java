package uriExamples.caracter.or.object.socket.serverMultiThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT extends Thread {

    private static int countClients = 0;
    public static void main(String[] args) {
        new ServerMT().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Server Started ... !!!!");
            while (true) {
                Socket socket = serverSocket.accept();
                ++ countClients;
                new Conversation(socket, countClients).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class Conversation extends Thread {

        private Socket socket;
        private int numClients;

        public Conversation(Socket socket, int numClients) {
            this.socket = socket;
            this.numClients = numClients;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                OutputStream outputStream = socket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(outputStream);
                String IP = socket.getRemoteSocketAddress().toString();
                System.out.println("Number of Client : " + numClients + "You're address is " + IP);
                printWriter.println("Welcome You're the " + numClients + " clients!");
                while(true) {
                    String str = bufferedReader.readLine();
                    System.out.println("The Client With the IP Address : " + IP + " was send this request : " + str );
                    printWriter.println(str.length());

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
