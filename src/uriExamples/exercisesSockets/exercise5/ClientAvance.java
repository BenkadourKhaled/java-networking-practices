package uriExamples.exercisesSockets.exercise5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientAvance {
    private final String nom;
    private volatile boolean running = true;

    public ClientAvance(String nom) {
        this.nom = nom;
    }

    public void connecter() {
        try (Socket socket = new Socket("localhost", 9002);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            // Thread pour écouter le serveur
            Thread ecouteur = new Thread(() -> {
                try {
                    String message;
                    while (running && (message = in.readLine()) != null) {
                        System.out.println("Serveur: " + message);
                    }
                } catch (IOException e) {
                    if (running) {
                        System.out.println("Connexion fermée par le serveur.");
                    }
                }
            });
            ecouteur.setDaemon(true);
            ecouteur.start();

            System.out.println("=== " + nom + " connecté ===");
            System.out.println("Tapez vos messages ('/stats' pour statistiques, 'quit' pour quitter):");

            String userInput;
            while (running && (userInput = console.readLine()) != null) {
                out.println(userInput);

                if ("quit".equalsIgnoreCase(userInput)) {
                    running = false;
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur " + nom + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String nom = args.length > 0 ? args[0] : "ClientAvance";
        new ClientAvance(nom).connecter();
    }
}
