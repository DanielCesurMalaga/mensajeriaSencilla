package ladoCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "localhost"; // Cambia esto a la IP del servidor si es necesario
        int port = 12345;
        Socket socket = null;

        try {
            socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            // Hilo para recibir mensajes del servidor
            Thread receiveMessages = new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println("Respuesta servidor: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            receiveMessages.start();

            // Enviar mensajes al servidor desde la consola
            
            System.out.println("Introduce mensajes desde la consola, para salir ('quit')");
            String userInput = consoleInput.readLine();
            while (! userInput.equals("quit")) {
                out.println(userInput);
                userInput = consoleInput.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
        }
    }
}
