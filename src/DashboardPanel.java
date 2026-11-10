import javax.swing.*;
import java.awt.*;

// Page 2: Dashboard
public class DashboardPanel extends BasePanel {
    JLabel[] values = new JLabel[8];
    String[] titles = {"Total Bills", "Total Paid Amount", "Total Due Amount", "This Month Expense",
            "Paid Bills", "Unpaid Bills", "Partial Bills", "Overdue Bills"};

    public DashboardPanel() {
        setLayout(new GridLayout(2, 4, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        for (int i = 0; i < 8; i++) {
            JPanel card = new JPanel(new GridLayout(2, 1));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            JLabel t = new JLabel(titles[i], JLabel.CENTER);
            values[i] = new JLabel("0", JLabel.CENTER);
            values[i].setFont(new Font("Arial", Font.BOLD, 24));
            card.add(t);
            card.add(values[i]);
            add(card);
        }
    }

    void refresh() {
        values[0].setText(String.valueOf((int) DBConnection.number("SELECT COUNT(*) FROM bills")));
        values[1].setText(String.format("%.2f", DBConnection.number("SELECT IFNULL(SUM(amount),0) FROM payments")));
        values[2].setText(String.format("%.2f", DBConnection.number("SELECT IFNULL(SUM(due),0) FROM bill_view")));
        values[3].setText(String.format("%.2f", DBConnection.number(
                "SELECT IFNULL(SUM(amount),0) FROM payments " +
                "WHERE DATE_FORMAT(pay_date,'%Y-%m') = DATE_FORMAT(CURDATE(),'%Y-%m')")));
        String[] st = {"Paid", "Unpaid", "Partial", "Overdue"};
        for (int i = 0; i < 4; i++) {
            values[4 + i].setText(String.valueOf((int) DBConnection.number(
                    "SELECT COUNT(*) FROM bill_view WHERE status=?", st[i])));
        }
    }
}
