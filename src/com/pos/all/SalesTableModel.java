package com.pos.all;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class SalesTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private List<Sale> sales = new ArrayList<>();
    private final String[] columnNames = {
        "Transaction ID", "Date & Time", "Cashier", "Total Amount", "Items", "Payment Method"
    };
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    
    @Override
    public int getRowCount() {
        return sales.size();
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
    public Object getValueAt(int row, int col) {
        Sale sale = sales.get(row);
        
        switch (col) {
            case 0:
                return sale.getTransactionId();
            case 1:
                return dateFormat.format(sale.getDate());
            case 2:
                return sale.getCashierName();
            case 3:
                return "₱" + decimalFormat.format(sale.getTotalAmount());
            case 4:
                return sale.getItemCount();
            case 5:
                return sale.getPaymentMethod().toUpperCase();
            default:
                return null;
        }
    }
    
    @Override
    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return String.class;
            case 4:
                return Integer.class;
            case 5:
                return String.class;
            default:
                return Object.class;
        }
    }
    
    public void setSales(List<Sale> sales) {
        this.sales = sales;
        fireTableDataChanged();
    }
    
    public List<Sale> getSales() {
        return sales;
    }
    
    public Sale getSaleAt(int row) {
        if (row >= 0 && row < sales.size()) {
            return sales.get(row);
        }
        return null;
    }
}
