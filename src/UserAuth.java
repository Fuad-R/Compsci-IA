import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UserAuth extends BankingApp {
        
    public static void loginmethod(boolean isloggedin) {
        Scanner scanner = new Scanner(System.in);

        boolean authed = false;
        
        // Load Database credentials
        String url = "jdbc:mysql://192.168.1.70:3306/BankingDB";
        String username = "";
        String password = "";

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

        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            scanner.close();
            return;
        }

        System.out.println("Establishing auth connection...");

        // Establishing permanent connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("\u001B[32mDatabase connection established!\u001B[0m");

        while (authed == false) {
        
        // Clear the terminal
        System.out.print(clear);
        System.out.flush();           

        // Display login screen
        System.out.println(filler);
        System.out.println("Welcome to the Banking App! Would you like to login or create an account?");
        System.out.println(filler);

        System.out.println("1. Login");
        System.out.println("2. Create Account");

        System.out.println();
        System.out.print("Please enter the number of the action you would like to perform: ");
        
        int userChoice = scanner.nextInt();

        // Clear the terminal
        System.out.print(clear);
        System.out.flush();

        switch (userChoice) {

            case 1: // Login

            // Ask for username input
            System.out.print("Please enter your username: ");
            String usernameInput = scanner.next();
            
            // Check if the user exists
            String query = "SELECT * FROM UserData WHERE Username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usernameInput);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("User exists, please enter your password to login: ");

                // Ask for password input
                System.out.print("Please enter your password: ");
                String passwordInput = scanner.next();

                String dbPassword = resultSet.getString("Password");
                if (dbPassword.equals(passwordInput)) {
                    System.out.println("Password matched, signing you in...");
                    authed = true;

                    break;
                
                } else {
                    System.out.println("Incorrect password, please try again.");
                    
                    int attempts = 3; // Number of attempts allowed
                    while (attempts > 0) {
                        System.out.print("Please enter your Password: ");
                        passwordInput = scanner.nextLine();

                        if (dbPassword.equals(passwordInput)) {
                            System.out.println("Password matched, signing you in...");
                            authed = true;

                            break; 

                        } else {
                            attempts--;
                            System.out.println("Incorrect password, " + attempts + " attempts remaining.");
                            if (attempts == 0) {

                                // Clear the terminal
                                System.out.print(clear);
                                System.out.flush();

                                System.out.println(filler);
                                System.out.println("You have exceeded the maximum number of attempts.");
                                System.out.println("Please try again later, or contact support if you believe this is a mistake.");
                                System.out.println(filler);

                                return;
                            }
                        }
                    }

                }
             
                
                
            }
 
            case 2: // Create Account

            System.out.print("Please enter your Username: ");
                    String newUsername = scanner.nextLine();
                    System.out.println("Your username was set to " + newUsername);
                    System.out.print("Please enter your password: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Please enter your password again to confirm: ");
                    String passwordConfirm = scanner.nextLine();

                    boolean passwordMatch = newPassword.equals(passwordConfirm);

                    while (!passwordMatch) {
                        System.out.println("Passwords do not match, please try again");
                        System.out.print("Please enter your password: ");
                        newPassword = scanner.nextLine();
                        System.out.print("Please enter your password again to confirm: ");
                        passwordConfirm = scanner.nextLine();
                        passwordMatch = newPassword.equals(passwordConfirm);
                    }
                    
                    String insertQuery = "INSERT INTO UserData (Username, Password) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, newUsername);
                    insertStatement.setString(2, newPassword);
                    insertStatement.executeUpdate();
                    System.out.println("Account created successfully, signing you in now!");
                    authed = true;


        }

        } // End of authed while loop

        isloggedin = true;

        } catch (SQLException e) {

            System.out.println("\u001B[31mConnection failed! Check output console\u001B[0m");
            e.printStackTrace();

            scanner.close();
            return;
        }

        
    
    }
}