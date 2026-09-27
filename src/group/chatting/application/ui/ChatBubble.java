package group.chatting.application.ui;

import group.chatting.application.model.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Utility for rendering user and server message bubbles cleanly.
 */
public class ChatBubble {

    /**
     * Creates a wrapper panel containing a styled chat bubble aligned according to sender type.
     */
    public static JPanel createMessagePanel(Message message, String currentUsername) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(4, 8, 4, 8));

        if (message.getType() == Message.Type.SERVER) {
            JPanel serverPanel = createServerBubble(message.getContent());
            wrapper.add(serverPanel, BorderLayout.CENTER);
        } else {
            boolean isSelf = message.getSender().equalsIgnoreCase(currentUsername);
            JPanel bubble = createBubble(
                    isSelf ? "You" : message.getSender(),
                    message.getContent(),
                    message.getTimestamp(),
                    isSelf ? Theme.MY_BUBBLE_BG : Theme.OTHER_BUBBLE_BG,
                    isSelf ? Theme.MY_BUBBLE_FG : Theme.OTHER_BUBBLE_FG,
                    isSelf
            );
            wrapper.add(bubble, isSelf ? BorderLayout.EAST : BorderLayout.WEST);
        }

        return wrapper;
    }

    private static JPanel createBubble(String senderName, String textContent, String timeStr,
                                       Color bg, Color fg, boolean isSelf) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Sender Label
        JLabel senderLabel = new JLabel(senderName);
        senderLabel.setFont(Theme.FONT_SENDER_NAME);
        senderLabel.setForeground(isSelf ? Theme.PRIMARY_DARK : Color.DARK_GRAY);
        senderLabel.setAlignmentX(isSelf ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);
        panel.add(senderLabel);

        // Escape HTML special characters in content before placing in HTML tag
        String escapedText = textContent.replace("&", "&amp;")
                                       .replace("<", "&lt;")
                                       .replace(">", "&gt;")
                                       .replace("\n", "<br>");

        JLabel textLabel = new JLabel("<html><body style='width:200px; word-wrap: break-word;'>" + escapedText + "</body></html>");
        textLabel.setOpaque(true);
        textLabel.setBackground(bg);
        textLabel.setForeground(fg);
        textLabel.setFont(Theme.FONT_MESSAGE_BODY);
        textLabel.setBorder(new EmptyBorder(8, 12, 8, 12));
        textLabel.setAlignmentX(isSelf ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(2));
        panel.add(textLabel);

        // Time Label
        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(Theme.FONT_TIMESTAMP);
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setAlignmentX(isSelf ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

        panel.add(Box.createVerticalStrut(2));
        panel.add(timeLabel);

        return panel;
    }

    private static JPanel createServerBubble(String textContent) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);

        JLabel label = new JLabel(textContent);
        label.setOpaque(true);
        label.setBackground(Theme.SERVER_BUBBLE_BG);
        label.setForeground(Theme.SERVER_BUBBLE_FG);
        label.setFont(Theme.FONT_HEADER_INFO);
        label.setBorder(new EmptyBorder(5, 12, 5, 12));

        wrapper.add(label);
        return wrapper;
    }
}
