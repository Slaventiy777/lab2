package com.backgammon.view;

import com.backgammon.controller.BackgammomBoardController;
import com.backgammon.model.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationWindow extends JFrame {

    private final JTextField loginField;
    private final JPasswordField passwordField;
    private final JCheckBox regCheckBox;

    private final BackgammomBoardController listener;

    public AuthorizationWindow(final BackgammomBoardController listener) {

        super("Authorization");

        this.listener = listener;

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Box box1 = Box.createHorizontalBox();
        JLabel loginLabel = new JLabel("Login:");
        loginField = new JTextField(15);
        box1.add(loginLabel);
        box1.add(Box.createHorizontalStrut(6));
        box1.add(loginField);

        Box box2 = Box.createHorizontalBox();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(15);
        box2.add(passwordLabel);
        box2.add(Box.createHorizontalStrut(6));
        box2.add(passwordField);

        Box box3 = Box.createHorizontalBox();
        JButton ok = new JButton("OK");
        ok.addMouseListener(new OKMouseL());
        JButton cancel = new JButton("Cancel");
        box3.add(Box.createHorizontalGlue());
        box3.add(ok);
        box3.add(Box.createHorizontalStrut(12));
        box3.add(cancel);

        Box box4 = Box.createHorizontalBox();
        regCheckBox = new JCheckBox("Registration");
        box4.add(Box.createHorizontalGlue());
        box4.add(regCheckBox);

        loginLabel.setPreferredSize(passwordLabel.getPreferredSize());

        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(12,12,12,12));
        mainBox.add(box1);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box2);
        mainBox.add(Box.createVerticalStrut(12));
        mainBox.add(box4);
        mainBox.add(Box.createVerticalStrut(17));
        mainBox.add(box3);
        setContentPane(mainBox);
        pack();
        setResizable(false);

    }

    private class OKMouseL implements MouseListener {

        public void mouseClicked(MouseEvent event) {
            if ( !isCorrectLogin() ) {
                JOptionPane.showMessageDialog(null,
                        Settings.DBG_InvalidUsername,
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ( !isCorrectPassword() ) {
                JOptionPane.showMessageDialog(null,
                        Settings.DBG_InvalidPassword,
                        "Error Message",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        public void mouseEntered(MouseEvent event) {}

        public void mouseExited(MouseEvent event) {}

        public void mousePressed(MouseEvent event) {}

        public void mouseReleased(MouseEvent event) {}

    }

    private class CancelActionL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private boolean isCorrectLogin() {
        Pattern pattern = Settings.USERNAME_PATTERN;
        Matcher matcher = pattern.matcher(getLogin());
        return matcher.matches();
    }

    private boolean isCorrectPassword() {
        Pattern pattern = Settings.PASS_PATTERN;
        Matcher matcher = pattern.matcher(getPassword());
        return matcher.matches();
    }

    public String getLogin() {
        String login = loginField.getText();
        if (login == null)
            login = "";

        return login;
    }

    public String getPassword() {
        String password = passwordField.getText();
        if (password == null)
            password = "";

        return password;
    }

    public boolean isSelectedRegistration() {
        return regCheckBox.isSelected();
    }

}
