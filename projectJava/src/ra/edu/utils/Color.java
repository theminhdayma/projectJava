package ra.edu.utils;

public class Color {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String BOLD = "\u001B[1m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String ITALIC = "\u001B[3m";
    public static final String STRIKETHROUGH = "\u001B[9m";

    public static final int WIDTH = 60;
    public static String padRight(String text, int length) {
        if (text == null) return " ".repeat(length);
        int realLength = stripColor(text).length();
        return realLength >= length ? text : text + " ".repeat(length - realLength);
    }

    public static String center(String text, int width) {
        String plain = stripColor(text);
        int padSize = (width - plain.length()) / 2;
        int extra = (width - plain.length()) % 2;
        return " ".repeat(padSize) + text + " ".repeat(padSize + extra);
    }

    public static String stripColor(String text) {
        return text.replaceAll("\u001B\\[[;\\d]*m", "");
    }


    public static String repeat(String str, int count) {
        return str.repeat(Math.max(0, count));
    }
}