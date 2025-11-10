package com.pos.all;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private List<Product> products;
    private final String[] columnNames = {"ID", "Barcode", "Name", "Price", "Stock", "Category", "Status"};
    
    public ProductTableModel() {
        this.products = new ArrayList<>();
    }
    
    public void setProducts(List<Product> products) {
        this.products = products;
        fireTableDataChanged();
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public Product getProductAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < products.size()) {
            return products.get(rowIndex);
        }
        return null;
    }
    
    @Override
    public int getRowCount() {
        return products.size();
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
        Product product = products.get(rowIndex);
        
        switch (columnIndex) {
            case 0: return product.getProductId();
            case 1: return product.getBarcode();
            case 2: return product.getName();
            case 3: return product.getPrice();
            case 4: return product.getStockQuantity();
            case 5: return product.getCategory();
            case 6: return getStatusText(product);
            default: return null;
        }
    }
    
    private String getStatusText(Product product) {
        if (!product.isActive()) {
            return "Inactive";
        } else if (product.getStockQuantity() <= 0) {
            return "Out of Stock";
        } else if (product.getStockQuantity() < 10) {
            return "Low Stock";
        } else {
            return "In Stock";
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class;
            case 3: return Double.class;
            case 4: return Integer.class;
            default: return String.class;
        }
    }
    
    // Add a new product to the model
    public void addProduct(Product product) {
        products.add(product);
        fireTableRowsInserted(products.size() - 1, products.size() - 1);
    }
    
    // Update a product in the model
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == product.getProductId()) {
                products.set(i, product);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }
    
    // Remove a product from the model
    public void removeProduct(int productId) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId() == productId) {
                products.remove(i);
                fireTableRowsDeleted(i, i);
                break;
            }
        }
    }
}