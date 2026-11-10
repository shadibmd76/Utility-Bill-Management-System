import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

// Page 5: View Bill (Search / Update / Delete) + Bill History
public class ViewBillPanel extends BasePanel {
    JTextField txtSearch = new JTextField(15);
    DefaultTableModel model = DBConnection.newModel(
            "ID", "Bill Name", "Category", "Month", "Amount", "Paid", "Due", "Due Date", "Status");
    JTable table = new JTable(model);

    public ViewBillPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSearch = new JButton("Search");
        JButton btnAll = new JButton("Show All");
        top.add(new JLabel("Search (name / category / month):"));
        top.add(txtSearch);
        top.add(btnSearch);
        top.add(btnAll);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnUpdate = new JButton("Update Selected");
        JButton btnDelete = new JButton("Delete Selected");
        bottom.add(btnUpdate);
        bottom.add(btnDelete);
        add(bottom, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> search());
        btnAll.addActionListener(e -> { txtSearch.setText(""); refresh(); });
        btnUpdate.addActionListener(e -> update());
        btnDelete.addActionListener(e -> delete());
    }

    void search() {
        String k = "%" + txtSearch.getText().trim() + "%";
        DBConnection.loadTable(model,
                "SELECT id, bill_name, category, bill_month, amount, paid, due, due_date, status " +
                "FROM bill_view WHERE bill_name LIKE ? OR category LIKE ? OR bill_month LIKE ? ORDER BY id DESC",
                k, k, k);
    }

    void update() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Age ekta bill select koro!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        JTextField name = new JTextField(model.getValueAt(row, 1).toString());
        JTextField amount = new JTextField(model.getValueAt(row, 4).toString());
        JTextField due = new JTextField(model.getValueAt(row, 7).toString());
        Object[] form = {"Bill Name:", name, "Amount:", amount, "Due Date (yyyy-MM-dd):", due};

        if (JOptionPane.showConfirmDialog(this, form, "Update Bill",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            double amt = Double.parseDouble(amount.getText().trim());
            if (amt <= 0 || name.getText().trim().isEmpty()) throw new NumberFormatException();
            LocalDate d = LocalDate.parse(due.getText().trim());
            if (DBConnection.run("UPDATE bills SET bill_name=?, amount=?, due_date=? WHERE id=?",
                    name.getText().trim(), amt, Date.valueOf(d), id)) refresh();
        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Amount ba date thik nai!");
        }
    }

    void delete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Age ekta bill select koro!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        if (JOptionPane.showConfirmDialog(this, "Bill ar tar payment gulo delete hobe. Sure?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (DBConnection.run("DELETE FROM bills WHERE id=?", id)) refresh();
        }
    }

    void refresh() {
        DBConnection.loadTable(model,
                "SELECT id, bill_name, category, bill_month, amount, paid, due, due_date, status " +
                "FROM bill_view ORDER BY id DESC");
    }
}
