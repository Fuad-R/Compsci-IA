import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardGUI extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");

        Label welcomeLabel = new Label("Welcome to your dashboard! What would you like to do today?");
        Button viewBalanceButton = new Button("View Balance");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button transferButton = new Button("Transfer");
        Button manageAccountButton = new Button("Manage Account");
        Button exitButton = new Button("Exit");

        VBox vbox = new VBox(welcomeLabel, viewBalanceButton, depositButton, withdrawButton, transferButton, manageAccountButton, exitButton);
        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
