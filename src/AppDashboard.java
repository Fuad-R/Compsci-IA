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
        int accountAction = scanner.nextInt();

        return accountAction;
    }
}
