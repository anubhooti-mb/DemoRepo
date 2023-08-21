import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class UserRegistration {

    public void registerUser() {
        Scanner scanner = new Scanner(System.in);
        String userFile = "src/users.txt";

        try {
            System.out.println("Enter the number of users:");
            int numUsers = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < numUsers; i++) {
                System.out.println("Enter details for user " + (i + 1) + ":");
                System.out.print("Please enter your user id: ");
                String username = scanner.nextLine();
                System.out.print("Please enter your password: ");
                String password = scanner.nextLine();

                String hashedUserId = hashUserId(username);
                String hashedPassword = hashUserPassword(password);

                if (validateUserPresent(hashedUserId)) {
                    System.out.println("Duplicate user details provided");
                } else {
                    System.out.print("Please enter a security question: ");
                    String securityQuestion = scanner.nextLine();
                    System.out.print("Please enter answer to your security question: ");
                    String securityAnswer = scanner.nextLine();

                    FileWriter writer = new FileWriter(userFile, true);
                    writer.write(
                            hashedUserId + "::" + hashedPassword + "::" + securityQuestion + "::" + securityAnswer
                                    + "\n");
                    writer.close();
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    System.out.println("User registered successfully.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String hashUserId(String username) {
        return hashString(username);
    }

    public String hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public String hashUserPassword(String password) {
        return hashString(password);
    }

    public boolean validateUserPresent(String hashedUsername) {
        boolean userAvailable = false;
        try {
            String line;
            try (BufferedReader bufferReader = new BufferedReader(
                    new FileReader("src/users.txt"))) {
                while ((line = bufferReader.readLine()) != null) {
                    String[] allLines = line.split("\n");
                    for (String everyLine : allLines) {
                        String[] values = everyLine.split("\\::");
                        if (values[0].equals(hashedUsername)) {
                            userAvailable = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAvailable;
    }
}
