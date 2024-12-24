package ladoServidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    // Lista de clientes conectados
    private static List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        int port = 12345; // Puedes cambiar el puerto
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado desde puerto: " + socket.getPort());

                // Crear un nuevo hilo para manejar el cliente
                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null)
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
        }
    }

    // Enviar el mensaje a todos los clientes conectados, excepto al que lo envió
    public static void broadcastMessage(String message, ClientHandler excludeClient) {
        for (ClientHandler client : clients) {
            // No enviar el mensaje al cliente que lo envió
            if (client != excludeClient) {
                client.sendMessage(message);
            }
        }
    }
}
