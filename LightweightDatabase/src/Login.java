import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Login {
    static Scanner sc = new Scanner(System.in);
    String userFile = "src/users.txt";
    UserRegistration userRegistration = new UserRegistration();

    public void loginUser() {
        try {
            System.out.print("Please enter your user id: ");
            String userId = sc.nextLine();
            String hashUserId = userRegistration.hashUserId(userId);
            if (userRegistration.validateUserPresent(hashUserId)) {
                System.out.print("Please enter your password: ");
                String userPassword = sc.nextLine();
                String hashPassword = userRegistration.hashUserPassword(userPassword);
                boolean validateUser = validateUser(hashUserId, hashPassword);
                if (validateUser) {
                    if (validateSecurityAnswer(hashUserId)) {
                        System.out.println("User logged in successfully!");
                    } else {
                        System.out.println("Please enter the correct security answer!");
                    }
                } else {
                    System.out.println("User Id or password entered is incorrect");
                }
            } else {
                System.out.println("User is not present in the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateUser(String username, String password) {
        boolean validUser = false;
        try {
            String eachLine = "";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
            while ((eachLine = bufferedReader.readLine()) != null) {
                String[] allLines = eachLine.split("\n");
                for (String everyLine : allLines) {
                    String[] values = everyLine.split("\\::");
                    if (values[0].equals(username)) {
                        if (values[1].equals(password)) {
                            validUser = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return validUser;
    }

    public boolean validateSecurityAnswer(String username) {
        boolean validSecurityAnswer = false;
        String eachLine = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userFile));
            while ((eachLine = bufferedReader.readLine()) != null) {
                String[] allLines = eachLine.split("\n");
                for (String everyLine : allLines) {
                    String[] values = everyLine.split("\\::");
                    if (values[0].equals(username)) {
                        System.out.println("Your security questions is: '" + values[2] + "'");
                        System.out.print("Please enter a valid security answer: ");
                        String securityAnswer = sc.nextLine();
                        if (values[3].equals(securityAnswer)) {
                            validSecurityAnswer = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return validSecurityAnswer;
    }
}
