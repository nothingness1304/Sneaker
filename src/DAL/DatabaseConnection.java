package DAL;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {

    }

    public void connectToDatabase() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLBG;encrypt=false";
        String username = "sa";
        String password = "1304";
        connection = java.sql.DriverManager.getConnection(url, username, password);
        System.out.println("Kết nối đến SQL Server thành công!");
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
