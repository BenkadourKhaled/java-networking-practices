package uriExamples.exercisesSockets.exercise3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientChat {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 9000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread pour écouter les messages du serveur
            Thread ecouteur = new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    System.out.println("Connexion fermée.");
                }
            });
            ecouteur.setDaemon(true);
            ecouteur.start();

            // Thread principal pour envoyer les messages
            System.out.println("=== CLIENT CHAT ===");
            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);

                if ("/quit".equals(userInput)) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        }
    }
}
