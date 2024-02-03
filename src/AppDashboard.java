import java.util.Scanner;

public class AppDashboard {
        
    public static void displayUserDashboard(String usernameInput) {
            
        Scanner scanner = new Scanner(System.in);
        String filler = "-------------------------------------------------------------------";
        String clear = "\033[H\033[2J";

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
        
        // Rest of the code...

    }
}
