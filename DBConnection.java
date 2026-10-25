import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// DBConnection.java
// Connects the program to the MySQL database using JDBC.
// Change USER and PASSWORD to the MySQL username and password on your computer.
public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/car_rental?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() throws SQLException {
        try {
            // load the MySQL driver (it comes from the mysql-connector-j jar file)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL connector jar file was not found. "
                    + "Add mysql-connector-j to the classpath.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
