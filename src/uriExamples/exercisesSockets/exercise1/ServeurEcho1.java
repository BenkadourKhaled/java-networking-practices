package uriExamples.exercisesSockets.exercise1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServeurEcho1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serveur = new ServerSocket(8282);
        System.out.println("Serveur démarré sur port 8282");

        while (true) {
            Socket client = serveur.accept();
            new Thread(new GestionnaireClient(client)).start();
        }
    }

    static class GestionnaireClient implements Runnable {
        private Socket client;

        public GestionnaireClient(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("Reçu: " + message);
                    out.println("Echo: " + message);

                    if ("quit".equalsIgnoreCase(message)) break;
                }
            } catch (IOException e) {
                System.err.println("Erreur client: " + e.getMessage());
            } finally {
                try { client.close(); } catch (IOException e) {}
            }
        }
    }
}