package uriExamples.simple.project.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    public static void main(String[] args) throws IOException {


        ServerSocket serverSocket = new ServerSocket(8000);
        System.out.println("Server Started ... Wait client ... !!!");
        Socket socket = serverSocket.accept();
        System.out.println("Get Remote Socket Client Address ... !!!" + socket.getRemoteSocketAddress());
        System.out.println("Client Accepted ... !!!");

        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        System.out.println("Wait client send a message ... !!!");
        int read = inputStream.read();
        System.out.println("Client send a message ... !!!" + read);
        int result = read * 4;
        System.out.println("Send a result by the server ... !!!" + result);
        outputStream.write(result);
        System.out.println("Close the connection ... !!!");
        socket.close();


    }

}
