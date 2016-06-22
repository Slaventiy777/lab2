
package com.backgammon.view;

import com.backgammon.model.PlayerModel;
import com.backgammon.controller.BackgammonController;
import com.backgammon.model.Settings;
import lab2.protocol.User;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;


public class PlayerListView extends AbstractView implements Observer {

    static Logger log = Logger.getLogger(PlayerListView.class.getName());

    private PlayerModel model;
    private PlayerListTableModel tableModel;

    private BackgammonController listener;

    private JFrame frame;
    private JPanel connectPanel;
    private JButton connectButton;
    private JScrollPane playerListScrollPane;
    private JTable playerListTable;

    private int rowIndex;
    private User selectedUser;

    public PlayerListView(PlayerModel model) {

        this.model = model;

        this.model.observable().addObserver(this);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeUI();
                update();
            }
        });
    }

    public void setListener(BackgammonController listener) {
        this.listener = listener;
    }

    public JFrame getFrame() {
        return frame;
    }

    protected void initializeUI() {

        frame = new JFrame();
        frame.setBounds(300, 300, 300, 300);
        frame.setTitle("Backgammon " + "[" + model.getMyNickname() + "]");

        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(200, 100));

        connectPanel = new JPanel();
        connectPanel.setLayout(new FlowLayout());

        connectButton = new JButton();
        connectButton.setText("Connect");
        connectButton.addActionListener(new ConnectActionL());
        connectPanel.add(connectButton);

        frame.add(connectPanel, BorderLayout.NORTH);

        tableModel = new PlayerListTableModel();
        playerListTable = new JTable(tableModel);

        playerListScrollPane = new JScrollPane();
        playerListScrollPane.setViewportView(playerListTable);

        frame.add(playerListScrollPane, BorderLayout.CENTER);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ActionEvent actionEvent = new ActionEvent(this, 0, Settings.ACTION_EXIT);
                listener.actionPerformed(actionEvent);
            }
        });

    }

    public void update() {
        update(null, null);
    }

    public void update(Observable o, Object arg) {
        tableModel.setUserSet(model.getPlayerSet());
        playerListTable.setModel(tableModel);

        frame.validate();
        frame.repaint();
    }


    private class ConnectActionL implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            rowIndex = playerListTable.getSelectedRow();
            if (rowIndex != -1) {
                selectedUser = tableModel.getUser(rowIndex);
                listener.sendRequest(selectedUser);
                setEnabledConnect(false);
            }

        }
    }

    public void setEnabledConnect(final boolean enabled) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                connectButton.setEnabled(enabled);
            }
        });
    }


    private class PlayerListTableModel extends AbstractTableModel {

        private int columnCount = 2;
        private Set<User> playerSet;
        private String[] columnNames = {"Player", "Rank"};

        public PlayerListTableModel() {
            playerSet = new HashSet<User>();
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            return playerSet.size();
        }

        @Override
        public int getColumnCount() {
            return columnCount;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return getUser(rowIndex).getNickname();
                case 1:
                    return getUser(rowIndex).getRank();
                default:
                    return "";
            }
        }

        public void setUserSet(Set<User> userList) {
            playerSet =  userList;
            fireTableDataChanged();
        }

        public User getUser(int index) {

            User returnedUser = null;

            Iterator<User> it = playerSet.iterator();

            int i = 0;
            while (it.hasNext()) {
                User user = it.next();
                if (i == index) {
                    returnedUser = user;
                    break;
                }

                i++;
            }

            return returnedUser;
        }

    }

}
