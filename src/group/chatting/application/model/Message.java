package group.chatting.application.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Encapsulates a chat message packet.
 * Supports user messages, server notifications, and group metadata packets.
 */
public class Message {

    public enum Type {
        USER,
        SERVER,
        GROUP_NAME
    }

    private final String sender;
    private final String content;
    private final Type type;
    private final String timestamp;

    public Message(String sender, String content, Type type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.timestamp = new SimpleDateFormat("HH:mm").format(new Date());
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public Type getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Formats the message into a standard wire protocol string ("sender|content").
     */
    public String toProtocolString() {
        return sender + "|" + content;
    }

    /**
     * Parses a raw protocol string ("sender|content") into a Message object.
     */
    public static Message parse(String rawPacket) {
        if (rawPacket == null || rawPacket.trim().isEmpty()) {
            return null;
        }

        String[] parts = rawPacket.split("\\|", 2);
        if (parts.length < 2) {
            return new Message("SERVER", rawPacket, Type.SERVER);
        }

        String sender = parts[0];
        String text = parts[1];

        if ("GROUP_NAME".equalsIgnoreCase(sender)) {
            return new Message("GROUP_NAME", text, Type.GROUP_NAME);
        } else if ("SERVER".equalsIgnoreCase(sender)) {
            return new Message("SERVER", text, Type.SERVER);
        } else {
            return new Message(sender, text, Type.USER);
        }
    }
}
