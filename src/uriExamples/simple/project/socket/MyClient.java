package uriExamples.simple.project.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        System.out.println("Connecting to the server...");
        Socket socket = new Socket("localhost", 8000);
        System.out.println("Connected successfully");
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        System.out.println("Enter an Integer positive number");
        int nb = scanner.nextInt();
        System.out.println("Send the number to the server" + nb);
        outputStream.write(nb);
        System.out.println("Wait the result of the server");
        int read = inputStream.read();
        System.out.println("The Result of the server is: " + read);
        System.out.println("Close the connection");
        socket.close();


    }
}
