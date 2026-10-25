import javax.swing.JOptionPane;
import java.sql.SQLException;

// Main.java
// Run this class to start the Car Rental System.
public class Main {
    public static void main(String[] args) {
        try {
            CarRentalSystem system = new CarRentalSystem();   // connects to MySQL
            LoginFrame login = new LoginFrame(system);
            login.setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Could not connect to the database.\n\n" + e.getMessage()
                    + "\n\nCheck that:\n"
                    + "1. MySQL is running and car_rental.sql has been run\n"
                    + "2. the user name and password in DBConnection.java are correct\n"
                    + "3. the mysql-connector-j jar file is on the classpath",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
