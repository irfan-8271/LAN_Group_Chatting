package group.chatting.application.client;

import javax.swing.*;

/**
 * Handles collecting connection details (Username, Server IP, and Port) from the user.
 */
public class ConnectionDialog {

    public static class ConnectionConfig {
        private final String username;
        private final String serverIP;
        private final int serverPort;

        public ConnectionConfig(String username, String serverIP, int serverPort) {
            this.username = username;
            this.serverIP = serverIP;
            this.serverPort = serverPort;
        }

        public String getUsername() {
            return username;
        }

        public String getServerIP() {
            return serverIP;
        }

        public int getServerPort() {
            return serverPort;
        }
    }

    /**
     * Prompts the user using input dialogs.
     * Returns null if user cancels any step.
     */
    public static ConnectionConfig prompt() {
        String username = JOptionPane.showInputDialog(
                null,
                "Enter Your Name:",
                "Group Chat - User Login",
                JOptionPane.PLAIN_MESSAGE
        );

        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String serverIP = JOptionPane.showInputDialog(
                null,
                "Enter Server IP Address:",
                "Group Chat - Server Connection",
                JOptionPane.QUESTION_MESSAGE
        );

        if (serverIP == null || serverIP.trim().isEmpty()) {
            serverIP = "127.0.0.1";
        }

        String portText = JOptionPane.showInputDialog(
                null,
                "Enter Server Port Number:",
                "2003"
        );

        if (portText == null || portText.trim().isEmpty()) {
            portText = "2003";
        }

        int port;
        try {
            port = Integer.parseInt(portText.trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid port number. Using default port 2003.", "Error", JOptionPane.ERROR_MESSAGE);
            port = 2003;
        }

        return new ConnectionConfig(username.trim(), serverIP.trim(), port);
    }
}
