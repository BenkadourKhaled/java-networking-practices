package uriExamples.caracter.or.object.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Serveur démarré, en attente d'un client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connecté.");

            // Flux pour recevoir et envoyer des chaînes de caractères
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Lecture du message du client
            String messageClient = reader.readLine();
            System.out.println("Message reçu du client : " + messageClient);

            // Réponse du serveur
            writer.println("Bonjour client, j'ai bien reçu : " + messageClient);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
