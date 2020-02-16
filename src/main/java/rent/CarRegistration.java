package rent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class CarRegistration extends JFrame implements WindowListener {
    private JPanel carRegPanel;
    private JTextField textCarReg;
    private JTextField textProducer;
    private JTextField textModel;
    private JButton editButton;
    private JButton addButton;
    private JButton cancelButton;
    private JButton deleteButton;
    private JTable table1;
    private JComboBox comboAvalibility;

    private Connection connection;
    private PreparedStatement statement;

    public CarRegistration() throws SQLException, ClassNotFoundException {
        initUI();
        autoID();
        updateTable();
        this.pack();
    }

    public final void initUI() {
        JPanel regPanel = new JPanel();
        getContentPane().add(regPanel);
        regPanel.add(carRegPanel);

        cancelButton.addActionListener(e -> hide());

        addButton.addActionListener(e -> {

            String regNumber = textCarReg.getText();
            String producer = textProducer.getText();
            String model = textModel.getText();
            String avalibility = comboAvalibility.getSelectedItem().toString();

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement = connection.prepareStatement("INSERT INTO car_reg(car_number,car_producer,car_model,car_avalibility) VALUES(?,?,?,?)");
                statement.setString(1, regNumber);
                statement.setString(2, producer);
                statement.setString(3, model);
                statement.setString(4, avalibility);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(carRegPanel, "New car added");

                textProducer.setText("");
                textModel.setText("");
                comboAvalibility.setSelectedIndex(-1);
                textProducer.requestFocus();
                autoID();
                updateTable();

            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(">>> Database connection error <<<");
            }
        });

        Object[] columns = {"Registration", "Producer", "Model", "Available?"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        table1.setModel(model);

        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
                int selectIndex = table1.getSelectedRow();

                textCarReg.setText(defaultTableModel.getValueAt(selectIndex, 0).toString());
                textProducer.setText(defaultTableModel.getValueAt(selectIndex, 1).toString());
                textModel.setText(defaultTableModel.getValueAt(selectIndex, 2).toString());
                comboAvalibility.setSelectedItem(defaultTableModel.getValueAt(selectIndex, 3).toString());
            }
        });

        editButton.addActionListener(e -> {

            DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
            int selectIndex = table1.getSelectedRow();

            try {
                String id = defaultTableModel.getValueAt(selectIndex, 0).toString();
                String producer = textProducer.getText();
                String model1 = textModel.getText();
                String avalibility = comboAvalibility.getSelectedItem().toString();

                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement = connection.prepareStatement("UPDATE car_reg SET car_producer=?,car_model=?,car_avalibility=? WHERE car_number=?");

                statement.setString(1, producer);
                statement.setString(2, model1);
                statement.setString(3, avalibility);
                statement.setString(4, id);
                statement.executeUpdate();
                updateTable();

                JOptionPane.showMessageDialog(carRegPanel, "Record is updated");

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> {

            DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
            int selectIndex = table1.getSelectedRow();

            String carID = defaultTableModel.getValueAt(selectIndex, 0).toString();
            int confirmDelete = JOptionPane.showConfirmDialog(carRegPanel, "Delete selected record?", "Confirm", JOptionPane.YES_NO_OPTION);

            if (confirmDelete == JOptionPane.YES_OPTION) {
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                    statement = connection.prepareStatement("DELETE FROM car_reg WHERE car_number=?");
                    statement.setString(1, carID);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(carRegPanel, "Record deleted");
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
            statement = connection.prepareStatement("SELECT * FROM car_reg");
            ResultSet resultSet = statement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int carRecordsCount = resultSetMetaData.getColumnCount();
            DefaultTableModel defaultTableModel = (DefaultTableModel) table1.getModel();
            defaultTableModel.setRowCount(0);

            while (resultSet.next()) {
                Vector vector = new Vector();

                for (int i = 1; i < carRecordsCount; i++) {
                    vector.add(resultSet.getString("car_number"));
                    vector.add(resultSet.getString("car_producer"));
                    vector.add(resultSet.getString("car_model"));
                    vector.add(resultSet.getString("car_avalibility"));
                }
                defaultTableModel.addRow(vector);
            }
            table1.setModel(defaultTableModel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void autoID() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT MAX(car_number) FROM car_reg");
            resultSet.next();
            resultSet.getString("Max(car_number)");

            if (resultSet.getString("Max(car_number)") == null) {
                textCarReg.setText("C001");
            } else {
                long id = Long.parseLong(resultSet.getString("Max(car_number)").substring(2));
                id++;
                textCarReg.setText("C0" + String.format("%02d", id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new CarRegistration().setVisible(true);

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