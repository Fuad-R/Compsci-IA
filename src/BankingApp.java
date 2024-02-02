import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

class BankingApp {
    public static void main(String[] args) {
        // Load Database credentials
        String url = "jdbc:mysql://192.168.1.70:3306/BankingDB";
        String username = "root";
        String password = "Fuadr1234";
        Scanner scanner = new Scanner(System.in);
        String filler = "-------------------------------------------------------------------";

        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("Establishing permanent connection to the database...");

        // Establishing permanent connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("\u001B[32mDatabase connection established!\u001B[0m");

            // Perform database operations here
            // Clear the terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();
            
            // Welcome message
            System.out.println(filler);
            System.out.println("Welcome to the Banking App!");
            System.out.println(filler);

            // Ask for Username input
            System.out.print("Please enter your Username: ");
            String usernameInput = scanner.nextLine();

            System.out.print("Please enter your Password: ");
            String passwordInput = scanner.nextLine();

            // Check if the user exists
            String query = "SELECT * FROM UserData WHERE Username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usernameInput);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                System.out.println("User exists, signing you in now...");

                String dbPassword = resultSet.getString("Password");
                if (dbPassword.equals(passwordInput)) {
                    System.out.println("Password matched, signing you in...");
                    // Add this line to exit the loop
                    
                } else {
                    System.out.println("Incorrect password, please try again.");
                    
                    int attempts = 3; // Number of attempts allowed
                    while (attempts > 0) {
                        System.out.print("Please enter your Password: ");
                        passwordInput = scanner.nextLine();

                        if (dbPassword.equals(passwordInput)) {
                            System.out.println("Password matched, signing you in...");
                            break; // Add this line to exit the loop

                        } else {
                            attempts--;
                            System.out.println("Incorrect password, " + attempts + " attempts remaining.");
                            if (attempts == 0) {

                                // Clear the terminal
                                System.out.print("\033[H\033[2J");
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

            } else {
                System.out.print("User does not exist!, would you like to create an account? (\u001B[32mY\u001B[0m/\u001B[31mN\u001B[0m): ");
                String createAccount = scanner.nextLine();

                if (createAccount.equals("Y") || createAccount.equals("y")) {
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
                    System.out.println("Account created successfully!");

                } else {

                }
            
            }

            // Clear the terminal
            System.out.print("\033[H\033[2J");
            System.out.flush();

            int action = 0;

            System.out.println(filler);
            System.out.println("Welcome to your dashboard, " + usernameInput + "! What would you like to do today?");
            System.out.println(filler);

            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Manage Account");
            System.out.println("6. Exit");
            
            System.out.print("Please enter the number of the action you would like to perform: ");
            action = scanner.nextInt();

            switch (action) {
                case 1:
                    // Pull balance from database
                    System.out.println("View Balance");
                    String balanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
                    balanceStatement.setString(1, usernameInput);
                    ResultSet balanceResult = balanceStatement.executeQuery();


                    // Print balance
                    if (balanceResult.next()) {
                        double balance = balanceResult.getDouble("Balance");
                        System.out.println("Your current balance is: $" + balance);
                        System.out.println("Press enter to return to the dashboard.");
                        String confirm = scanner.nextLine();
                        

                    } else {
                        System.out.println("Failed to retrieve balance.");
                    }

                    balanceResult.close();
                    balanceStatement.close();
                    break;
                case 2:
                    System.out.println("Deposit");
                    break;
                case 3:
                    System.out.println("Withdraw");
                    break;
                case 4:
                    System.out.println("Transfer");
                    break;
                case 5:
                    System.out.println("Manage Account");
                    break;
                case 6:
                    System.out.println("Exit");
                    break;
                default:
                    System.out.println("Invalid action, returning to dashboard.");
                    break;
            }

            //Close remaining operations
            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {

            System.out.println("\u001B[31mConnection failed! Check output console\u001B[0m");
            e.printStackTrace();

            return;
        }
        //Close scanner
        scanner.close();
        
    }
    
}