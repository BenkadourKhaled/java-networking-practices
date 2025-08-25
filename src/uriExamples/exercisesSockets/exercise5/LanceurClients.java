package uriExamples.exercisesSockets.exercise5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class LanceurClients {
    public static void main(String[] args) {
        int nbClients = args.length > 0 ? Integer.parseInt(args[0]) : 5;

        System.out.println("Lancement de " + nbClients + " clients...");

        for (int i = 1; i <= nbClients; i++) {
            final int clientId = i;
            new Thread(() -> {
                try {
                    Thread.sleep(clientId * 1000); // Décalage
                    new ClientTest("Client" + clientId).testerServeur();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

    static class ClientTest {
        private final String nom;

        public ClientTest(String nom) {
            this.nom = nom;
        }

        public void testerServeur() {
            try (Socket socket = new Socket("localhost", 8080);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                System.out.println(nom + " connecté");

                // Lire messages de bienvenue
                for (int i = 0; i < 3; i++) {
                    System.out.println(nom + " - " + in.readLine());
                }

                // Envoyer quelques messages
                for (int i = 1; i <= 3; i++) {
                    out.println("Message " + i + " de " + nom);
                    System.out.println(nom + " - " + in.readLine());
                    Thread.sleep(2000);
                }

                // Demander les stats
                out.println("/stats");
                System.out.println(nom + " - " + in.readLine());

                // Quitter
                out.println("quit");
                System.out.println(nom + " - " + in.readLine());

                System.out.println(nom + " terminé");

            } catch (IOException | InterruptedException e) {
                System.err.println("Erreur " + nom + ": " + e.getMessage());
            }
        }
    }
}