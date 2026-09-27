package group.chatting.application.server;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handles communication with an individual client on a dedicated background thread.
 */
public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ServerManager serverManager;

    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public ClientHandler(Socket socket, ServerManager serverManager) {
        this.socket = socket;
        this.serverManager = serverManager;

        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException ignored) {
        }
    }

    public String getUsername() {
        return username;
    }

    private String getTimestamp() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @Override
    public void run() {
        try {
            // First line sent by client is mandatory username
            username = reader.readLine();
            if (username == null || username.trim().isEmpty()) {
                username = "Anonymous_" + socket.getPort();
            }

            // Log join event on server console with timestamp
            System.out.println("[" + getTimestamp() + "] " + username + " joined the chat.");

            // Send custom group name to joining client immediately
            sendMessage("GROUP_NAME|" + serverManager.getGroupName());

            // Broadcast join notification to all users
            serverManager.broadcast("SERVER|" + username + " joined the chat.");

            String incomingLine;
            while ((incomingLine = reader.readLine()) != null) {
                serverManager.broadcast(incomingLine);
            }
        } catch (IOException ignored) {
        } finally {
            close();
            serverManager.removeClient(this);
            if (username != null) {
                // Log left event on server console with timestamp
                System.out.println("[" + getTimestamp() + "] " + username + " left the chat.");
                serverManager.broadcast("SERVER|" + username + " left the chat.");
            }
        }
    }

    /**
     * Sends a raw message line to this client.
     */
    public void sendMessage(String message) throws IOException {
        writer.write(message);
        writer.newLine();
        writer.flush();
    }

    /**
     * Closes socket resources for this client handler.
     */
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
