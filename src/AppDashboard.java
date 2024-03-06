import java.util.Scanner;

public class AppDashboard extends BankOperations{
        
    public static void displayUserDashboard(String usernameInput) {
            
        String filler = "-------------------------------------------------------------------";
        String clear = "\033[H\033[2J";

        // Clear the terminal
        System.out.print(clear);
        System.out.flush();

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
        
        // Rest of the code...

    }

    public static int displayReturnDashboard() {

        Scanner scanner = new Scanner(System.in);
        // Decide what to do next

        System.out.print("Type 1 to return to the dashboard, or 2 to exit: ");
        return scanner.nextInt();
    }

    public static int accountdashboard(String usernameInput){

        Scanner scanner = new Scanner(System.in);

        System.out.println(FILLER);
        System.out.println("Welcome to your account menu " + usernameInput + "! What would you like to do today?");
        System.out.println(FILLER);
        System.out.println("1. Change Password");
        System.out.println("2. Delete Account");
        System.out.println("3. Return to Dashboard");
        System.out.println();
        System.out.print("Please enter the number of the action you would like to perform: ");

        return scanner.nextInt();
    }

    public static int operations(int action, String usernameInput) {

        // Clear terminal
        System.out.print(CLEAR);
        System.out.flush();

        int exitDash = 1;

        switch (action) {
            case 1: // View Balance
                double returnedbalance = BankOperations.checkBalance(usernameInput);
                                
                System.out.println(FILLER);
                System.out.println("Your current balance is: $" + returnedbalance);
                System.out.println(FILLER);
                break;

            case 2: // Deposit
                BankOperations.deposit(usernameInput);
                break;

            case 3: // Withdraw
                BankOperations.withdraw(usernameInput);
                break;

            case 4: // Transfer
                BankOperations.transfer(usernameInput);
                break;

            case 5: // Manage Account
                int accountAction = AppDashboard.accountdashboard(usernameInput);

                // Clear terminal
                System.out.print(CLEAR);
                System.out.flush();

                switch (accountAction) {
                    case 1: // Change Password
                        BankOperations.changePassword(usernameInput);
                        break;
                    case 2: // Delete Account
                        exitDash = BankOperations.deleteAccount(usernameInput);
                        break;
                    case 3: // Return to Dashboard
                        System.out.println("Returning to dashboard...");
                        break;
                    default: // Invalid action
                        System.out.println("Invalid action, returning to dashboard.");
                        break;
                }

                break;
            case 6: // Exit
                System.out.println(EXITMSG);
                exitDash = 2;
                break;
            default: // Invalid action
                System.out.println("Invalid action, returning to dashboard.");
                break;
        }

        if (exitDash != 2) {
            exitDash = AppDashboard.displayReturnDashboard();
            if (exitDash == 2) {
                System.out.println(EXITMSG);
            }
        }

        return exitDash;
    }
}
