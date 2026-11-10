import javax.swing.*;

// Login er por main window. Sob page tab hisebe ache
public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Utility Bill Management System");
        setSize(900, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", new DashboardPanel());
        tabs.addTab("Categories", new CategoryPanel());
        tabs.addTab("Add Bill", new AddBillPanel());
        tabs.addTab("View Bills", new ViewBillPanel());
        tabs.addTab("Payment", new PaymentPanel());
        tabs.addTab("Due Management", new DuePanel());
        tabs.addTab("Monthly Report", new ReportPanel());

        // Tab change hole data notun kore load hobe
        tabs.addChangeListener(e -> ((BasePanel) tabs.getSelectedComponent()).refresh());
        ((BasePanel) tabs.getSelectedComponent()).refresh();
        add(tabs);
    }
}
