// Admin.java
// The person who uses the system. Can login and view reports.
public class Admin {
    private String adminId;
    private String password;

    public Admin(String adminId, String password) {
        this.adminId = adminId;
        this.password = password;
    }

    // Returns true if the id and password are correct
    public boolean login(String id, String pass) {
        return adminId.equals(id) && password.equals(pass);
    }

    // Makes a short summary report of the whole system
    public String viewReports(CarRentalSystem system) {
        int totalCars = system.getCars().size();
        int availableCars = system.getAvailableCars().size();
        int activeBookings = 0;
        for (Booking b : system.getBookings()) {
            if (b.getStatus().equals("Active")) {
                activeBookings++;
            }
        }

        String report = "SUMMARY REPORT\n";
        report += "----------------------------\n";
        report += "Total cars          : " + totalCars + "\n";
        report += "Available cars      : " + availableCars + "\n";
        report += "Cars on rent        : " + (totalCars - availableCars) + "\n";
        report += "Total customers     : " + system.getCustomers().size() + "\n";
        report += "Total bookings      : " + system.getBookings().size() + "\n";
        report += "Active bookings     : " + activeBookings + "\n";
        return report;
    }
}
