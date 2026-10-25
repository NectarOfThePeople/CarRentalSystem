import javax.swing.JOptionPane;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

// AdminFrame.java
// The window of the admin. The admin can manage cars and customers,
// book cars for customers, take cars back (return + bill) and see the lists and reports.
public class AdminFrame extends BaseFrame {
    private Admin admin;

    public AdminFrame(CarRentalSystem system, Admin admin) {
        super(system, "Car Rental System - Admin", new String[]{
                "Add Car", "Remove Car", "Search Car", "View Available Cars",
                "Register Customer", "Book Car", "Return Car",
                "Car List", "Customer List", "Booking List", "Reports", "Logout"});
        this.admin = admin;
        output.setText("Welcome, Admin!\n\nChoose an option from the menu on the left.");
    }

    // Runs when any menu button is clicked
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        try {
            if (command.equals("Add Car")) {
                addCar();
            } else if (command.equals("Remove Car")) {
                removeCar();
            } else if (command.equals("Search Car")) {
                searchCar();
            } else if (command.equals("View Available Cars")) {
                showCars("AVAILABLE CARS", system.getAvailableCars());
            } else if (command.equals("Register Customer")) {
                registerCustomer();
            } else if (command.equals("Book Car")) {
                bookCar();
            } else if (command.equals("Return Car")) {
                returnCar();
            } else if (command.equals("Car List")) {
                showCars("ALL CARS", system.getAllCars());
            } else if (command.equals("Customer List")) {
                showCustomers();
            } else if (command.equals("Booking List")) {
                showBookings("ALL BOOKINGS", system.getAllBookings());
            } else if (command.equals("Reports")) {
                output.setText(admin.viewReports(system));
            } else if (command.equals("Logout")) {
                logout();
            }
        } catch (SQLException ex) {
            // any problem with the database comes here
            showError("Database error: " + ex.getMessage());
        }
    }

    // ---------- one method for each menu option ----------

    private void addCar() throws SQLException {
        String[] v = showForm("Add Car", new String[]{"Car ID:", "Car Name:", "Type:", "Rent Per Day:"});
        if (v == null) {
            return;                     // user pressed Cancel
        }
        if (hasEmpty(v)) {
            showError("Please fill in all the fields.");
            return;
        }
        try {
            int rent = Integer.parseInt(v[3]);
            if (rent <= 0) {
                showError("Rent per day must be more than 0.");
                return;
            }
            output.setText(system.addCar(v[0], v[1], v[2], rent));
        } catch (NumberFormatException ex) {
            showError("Rent per day must be a number.");
        }
    }

    private void removeCar() throws SQLException {
        String[] v = showForm("Remove Car", new String[]{"Car ID:"});
        if (v == null) {
            return;
        }
        if (hasEmpty(v)) {
            showError("Please enter the Car ID.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this, "Remove car " + v[0] + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            output.setText(system.removeCar(v[0]));
        }
    }

    private void registerCustomer() throws SQLException {
        String[] v = showForm("Register Customer",
                new String[]{"Customer ID:", "Name:", "Phone (10 digits):", "License No:"});
        if (v == null) {
            return;
        }
        if (hasEmpty(v)) {
            showError("Please fill in all the fields.");
            return;
        }
        if (!isValidPhone(v[2])) {
            showError("Phone number must have exactly 10 digits.");
            return;
        }
        output.setText(system.registerCustomer(v[0], v[1], v[2], v[3]));
    }

    private void bookCar() throws SQLException {
        String[] v = showForm("Book Car", new String[]{"Car ID:", "Customer ID:", "Number of Days:"});
        if (v == null) {
            return;
        }
        if (hasEmpty(v)) {
            showError("Please fill in all the fields.");
            return;
        }
        try {
            int days = Integer.parseInt(v[2]);
            if (days <= 0) {
                showError("Number of days must be more than 0.");
                return;
            }
            output.setText(system.bookCar(v[0], v[1], days));
        } catch (NumberFormatException ex) {
            showError("Number of days must be a number.");
        }
    }

    private void returnCar() throws SQLException {
        String[] v = showForm("Return Car", new String[]{"Booking ID:", "Days Actually Used:"});
        if (v == null) {
            return;
        }
        if (hasEmpty(v)) {
            showError("Please fill in all the fields.");
            return;
        }
        try {
            int daysUsed = Integer.parseInt(v[1]);
            if (daysUsed <= 0) {
                showError("Days used must be more than 0.");
                return;
            }
            output.setText(system.returnCar(v[0], daysUsed));
        } catch (NumberFormatException ex) {
            showError("Days used must be a number.");
        }
    }

    private void showCustomers() throws SQLException {
        String text = "ALL CUSTOMERS\n\n";
        ArrayList<Customer> list = system.getAllCustomers();
        if (list.size() == 0) {
            text += "No customers registered.";
        } else {
            text += Customer.getHeading() + "\n";
            text += "-------------------------------------------------\n";
            for (Customer c : list) {
                text += c.displayCustomer() + "\n";
            }
        }
        output.setText(text);
    }

    // true if the phone number has exactly 10 digits
    private boolean isValidPhone(String phone) {
        if (phone.length() != 10) {
            return false;
        }
        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
