package group.chatting.application.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

/**
 * Manages ServerSocket binding, client connections, broadcasting, and client disconnects.
 */
public class ServerManager {

    private final String groupName;
    private final int port;
    private final Vector<ClientHandler> activeClients = new Vector<>();
    private ServerSocket serverSocket;
    private volatile boolean isRunning = false;

    public ServerManager(String groupName, int port) {
        this.groupName = (groupName == null || groupName.trim().isEmpty()) ? "Group Chat" : groupName.trim();
        this.port = port;
    }

    public String getGroupName() {
        return groupName;
    }

    /**
     * Starts accepting incoming client connections.
     */
    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        isRunning = true;

        String hostAddress;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            hostAddress = "127.0.0.1";
        }

        System.out.println("\nServer Started Successfully!");
        System.out.println("---------------------------------------");
        System.out.println(" Group Name  : " + groupName);
        System.out.println(" Server IP   : " + hostAddress);
        System.out.println(" Server Port : " + port);
        System.out.println("---------------------------------------");
        System.out.println("Waiting for clients to connect...\n");

        while (isRunning) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                activeClients.add(handler);

                Thread thread = new Thread(handler, "ClientHandler-" + clientSocket.getPort());
                thread.start();
            } catch (IOException e) {
                if (!isRunning) {
                    break;
                }
            }
        }
    }

    /**
     * Broadcasts a raw message line to all connected clients.
     */
    public void broadcast(String message) {
        synchronized (activeClients) {
            for (int i = activeClients.size() - 1; i >= 0; i--) {
                ClientHandler client = activeClients.get(i);
                try {
                    client.sendMessage(message);
                } catch (IOException e) {
                    // Client disconnected ungracefully
                    client.close();
                    activeClients.remove(i);
                }
            }
        }
    }

    /**
     * Removes a disconnected client handler from active list.
     */
    public void removeClient(ClientHandler clientHandler) {
        activeClients.remove(clientHandler);
    }

    /**
     * Stops the server socket.
     */
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
