package rent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Login extends JFrame implements WindowListener {

    private JPanel loginPanel;
    private JPanel login;
    private JTextField loginTextfield;
    private JTextField passwordTextfield;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginButton;
    private JButton cancelButton;

    public Login() {
        initUI();
        this.pack();
    }

    public final void initUI() {
        JPanel lp = new JPanel();
        getContentPane().add(lp);
        lp.add(loginPanel);
        addWindowListener(this);

        loginButton.addActionListener(e -> {
            String username = loginTextfield.getText();
            String password = passwordTextfield.getText();

            if (username.equals("admin") && password.equals("admin")) {
                MainView mainView = new MainView();
                Login.this.hide();
                mainView.setVisible(true);

            } else {
                JOptionPane.showMessageDialog(login, "Username or password incorrect.");
                cancelButton.requestFocus();
            }
        });

        cancelButton.addActionListener(e -> {
            loginTextfield.setText("");
            passwordTextfield.setText("");
        });
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent wn) {
        dispose();
        System.exit(0);
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