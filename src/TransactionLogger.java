import java.sql.Connection;
import java.sql.PreparedStatement;

public class TransactionLogger extends BankingApp{
    
    // Load Database credentials
    static String dburl = "";
    static String dbusername = "";
    static String dbpassword = "";
    

    public static void logTransaction(String sender,String reciever, String action, double amount) {

        // Log transaction to database
        try {
            
            Connection connection = BankOperations.getConnection();

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
