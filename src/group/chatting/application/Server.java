package group.chatting.application;

import group.chatting.application.server.ServerManager;

import java.util.Scanner;

/**
 * Main application launcher for the Group Chat Server.
 */
public class Server {

    public static final int DEFAULT_PORT = 2003;
    public static final String DEFAULT_GROUP_NAME = "Group Chat";

    public static void main(String[] args) {
        String groupName = DEFAULT_GROUP_NAME;
        int port = DEFAULT_PORT;

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("========== GROUP CHAT SERVER ==========");
            System.out.print("Enter Chat Group Name (Default: " + DEFAULT_GROUP_NAME + "): ");
            String nameInput = scanner.nextLine().trim();
            if (!nameInput.isEmpty()) {
                groupName = nameInput;
            }

            System.out.print("Enter Port Number (Default: " + DEFAULT_PORT + "): ");
            String portInput = scanner.nextLine().trim();
            if (!portInput.isEmpty()) {
                try {
                    port = Integer.parseInt(portInput);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid port entered. Using default port " + DEFAULT_PORT);
                }
            }
        } finally {
            // Scanner input phase finished
        }

        try {
            ServerManager serverManager = new ServerManager(groupName, port);
            serverManager.start();
        } catch (Exception e) {
            System.err.println("Failed to start server on port " + port + ": " + e.getMessage());
        }
    }
}