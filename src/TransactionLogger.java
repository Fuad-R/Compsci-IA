import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.sql.PreparedStatement;

public class TransactionLogger extends BankingApp{
    
    // Load Database credentials
    static String dburl = "";
    static String dbusername = "";
    static String dbpassword = "";
    

    public static void logTransaction(String sender,String reciever, String action, double amount) {
        
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fis);

            dbusername = props.getProperty("username");
            dbpassword = props.getProperty("password");
            dburl = props.getProperty("url");
        } catch (IOException e) {
            System.out.println("Error loading database credentials");
            e.printStackTrace();
        }

        // Log transaction to database
        try {
            
            Connection connection = DriverManager.getConnection(dburl, dbusername, dbpassword);

            String sql = "INSERT INTO Transactions (Sender, Reciever, Value, Action, Timestamp) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sender);
            preparedStatement.setString(2, reciever);
            preparedStatement.setDouble(3, amount);
            preparedStatement.setString(4, action);
            preparedStatement.setTimestamp(5, new java.sql.Timestamp(new java.util.Date().getTime()));

            preparedStatement.executeUpdate();


        } catch (Exception e) {
            System.out.println("Error logging transaction");
            e.printStackTrace();
        }
        
    }
}
