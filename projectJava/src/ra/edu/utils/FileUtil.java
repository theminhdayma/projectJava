package ra.edu.utils;

import java.io.*;

public class FileUtil {

    private static final String FILE_NAME = "text.txt";

    public static void writeToFile(String content) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Không thể ghi vào file: " + FILE_NAME);
        }
    }

    public static String readFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            return br.readLine();
        } catch (IOException e) {
            return null;
        }
    }

}
