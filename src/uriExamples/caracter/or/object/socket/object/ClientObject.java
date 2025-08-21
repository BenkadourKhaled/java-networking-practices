package uriExamples.caracter.or.object.socket.object;
import java.io.*;
import java.net.*;

public class ClientObject {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 6000)) {
            System.out.println("Connecté au serveur.");

            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());

            // Envoi d'un objet Person
            Person person = new Person("Khaled", 25);
            objectOutput.writeObject(person);

            // Réception de la réponse (objet modifié)
            Person response = (Person) objectInput.readObject();
            System.out.println("Réponse du serveur : " + response);

            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
