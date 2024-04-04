package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private static final Logger log = LoggerFactory.getLogger(UserManager.class);

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
            log.info("User registered successfully with username: {}", username);
        } catch (SQLException e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            throw e;
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
            boolean userExists = rs.next();
            if (userExists) {
                log.info("User login successful for username: {}", username);
            } else {
                log.info("User login failed for username: {}", username);
            }
            return userExists;
        } catch (SQLException e) {
            log.error("Error logging in user: {}", e.getMessage(), e);
            throw e;
        }
    }

    public static String[] findUsernameAndPassword(String hintQuestion, String hintAnswer) throws SQLException {
        String sql = "SELECT username, \"password_hash\" FROM User WHERE hint_question = ? AND hint_answer = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hintQuestion);
            pstmt.setString(2, hintAnswer);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                String passwordHash = rs.getString("password_hash");
                log.info("Username and password hash found for hint question and answer");
                return new String[]{username, passwordHash};
            } else {
                log.info("No user found for given hint question and answer");
                return null;
            }
        } catch (SQLException e) {
            log.error("Error finding username and password: {}", e.getMessage(), e);
            throw e;
        }
    }

    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error hashing password: {}", e.getMessage(), e);
            throw new IllegalStateException("SHA-256 algorithm not available.", e);
        }
    }
}

