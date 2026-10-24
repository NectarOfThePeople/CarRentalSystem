import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// LoginFrame.java
// The first window of the program. The admin must login to continue.
// Default login:  Admin ID = admin   Password = admin123
public class LoginFrame extends JFrame implements ActionListener {
    private JTextField idField;
    private JPasswordField passField;
    private JButton loginButton;
    private JButton exitButton;

    private Admin admin;
    private CarRentalSystem system;

    public LoginFrame() {
        admin = new Admin("admin", "admin123");
        system = new CarRentalSystem();

        setTitle("Car Rental System - Login");
        setSize(380, 240);
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

        // id and password fields
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.add(new JLabel("Admin ID:"));
        idField = new JTextField(15);
        form.add(idField);
        form.add(new JLabel("Password:"));
        passField = new JPasswordField(15);
        form.add(passField);

        // the FlowLayout panel keeps the text boxes at their normal size
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 25));
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
        if (e.getSource() == loginButton) {
            String id = idField.getText().trim();
            String pass = new String(passField.getPassword());

            if (admin.login(id, pass)) {
                dispose();                              // close the login window
                new MainFrame(system, admin).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Admin ID or Password.",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
                passField.setText("");
            }
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
