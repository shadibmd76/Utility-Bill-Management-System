import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Page 6: Payment Page (+ Payment History)
public class PaymentPanel extends BasePanel {
    JComboBox<Item> cmbBill = new JComboBox<>();
    JTextField txtAmount = new JTextField(8);
    JTextField txtDate = new JTextField(LocalDate.now().toString(), 10);
    JComboBox<String> cmbMethod = new JComboBox<>(new String[]{"Cash", "bKash", "Nagad", "Bank", "Card"});
    DefaultTableModel model = DBConnection.newModel("Pay ID", "Bill", "Month", "Amount", "Date", "Method");
    JTable table = new JTable(model);

    public PaymentPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        JButton btnPay = new JButton("Save Payment");
        form.add(new JLabel("Bill:"));            form.add(cmbBill);
        form.add(new JLabel("Amount:"));          form.add(txtAmount);
        form.add(new JLabel("Date:"));            form.add(txtDate);
        form.add(new JLabel("Method:"));          form.add(cmbMethod);
        form.add(btnPay);
        add(form, BorderLayout.NORTH);

        add(new JScrollPane(table), BorderLayout.CENTER);
        JButton btnDelete = new JButton("Delete Selected Payment");
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnDelete);
        add(bottom, BorderLayout.SOUTH);

        // Bill select korle due amount auto boshe jabe
        cmbBill.addActionListener(e -> {
            Item it = (Item) cmbBill.getSelectedItem();
            if (it != null) txtAmount.setText(String.valueOf(it.value));
        });
        btnPay.addActionListener(e -> pay());
        btnDelete.addActionListener(e -> deletePayment());
    }

    void pay() {
        Item bill = (Item) cmbBill.getSelectedItem();
        if (bill == null) {
            JOptionPane.showMessageDialog(this, "Kono unpaid bill nai!");
            return;
        }
        try {
            double amt = Double.parseDouble(txtAmount.getText().trim());
            LocalDate date = LocalDate.parse(txtDate.getText().trim());
            if (amt <= 0) {
                JOptionPane.showMessageDialog(this, "Amount 0 er beshi hote hobe!");
                return;
            }
            if (amt > bill.value) {
                JOptionPane.showMessageDialog(this, "Payment due (" + bill.value + ") er cheye beshi hote pare na!");
                return;
            }
            if (DBConnection.run("INSERT INTO payments(bill_id, amount, pay_date, method) VALUES(?,?,?,?)",
                    bill.id, amt, Date.valueOf(date), cmbMethod.getSelectedItem().toString())) {
                JOptionPane.showMessageDialog(this, "Payment saved!");
                refresh();
            }
        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Amount ba date (yyyy-MM-dd) thik nai!");
        }
    }

    void deletePayment() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Age ekta payment select koro!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        if (DBConnection.run("DELETE FROM payments WHERE id=?", id)) refresh();
    }

    void refresh() {
        cmbBill.removeAllItems();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT id, bill_name, bill_month, due FROM bill_view WHERE status <> 'Paid' ORDER BY due_date")) {
            while (rs.next()) {
                String text = rs.getInt(1) + " - " + rs.getString(2) + " (" + rs.getString(3)
                        + ") Due: " + rs.getDouble(4);
                cmbBill.addItem(new Item(rs.getInt(1), text, rs.getDouble(4)));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
        DBConnection.loadTable(model,
                "SELECT p.id, b.bill_name, b.bill_month, p.amount, p.pay_date, p.method " +
                "FROM payments p JOIN bills b ON p.bill_id = b.id ORDER BY p.id DESC");
    }
}
