package rent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class Customer extends JFrame implements WindowListener {
    private JPanel customerPanel;
    private JTextField textCustID;
    private JTextField textCustName;
    private JButton addButton;
    private JButton editButton;
    private JTable table2;
    private JTextArea textCustAdress;
    private JTextField textCustPhone;
    private JButton deleteButton;
    private JButton cancelButton;

    private Connection connection;
    private PreparedStatement statement;

    public Customer() throws SQLException, ClassNotFoundException {
        initUI();
        autoID();
        updateTable();
        this.pack();
    }

    public final void initUI() {
        JPanel custPanel = new JPanel();
        getContentPane().add(custPanel);
        custPanel.add(customerPanel);

        cancelButton.addActionListener(e -> dispose());

        addButton.addActionListener(e -> {

            String customerID = textCustID.getText();
            String customerName = textCustName.getText();
            String customerAddress = textCustAdress.getText();
            String customerPhone = textCustPhone.getText();

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement = connection.prepareStatement("INSERT INTO customer(customer_id,customer_name,customer_address,customer_phone) VALUES(?,?,?,?)");

                statement.setString(1, customerID);
                statement.setString(2, customerName);
                statement.setString(3, customerAddress);
                statement.setString(4, customerPhone);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(customerPanel, "Customer added");

                textCustName.setText("");
                textCustAdress.setText("");
                textCustPhone.setText("");
                textCustName.requestFocus();
                autoID();
                updateTable();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        Object[] columns = {"CustomerID", "Name", "Address", "Phone"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table2.setModel(model);

        table2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) table2.getModel();
                int selectIndex = table2.getSelectedRow();

                textCustID.setText(defaultTableModel.getValueAt(selectIndex, 0).toString());
                textCustName.setText(defaultTableModel.getValueAt(selectIndex, 1).toString());
                textCustAdress.setText(defaultTableModel.getValueAt(selectIndex, 2).toString());
                textCustPhone.setText(defaultTableModel.getValueAt(selectIndex, 3).toString());
            }
        });

        editButton.addActionListener(e -> {

            DefaultTableModel defaultTableModel = (DefaultTableModel) table2.getModel();
            int selectIndex = table2.getSelectedRow();

            try {
                String customerID = defaultTableModel.getValueAt(selectIndex, 0).toString();
                String customerName = textCustName.getText();
                String customerAddress = textCustAdress.getText();
                String customerPhone = textCustPhone.getText();

                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement = connection.prepareStatement("UPDATE customer SET customer_name=?,customer_address=?,customer_phone=? WHERE customer_id=?");

                statement.setString(1, customerName);
                statement.setString(2, customerAddress);
                statement.setString(3, customerPhone);
                statement.setString(4, customerID);
                statement.executeUpdate();
                updateTable();

                JOptionPane.showMessageDialog(customerPanel, "Record is updated");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener((e) -> {

            DefaultTableModel defaultTableModel = (DefaultTableModel) table2.getModel();
            int selectIndex = table2.getSelectedRow();

            String customerID = defaultTableModel.getValueAt(selectIndex, 0).toString();
            int confirmDelete = JOptionPane.showConfirmDialog(customerPanel, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirmDelete == JOptionPane.YES_OPTION) {
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                    statement = connection.prepareStatement("DELETE FROM customer WHERE customer_id=?");
                    statement.setString(1, customerID);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(customerPanel, "Record deleted");
                    updateTable();

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void updateTable() {

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            statement = connection.prepareStatement("SELECT * FROM customer");
            ResultSet resultSet = statement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int carRecordsCount = resultSetMetaData.getColumnCount();
            DefaultTableModel defaultTableModel = (DefaultTableModel) table2.getModel();
            defaultTableModel.setRowCount(0);

            while (resultSet.next()) {
                Vector vector = new Vector();

                for (int i = 1; i < carRecordsCount; i++) {
                    vector.add(resultSet.getString("customer_id"));
                    vector.add(resultSet.getString("customer_name"));
                    vector.add(resultSet.getString("customer_address"));
                    vector.add(resultSet.getString("customer_phone"));
                }
                defaultTableModel.addRow(vector);
            }
            table2.setModel(defaultTableModel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void autoID() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(customer_id) FROM customer");
            resultSet.next();
            resultSet.getString("MAX(customer_id)");

            if (resultSet.getString("MAX(customer_id)") == null) {
                textCustID.setText("CUST001");
            } else {
                long id = Long.parseLong(resultSet.getString("MAX(customer_id)").substring(4));
                id++;
                textCustID.setText("CUST" + String.format("%03d", id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(">>> Database connection error <<<");
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Customer().setVisible(true);

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        dispose();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}