import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Scanner;
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

    public static void deposit(String usernameInput) {

        Scanner scanner = new Scanner(System.in);

        try (FileInputStream fis = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fis);

            username = props.getProperty("username");
            password = props.getProperty("password");
        } catch (IOException e) {
            System.out.println("Error loading database credentials");
            e.printStackTrace();
        }

        try {
        Connection connection2 = DriverManager.getConnection(url, username, password);

        // Clear terminal
        System.out.print(clear);
        System.out.flush();

        System.out.println(filler);
        System.out.println("Depositing Menu");
        System.out.println(filler);

        // Ask for deposit amount
        System.out.print("Please enter the amount you would like to deposit: ");
        double depositAmount = scanner.nextDouble();

        scanner.close();

        // Update balance in database
        String depositQuery = "UPDATE UserData SET Balance = Balance + ? WHERE Username = ?";
        PreparedStatement depositStatement = connection2.prepareStatement(depositQuery);
        depositStatement.setDouble(1, depositAmount);
        depositStatement.setString(2, usernameInput);
        depositStatement.executeUpdate();

        // Pull new balance to print
        String newBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
        PreparedStatement newBalanceStatement = connection2.prepareStatement(newBalanceQuery);
        newBalanceStatement.setString(1, usernameInput);
        ResultSet newBalanceResult = newBalanceStatement.executeQuery();
        
        System.out.println();

        // Print new balance
        if (newBalanceResult.next()) {
            double newBalance = newBalanceResult.getDouble("Balance");

            // Clear terminal
            System.out.print(clear);
            System.out.flush();

            System.out.println(filler);
            System.out.println("Deposit successful! You deposited: $" + depositAmount);
            System.out.println("Your new balance is: $" + newBalance);
            System.out.println(filler);

        } else {
            System.out.println("Failed to retrieve new balance.");
        }

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

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
