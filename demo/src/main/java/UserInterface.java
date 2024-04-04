import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("1. Register User");
        System.out.println("2. Login User");
        System.out.println("3. Find Username and Password");
        System.out.println("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                registerUser();
                break;
            case 2:
                loginUser();
                break;
            case 3:
                findUsernameAndPassword();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void registerUser() {
        System.out.println("Enter username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();
        System.out.println("Enter email:");
        String email = scanner.next();
        System.out.println("Enter hint question:");
        String hintQuestion = scanner.next();
        System.out.println("Enter hint answer:");
        String hintAnswer = scanner.next();

        try {
            UserManager.registerUser(username, password, email, hintQuestion, hintAnswer);
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

    private static void loginUser() {
        System.out.println("Enter username:");
        String username = scanner.next();
        System.out.println("Enter password:");
        String password = scanner.next();

        try {
            boolean loggedIn = UserManager.loginUser(username, password);
            if (loggedIn) {
                System.out.println("Login successful.");
            } else {
                System.out.println("Login failed. Invalid username or password.");
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
    }

    private static void findUsernameAndPassword() {
        System.out.println("Enter hint question:");
        String hintQuestion = scanner.next();
        System.out.println("Enter hint answer:");
        String hintAnswer = scanner.next();

        try {
            String[] usernameAndPassword = UserManager.findUsernameAndPassword(hintQuestion, hintAnswer);
            if (usernameAndPassword != null) {
                System.out.println("Username: " + usernameAndPassword[0]);
                System.out.println("Password Hash: " + usernameAndPassword[1]);
            } else {
                System.out.println("No user found with provided hint question and answer.");
            }
        } catch (SQLException e) {
            System.err.println("Error finding username and password: " + e.getMessage());
        }
    }
}
