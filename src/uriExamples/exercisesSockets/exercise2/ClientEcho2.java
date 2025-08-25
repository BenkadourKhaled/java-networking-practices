package uriExamples.exercisesSockets.exercise2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientEcho2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            // Lire le message de bienvenue
            String welcome = in.readLine();
            System.out.println("Serveur: " + welcome);

            System.out.println("Tapez vos messages (quit pour quitter):");

            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);

                String response = in.readLine();
                System.out.println("Serveur: " + response);

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        }
    }
}
