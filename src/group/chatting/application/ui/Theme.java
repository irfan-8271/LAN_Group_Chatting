package group.chatting.application.ui;

import java.awt.Color;
import java.awt.Font;

/**
 * Defines styling, color palettes, and typography constants for the Swing UI.
 */
public class Theme {

    public static final Color PRIMARY_COLOR = new Color(192, 73, 226);
    public static final Color PRIMARY_DARK = new Color(150, 45, 180);
    public static final Color BACKGROUND_COLOR = Color.WHITE;

    public static final Color MY_BUBBLE_BG = new Color(192, 73, 226);
    public static final Color MY_BUBBLE_FG = Color.WHITE;

    public static final Color OTHER_BUBBLE_BG = new Color(240, 240, 245);
    public static final Color OTHER_BUBBLE_FG = Color.BLACK;

    public static final Color SERVER_BUBBLE_BG = new Color(255, 245, 180);
    public static final Color SERVER_BUBBLE_FG = new Color(80, 80, 0);

    public static final Font FONT_HEADER_TITLE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_HEADER_INFO = new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONT_SENDER_NAME = new Font("SansSerif", Font.BOLD, 12);
    public static final Font FONT_MESSAGE_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_TIMESTAMP = new Font("SansSerif", Font.PLAIN, 10);
    public static final Font FONT_INPUT_FIELD = new Font("SansSerif", Font.PLAIN, 15);
    public static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 14);
}
