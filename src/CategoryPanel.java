import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Page 3: Category Management
public class CategoryPanel extends BasePanel {
    JTextField txtName = new JTextField(15);
    DefaultTableModel model = DBConnection.newModel("ID", "Category Name");
    JTable table = new JTable(model);

    public CategoryPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Add Category");
        JButton btnDelete = new JButton("Delete Selected");
        top.add(new JLabel("Category Name:"));
        top.add(txtName);
        top.add(btnAdd);
        top.add(btnDelete);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Category name dao!");
                return;
            }
            if (DBConnection.run("INSERT INTO categories(name) VALUES(?)", name)) {
                txtName.setText("");
                refresh();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Age ekta category select koro!");
                return;
            }
            int id = (int) model.getValueAt(row, 0);
            if (DBConnection.number("SELECT COUNT(*) FROM bills WHERE category_id=?", id) > 0) {
                JOptionPane.showMessageDialog(this, "Ei category te bill ache, delete kora jabe na!");
                return;
            }
            if (DBConnection.run("DELETE FROM categories WHERE id=?", id)) refresh();
        });
    }

    void refresh() {
        DBConnection.loadTable(model, "SELECT id, name FROM categories ORDER BY id");
    }
}
