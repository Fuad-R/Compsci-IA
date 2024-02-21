import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DashboardGUI extends Application{
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ATM Dashboard");

        Label welcomeLabel = new Label("Welcome to your ATM dashboard! What would you like to do today?");
        Button viewBalanceButton = new Button("View Balance");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        Button transferButton = new Button("Transfer");
        Button manageAccountButton = new Button("Manage Account");
        Button exitButton = new Button("Exit");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(welcomeLabel, 0, 0);
        gridPane.add(viewBalanceButton, 0, 1);
        gridPane.add(depositButton, 1, 1);
        gridPane.add(withdrawButton, 0, 2);
        gridPane.add(transferButton, 1, 2);
        gridPane.add(manageAccountButton, 0, 3);
        gridPane.add(exitButton, 1, 3);

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
