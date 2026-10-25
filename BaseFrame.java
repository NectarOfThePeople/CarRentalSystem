import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

// BaseFrame.java
// The part that is the same in the Admin window and the Customer window:
// heading, menu buttons on the left, output area on the right and some helper methods.
// AdminFrame and CustomerFrame extend this class and write their own actionPerformed().
public abstract class BaseFrame extends JFrame implements ActionListener {
    protected CarRentalSystem system;
    protected JTextArea output;

    public BaseFrame(CarRentalSystem system, String windowTitle, String[] menuNames) {
        this.system = system;

        setTitle(windowTitle);
        setSize(850, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // heading
        JLabel title = new JLabel(windowTitle.toUpperCase(), JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setOpaque(true);
        title.setBackground(new Color(30, 90, 160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // menu buttons on the left side
        JPanel menu = new JPanel(new GridLayout(menuNames.length, 1, 5, 5));
        for (int i = 0; i < menuNames.length; i++) {
            JButton button = new JButton(menuNames[i]);
            button.setPreferredSize(new Dimension(190, 36));
            button.addActionListener(this);
            menu.add(button);
        }
        JPanel left = new JPanel(new BorderLayout());
        left.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 0));
        left.add(menu, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);

        // output area on the right side
        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(output);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 10, 10),
                BorderFactory.createTitledBorder("Output")));
        add(scroll, BorderLayout.CENTER);
    }

    // ---------- menu options that both windows have ----------

    protected void searchCar() throws SQLException {
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

    // goes back to the login window
    protected void logout() {
        dispose();
        new LoginFrame(system).setVisible(true);
    }

    // ---------- methods that show lists in the output area ----------

    protected void showCars(String title, ArrayList<Car> list) {
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

    protected void showBookings(String title, ArrayList<Booking> list) {
        String text = title + "\n\n";
        if (list.size() == 0) {
            text += "No bookings found.";
        } else {
            text += Booking.getHeading() + "\n";
            text += "---------------------------------------------------\n";
            for (Booking b : list) {
                text += b.displayBooking() + "\n";
            }
        }
        output.setText(text);
    }

    // ---------- small helper methods ----------

    // Shows a window with one text box for each label.
    // Returns the typed values, or null if the user pressed Cancel.
    protected String[] showForm(String title, String[] labels) {
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
    protected boolean hasEmpty(String[] values) {
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals("")) {
                return true;
            }
        }
        return false;
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
