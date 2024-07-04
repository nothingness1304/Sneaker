package Service;

import DAL.DatabaseConnection;
import Model.ModelUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.text.DecimalFormat;

public class ServiceUser {
    private final Connection con;
    private final Map<String, String> verificationCodes;

    public ServiceUser() {
        con = DatabaseConnection.getInstance().getConnection();
        verificationCodes = new HashMap<>();
    }

    public void registerUser(ModelUser user) throws SQLException {
        if (checkDuplicateUser(user.getUserName()) || checkDuplicateEmail(user.getEmail())) {
            throw new SQLException("Username or Email already exists.");
        }

        String verificationCode = generateVerificationCode();
        verificationCodes.put(user.getEmail(), verificationCode);
        sendVerificationEmail(user.getEmail(), verificationCode);
    }

    public boolean verifyUser(ModelUser user, String verificationCode) throws SQLException {
        String storedCode = verificationCodes.get(user.getEmail());

        if (storedCode != null && storedCode.equals(verificationCode)) {
            insertUserToAccount(user);
            verificationCodes.remove(user.getEmail());
            return true;
        } else {
            return false;
        }
    }
    

    private void insertUserToAccount(ModelUser user) throws SQLException {
        String query = "INSERT INTO Account (UserName, Email, Password) OUTPUT INSERTED.MaQuanTriVien VALUES (?, ?, ?)";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, user.getUserName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword());
            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    int userID = r.getInt(1);
                    user.setUserID(userID);
                }
            }
        }
    }

    private String generateVerificationCode() {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        return df.format(ran.nextInt(1000000));  // Random từ 0 đến 999999
    }

    public boolean checkDuplicateUser(String userName) throws SQLException {
        String query = "SELECT TOP 1 MaQuanTriVien FROM Account WHERE UserName = ?";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, userName);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public boolean checkDuplicateEmail(String email) throws SQLException {
        String query = "SELECT TOP 1 MaQuanTriVien FROM Account WHERE Email = ?";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    private void sendVerificationEmail(String email, String code) {
        ServiceEmail serviceEmail = new ServiceEmail();
        serviceEmail.sendVerificationEmail(email, code);
    }
    
    public boolean loginUser(String email, String password) throws SQLException {
        String query = "SELECT * FROM Account WHERE Email = ? AND Password = ?";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, email);
            p.setString(2, password);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next();
            }
        }
    }
}

