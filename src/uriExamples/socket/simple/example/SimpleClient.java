package uriExamples.socket.simple.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) throws IOException {
        // 1️- Connexion au serveur
        // Le client essaie de se connecter à la machine "localhost"
        // (c’est-à-dire l’ordinateur local) sur le port 5000.
        Socket socket = new Socket("localhost", 5000);

        // 2️- Préparation des flux de communication
        // out → permet d’envoyer un message vers le serveur.
        // in → permet de lire la réponse envoyée par le serveur.
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // 3️- Envoi d’un message au serveur
        // Ici le client envoie un texte "Hello serveur 👋".
        out.println("Hello serveur 👋");

        // 4️- Réception de la réponse du serveur
        // Le client attend que le serveur lui renvoie un message,
        // puis il l’affiche dans la console.
        String response = in.readLine();
        System.out.println("Réponse du serveur : " + response);

        // 5️- Fermeture de la connexion
        // Une fois la communication terminée, on coupe la "prise".
        socket.close();
    }
}
