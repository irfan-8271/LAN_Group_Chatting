package group.chatting.application.client;

import group.chatting.application.model.Message;
import group.chatting.application.ui.ChatBubble;
import group.chatting.application.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

/**
 * Responsive Swing JFrame GUI for the group chat client.
 * Uses layout managers (BorderLayout) instead of static absolute bounds,
 * ensuring fluid resizing and layout adaptability across all window sizes.
 */
public class ChatWindow {

    private final String username;
    private final String serverIP;
    private final int serverPort;

    private JFrame frame;
    private JLabel titleLabel;
    private JPanel chatPanel;
    private JScrollPane scrollPane;
    private JTextField messageField;
    private JButton sendButton;

    private Consumer<String> onSendMessageHandler;
    private Runnable onCloseHandler;

    public ChatWindow(String username, String serverIP, int serverPort) {
        this.username = username;
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        initializeUI();
    }

    public void setOnSendMessageHandler(Consumer<String> handler) {
        this.onSendMessageHandler = handler;
    }

    public void setOnCloseHandler(Runnable handler) {
        this.onCloseHandler = handler;
    }

    private void initializeUI() {
        frame = new JFrame("Group Chat - " + username);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(380, 500));
        frame.setPreferredSize(new Dimension(480, 680));
        frame.setLayout(new BorderLayout());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (onCloseHandler != null) {
                    onCloseHandler.run();
                }
                frame.dispose();
                System.exit(0);
            }
        });

        // ----------------------------------------------------
        // 1. Header Panel (NORTH)
        // ----------------------------------------------------
        JPanel headerPanel = createHeaderPanel();
        frame.add(headerPanel, BorderLayout.NORTH);

        // ----------------------------------------------------
        // 2. Chat Scroll Area (CENTER) - Fully responsive!
        // ----------------------------------------------------
        chatPanel = new JPanel();
        chatPanel.setBackground(Theme.BACKGROUND_COLOR);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add(scrollPane, BorderLayout.CENTER);

        // ----------------------------------------------------
        // 3. Input Panel (SOUTH) - Expands horizontally with window!
        // ----------------------------------------------------
        JPanel inputPanel = createInputPanel();
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(5, 5));
        header.setBackground(Theme.PRIMARY_COLOR);
        header.setBorder(new EmptyBorder(12, 16, 12, 16));

        titleLabel = new JLabel("<html><b>Group Chat</b></html>");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        JLabel infoLabel = new JLabel("Connected as: " + username + " (" + serverIP + ":" + serverPort + ")");
        infoLabel.setFont(Theme.FONT_HEADER_INFO);
        infoLabel.setForeground(new Color(240, 230, 250));

        JPanel textWrapper = new JPanel(new GridLayout(2, 1, 0, 4));
        textWrapper.setOpaque(false);
        textWrapper.add(titleLabel);
        textWrapper.add(infoLabel);

        header.add(textWrapper, BorderLayout.WEST);
        return header;
    }

    public void updateGroupName(String groupName) {
        SwingUtilities.invokeLater(() -> {
            titleLabel.setText("<html><b>" + groupName + "</b></html>");
            frame.setTitle(groupName + " - " + username);
        });
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        messageField.setFont(Theme.FONT_INPUT_FIELD);
        messageField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 220), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Submit message on Enter keypress
        messageField.addActionListener(e -> triggerSend());

        sendButton = new JButton("Send");
        sendButton.setFont(Theme.FONT_BUTTON);
        sendButton.setBackground(Theme.PRIMARY_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setPreferredSize(new Dimension(85, 38));

        sendButton.addActionListener(e -> triggerSend());

        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        return panel;
    }

    private void triggerSend() {
        String text = messageField.getText().trim();
        if (!text.isEmpty()) {
            if (onSendMessageHandler != null) {
                onSendMessageHandler.accept(text);
            }
            messageField.setText("");
            messageField.requestFocus();
        }
    }

    /**
     * Displays a message in the chat pane safely on the Swing EDT.
     */
    public void displayMessage(Message message) {
        SwingUtilities.invokeLater(() -> {
            JPanel bubblePanel = ChatBubble.createMessagePanel(message, username);
            chatPanel.add(bubblePanel);
            chatPanel.revalidate();
            chatPanel.repaint();
            scrollToBottom();
        });
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }

    public void showWindow() {
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public JFrame getFrame() {
        return frame;
    }
}
