import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Page 8: Monthly Report
public class ReportPanel extends BasePanel {
    JTextField txtMonth = new JTextField(8);
    DefaultTableModel model = DBConnection.newModel(
            "Month", "No. of Bills", "Total Bill", "Total Paid (Expense)", "Total Due");
    JTable table = new JTable(model);

    public ReportPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnShow = new JButton("Show");
        JButton btnAll = new JButton("All Months");
        top.add(new JLabel("Month (yyyy-MM):"));
        top.add(txtMonth);
        top.add(btnShow);
        top.add(btnAll);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnShow.addActionListener(e -> {
            String m = txtMonth.getText().trim();
            DBConnection.loadTable(model, query("WHERE bill_month = ?"), m);
        });
        btnAll.addActionListener(e -> { txtMonth.setText(""); refresh(); });
    }

    String query(String where) {
        return "SELECT bill_month, COUNT(*), SUM(amount), SUM(paid), SUM(due) FROM bill_view "
                + where + " GROUP BY bill_month ORDER BY bill_month DESC";
    }

    void refresh() {
        DBConnection.loadTable(model, query(""));
    }
}
