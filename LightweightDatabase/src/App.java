import java.io.File;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        UserRegistration userRegister = new UserRegistration();
        userRegister.registerUser();
        Login login = new Login();
        login.loginUser();

        String isQuit = "";
        while (!isQuit.equals("exit")) {
            System.out.print("Enter SQL query (or type 'exit' to quit): ");
            isQuit = ExecuteQueries.runQueries();
        }
    }

}

// CREATE TABLE person (id INT, name VARCHAR(50), age INT)
// INSERT INTO person (id, name, age) VALUES (1, 'Anu, 29)
// Select * from person where age>18
// DELETE FROM person WHERE name = 'Anu'
// UPDATE person SET name = 'Anubhooti' WHERE name ='Anu'
