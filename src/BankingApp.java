import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class BankingApp {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://192.168.1.70:3306/BankingDB";
        String username = "root";
        String password = "Fuadr1234";

        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        // Establishing connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
            // Perform database operations here
            // ...
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check output console");
            e.printStackTrace();
            return;
        }
    }
}