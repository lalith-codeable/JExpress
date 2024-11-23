import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                System.out.println("New Client connection Accepted.");

                String requestLine = null;

                //input-stream
                InputStream ipStream = clientSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = ipStream.read(buffer);
                if (bytesRead != -1) {
                    String received = new String(buffer, 0, bytesRead);
                    int requestEOL = received.indexOf("\r\n");
                    if (requestEOL != -1) {
                        requestLine = received.substring(0, requestEOL).trim();
                    }
                    System.out.println(received);
                }
                if (requestLine == null) {
                    System.out.println("Invalid request.");
                    clientSocket.close();
                    continue;
                }
                String[] requestParts = requestLine.split(" ");
                if (requestParts.length < 3) {
                    System.out.println("Malformed request.");
                    clientSocket.close();
                    continue;
                }

                String requestTarget = requestParts[1];
                System.out.println("Request target: " + requestTarget);

                //output-stream
                OutputStream opstream = clientSocket.getOutputStream();
                if ("/".equals(requestTarget)) {
                    String responseBody = "Hello from server!";
                    opstream.write(
                        ("HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length:" +
                            responseBody.length() +
                            "\r\n" +
                            "\r\n" +
                            responseBody).getBytes()
                    );
                    opstream.flush();
                } else {
                    String responseBody = "404 Not Found";
                    opstream.write(
                        ("HTTP/1.1 404 Not Found\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " +
                            responseBody.length() +
                            "\r\n" +
                            "\r\n" +
                            responseBody).getBytes()
                    );
                    opstream.flush();
                }
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
