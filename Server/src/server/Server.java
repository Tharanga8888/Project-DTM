
package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Server {
    private static final int SERVER_PORT = 8080;
    private static long startRange = 0L;
    private static long endRange = 999999L;
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is listening on port " + SERVER_PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from: " + clientSocket.getInetAddress()+ " port - " + clientSocket.getPort());

                Runnable clientTask = new ClientHandler(clientSocket);
                new Thread(clientTask).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static synchronized long getStartRange() {
        long currentStartRange = startRange;
        startRange += 1000000L; // Increment startRange for the next request
        return currentStartRange;
    }

    static synchronized long getEndRange() {
        long currentEndRange = endRange;
        endRange += 1000000L; // Increment endRange for the next request
        return currentEndRange;
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request;
            while ((request = in.readLine()) != null) {
                if (request.equals("Asking for the range")) {
                    System.out.println("Sending the range to the client - " + clientSocket.getInetAddress() + " port - " + clientSocket.getPort());
                    sendRange();
                } else if (request.startsWith("Prime Numbers: ")) {
                    // Process and print results
                    processResults(request);
                } else if (request.equals("TASK_COMPLETE")) {
                    System.out.println("Client - " + clientSocket.getInetAddress() + " port - " + clientSocket.getPort() + " completed the task");
                } else if (request.equals("CONNECT")) {
                    System.out.println("Client - " + clientSocket.getInetAddress() + " port - " + clientSocket.getPort() + " connected.");
                } else if (request.equals("PING")) {
                    // Respond to ping
                    out.println("PONG");
                } else if (request.equals("DISCONNECT")) {
                    System.out.println("Client - " + clientSocket.getInetAddress() + " port - " + clientSocket.getPort() + " disconnected.");
                    break;
                }
            }
        } catch (SocketException e) {
            // Handle client disconnection gracefully
            System.out.println("Client - " + clientSocket.getInetAddress() + " port - " + clientSocket.getPort() + " is disconnected.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    private void sendRange() {
        // Get the startRange and endRange from the server
        long startRange = Server.getStartRange();
        long endRange = Server.getEndRange();

        out.println(startRange + "-" + endRange);
    }

    private void processResults(String request) {
        // Process and print the received results
        System.out.println(request);
        
        if (request.startsWith("Time taken for task completion is")) {
            // Extract and print the time taken
            String timeTaken = request.substring(request.indexOf("is") + 3).trim();
            System.out.println("Time taken for task completion: " + timeTaken);
        }
        
        // Send a confirmation message to the client
        sendConfirmation();  
    }

    private void sendConfirmation() {
        out.println("CONFIRMATION: Results received successfully.");
    }
}
