package uriExamples.caracter.or.object.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClientCharacter {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Connecté au serveur.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            // Envoi d'un message au serveur
            writer.println("Salut serveur !");

            // Réception de la réponse
            String response = reader.readLine();
            System.out.println("Réponse du serveur : " + response);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
