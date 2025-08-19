package uriExamples.socket.simple.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        // 1️- Création d’un ServerSocket sur le port 5000
        // Cela signifie que le programme "ouvre une porte" sur la machine
        // et attend que des clients viennent frapper à cette porte.
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur démarré sur le port 5000...");

        // 2️- Attente d’un client
        // La méthode accept() est BLOQUANTE → le programme reste ici
        // jusqu’à ce qu’un client essaie de se connecter.
        Socket socket = serverSocket.accept();
        System.out.println("Un client est connecté !");

        // 3️- Préparation des flux de communication
        // in → permet de LIRE ce que le client envoie.
        // out → permet d’ÉCRIRE des réponses à destination du client.
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // 4️- Lecture du message envoyé par le client
        // Le serveur attend que le client envoie un texte (une ligne).
        String message = in.readLine();
        System.out.println("Message du client : " + message);

        // 5️- Envoi d’une réponse au client
        // Ici le serveur confirme qu’il a bien reçu le message.
        out.println("Salut, j’ai bien reçu ton message");

        // 6️- Fermeture des connexions
        // On ferme d’abord la "prise" (socket client), puis le serveur.
        socket.close();
        serverSocket.close();
    }
}
