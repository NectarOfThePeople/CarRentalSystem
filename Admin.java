import java.sql.SQLException;

// Admin.java
// The admin who manages the cars, customers and bookings.
// The admin must login with a password (the password is stored in the admins table).
public class Admin {
    private String adminId;
    private String password;

    public Admin(String adminId, String password) {
        this.adminId = adminId;
        this.password = password;
    }

    // true if this admin id and password match the admins table in the database
    public boolean login(CarRentalSystem system) throws SQLException {
        return system.checkAdmin(adminId, password);
    }

    // Makes a short summary report of the whole system
    public String viewReports(CarRentalSystem system) throws SQLException {
        int totalCars = system.getAllCars().size();
        int availableCars = system.getAvailableCars().size();
        int activeBookings = 0;
        for (Booking b : system.getAllBookings()) {
            if (b.getStatus().equals("Active")) {
                activeBookings++;
            }
        }

        String report = "SUMMARY REPORT\n";
        report += "----------------------------\n";
        report += "Total cars          : " + totalCars + "\n";
        report += "Available cars      : " + availableCars + "\n";
        report += "Cars on rent        : " + (totalCars - availableCars) + "\n";
        report += "Total customers     : " + system.getAllCustomers().size() + "\n";
        report += "Total bookings      : " + system.getAllBookings().size() + "\n";
        report += "Active bookings     : " + activeBookings + "\n";
        return report;
    }
}
