package rent;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Rental extends JFrame implements WindowListener {
    private JPanel rentalPanel;
    private JButton rentButton;
    private JButton cancelButton;
    private JTextField textCustName;
    private JComboBox comboCarID;
    private JTextField textFee;
    private JDateChooser txtRentDate = null;
    private JDateChooser txtReturnDate = null;
    private JTextField textAvalibility;
    private JComboBox comboCustID;

    private Connection connection;
    private PreparedStatement statement1;
    private PreparedStatement statement2;
    private PreparedStatement statement3;
    private PreparedStatement statement4;
    private PreparedStatement statement5;
    private PreparedStatement statement6;
    private ResultSet resultSet1;
    private ResultSet resultSet2;
    private ResultSet resultSet3;

    public Rental() throws SQLException, ClassNotFoundException {
        initUI();
        carIDLoader();
        custIDLoader();
        this.pack();
    }

    private void createUIComponents() {
        if (txtRentDate == null) {
            txtRentDate = new JDateChooser();
            txtRentDate.setDateFormatString("yyyy/MM/dd");
        }

        if (txtReturnDate == null) {
            txtReturnDate = new JDateChooser();
            txtReturnDate.setDateFormatString("yyyy/MM/dd");
        }
    }

    public final void initUI() {
        createUIComponents();
        JPanel rentPanel = new JPanel();
        getContentPane().add(rentPanel);
        rentPanel.add(rentalPanel);

        cancelButton.addActionListener(e -> dispose());

        comboCarID.addActionListener(e -> {
            String carId = comboCarID.getSelectedItem().toString();

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement2 = connection.prepareStatement("SELECT * FROM car_reg WHERE car_number = ?");
                statement2.setString(1, carId);
                resultSet1 = statement2.executeQuery();

                if (!resultSet1.next()) {
                    JOptionPane.showMessageDialog(this, "Car not found");
                } else {
                    String aval = resultSet1.getString("car_avalibility");
                    textAvalibility.setText(aval);

                    if (aval.equals("Yes")) {
                        comboCustID.setEnabled(true);
                        textCustName.setEnabled(true);
                        textFee.setEnabled(true);
                        txtRentDate.setEnabled(true);
                        txtReturnDate.setEnabled(true);
                    } else if (aval.equals("No")) {
                        comboCustID.setEnabled(false);
                        textCustName.setEnabled(false);
                        textFee.setEnabled(false);
                        txtRentDate.setEnabled(false);
                        txtReturnDate.setEnabled(false);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        comboCustID.addActionListener(e -> {
            String custID = comboCustID.getSelectedItem().toString();

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
                statement3 = connection.prepareStatement("SELECT * FROM customer WHERE customer_id=?");
                statement3.setString(1, custID);
                resultSet1 = statement3.executeQuery();

                if (!resultSet1.next()) {
                    JOptionPane.showMessageDialog(rentalPanel, "Customer not found");
                } else {
                    String custname = resultSet1.getString("customer_name");
                    textCustName.setText(custname);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        rentButton.addActionListener(e1 -> {
            String carId = comboCarID.getSelectedItem().toString();
            String custId = comboCustID.getSelectedItem().toString();
            String fee = textFee.getText();

            SimpleDateFormat rentDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            String rentDate = rentDateFormat.format(txtRentDate.getDate());

            SimpleDateFormat returnDateformat = new SimpleDateFormat("yyyy/MM/dd");
            String returnDate = returnDateformat.format(txtReturnDate.getDate());

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");

                statement4 = connection.prepareStatement("INSERT INTO rent(car_id, customer_id, rent_fee, rent_date, return_date) VALUES(?,?,?,?,?) ");
                statement4.setString(1, carId);
                statement4.setString(2, custId);
                statement4.setString(3, fee);
                statement4.setString(4, rentDate);
                statement4.setString(5, returnDate);
                statement4.executeUpdate();

                statement5 = connection.prepareStatement("UPDATE car_reg SET car_avalibility = 'No' WHERE car_number = ?");
                statement5.setString(1, carId);
                statement5.executeUpdate();

                JOptionPane.showMessageDialog(this, "Car rented");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void carIDLoader() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            statement1 = connection.prepareStatement("SELECT * FROM car_reg");
            resultSet2 = statement1.executeQuery();
            comboCarID.removeAllItems();

            while (resultSet2.next()) {
                comboCarID.addItem(resultSet2.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void custIDLoader() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/carrentalbase?serverTimezone=UTC", "root", "xxx");
            statement6 = connection.prepareStatement("SELECT * FROM customer");
            resultSet3 = statement6.executeQuery();
            comboCustID.removeAllItems();

            while (resultSet3.next()) {
                comboCustID.addItem(resultSet3.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new Rental().setVisible(true);
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