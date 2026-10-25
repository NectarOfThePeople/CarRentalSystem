import java.awt.event.*;
import java.sql.SQLException;

// CustomerFrame.java
// The window of a customer (opened after logging in with the Customer ID).
// A customer can see and search the available cars, book a car and see his or her own bookings.
// Adding cars, registering customers, returning cars and reports are only for the admin.
public class CustomerFrame extends BaseFrame {
    private Customer customer;

    public CustomerFrame(CarRentalSystem system, Customer customer) {
        super(system, "Car Rental System - Customer", new String[]{
                "View Available Cars", "Search Car", "Book Car", "My Bookings", "Logout"});
        this.customer = customer;

        setSize(800, 480);
        setLocationRelativeTo(null);
        output.setText("Welcome, " + customer.getName() + "!   (Customer ID: "
                + customer.getCustomerId() + ")\n\n"
                + "Click 'View Available Cars' to see the cars you can book.\n"
                + "To return a car, please meet the admin.");
    }

    // Runs when any menu button is clicked
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (command.equals("View Available Cars")) {
                showCars("AVAILABLE CARS", system.getAvailableCars());
            } else if (command.equals("Search Car")) {
                searchCar();
            } else if (command.equals("Book Car")) {
                bookCar();
            } else if (command.equals("My Bookings")) {
                showBookings("MY BOOKINGS", system.getBookingsOfCustomer(customer.getCustomerId()));
            } else if (command.equals("Logout")) {
                logout();
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    // The customer id is already known, so only the car and the days are asked
    private void bookCar() throws SQLException {
        String[] v = showForm("Book Car", new String[]{"Car ID:", "Number of Days:"});
        if (v == null) {
            return;
        }
        if (hasEmpty(v)) {
            showError("Please fill in all the fields.");
            return;
        }
        try {
            int days = Integer.parseInt(v[1]);
            if (days <= 0) {
                showError("Number of days must be more than 0.");
                return;
            }
            output.setText(system.bookCar(v[0], customer.getCustomerId(), days));
        } catch (NumberFormatException ex) {
            showError("Number of days must be a number.");
        }
    }
}
