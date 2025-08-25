package uriExamples.exercisesSockets.exercise4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class ServeurPoolThreads {
    private static final int PORT = 9001;
    private static final int THREAD_POOL_SIZE = 10;
    private final ExecutorService threadPool;
    private final AtomicInteger clientCounter = new AtomicInteger(0);

    public ServeurPoolThreads() {
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void demarrer() {
        try (ServerSocket serveur = new ServerSocket(PORT)) {
            System.out.println("Serveur avec pool de " + THREAD_POOL_SIZE + " threads sur port " + PORT);

            while (true) {
                Socket client = serveur.accept();
                int clientId = clientCounter.incrementAndGet();

                threadPool.submit(new TacheClient(client, clientId));
                System.out.println("Client " + clientId + " assigné au pool");
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    static class TacheClient implements Runnable {
        private final Socket client;
        private final int clientId;

        public TacheClient(Socket client, int clientId) {
            this.client = client;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            String threadName = Thread.currentThread().getName();
            System.out.println("Client " + clientId + " traité par thread: " + threadName);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                out.println("Connexion établie (Thread: " + threadName + ")");
                out.println("Commandes: 'slow' pour test lent, 'quit' pour quitter");

                String message;
                while ((message = in.readLine()) != null) {
                    if ("quit".equalsIgnoreCase(message)) break;

                    if ("slow".equals(message)) {
                        out.println("Traitement lent en cours...");
                        Thread.sleep(5000);
                        out.println("Traitement terminé!");
                    } else {
                        out.println("Echo[" + threadName + "]: " + message);
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Erreur client " + clientId + ": " + e.getMessage());
            } finally {
                try { client.close(); } catch (IOException e) {}
                System.out.println("Client " + clientId + " terminé");
            }
        }
    }

    public static void main(String[] args) {
        new ServeurPoolThreads().demarrer();
    }
}