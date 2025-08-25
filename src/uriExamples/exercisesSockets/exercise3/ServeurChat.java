package uriExamples.exercisesSockets.exercise3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class ServeurChat {
    private static final Map<Integer, PrintWriter> clients = new ConcurrentHashMap<>();
    private static final Map<Integer, String> clientNames = new ConcurrentHashMap<>();
    private static final AtomicInteger clientCounter = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serveur = new ServerSocket(9000)) {
            System.out.println("Serveur de chat démarré sur port 9000");

            while (true) {
                Socket client = serveur.accept();
                int clientId = clientCounter.incrementAndGet();
                new Thread(new GestionnaireChat(client, clientId)).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        }
    }

    static class GestionnaireChat implements Runnable {
        private final Socket client;
        private final int clientId;

        public GestionnaireChat(Socket client, int clientId) {
            this.client = client;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                clients.put(clientId, out);

                out.println("Entrez votre nom:");
                String nom = in.readLine();
                clientNames.put(clientId, nom);

                diffuserMessage("*** " + nom + " a rejoint le chat ***", clientId);
                out.println("Bienvenue " + nom + "! Commandes: /quit, /list, /pm nom message");

                String message;
                while ((message = in.readLine()) != null) {
                    if ("/quit".equals(message)) {
                        break;
                    } else if (message.startsWith("/list")) {
                        out.println("Utilisateurs: " + clientNames.values());
                    } else if (message.startsWith("/pm ")) {
                        envoyerMessagePrive(message, nom);
                    } else {
                        diffuserMessage(nom + ": " + message, clientId);
                    }
                }
            } catch (IOException e) {
                System.err.println("Erreur client " + clientId + ": " + e.getMessage());
            } finally {
                deconnecterClient();
            }
        }

        private void diffuserMessage(String message, int expediteur) {
            System.out.println("Diffusion: " + message);
            for (Map.Entry<Integer, PrintWriter> entry : clients.entrySet()) {
                if (entry.getKey() != expediteur) {
                    entry.getValue().println(message);
                }
            }
        }

        private void envoyerMessagePrive(String message, String expediteur) {
            String[] parts = message.split(" ", 3);
            if (parts.length >= 3) {
                String destinataire = parts[1];
                String contenu = parts[2];

                for (Map.Entry<Integer, String> entry : clientNames.entrySet()) {
                    if (entry.getValue().equals(destinataire)) {
                        PrintWriter out = clients.get(entry.getKey());
                        if (out != null) {
                            out.println("[Privé de " + expediteur + "]: " + contenu);
                            return;
                        }
                    }
                }
                clients.get(clientId).println("Utilisateur '" + destinataire + "' introuvable.");
            }
        }

        private void deconnecterClient() {
            String nom = clientNames.remove(clientId);
            clients.remove(clientId);

            if (nom != null) {
                diffuserMessage("*** " + nom + " a quitté le chat ***", clientId);
            }

            try { client.close(); } catch (IOException e) {}
            System.out.println("Client " + clientId + " (" + nom + ") déconnecté");
        }
    }
}