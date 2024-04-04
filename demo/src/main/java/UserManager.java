import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {

    public static void registerUser(String username, String password, String email, String hintQuestion, String hintAnswer) throws SQLException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        String sql = "INSERT INTO User (username, password_hash, email, hint_question, hint_answer) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, hintQuestion);
            pstmt.setString(5, hintAnswer);
            pstmt.executeUpdate();
            System.out.println("User registered successfully.");
        }
    }

    public static boolean loginUser(String username, String password) throws SQLException, NoSuchAlgorithmException {
        String hashedPassword = hashPassword(password);
        String sql = "SELECT * FROM User WHERE username = ? AND password_hash = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    public static String[] findUsernameAndPassword(String hintQuestion, String hintAnswer) throws SQLException {
        String sql = "SELECT username, password_hash FROM User WHERE hint_question = ? AND hint_answer = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hintQuestion);
            pstmt.setString(2, hintAnswer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                return new String[] { username, passwordHash };
            }
        }
        return null;
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }
}

