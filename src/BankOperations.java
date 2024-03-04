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
            Connection connection = DriverManager.getConnection(url, username, password);
        
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

        System.out.println(FILLER);
        System.out.println("Depositing Menu");
        System.out.println(FILLER);

        // Ask for deposit amount
        System.out.print("Please enter the amount you would like to deposit: ");
        double depositAmount = scanner.nextDouble();
        ///scanner.close();

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
            System.out.print(CLEAR);
            System.out.flush();

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
        Connection connection3 = DriverManager.getConnection(url, username, password);
        
        // Print withdraw menu message
        System.out.println(FILLER);
        System.out.println("Withdraw Menu");
        System.out.println(FILLER);

        // Ask for withdraw amount
        System.out.print("Please enter the amount you would like to withdraw: ");
        double withdrawAmount = scanner.nextDouble();
        // scanner.close();

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
        String newWithdrawQuery = "SELECT Balance FROM UserData WHERE Username = ?";
        PreparedStatement newWithdrawStatement = connection3.prepareStatement(newWithdrawQuery);
        newWithdrawStatement.setString(1, usernameInput);
        ResultSet newWithdrawResult = newWithdrawStatement.executeQuery();

        // Print new balance
        if (newWithdrawResult.next()) {
            double newBalance = newWithdrawResult.getDouble("Balance");

            // Clear terminal
            System.out.print(CLEAR);
            System.out.flush();

            System.out.println(FILLER);
            System.out.println("Withdrawal successful! You withdrew: $" + withdrawAmount);
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

    public static boolean transfer(String usernameInput) {

        boolean transfercomplete = false;
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
            Connection connection5 = DriverManager.getConnection(url, username, password);           

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

                // Clear terminal
                System.out.print(CLEAR);
                System.out.flush();

                // Print transfer info message
                System.out.println(FILLER);
                System.out.println("The total fee for this transfer will be: $" + transferAmount * 0.01);
                System.out.println("The total amount to be deducted from your account will be: $" + transferAmount * 1.01);
                System.out.println(FILLER);
                System.out.print("Please confirm the transfer by entering \u001B[32mY\u001B[0m or \u001B[31mN\u001B[0m: ");
                String confirmTransfer = scanner.nextLine();

                if (confirmTransfer.equals("N") || confirmTransfer.equals("n")) {

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    System.out.println("Transfer cancelled, returning to dashboard.");
                    break;
                } else {

                // Clear terminal
                System.out.println(CLEAR);
                System.out.flush();

                // Check if balance is sufficient
                String checkBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                PreparedStatement checkBalanceStatement = connection5.prepareStatement(checkBalanceQuery);
                checkBalanceStatement.setString(1, usernameInput);
                ResultSet checkBalanceResult = checkBalanceStatement.executeQuery();

                if (checkBalanceResult.next()) {
                    double balance = checkBalanceResult.getDouble("Balance");
                    if (balance < transferAmount * 1.01) {

                        // Clear terminal
                        System.out.print(CLEAR);
                        System.out.flush();

                        System.out.println(FILLER);
                        System.out.println("Insufficient funds, transfer cancelled.");
                        System.out.println(FILLER);
                        break;
                    }
                } else {

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    System.out.println("Failed to retrieve balance.");
                }

                // Check if recipient exists
                String checkRecipientQuery = "SELECT * FROM UserData WHERE Username = ?";
                PreparedStatement checkRecipientStatement = connection5.prepareStatement(checkRecipientQuery);
                checkRecipientStatement.setString(1, recipientUsername);
                ResultSet checkRecipientResult = checkRecipientStatement.executeQuery();

                if (!checkRecipientResult.next()) {

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

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

                // Clear the terminal
                System.out.print(CLEAR);
                System.out.flush();

                // Confirm transfer
                System.out.println(FILLER);
                System.out.println("Transfer of $" + transferAmount + " to " + recipientUsername + " was successful!");

                // Pull new balance to print
                String newTransferQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                PreparedStatement newTransferStatement = connection5.prepareStatement(newTransferQuery);
                newTransferStatement.setString(1, usernameInput);
                ResultSet newTransferResult = newTransferStatement.executeQuery();

                // Print new balance
                if (newTransferResult.next()) {
                    double newBalance = newTransferResult.getDouble("Balance");
                    System.out.println("Your new balance is: $" + newBalance);
                    System.out.println(FILLER);
                } else {

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    System.out.println("Failed to retrieve new balance.");
                }
                transfercomplete = true;
        }
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        
        return transfercomplete;

    }

    public static void manageaccount() {

        // Add account managment method here

    } 

}
