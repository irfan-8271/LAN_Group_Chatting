package group.chatting.application;

import group.chatting.application.client.ClientManager;

import javax.swing.SwingUtilities;

/**
 * Main entry point for launching the Group Chat Client application.
 */
public class Client {

    public static void main(String[] args) {
        // Run Swing GUI initialization on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            ClientManager clientManager = new ClientManager();
            clientManager.start();
        });
    }
}