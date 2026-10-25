import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

// LoginFrame.java
// The first window of the program.
//   Admin    : needs the Admin ID and the password   (default: admin / admin123)
//   Customer : needs only the Customer ID, no password (for example CU101)
public class LoginFrame extends JFrame implements ActionListener {
    private JRadioButton adminRadio;
    private JRadioButton customerRadio;
    private JLabel idLabel;
    private JTextField idField;
    private JPasswordField passField;
    private JButton loginButton;
    private JButton exitButton;

    private CarRentalSystem system;

    public LoginFrame(CarRentalSystem system) {
        this.system = system;

        setTitle("Car Rental System - Login");
        setSize(400, 300);
        setLocationRelativeTo(null);               // show in the centre of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // heading
        JLabel title = new JLabel("CAR RENTAL SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setOpaque(true);
        title.setBackground(new Color(30, 90, 160));
        title.setForeground(Color.WHITE);
        title.setPreferredSize(new Dimension(100, 50));
        add(title, BorderLayout.NORTH);

        // radio buttons to choose Admin or Customer
        adminRadio = new JRadioButton("Admin", true);
        customerRadio = new JRadioButton("Customer");
        ButtonGroup group = new ButtonGroup();     // only one of the two can be selected
        group.add(adminRadio);
        group.add(customerRadio);
        adminRadio.addActionListener(this);
        customerRadio.addActionListener(this);
        JPanel radios = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        radios.add(adminRadio);
        radios.add(customerRadio);

        // the form: login as, id, password
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 15));
        form.add(new JLabel("Login as:"));
        form.add(radios);
        idLabel = new JLabel("Admin ID:");
        form.add(idLabel);
        idField = new JTextField(15);
        form.add(idField);
        form.add(new JLabel("Password:"));
        passField = new JPasswordField(15);
        form.add(passField);

        // the FlowLayout panel keeps the text boxes at their normal size
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        center.add(form);
        add(center, BorderLayout.CENTER);

        // buttons
        JPanel buttons = new JPanel(new FlowLayout());
        loginButton = new JButton("Login");
        exitButton = new JButton("Exit");
        loginButton.addActionListener(this);
        exitButton.addActionListener(this);
        buttons.add(loginButton);
        buttons.add(exitButton);
        add(buttons, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(loginButton);   // Enter key presses Login
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == adminRadio) {
            idLabel.setText("Admin ID:");
            passField.setEnabled(true);
        } else if (source == customerRadio) {
            idLabel.setText("Customer ID:");
            passField.setText("");
            passField.setEnabled(false);               // customers do not need a password
        } else if (source == loginButton) {
            login();
        } else if (source == exitButton) {
            system.close();
            System.exit(0);
        }
    }

    private void login() {
        String id = idField.getText().trim();
        if (id.equals("")) {
            showError("Please enter your ID.");
            return;
        }

        try {
            if (adminRadio.isSelected()) {
                // admin: id + password are checked in the database
                String password = new String(passField.getPassword());
                Admin admin = new Admin(id, password);
                if (admin.login(system)) {
                    dispose();                          // close the login window
                    new AdminFrame(system, admin).setVisible(true);
                } else {
                    showError("Wrong Admin ID or Password.");
                    passField.setText("");
                }
            } else {
                // customer: only the id is checked
                Customer customer = system.findCustomer(id);
                if (customer == null) {
                    showError("Customer ID not found. Please ask the admin to register you.");
                } else {
                    dispose();
                    new CustomerFrame(system, customer).setVisible(true);
                }
            }
        } catch (SQLException ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
}
