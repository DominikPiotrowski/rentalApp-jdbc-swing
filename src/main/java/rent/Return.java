package rent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class Return extends JFrame {
    private JPanel returnPanel;
    private JTextField textCarID;
    private JTextField textCustID;
    private JButton okButton;
    private JButton cancelButton;
    private JTable tableRentedCars;
    private JTextField textRentFeePerDay;
    private JTextField textCharge;
    private JTextField textReturnDate;

    private Connection connection;
    private PreparedStatement statement1;
    private PreparedStatement statement2;
    private PreparedStatement statement3;

    public Return() throws SQLException, ClassNotFoundException {
        initUI();
        updateJTable();
        this.pack();
    }

    public final void initUI() {

        JPanel retPanel = new JPanel();
        getContentPane().add(retPanel);
        retPanel.add(returnPanel);

        Object[] columns = {"Car ID", "Customer ID", "Rent fee", "Rent date", "Return date"};
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        tableRentedCars.setModel(model);

        cancelButton.addActionListener(e -> dispose());

        tableRentedCars.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    DefaultTableModel defaultTableModel = (DefaultTableModel) tableRentedCars.getModel();
                    int selectIndex = tableRentedCars.getSelectedRow();

                    textCarID.setText(defaultTableModel.getValueAt(selectIndex, 0).toString());
                    textCustID.setText(defaultTableModel.getValueAt(selectIndex, 1).toString());
                    textRentFeePerDay.setText(defaultTableModel.getValueAt(selectIndex, 2).toString());
                    textReturnDate.setText(defaultTableModel.getValueAt(selectIndex, 4).toString());

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    Date start = dateFormat.parse(defaultTableModel.getValueAt(selectIndex, 3).toString());
                    Date end = dateFormat.parse(defaultTableModel.getValueAt(selectIndex, 4).toString());
                    int days = (Math.round(end.getTime() - start.getTime())/86400000);
                    int charge;

                    if (days > 0) {
                        charge = days * Integer.parseInt(textRentFeePerDay.getText());
                    } else {
                        charge = 0;
                    }
                    textCharge.setText(String.valueOf(charge));

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
        });

        okButton.addActionListener(e -> {
            String carId = textCarID.getText();
            String customerId = textCustID.getText();
            String returnDate = textReturnDate.getText();

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");

                statement1 = connection.prepareStatement("DELETE FROM rent WHERE car_id=? AND customer_id=? AND return_date=?");
                statement1.setString(1, carId);
                statement1.setString(2, customerId);
                statement1.setString(3, returnDate);
                statement1.executeUpdate();

                statement2 = connection.prepareStatement("UPDATE car_reg SET car_avalibility ='Yes' WHERE car_number = ?");
                statement2.setString(1, carId);
                statement2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Car returned");
                updateJTable();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void updateJTable() {
        tableRentedCars.clearSelection();
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            statement3 = connection.prepareStatement("SELECT * FROM rent");
            ResultSet resultSet = statement3.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int carRecordsCount = resultSetMetaData.getColumnCount();
            DefaultTableModel defaultTableModel = (DefaultTableModel) tableRentedCars.getModel();
            defaultTableModel.setRowCount(0);

            while (resultSet.next()) {
                Vector vector = new Vector();

                for (int i = 1; i < carRecordsCount; i++) {
                    vector.add(resultSet.getString("car_id"));
                    vector.add(resultSet.getString("customer_id"));
                    vector.add(resultSet.getString("rent_fee"));
                    vector.add(resultSet.getString("rent_date"));
                    vector.add(resultSet.getString("return_date"));
                }
                defaultTableModel.addRow(vector);
            }
            tableRentedCars.setModel(defaultTableModel);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Return().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}