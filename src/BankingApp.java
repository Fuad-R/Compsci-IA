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
        String clear = "\033[H\033[2J";

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

            // Perform database operations here
            // Clear the terminal
            System.out.print(clear);
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
                    System.out.println("Exiting now, goodbye.");
                    return;
                }
            
            }

            // Clear the terminal
            System.out.print(clear);
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


                    // Clear terminal
                    System.out.print(clear);
                    System.out.flush();
                    
                    // Print balance
                    if (balanceResult.next()) {

                        double balance = balanceResult.getDouble("Balance");
                        System.out.println("Your current balance is: $" + balance);
                        System.out.print("Press enter to return to the dashboard:");
                        
                        // Wait for user to press enter
                        scanner.nextLine();
                        scanner.nextLine();


                    } else {
                        System.out.println("Failed to retrieve balance.");
                    }

                    balanceResult.close();
                    balanceStatement.close();
                    break;
                case 2:
                    System.out.println("Deposit");

                    // Ask for deposit amount
                    System.out.print("Please enter the amount you would like to deposit: ");
                    double depositAmount = scanner.nextDouble();

                    // Update balance in database
                    String depositQuery = "UPDATE UserData SET Balance = Balance + ? WHERE Username = ?";
                    PreparedStatement depositStatement = connection.prepareStatement(depositQuery);
                    depositStatement.setDouble(1, depositAmount);
                    depositStatement.setString(2, usernameInput);
                    depositStatement.executeUpdate();

                    // Pull new balance to print
                    String newBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement newBalanceStatement = connection.prepareStatement(newBalanceQuery);
                    newBalanceStatement.setString(1, usernameInput);
                    ResultSet newBalanceResult = newBalanceStatement.executeQuery();
                    
                    // Print new balance
                    if (newBalanceResult.next()) {
                        double newBalance = newBalanceResult.getDouble("Balance");
                        System.out.println("Your new balance is: $" + newBalance);
                    } else {
                        System.out.println("Failed to retrieve new balance.");
                    }

                    break;
                case 3:
                    System.out.println("Withdraw");

                    // Ask for withdraw amount
                    System.out.print("Please enter the amount you would like to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();

                    // Update balance in database
                    String withdrawQuery = "UPDATE UserData SET Balance = Balance - ? WHERE Username = ?";
                    PreparedStatement withdrawStatement = connection.prepareStatement(withdrawQuery);
                    withdrawStatement.setDouble(1, withdrawAmount);
                    withdrawStatement.setString(2, usernameInput);
                    withdrawStatement.executeUpdate();

                    // Pull new balance to print
                    String newWithdrawQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement newWithdrawStatement = connection.prepareStatement(newWithdrawQuery);
                    newWithdrawStatement.setString(1, usernameInput);
                    ResultSet newWithdrawResult = newWithdrawStatement.executeQuery();

                    // Print new balance
                    if (newWithdrawResult.next()) {
                        double newBalance = newWithdrawResult.getDouble("Balance");
                        System.out.println("Your new balance is: $" + newBalance);
                    } else {
                        System.out.println("Failed to retrieve new balance.");
                    }

                    break;
                case 4:

                    // Clear terminal
                    System.out.print(clear);
                    System.out.flush();

                    // Print transfer message
                    System.out.println(filler);
                    System.out.println("Transfer Menu");
                    System.out.println("Please note that a 1% fee will be charged for each transfer.");
                    System.out.println(filler);


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
                    System.out.println(clear);
                    System.out.flush();

                    // Check if balance is sufficient
                    String checkBalanceQuery = "SELECT Balance FROM UserData WHERE Username = ?";
                    PreparedStatement checkBalanceStatement = connection.prepareStatement(checkBalanceQuery);
                    checkBalanceStatement.setString(1, usernameInput);
                    ResultSet checkBalanceResult = checkBalanceStatement.executeQuery();

                    if (checkBalanceResult.next()) {
                        double balance = checkBalanceResult.getDouble("Balance");
                        if (balance < transferAmount * 1.01) {
                            System.out.println(filler);
                            System.out.println("Insufficient funds, transfer cancelled.");
                            System.out.println(filler);
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
                        System.out.println(filler);
                        System.out.println("Recipient does not exist, transfer cancelled.");
                        System.out.println(filler);
                        break;
                    }

                    System.out.println(filler);
                    System.out.println("Transfer confirmed, processing now...");}
                    System.out.println(filler);

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

                    break;
                case 5:
                    System.out.println("Manage Account");
                    break;
                case 6:
                    System.out.println("Exiting now, goodbye.");
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

            scanner.close();
            return;
        }
        //Close scanner
        scanner.close();
        
    }
    
}