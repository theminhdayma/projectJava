package ra.edu.utils;

import java.io.*;

public class FileUtil {

//    String token = readFromFile();
//if (token != null && !token.isBlank()) {
//        String[] parts = token.split(":");
//        String role = parts[0];
//        String username = parts[1];
//
//        if (role.equals("admin")) {
//            System.out.println("\nDuy trì đăng nhập với quản trị viên: " + username);
//            AdminMain.displayMenuManagentAdmin();
//            return;
//        } else if (role.equals("candidate")) {
//            System.out.println("\nDuy trì đăng nhập với ứng viên: " + username);
//            // CandidateMain.displayMenuCandidate(); // ví dụ
//            return;
//        }
//    }


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
