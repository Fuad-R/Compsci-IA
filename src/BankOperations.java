import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BankOperations extends BankingApp{


    // Load Database credentials
        static String url = "jdbc:mysql://192.168.1.70:3306/BankingDB";
        static String username = "";
        static String password = "";

    public BankOperations() {

        // Load credentials from properties file
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fis);

            username = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            System.out.println("Error loading database credentials");
            e.printStackTrace();
            return;
        }

    }

    public static Double checkBalance(String usernameInput) {

        Double balance = 0.0;
        
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
        
            System.out.println("View Balance");
            String balanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
            PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
            balanceStatement.setString(1, usernameInput);
            ResultSet balanceResult = balanceStatement.executeQuery();

            if (balanceResult.next()) {
                balance = balanceResult.getDouble("Balance");
            }

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();

        }

        return balance;
    
    }

    public static void deposit() {

        // Add deposit method here

    }

    public static void withdraw() {

        // Add withdraw method here

    }

    public static void transfer() {

        // Add transfer method here

    }

    public static void manageaccount() {

        // Add account managment method here

    } 

}
