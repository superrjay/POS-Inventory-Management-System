package com.pos.all;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class UserTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private List<User> users;
    private final String[] columnNames = {"ID", "Username", "Role", "Full Name"};
    
    public UserTableModel() {
        this.users = new ArrayList<>();
    }
    
    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged();
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public User getUserAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < users.size()) {
            return users.get(rowIndex);
        }
        return null;
    }
    
    @Override
    public int getRowCount() {
        return users.size();
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return user.getUserId();
            case 1: return user.getUsername();
            case 2: return user.getRole();
            case 3: return user.getFullName();
            default: return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            default: return String.class;
        }
    }
    
    // Add a new user to the model
    public void addUser(User user) {
        users.add(user);
        fireTableRowsInserted(users.size() - 1, users.size() - 1);
    }
    
    // Update a user in the model
    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == user.getUserId()) {
                users.set(i, user);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }
    
    // Remove a user from the model
    public void removeUser(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                users.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }
}
