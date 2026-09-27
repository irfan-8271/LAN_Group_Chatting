package group.chatting.application.client;

import group.chatting.application.client.ConnectionDialog.ConnectionConfig;
import group.chatting.application.model.Message;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Controller class that ties together the connection dialog, network client, and chat window.
 */
public class ClientManager {

    private NetworkClient networkClient;
    private ChatWindow chatWindow;

    public void start() {
        // Step 1: Request connection details from user
        ConnectionConfig config = ConnectionDialog.prompt();
        if (config == null) {
            System.exit(0);
            return;
        }

        // Step 2: Initialize Network Client
        networkClient = new NetworkClient(config.getServerIP(), config.getServerPort(), config.getUsername());

        // Step 3: Initialize Responsive UI
        chatWindow = new ChatWindow(config.getUsername(), config.getServerIP(), config.getServerPort());

        // Attach UI event handlers
        chatWindow.setOnSendMessageHandler(text -> {
            networkClient.sendMessage(text);
        });

        chatWindow.setOnCloseHandler(() -> {
            networkClient.close();
        });

        // Attach Network event handlers
        networkClient.setMessageListener(message -> {
            if (message.getType() == Message.Type.GROUP_NAME) {
                chatWindow.updateGroupName(message.getContent());
            } else {
                chatWindow.displayMessage(message);
            }
        });

        networkClient.setErrorListener(errorMsg -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        chatWindow.getFrame(),
                        errorMsg,
                        "Network Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });
        });

        // Step 4: Connect to Server Socket
        try {
            networkClient.connect();
            networkClient.startListening();
            chatWindow.showWindow();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to connect to server at " + config.getServerIP() + ":" + config.getServerPort() + "\n" + e.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(0);
        }
    }
}
