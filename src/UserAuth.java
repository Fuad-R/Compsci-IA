import com.password4j.Password;
import com.password4j.Hash;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserAuth extends BankingApp {
    
    private static boolean isloggedin = false;
    private static String usernameInput = "";
    private static boolean authed = false;
    
    private static String url = "";
    private static String username = "";
    private static String password = "";


    public static void authmethod() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Establishing auth connection...");

        // Establishing permanent connection

        try {
            Connection connection = BankOperations.getConnection();
            System.out.println("\u001B[32mDatabase connection established!\u001B[0m");
        
        // Clear the terminal
        System.out.print(CLEAR);
        System.out.flush();           

        // Display login screen
        System.out.println(FILLER);
        System.out.println("Welcome to the Banking App! Would you like to login or create an account?");
        System.out.println(FILLER);

        System.out.println("1. Login");
        System.out.println("2. Create Account");

        System.out.println();
        System.out.print("Please enter the number of the action you would like to perform: ");
        
        int userChoice = scanner.nextInt();

        // Clear the terminal
        System.out.print(CLEAR);
        System.out.flush();

        switch (userChoice) {

            case 1: // Login

        while (!authed){
        authed = loginPage();
        }

        break;

            case 2: // Create Account

                    boolean usernamepicked = false;

                    usernameInput = "";

                    while (!usernamepicked) {

                    System.out.print("Please enter your Username: ");
                    usernameInput = scanner.next(); 

                    // newUsername = scanner.nextLine(); // Still cant figure out why i need to do this twice

                    // Check if username already exists
                    String checkQuery = "SELECT * FROM UserData WHERE Username = ?";
                    PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                    checkStatement.setString(1, usernameInput);
                    ResultSet checkResult = checkStatement.executeQuery();

                    if (checkResult.next()) {
                        System.out.println("Username already exists, please choose another one.");
                        usernamepicked = false;
                        
                    } else {

                        // Clear the terminal
                        System.out.print(CLEAR);
                        System.out.flush();

                        usernamepicked = true;
                    }

                    } // End ussernamepicked while loop

                    
                    
                    System.out.println("Your username was set to " + usernameInput);
                    System.out.print("Please enter your password: ");
                    String newPassword = scanner.next();
                    System.out.print("Please enter your password again to confirm: ");
                    String passwordConfirm = scanner.next();

                    boolean passwordMatch = newPassword.equals(passwordConfirm);

                    while (!passwordMatch) {
                        System.out.println("Passwords do not match, please try again");
                        System.out.print("Please enter your password: ");
                        newPassword = scanner.next();
                        
                        System.out.print("Please enter your password again to confirm: ");
                        passwordConfirm = scanner.next();
                        passwordMatch = newPassword.equals(passwordConfirm);

                    }


                    // Hash the password
                    Hash hash = Password.hash(newPassword).addRandomSalt(32).withArgon2();
                    newPassword = hash.getResult();                  
                    

                    String insertQuery = "INSERT INTO UserData (Username, Password) VALUES (?, ?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, usernameInput);
                    insertStatement.setString(2, newPassword);
                    insertStatement.executeUpdate();
                    System.out.println("Account created successfully, signing you in now!");
                    authed = true;

        }

        if (authed) {

            isloggedin = true;
        }
    

        } catch (SQLException e) {

            System.out.println("\u001B[31mConnection failed! Check output console\u001B[0m");
            e.printStackTrace();

        }

        
    
    }

    public static boolean isLoggedIn() {
        return isloggedin;
    }

    public static String getUsername() {
        return usernameInput;
    }
    
    public static boolean loginPage() {

        Scanner scanner = new Scanner(System.in);

        try {
            Connection connection = BankOperations.getConnection();
            

            while (!authed) {

                // Ask for username input
                System.out.print("Please enter your username: ");
                usernameInput = scanner.next();
                
                // Check if the user exists
                String query = "SELECT * FROM UserData WHERE Username = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, usernameInput);
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
    
                    // Clear the terminal
                    System.out.print(CLEAR);
                    System.out.flush();
    
                    System.out.println("User exists!");
    
                    // Ask for password input
                    System.out.print("Please enter your password: ");
                    String passwordInput = scanner.next();
               
                    String dbPassword = resultSet.getString("Password");
    
                    // Check hashed password
                    boolean result = Password.check(passwordInput, dbPassword).withArgon2();
    
                    if (result) {
                        System.out.println("Password matched, signing you in...");
                        authed = true;
    
                        break;
                    
                    } else {
                        System.out.println("Incorrect password, please try again.");
                        System.out.println();
                        
                        int attempts = 3; // Number of attempts allowed
                        while (attempts > 0) {
    
                            System.out.print("Please enter your Password: ");
                            passwordInput = scanner.nextLine();
    
                            // Check hashed password
                            result = Password.check(passwordInput, dbPassword).withArgon2();      
    
                            if (result) {
                                System.out.println("Password matched, signing you in...");
                                authed = true;
                                isloggedin = true; 
    
                            } else {
                                attempts--;
    
                                // Clear the terminal
                                System.out.print(CLEAR);
                                System.out.flush();
    
                                System.out.println("Incorrect password, " + attempts + " attempts remaining.");
                                if (attempts == 0) {
    
                                    // Clear the terminal
                                    System.out.print(CLEAR);
                                    System.out.flush();
    
                                    System.out.println(FILLER);
                                    System.out.println("You have exceeded the maximum number of attempts.");
                                    System.out.println("Please try again later, or contact support if you believe this is a mistake.");
                                    System.out.println(FILLER);
    
                                    break;
                                }
                            }
                        }
    
                    }
                 
                } else {
                    System.out.println("User does not exist, please try again.");
                }
     
            }
            
        } catch (SQLException e) {

            System.out.println("\u001B[31mConnection failed! Check output console\u001B[0m");
            e.printStackTrace();
        }

        return authed;
    }


}

