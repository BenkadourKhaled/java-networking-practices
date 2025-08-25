package uriExamples.exercisesSockets.exercise5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

class ServeurRobuste {
    private static final int PORT = 9002;
    private static final int MAX_THREADS = 20;
    private static final int CORE_THREADS = 5;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final int TIMEOUT_SECONDS = 30;

    private final AtomicInteger clientsConnectes = new AtomicInteger(0);
    private final AtomicLong totalConnexions = new AtomicLong(0);
    private final AtomicLong messagesTraites = new AtomicLong(0);

    private final ThreadPoolExecutor threadPool;
    private volatile boolean running = true;

    public ServeurRobuste() {
        this.threadPool = new ThreadPoolExecutor(
                CORE_THREADS, MAX_THREADS, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactory() {
                    private final AtomicInteger counter = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread t = new Thread(r, "ServeurWorker-" + counter.getAndIncrement());
                        t.setDaemon(true);
                        return t;
                    }
                }
        );
    }

    public void demarrer() {
        new Thread(this::afficherStatistiques).start();

        try (ServerSocket serveur = new ServerSocket(PORT)) {
            serveur.setSoTimeout(1000);

            log("Serveur robuste démarré sur port " + PORT);
            log("Pool: " + CORE_THREADS + "-" + MAX_THREADS + " threads");

            while (running) {
                try {
                    Socket client = serveur.accept();
                    client.setSoTimeout(TIMEOUT_SECONDS * 1000);

                    long clientId = totalConnexions.incrementAndGet();
                    clientsConnectes.incrementAndGet();

                    threadPool.submit(new GestionnaireClient(client, clientId));

                } catch (SocketTimeoutException e) {
                    // Normal
                } catch (IOException e) {
                    if (running) {
                        log("Erreur acceptation client: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log("Erreur serveur: " + e.getMessage());
        } finally {
            arreter();
        }
    }

    public void arreter() {
        log("Arrêt du serveur en cours...");
        running = false;
        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }

        log("Serveur arrêté");
    }

    private void afficherStatistiques() {
        while (running) {
            try {
                Thread.sleep(10000);

                log("=== STATS ===");
                log("Clients: " + clientsConnectes.get());
                log("Total: " + totalConnexions.get());
                log("Messages: " + messagesTraites.get());
                log("Threads actifs: " + threadPool.getActiveCount());
                log("============");

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println("[" + timestamp + "] " + message);
    }

    class GestionnaireClient implements Runnable {
        private final Socket client;
        private final long clientId;

        public GestionnaireClient(Socket client, long clientId) {
            this.client = client;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            String clientInfo = client.getInetAddress().getHostAddress();
            log("Client " + clientId + " connecté depuis " + clientInfo);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {

                out.println("Bienvenue! ID: " + clientId);
                out.println("Thread: " + Thread.currentThread().getName());
                out.println("Commandes: /stats, /help, quit");

                String message;
                while ((message = in.readLine()) != null) {
                    messagesTraites.incrementAndGet();

                    if ("quit".equalsIgnoreCase(message)) {
                        out.println("Au revoir!");
                        break;
                    } else if ("/stats".equals(message)) {
                        out.println("Connectés: " + clientsConnectes.get() +
                                ", Total: " + totalConnexions.get() +
                                ", Messages: " + messagesTraites.get());
                    } else if ("/help".equals(message)) {
                        out.println("Commandes: /stats, /help, quit");
                    } else {
                        out.println("Echo[" + clientId + "]: " + message);
                    }
                }

            } catch (SocketTimeoutException e) {
                log("Timeout client " + clientId);
            } catch (IOException e) {
                log("Erreur client " + clientId + ": " + e.getMessage());
            } finally {
                try { client.close(); } catch (IOException e) {}
                clientsConnectes.decrementAndGet();
                log("Client " + clientId + " déconnecté");
            }
        }
    }

    public static void main(String[] args) {
        ServeurRobuste serveur = new ServeurRobuste();
        Runtime.getRuntime().addShutdownHook(new Thread(serveur::arreter));
        serveur.demarrer();
    }
}