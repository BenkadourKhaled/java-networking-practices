package uriExamples.exercisesSockets.exercise2;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

class ServeurEcho2 {
    private static final AtomicInteger clientCounter = new AtomicInteger(0);
    private static final int PORT = 8000;

    public static void main(String[] args) {
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur port " + PORT);

            while (true) {
                Socket client = serveur.accept();
                int clientId = clientCounter.incrementAndGet();

                Thread clientThread = new Thread(new GestionnaireClient(client, clientId));
                clientThread.setName("Client-" + clientId);
                clientThread.start();

                System.out.println("Nouveau client connecté (ID: " + clientId + ")");
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        }
    }

    static class GestionnaireClient implements Runnable {
        private final Socket client;
        private final int clientId;

        public GestionnaireClient(Socket client, int clientId) {
            this.client = client;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                out.println("Bienvenue! Votre ID est: " + clientId);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Client " + clientId + ": " + message);

                    if ("quit".equalsIgnoreCase(message)) {
                        out.println("Au revoir client " + clientId + "!");
                        break;
                    }

                    out.println("Echo[" + clientId + "]: " + message.toUpperCase());
                }
            } catch (IOException e) {
                System.err.println("Erreur avec client " + clientId + ": " + e.getMessage());
            } finally {
                try {
                    client.close();
                    System.out.println("Client " + clientId + " déconnecté");
                } catch (IOException e) {}
            }
        }
    }
}
