import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

// MainFrame.java
// The main window. The menu buttons are on the left and the results are shown on the right.
public class MainFrame extends JFrame implements ActionListener {
    private CarRentalSystem system;
    private Admin admin;
    private JTextArea output;

    public MainFrame(CarRentalSystem system, Admin admin) {
        this.system = system;
        this.admin = admin;

        setTitle("Car Rental System");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // heading
        JLabel title = new JLabel("CAR RENTAL SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(30, 90, 160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // menu buttons on the left side
        String[] names = {"Add Car", "Remove Car", "Search Car", "View Available Cars",
                "Register Customer", "Book Car", "Return Car",
                "Car List", "Customer List", "Booking List", "Reports", "Exit"};
        JPanel menu = new JPanel(new GridLayout(names.length, 1, 5, 5));
        menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));
        for (int i = 0; i < names.length; i++) {
            JButton button = new JButton(names[i]);
            button.addActionListener(this);
            menu.add(button);
        }
        add(menu, BorderLayout.WEST);

        // output area on the right side
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        output.setText("Welcome, Admin!\n\nChoose an option from the menu on the left.");
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 10, 10),
                BorderFactory.createTitledBorder("Output")));
        add(scroll, BorderLayout.CENTER);
    }

    // Runs when any menu button is clicked
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

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
            showCars("ALL CARS", system.getCars());
        } else if (command.equals("Customer List")) {
            showCustomers();
        } else if (command.equals("Booking List")) {
            showBookings();
        } else if (command.equals("Reports")) {
            output.setText(admin.viewReports(system));
        } else if (command.equals("Exit")) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to exit?",
                    "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    // ---------- one method for each menu option ----------

    private void addCar() {
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

    private void removeCar() {
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

    private void searchCar() {
        JComboBox<String> searchBy = new JComboBox<String>(new String[]{"Type", "Price"});
        JTextField valueField = new JTextField(12);

        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Search by:"));
        panel.add(searchBy);
        panel.add(new JLabel("Type name / Max rent:"));
        panel.add(valueField);

        int choice = JOptionPane.showConfirmDialog(this, panel, "Search Car",
                JOptionPane.OK_CANCEL_OPTION);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String value = valueField.getText().trim();
        if (value.equals("")) {
            showError("Please enter a value to search.");
            return;
        }

        if (searchBy.getSelectedIndex() == 0) {
            showCars("CARS OF TYPE: " + value, system.searchByType(value));
        } else {
            try {
                int price = Integer.parseInt(value);
                showCars("CARS WITH RENT UP TO Rs. " + price + " PER DAY", system.searchByPrice(price));
            } catch (NumberFormatException ex) {
                showError("Price must be a number.");
            }
        }
    }

    private void registerCustomer() {
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

    private void bookCar() {
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

    private void returnCar() {
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

    // ---------- methods that show lists in the output area ----------

    private void showCars(String title, ArrayList<Car> list) {
        String text = title + "\n\n";
        if (list.size() == 0) {
            text += "No cars found.";
        } else {
            text += Car.getHeading() + "\n";
            text += "-------------------------------------------------------\n";
            for (Car c : list) {
                text += c.displayCar() + "\n";
            }
        }
        output.setText(text);
    }

    private void showCustomers() {
        String text = "ALL CUSTOMERS\n\n";
        if (system.getCustomers().size() == 0) {
            text += "No customers registered.";
        } else {
            text += Customer.getHeading() + "\n";
            text += "-------------------------------------------------\n";
            for (Customer c : system.getCustomers()) {
                text += c.displayCustomer() + "\n";
            }
        }
        output.setText(text);
    }

    private void showBookings() {
        String text = "ALL BOOKINGS\n\n";
        if (system.getBookings().size() == 0) {
            text += "No bookings yet.";
        } else {
            text += Booking.getHeading() + "\n";
            text += "---------------------------------------------------\n";
            for (Booking b : system.getBookings()) {
                text += b.displayBooking() + "\n";
            }
        }
        output.setText(text);
    }

    // ---------- small helper methods ----------

    // Shows a window with one text box for each label.
    // Returns the typed values, or null if the user pressed Cancel.
    private String[] showForm(String title, String[] labels) {
        JPanel panel = new JPanel(new GridLayout(labels.length, 2, 5, 8));
        JTextField[] fields = new JTextField[labels.length];
        for (int i = 0; i < labels.length; i++) {
            panel.add(new JLabel(labels[i]));
            fields[i] = new JTextField(15);
            panel.add(fields[i]);
        }

        int choice = JOptionPane.showConfirmDialog(this, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return null;
        }

        String[] values = new String[labels.length];
        for (int i = 0; i < labels.length; i++) {
            values[i] = fields[i].getText().trim();
        }
        return values;
    }

    // true if any of the values is empty
    private boolean hasEmpty(String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals("")) {
                return true;
            }
        }
        return false;
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
