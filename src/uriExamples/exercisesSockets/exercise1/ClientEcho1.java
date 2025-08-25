package uriExamples.exercisesSockets.exercise1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientEcho1 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8282);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connecté au serveur. Tapez vos messages (quit pour quitter):");

            String userInput;
            while ((userInput = console.readLine()) != null) {
                out.println(userInput);

                if ("quit".equalsIgnoreCase(userInput)) {
                    break;
                }

                String response = in.readLine();
                System.out.println("Serveur: " + response);
            }

        } catch (IOException e) {
            System.err.println("Erreur client: " + e.getMessage());
        }
    }
}