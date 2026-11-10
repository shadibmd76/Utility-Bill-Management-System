import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Page 7: Due Management
public class DuePanel extends BasePanel {
    JComboBox<String> cmbFilter = new JComboBox<>(new String[]{"All Due", "Unpaid", "Partial", "Overdue"});
    DefaultTableModel model = DBConnection.newModel(
            "ID", "Bill Name", "Category", "Month", "Amount", "Paid", "Due", "Due Date", "Status");
    JTable table = new JTable(model);
    JLabel lblTotal = new JLabel("Total Due: 0");

    public DuePanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Show:"));
        top.add(cmbFilter);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTotal, BorderLayout.SOUTH);

        cmbFilter.addActionListener(e -> refresh());
    }

    void refresh() {
        String f = cmbFilter.getSelectedItem().toString();
        String sql = "SELECT id, bill_name, category, bill_month, amount, paid, due, due_date, status FROM bill_view ";
        double total;
        if (f.equals("All Due")) {
            DBConnection.loadTable(model, sql + "WHERE status <> 'Paid' ORDER BY due_date");
            total = DBConnection.number("SELECT IFNULL(SUM(due),0) FROM bill_view WHERE status <> 'Paid'");
        } else {
            DBConnection.loadTable(model, sql + "WHERE status = ? ORDER BY due_date", f);
            total = DBConnection.number("SELECT IFNULL(SUM(due),0) FROM bill_view WHERE status = ?", f);
        }
        lblTotal.setText("Total Due: " + String.format("%.2f", total));
    }
}
