import javax.swing.*;
import java.awt.*;
import java.sql.*;

// Page 1: Login Page
public class LoginFrame extends JFrame {
    JTextField txtUser = new JTextField(15);
    JPasswordField txtPass = new JPasswordField(15);

    public LoginFrame() {
        setTitle("Utility Bill System - Login");
        setSize(340, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Login", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        form.add(new JLabel("Username:"));
        form.add(txtUser);
        form.add(new JLabel("Password:"));
        form.add(txtPass);
        JButton btnLogin = new JButton("Login");
        form.add(new JLabel(""));
        form.add(btnLogin);
        add(form, BorderLayout.CENTER);

        getRootPane().setDefaultButton(btnLogin);   // Enter chapleo login hobe
        btnLogin.addActionListener(e -> login());
    }

    void login() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username ar Password dao!");
            return;
        }
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT id FROM users WHERE username=? AND password=?")) {
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong username or password!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}
