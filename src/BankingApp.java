import java.util.Scanner;

public class BankingApp {

    //universal variables
    public static final String FILLER = "-------------------------------------------------------------------";
    public static final String CLEAR = "\033[H\033[2J";
    public static final String EXITMSG = "Exiting now, goodbye.";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        AppDashboard.clearTerminal();
          
        // Check if user is logged in
        boolean loggedInStatus = false;

        while (!loggedInStatus) {

            // Call UserAuth class to display login dashboard
            UserAuth.authmethod();
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

            AppDashboard.clearTerminal();
            
            // Call the actions method 
            exitDash = AppDashboard.operations(action, usernameInput);

        }
        //Close scanner
        scanner.close();
        }        
}
