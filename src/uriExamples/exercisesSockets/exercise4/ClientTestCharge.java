package uriExamples.exercisesSockets.exercise4;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientTestCharge {
    public static void main(String[] args) {
        String clientName = args.length > 0 ? args[0] : "Client" + System.currentTimeMillis();

        try (Socket socket = new Socket("localhost", 9001);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("=== " + clientName + " ===");

            // Lire les messages de bienvenue
            System.out.println(in.readLine());
            System.out.println(in.readLine());

            System.out.println("Tapez vos messages ('slow' pour test, 'auto' pour test automatique):");

            String userInput;
            while ((userInput = console.readLine()) != null) {
                if ("auto".equals(userInput)) {
                    // Test automatique
                    testAutomatique(out, in);
                } else {
                    out.println(userInput);

                    if ("quit".equalsIgnoreCase(userInput)) {
                        break;
                    }

                    String response = in.readLine();
                    System.out.println("Serveur: " + response);

                    // Si c'est un traitement lent, lire la réponse finale
                    if ("slow".equals(userInput)) {
                        response = in.readLine();
                        System.out.println("Serveur: " + response);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur " + clientName + ": " + e.getMessage());
        }
    }

    private static void testAutomatique(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Début test automatique...");

        // Test messages normaux
        for (int i = 1; i <= 3; i++) {
            out.println("Message " + i);
            System.out.println("Envoyé: Message " + i);
            System.out.println("Reçu: " + in.readLine());
        }

        // Test message lent
        out.println("slow");
        System.out.println("Envoyé: slow");
        System.out.println("Reçu: " + in.readLine()); // "Traitement en cours..."
        System.out.println("Reçu: " + in.readLine()); // "Traitement terminé!"

        System.out.println("Test automatique terminé.");
    }
}