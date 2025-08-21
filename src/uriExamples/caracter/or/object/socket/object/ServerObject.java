package uriExamples.caracter.or.object.socket.object;
import java.io.*;
import java.net.*;

public class ServerObject {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("Serveur en attente d'un client...");

            Socket socket = serverSocket.accept();
            System.out.println("Client connecté.");

            // Flux pour recevoir et envoyer des objets
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());

            // Lecture de l'objet envoyé par le client
            Person person = (Person) objectInput.readObject();
            System.out.println("Objet reçu du client : " + person);

            // Réponse : renvoi de l'objet modifié
            person = new Person(person.toString() + " (modifié par le serveur)", person.hashCode() % 100);
            objectOutput.writeObject(person);

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
