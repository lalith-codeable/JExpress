import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        int PORT = 4221;
        ServerSocket serverSocket = null;
        System.out.println("Starting server at port " + PORT);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                clientSocket
                    .getOutputStream()
                    .write(
                        ("HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: 20\r\n" +
                            "\r\n" +
                            "Hello from the server!").getBytes()
                    );
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            System.out.println("Stopping server ...");
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                }
            }
        }
    }
}
