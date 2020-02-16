package rent;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

public class MainView extends JFrame implements WindowListener {
    private JPanel mainViewPanel;
    private JButton carRegistrationButton;
    private JButton logoutButton;
    private JButton rentalButton;
    private JButton returnButton;
    private JButton customerButton;

    public MainView() {
        initUI();
        this.pack();
    }

    public final void initUI() {
        JPanel mw = new JPanel();
        getContentPane().add(mw);
        mw.add(mainViewPanel);

        logoutButton.addActionListener(e -> {
            this.dispose();
            Login aLogin = new Login();
            aLogin.setVisible(true);
        });

        carRegistrationButton.addActionListener(e -> {
            CarRegistration aCarRegistration;
            try {
                aCarRegistration = new CarRegistration();
                aCarRegistration.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        rentalButton.addActionListener(e -> {
            Rental aRental;
            try {
                aRental = new Rental();
                aRental.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        customerButton.addActionListener(e -> {
            Customer aCustomer;
            try {
                aCustomer = new Customer();
                aCustomer.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        returnButton.addActionListener(e -> {
            Return aReturn;
            try {
                aReturn = new Return();
                aReturn.setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
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