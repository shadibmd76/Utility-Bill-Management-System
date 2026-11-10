import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

// Page 4: Add Bill
public class AddBillPanel extends BasePanel {
    JTextField txtName = new JTextField();
    JComboBox<Item> cmbCategory = new JComboBox<>();
    JTextField txtMonth = new JTextField(YearMonth.now().toString());
    JTextField txtAmount = new JTextField();
    JTextField txtBillDate = new JTextField(LocalDate.now().toString());
    JTextField txtDueDate = new JTextField(LocalDate.now().plusDays(15).toString());
    JTextField txtDesc = new JTextField();

    public AddBillPanel() {
        setLayout(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 12));
        form.setBorder(BorderFactory.createEmptyBorder(25, 120, 25, 120));

        form.add(new JLabel("Bill Name:"));              form.add(txtName);
        form.add(new JLabel("Category:"));               form.add(cmbCategory);
        form.add(new JLabel("Bill Month (yyyy-MM):"));   form.add(txtMonth);
        form.add(new JLabel("Bill Amount:"));            form.add(txtAmount);
        form.add(new JLabel("Bill Date (yyyy-MM-dd):")); form.add(txtBillDate);
        form.add(new JLabel("Due Date (yyyy-MM-dd):"));  form.add(txtDueDate);
        form.add(new JLabel("Description:"));            form.add(txtDesc);

        JButton btnSave = new JButton("Save Bill");
        JButton btnClear = new JButton("Clear");
        form.add(btnClear);
        form.add(btnSave);
        add(form, BorderLayout.NORTH);

        btnSave.addActionListener(e -> save());
        btnClear.addActionListener(e -> clear());
    }

    void save() {
        try {
            String name = txtName.getText().trim();
            Item cat = (Item) cmbCategory.getSelectedItem();
            if (name.isEmpty() || cat == null) {
                JOptionPane.showMessageDialog(this, "Bill name ar category dao!");
                return;
            }
            String month = YearMonth.parse(txtMonth.getText().trim()).toString();
            double amount = Double.parseDouble(txtAmount.getText().trim());
            if (amount <= 0) throw new NumberFormatException();
            LocalDate billDate = LocalDate.parse(txtBillDate.getText().trim());
            LocalDate dueDate = LocalDate.parse(txtDueDate.getText().trim());
            if (dueDate.isBefore(billDate)) {
                JOptionPane.showMessageDialog(this, "Due date, bill date er age hote pare na!");
                return;
            }
            boolean ok = DBConnection.run(
                    "INSERT INTO bills(bill_name, category_id, bill_month, amount, bill_date, due_date, description) " +
                    "VALUES(?,?,?,?,?,?,?)",
                    name, cat.id, month, amount, Date.valueOf(billDate), Date.valueOf(dueDate), txtDesc.getText().trim());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Bill saved!");
                clear();
            }
        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Amount ba date/month format thik nai!\nMonth: 2026-11   Date: 2026-11-25");
        }
    }

    void clear() {
        txtName.setText("");
        txtAmount.setText("");
        txtDesc.setText("");
        txtMonth.setText(YearMonth.now().toString());
        txtBillDate.setText(LocalDate.now().toString());
        txtDueDate.setText(LocalDate.now().plusDays(15).toString());
    }

    void refresh() {
        cmbCategory.removeAllItems();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, name FROM categories ORDER BY name")) {
            while (rs.next()) cmbCategory.addItem(new Item(rs.getInt(1), rs.getString(2), 0));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }
}
