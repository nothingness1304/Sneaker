package Service;

import DAL.DatabaseConnection;
import java.sql.Connection;
import Model.ModelUser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;


public class ServiceUser {
    private final Connection con;
    
   public ServiceUser() {
       con = DatabaseConnection.getInstance().getConnection();
    }

    public void insertUser(ModelUser user) throws SQLException {
        String query = "INSERT INTO [user] (UserName, Email, [Password], VerifyCode) OUTPUT INSERTED.UserID VALUES (?, ?, ?, ?)";
        try (PreparedStatement p = con.prepareStatement(query)) {
            String code = generateVerifyCode();
            p.setString(1, user.getUserName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword());
            p.setString(4, code);
            ResultSet r = p.executeQuery();
            if (r.next()) {
                int userID = r.getInt(1);
                user.setUserID(userID);
                user.setVerifyCode(code);
            }
            r.close();
        }
    }

    private String generateVerifyCode() throws SQLException {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code = df.format(ran.nextInt(1000000));  // Random from 0 to 999999
        while (checkDuplicateCode(code)) {
            code = df.format(ran.nextInt(1000000));
        }
        return code;
    }

    private boolean checkDuplicateCode(String code) throws SQLException {
        String query = "SELECT UserID FROM [user] WHERE VerifyCode=? FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, code);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public boolean checkDuplicateUser(String user) throws SQLException {
        String query = "SELECT UserID FROM [user] WHERE UserName=? AND [Status]='Verified' FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, user);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public boolean checkDuplicateEmail(String email) throws SQLException {
        String query = "SELECT UserID FROM [user] WHERE Email=? AND [Status]='Verified' FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public void doneVerify(int userID) throws SQLException {
        String query = "UPDATE [user] SET VerifyCode='', [Status]='Verified' WHERE UserID=? FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, userID);
            p.executeUpdate();
        }
    }

    public boolean verifyCodeWithUser(int userID, String code) throws SQLException {
        String query = "SELECT UserID FROM [user] WHERE UserID=? AND VerifyCode=? FETCH FIRST 1 ROWS ONLY";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, userID);
            p.setString(2, code);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }
}
