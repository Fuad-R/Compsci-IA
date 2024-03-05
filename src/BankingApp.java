import java.util.Scanner;

public class BankingApp {

    //universal variables
    public static final String FILLER = "-------------------------------------------------------------------";
    public static final String CLEAR = "\033[H\033[2J";

    public static void main(String[] args) {
        
        // Define common strings to avoid repetition
        String exitMsg = "Exiting now, goodbye.";

        Scanner scanner = new Scanner(System.in);

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

                    // Call the transfer method
                    BankOperations.transfer(usernameInput);
                    

                    exitDash = AppDashboard.displayReturnDashboard();

                    if (exitDash == 2) {
                        System.out.println(exitMsg);
                        break;
                    }

                break;

                case 5: // Manage Account

                    int accountAction = AppDashboard.accountdashboard(usernameInput);

                    // Clear terminal
                    System.out.print(CLEAR);
                    System.out.flush();

                    switch (accountAction) {
                        case 1:

                            // Call the changepassword method
                            BankOperations.changePassword(usernameInput);
                            
                            System.out.println(FILLER);
                            System.out.println("Password changed successfully!");
                            System.out.println(FILLER);
                            System.out.print("Returning to dashboard in 5 seconds...");
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 2:

                            // Call deleteaccount method
                            exitDash = BankOperations.deleteAccount(usernameInput);

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
        //Close scanner
        scanner.close();
        }        
}
