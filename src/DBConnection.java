import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

// MySQL connection + chhoto chhoto helper method
public class DBConnection {
    static final String URL = "jdbc:mysql://localhost:3306/utility_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "Shadib";   // <-- tomar MySQL password ekhane dao (XAMPP hole faka)

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // INSERT / UPDATE / DELETE chalanor jonno
    public static boolean run(String sql, Object... params) {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }

    // Ekta number (COUNT / SUM) ferot ane
    public static double number(String sql, Object... params) {
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
        return 0;
    }

    // SELECT er result JTable er model e bhore deya
    public static void loadTable(DefaultTableModel model, String sql, Object... params) {
        model.setRowCount(0);
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
            ResultSet rs = ps.executeQuery();
            int cols = model.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
    }

    // Edit kora jabe na emon table model
    public static DefaultTableModel newModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }
}
