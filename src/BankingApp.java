import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
public class BankingApp {

    //universal variables
    public static final String FILLER = "-------------------------------------------------------------------";
    public static final String CLEAR = "\033[H\033[2J";

    public static void main(String[] args) {
        // Load Database credentials
        String url = "jdbc:mysql://192.168.1.70:3306/BankingDB";
        String username = "";
        String password = "";

        // Define common strings to avoid repetition
        String exitMsg = "Exiting now, goodbye.";

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

        Scanner scanner = new Scanner(System.in);

        // Load MySQL JDBC Driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path ");
            e.printStackTrace();
            scanner.close();
            return;
        }

        System.out.println("Establishing permanent connection to the database...");

        // Establishing permanent connection
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("\u001B[32mDatabase connection established!\u001B[0m");

            // Clear the terminal
            System.out.print(CLEAR);
            System.out.flush();
          
            // Check if user is logged in
            boolean loggedInStatus = false;
            while (!loggedInStatus) {

            // Call UserAuth class to display login dashboard
            UserAuth.loginmethod();

            loggedInStatus = UserAuth.isLoggedIn();

            }
            String usernameInput = UserAuth.getUsername();
            int exitDash = 1;
            
            // Display dashboard

        while (exitDash == 1) {
            
            int action = 0;

            // Call the displayUserDashboard method
            AppDashboard.displayUserDashboard(usernameInput);

            // Ask for action
            action = scanner.nextInt();

            // Clear terminal
            System.out.print(CLEAR);
            System.out.flush();

            switch (action) {
                case 1: // View Balance
                    
                    // Call the checkBalance method
                    double returnedbalance = BankOperations.checkBalance(usernameInput);
                                    
                    System.out.println(FILLER);
                    System.out.println("Your current balance is: $" + returnedbalance);
                    System.out.println(FILLER);
                        
                    
                    exitDash = AppDashboard.displayReturnDashboard();
                    
                    if (exitDash == 2) {
                        System.out.println(exitMsg);
                        break;
                    }

                    break;

                case 2: // Deposit

                // Call the deposit method
                BankOperations.deposit(usernameInput);

                exitDash = AppDashboard.displayReturnDashboard();
                    
                if (exitDash == 2) {
                    System.out.println(exitMsg);
                    break;
                }
                   
                break;

                case 3: // Withdraw

                    // Call the withdraw method
                    BankOperations.withdraw(usernameInput);

                    exitDash = AppDashboard.displayReturnDashboard();
                    
                    if (exitDash == 2) {
                        System.out.println(exitMsg);
                        break;
                    }

                    break;
                case 4: // Transfer

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    // Print transfer message
                    System.out.println(FILLER);
                    System.out.println("Transfer Menu");
                    System.out.println("Please note that a 1% fee will be charged for each transfer.");
                    System.out.println(FILLER);


                    // Ask for transfer amount
                    System.out.print("Please enter the amount you would like to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    System.out.println();

                    // Ask for recipient username
                    System.out.print("Please enter the recipient's username: ");
                    String recipientUsername = scanner.nextLine();
                    recipientUsername = scanner.nextLine(); // No idea why this is needed, but it is
                    System.out.println();

                    // Print transfer info message
                    System.out.println("The total fee for this transfer will be: $" + transferAmount * 0.01);
                    System.out.println("The total amount to be deducted from your account will be: $" + transferAmount * 1.01);
                    System.out.print("Please confirm the transfer by entering \u001B[32mY\u001B[0m or \u001B[31mN\u001B[0m: ");
                    String confirmTransfer = scanner.nextLine();

                    if (confirmTransfer.equals("N") || confirmTransfer.equals("n")) {
                        System.out.println("Transfer cancelled, returning to dashboard.");
                        break;
                    } else {

                    // Clear terminal
                    System.out.println(CLEAR);
                    System.out.flush();

                    // Check if balance is sufficient
                    String checkBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement checkBalanceStatement = connection.prepareStatement(checkBalanceQuery);
                    checkBalanceStatement.setString(1, usernameInput);
                    ResultSet checkBalanceResult = checkBalanceStatement.executeQuery();

                    if (checkBalanceResult.next()) {
                        double balance = checkBalanceResult.getDouble("Balance");
                        if (balance < transferAmount * 1.01) {
                            System.out.println(FILLER);
                            System.out.println("Insufficient funds, transfer cancelled.");
                            System.out.println(FILLER);
                            break;
                        }
                    } else {
                        System.out.println("Failed to retrieve balance.");
                    }

                    // Check if recipient exists
                    String checkRecipientQuery = "SELECT * FROM UserData WHERE Username = ?";
                    PreparedStatement checkRecipientStatement = connection.prepareStatement(checkRecipientQuery);
                    checkRecipientStatement.setString(1, recipientUsername);
                    ResultSet checkRecipientResult = checkRecipientStatement.executeQuery();

                    if (!checkRecipientResult.next()) {
                        System.out.println(FILLER);
                        System.out.println("Recipient does not exist, transfer cancelled.");
                        System.out.println(FILLER);
                        break;
                    }

                    System.out.println(FILLER);
                    System.out.println("Transfer confirmed, processing now...");}
                    System.out.println(FILLER);

                    // Update balance in database with 1% fee
                    double transferAmountWithFee = transferAmount * 1.01; // Add 1% fee
                    String transferQuery = "UPDATE UserData SET Balance = Balance - ? WHERE Username = ?";
                    PreparedStatement transferStatement = connection.prepareStatement(transferQuery);
                    transferStatement.setDouble(1, transferAmountWithFee);
                    transferStatement.setString(2, usernameInput);
                    transferStatement.executeUpdate();

                    // Update recipient balance in database
                    String recipientQuery = "UPDATE UserData SET Balance = Balance + ? WHERE Username = ?";
                    PreparedStatement recipientStatement = connection.prepareStatement(recipientQuery);
                    recipientStatement.setDouble(1, transferAmount);
                    recipientStatement.setString(2, recipientUsername);
                    recipientStatement.executeUpdate();

                    // Confirm transfer
                    System.out.println("Transfer of $" + transferAmount + " to " + recipientUsername + " was successful!");
                    System.out.println();

                    // Pull new balance to print
                    String newTransferQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement newTransferStatement = connection.prepareStatement(newTransferQuery);
                    newTransferStatement.setString(1, usernameInput);
                    ResultSet newTransferResult = newTransferStatement.executeQuery();

                    // Print new balance
                    if (newTransferResult.next()) {
                        double newBalance = newTransferResult.getDouble("Balance");
                        System.out.println("Your new balance is: $" + newBalance);
                    } else {
                        System.out.println("Failed to retrieve new balance.");
                    }

                     // Decide what to do next
                     System.out.println();
                     System.out.println(FILLER);
                     System.out.println();
                     System.out.print("Type 1 to return to the dashboard, or 2 to exit: ");
 
                     exitDash = scanner.nextInt();
                     if (exitDash == 2) {
                         System.out.println(exitMsg);
                         break;
                     }

                    break;
                case 5: // Manage Account
                    System.out.println("Manage Account");

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    System.out.println(FILLER);
                    System.out.println("Account Management Menu");
                    System.out.println(FILLER);

                    System.out.println("1. Change Password");
                    System.out.println("2. Delete Account");
                    System.out.println("3. Return to Dashboard");
                    System.out.println();
                    System.out.print("Please enter the number of the action you would like to perform: ");
                    int accountAction = scanner.nextInt();

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    switch (accountAction) {
                        case 1:
                            System.out.println(FILLER);
                            System.out.println("Password Change Menu");
                            System.out.println(FILLER);
                            System.out.println();

                            // Ask for old password
                            System.out.print("Please enter your old password: ");
                            String oldPassword = scanner.nextLine();
                            oldPassword = scanner.nextLine(); // Once again no idea why this is needed, but it is :(

                            // Verify old password
                            String verifyQuery = "SELECT Password FROM UserData WHERE Username = ?";
                            PreparedStatement verifyStatement = connection.prepareStatement(verifyQuery);
                            verifyStatement.setString(1, usernameInput);
                            ResultSet verifyResult = verifyStatement.executeQuery();

                            if (verifyResult.next()) {
                                String storedPassword = verifyResult.getString("Password");
                                if (!storedPassword.equals(oldPassword)) {

                                    // Clear terminal
                                    System.out.print(CLEAR);
                                    System.out.flush();

                                    // Print error message
                                    System.out.println("Incorrect old password. Password change cancelled.");
                                    System.out.print("Press enter to return to dashboard:");
                                    scanner.nextLine();

                                    break;
                                }
                            } else {
                                System.out.println("Failed to retrieve old password. Password change cancelled.");
                                break;
                            }

                            System.out.println();
                            System.out.print("Please enter your new password: ");
                            String newPassword = scanner.nextLine();
                            newPassword = scanner.nextLine(); // Same as before, again...

                            System.out.println();
                            System.out.print("Please enter your new password again to confirm: ");
                            String passwordConfirm = scanner.nextLine();
                            passwordConfirm = scanner.nextLine(); // Same here lol (4th time now)
                            System.out.println();

                            boolean passwordMatch = newPassword.equals(passwordConfirm);

                            while (!passwordMatch) {
                                System.out.println("Passwords do not match, please try again");
                                System.out.print("Please enter your new password: ");
                                newPassword = scanner.nextLine();
                                System.out.print("Please enter your new password again to confirm: ");
                                passwordConfirm = scanner.nextLine();
                                passwordMatch = newPassword.equals(passwordConfirm);
                            }

                            // Update password in database
                            String passwordQuery = "UPDATE UserData SET Password = ? WHERE Username = ?";
                            PreparedStatement passwordStatement = connection.prepareStatement(passwordQuery);
                            passwordStatement.setString(1, newPassword);
                            passwordStatement.setString(2, usernameInput);
                            passwordStatement.executeUpdate();

                            System.out.println("Password changed successfully!");

                            break;
                        case 2:
                            System.out.println("Delete Account");

                            // Ask for confirmation
                            System.out.print("Are you sure you want to delete your account? (\u001B[32mY\u001B[0m/\u001B[31mN\u001B[0m): ");
                            String deleteAccount = scanner.nextLine();
                            deleteAccount = scanner.nextLine(); // No idea why this is needed, but it is

                            if (deleteAccount.equals("Y") || deleteAccount.equals("y")) {
                                // Delete account from database
                                String deleteQuery = "DELETE FROM UserData WHERE Username = ?";
                                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                                deleteStatement.setString(1, usernameInput);
                                deleteStatement.executeUpdate();

                                System.out.println("Account deleted successfully!");
                                exitDash = 2;
                            } else {
                                System.out.println("Account deletion cancelled, returning to dashboard.");
                            }

                            break;
                        case 3:
                            System.out.println("Returning to dashboard...");
                            break;
                        default:
                            System.out.println("Invalid action, returning to dashboard.");
                            break;
                    }

                    break;
                case 6: // Exit
                    System.out.println(exitMsg);

                    exitDash = 2;

                    break;
                default:// Invalid action
                    System.out.println("Invalid action, returning to dashboard.");
                    break;
            }
            
        }

            connection.close();

        } catch (SQLException e) {

            System.out.println("\u001B[31mConnection failed! Check output console\u001B[0m");
            e.printStackTrace();

            scanner.close();
            return;
        }
        //Close scanner
        scanner.close();
        
    }
    
}