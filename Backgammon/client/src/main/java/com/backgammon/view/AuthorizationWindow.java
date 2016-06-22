package com.backgammon.view;

import com.backgammon.controller.BackgammonController;
import com.backgammon.model.Settings;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationWindow extends JPanel {

    private final BackgammonController listener;

    private final JTextField loginField;
    private final JPasswordField passwordField;
    private final JCheckBox regCheckBox;
    private JButton okButton;
    private JDialog dialog;

    private boolean enterOk;

    public AuthorizationWindow(final BackgammonController listener) {

        this.listener = listener;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

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
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new OKActoinL());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelActionL());
        box3.add(Box.createHorizontalGlue());
        box3.add(okButton);
        box3.add(Box.createHorizontalStrut(12));
        box3.add(cancelButton);

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

        this.add(mainBox);

    }

    private class OKActoinL implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if ( ! isCorrectLogin() ) {
                JOptionPane.showMessageDialog(dialog, Settings.DBG_InvalidUsername, "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ( ! isCorrectPassword() ) {
                JOptionPane.showMessageDialog(dialog, Settings.DBG_InvalidPassword, "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            enterOk = true;
            Settings.setPassword(getPassword());
            Settings.setUsername(getLogin());
            dialog.setVisible(false);

        }
    }

    private class CancelActionL implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ActionEvent actionEvent = new ActionEvent(this, 0, Settings.EXIT);
            listener.actionPerformed(actionEvent);
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

    public void setDefaultUser() {
        this.loginField.setText(Settings.getUsername());
        this.passwordField.setText(Settings.getPassword());
    }


    public boolean showDialog(Component parent, String title) {

        enterOk = false;

        // locate the owner frame
        Frame owner = null;
        if (parent instanceof Frame)
            owner = (Frame) parent;
        else
            owner = (Frame)SwingUtilities.getAncestorOfClass(Frame.class, parent);

        // if first time, or if owner has changed, make new dialog
        if (dialog == null || dialog.getOwner() != owner)
        {
            dialog = new JDialog(owner, true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    ActionEvent actionEvent = new ActionEvent(this, 0, Settings.EXIT);
                    listener.actionPerformed(actionEvent);
                }
            });

            dialog.getContentPane().add(this);
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack();
        }

        // set title and show dialog
        dialog.setTitle(title);
        dialog.setVisible(true);

        return enterOk;

    }

}
