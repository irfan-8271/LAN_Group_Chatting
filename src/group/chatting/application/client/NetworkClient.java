package group.chatting.application.client;

import group.chatting.application.model.Message;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Manages the TCP Socket networking connection for the chat client.
 * Operates incoming reads on a background thread and dispatches received messages to a callback listener.
 */
public class NetworkClient implements Runnable {

    private final String serverIP;
    private final int serverPort;
    private final String username;

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private Consumer<Message> messageListener;
    private Consumer<String> errorListener;

    private volatile boolean isRunning = false;

    public NetworkClient(String serverIP, int serverPort, String username) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.username = username;
    }

    public void setMessageListener(Consumer<Message> listener) {
        this.messageListener = listener;
    }

    public void setErrorListener(Consumer<String> listener) {
        this.errorListener = listener;
    }

    /**
     * Connects to the chat server socket.
     */
    public void connect() throws IOException {
        socket = new Socket(serverIP, serverPort);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        isRunning = true;

        // Send username as the first line required by the server protocol
        writer.write(username);
        writer.newLine();
        writer.flush();
    }

    /**
     * Starts listening for incoming server messages in a daemon thread.
     */
    public void startListening() {
        Thread thread = new Thread(this, "NetworkClient-Receiver");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            String line;
            while (isRunning && (line = reader.readLine()) != null) {
                Message message = Message.parse(line);
                if (message != null && messageListener != null) {
                    messageListener.accept(message);
                }
            }
        } catch (IOException e) {
            if (isRunning && errorListener != null) {
                errorListener.accept("Disconnected from server.");
            }
        } finally {
            close();
        }
    }

    /**
     * Sends a text message to the chat server.
     */
    public synchronized boolean sendMessage(String text) {
        if (socket == null || socket.isClosed() || text == null || text.trim().isEmpty()) {
            return false;
        }

        try {
            Message msg = new Message(username, text.trim(), Message.Type.USER);
            writer.write(msg.toProtocolString());
            writer.newLine();
            writer.flush();
            return true;
        } catch (IOException e) {
            if (errorListener != null) {
                errorListener.accept("Failed to send message: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Closes socket and network streams cleanly.
     */
    public void close() {
        isRunning = false;
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }
}
