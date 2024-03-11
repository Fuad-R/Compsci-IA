import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;

import com.password4j.Hash;
import com.password4j.Password;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class BankOperations extends BankingApp{

    public static Connection getConnection() {
        Connection connection = null;
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fis);

            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");

            connection = DriverManager.getConnection(url, username, password);
        } catch (IOException | SQLException e) {
            System.out.println("Error loading database credentials");
            e.printStackTrace();
        }
        return connection;
    }
        

    public static void checkBalance(String usernameInput) {

        Double balance = 0.0;

        try {
            Connection connection = getConnection();
        
            ResultSet balanceResult = pullBalance(usernameInput);

            if (balanceResult.next()) {
                balance = balanceResult.getDouble("Balance");
            }

            System.out.println(FILLER);
            System.out.println("Your current balance is: $" + balance);
            System.out.println(FILLER);

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();

        }

        return;
    
    }

    public static void deposit(String usernameInput) {

        Scanner scanner = new Scanner(System.in);

        try {
        Connection connection2 = getConnection();

        System.out.println(FILLER);
        System.out.println("Depositing Menu");
        System.out.println(FILLER);

        // Ask for deposit amount
        System.out.print("Please enter the amount you would like to deposit: ");
        double depositAmount = scanner.nextDouble();

        // Update balance in database
        String depositQuery = "UPDATE UserData SET Balance = Balance + ? WHERE Username = ?";
        PreparedStatement depositStatement = connection2.prepareStatement(depositQuery);
        depositStatement.setDouble(1, depositAmount);
        depositStatement.setString(2, usernameInput);
        depositStatement.executeUpdate();

        // Pull new balance to print
        ResultSet newBalanceResult = pullBalance(usernameInput);
        
        System.out.println();

        // Print new balance
        if (newBalanceResult.next()) {
            double newBalance = newBalanceResult.getDouble("Balance");

            AppDashboard.clearTerminal();

            // Log transaction
            TransactionLogger.logTransaction(usernameInput, usernameInput, "Deposit", depositAmount);
            

            System.out.println(FILLER);
            System.out.println("Deposit successful! You deposited: $" + depositAmount);
            System.out.println("Your new balance is: $" + newBalance);
            System.out.println(FILLER);

        } else {
            System.out.println("Failed to retrieve new balance.");
        }

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

    }

    public static void withdraw(String usernameInput) {

        Scanner scanner = new Scanner(System.in);

        try {
        Connection connection3 = getConnection();
        
        // Print withdraw menu message
        System.out.println(FILLER);
        System.out.println("Withdraw Menu");
        System.out.println(FILLER);

        // Ask for withdraw amount
        System.out.print("Please enter the amount you would like to withdraw: ");
        double withdrawAmount = scanner.nextDouble();

        //! *****************************************************
        //! ****** Add check for sufficient funds here **********
        //! *****************************************************

        // Update balance in database
        String withdrawQuery = "UPDATE UserData SET Balance = Balance - ? WHERE Username = ?";
        PreparedStatement withdrawStatement = connection3.prepareStatement(withdrawQuery);
        withdrawStatement.setDouble(1, withdrawAmount);
        withdrawStatement.setString(2, usernameInput);
        withdrawStatement.executeUpdate();

        // Pull new balance to print
        ResultSet newWithdrawResult = pullBalance(usernameInput);

        // Print new balance
        if (newWithdrawResult.next()) {
            double newBalance = newWithdrawResult.getDouble("Balance");

            AppDashboard.clearTerminal();

            System.out.println(FILLER);
            System.out.println("Withdrawal successful! You withdrew: $" + withdrawAmount);
            System.out.println("Your new balance is: $" + newBalance);
            System.out.println(FILLER);

            // Log transaction
            TransactionLogger.logTransaction(usernameInput, usernameInput, "Withdraw", withdrawAmount);

        } else {
            System.out.println("Failed to retrieve new balance.");
        }

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

    }

    public static void transfer(String usernameInput) {

        boolean transfercomplete = false;
        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection5 = getConnection();           

        while (!transfercomplete) {
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

                AppDashboard.clearTerminal();

                // Print transfer info message
                System.out.println(FILLER);
                System.out.println("The total fee for this transfer will be: $" + transferAmount * 0.01);
                System.out.println("The total amount to be deducted from your account will be: $" + transferAmount * 1.01);
                System.out.println(FILLER);
                System.out.print("Please confirm the transfer by entering \u001B[32mY\u001B[0m or \u001B[31mN\u001B[0m: ");
                String confirmTransfer = scanner.nextLine();

                if (confirmTransfer.equals("N") || confirmTransfer.equals("n")) {

                    AppDashboard.clearTerminal();

                    System.out.println("Transfer cancelled, returning to dashboard.");
                    break;
                } else {

                AppDashboard.clearTerminal();

                // Check if balance is sufficient
                String checkBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                PreparedStatement checkBalanceStatement = connection5.prepareStatement(checkBalanceQuery);
                checkBalanceStatement.setString(1, usernameInput);
                ResultSet checkBalanceResult = checkBalanceStatement.executeQuery();

                if (checkBalanceResult.next()) {
                    double balance = checkBalanceResult.getDouble("Balance");
                    if (balance < transferAmount * 1.01) {

                        AppDashboard.clearTerminal();

                        System.out.println(FILLER);
                        System.out.println("Insufficient funds, transfer cancelled.");
                        System.out.println(FILLER);
                        break;
                    }
                } else {

                    AppDashboard.clearTerminal();

                    System.out.println("Failed to retrieve balance.");
                }

                // Check if recipient exists
                String checkRecipientQuery = "SELECT * FROM UserData WHERE Username = ?";
                PreparedStatement checkRecipientStatement = connection5.prepareStatement(checkRecipientQuery);
                checkRecipientStatement.setString(1, recipientUsername);
                ResultSet checkRecipientResult = checkRecipientStatement.executeQuery();

                if (!checkRecipientResult.next()) {

                    AppDashboard.clearTerminal();

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
                PreparedStatement transferStatement = connection5.prepareStatement(transferQuery);
                transferStatement.setDouble(1, transferAmountWithFee);
                transferStatement.setString(2, usernameInput);
                transferStatement.executeUpdate();

                // Update recipient balance in database
                String recipientQuery = "UPDATE UserData SET Balance = Balance + ? WHERE Username = ?";
                PreparedStatement recipientStatement = connection5.prepareStatement(recipientQuery);
                recipientStatement.setDouble(1, transferAmount);
                recipientStatement.setString(2, recipientUsername);
                recipientStatement.executeUpdate();

                AppDashboard.clearTerminal();

                // Confirm transfer
                System.out.println(FILLER);
                System.out.println("Transfer of $" + transferAmount + " to " + recipientUsername + " was successful!");

                // Pull new balance to print
                ResultSet newTransferResult = pullBalance(usernameInput);

                // Print new balance
                if (newTransferResult.next()) {
                    double newBalance = newTransferResult.getDouble("Balance");
                    System.out.println("Your new balance is: $" + newBalance);
                    System.out.println(FILLER);

                    // Log transaction
                    TransactionLogger.logTransaction(usernameInput, recipientUsername, "Transfer", transferAmount);
                } else {

                    AppDashboard.clearTerminal();

                    System.out.println("Failed to retrieve new balance.");
                }
                transfercomplete = true;
        }
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return;

    }

    public static void changePassword(String usernameInput) {

        Scanner scanner = new Scanner(System.in);

        boolean actionDone = false;

            try {
                Connection connection6 = getConnection();
                while (!actionDone){           

                System.out.println(FILLER);
                System.out.println("Password Change Menu");
                System.out.println(FILLER);

                // Ask for old password
                System.out.print("Please enter your old password: ");
                String oldPassword = scanner.next();

                // Get password of user from database
                String passwordQuery = "SELECT Password FROM UserData WHERE Username = ?";
                PreparedStatement passwordStatement = connection6.prepareStatement(passwordQuery);
                passwordStatement.setString(1, usernameInput);
                ResultSet resultSet = passwordStatement.executeQuery();
                resultSet.next();

                // Set password from database to dbPassword
                String dbPassword = resultSet.getString("Password");


                // Check if old password matches
                boolean result = Password.check(oldPassword, dbPassword).withArgon2();

                if (!result) {

                    AppDashboard.clearTerminal();

                    // Print error message
                    System.out.println("Incorrect old password. Password change cancelled.");
                    System.out.print("Press enter to return to dashboard:");
                    scanner.nextLine();
                    actionDone = false;

                    break;
                    }

                AppDashboard.clearTerminal();

                System.out.print("Password matched, please enter your new password: ");
                String newPassword = scanner.next();

                System.out.print("Please enter your new password again to confirm: ");
                String passwordConfirm = scanner.nextLine();
                passwordConfirm = scanner.next();

                boolean passwordMatch = newPassword.equals(passwordConfirm);

                while (!passwordMatch) {
                    System.out.println("Passwords do not match, please try again");
                    System.out.print("Please enter your new password: ");
                    newPassword = scanner.nextLine();
                    System.out.print("Please enter your new password again to confirm: ");
                    passwordConfirm = scanner.nextLine();
                    passwordMatch = newPassword.equals(passwordConfirm);
                }

                // Hash the new password
                Hash hash = Password.hash(newPassword).addRandomSalt(32).withArgon2();
                newPassword = hash.getResult();

                // Update password in database
                String passwordChangeQuery = "UPDATE UserData SET Password = ? WHERE Username = ?";
                PreparedStatement passwordChangeStatement = connection6.prepareStatement(passwordChangeQuery);
                passwordChangeStatement.setString(1, newPassword);
                passwordChangeStatement.setString(2, usernameInput);
                passwordChangeStatement.executeUpdate();

                AppDashboard.clearTerminal();

                actionDone = true;
            }
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
    } 

    public static int deleteAccount(String usernameInput) {

            Scanner scanner = new Scanner(System.in);
    
            int exitDash = 1;

                try {
                    
                    Connection connection7 = getConnection();

                    // Ask for confirmation
                    System.out.print("Are you sure you want to delete your account? (\u001B[32mY\u001B[0m/\u001B[31mN\u001B[0m): ");
                    String deleteAccount = scanner.next();

                    if (deleteAccount.equals("Y") || deleteAccount.equals("y")) {
                        // Delete account from database
                        String deleteQuery = "DELETE FROM UserData WHERE Username = ?";
                        PreparedStatement deleteStatement = connection7.prepareStatement(deleteQuery);
                        deleteStatement.setString(1, usernameInput);
                        deleteStatement.executeUpdate();

                        System.out.println("Account deleted successfully!");
                        exitDash = 2;
                    } else {
                        System.out.println("Account deletion cancelled, returning to dashboard.");
                    }
                
                } catch (SQLException e) {
                    System.out.println("Connection Failed! Check output console");
                    e.printStackTrace();
                }

        return exitDash;
    }
    
    public static ResultSet pullBalance(String usernameInput) {

        ResultSet balanceResult = null;

        try {
            Connection connection = getConnection();
        
            String balanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
            PreparedStatement balanceStatement = connection.prepareStatement(balanceQuery);
            balanceStatement.setString(1, usernameInput);
            balanceResult = balanceStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();

        }

        return balanceResult;
    }
}