package com.pos.all;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProductDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField barcodeField;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField stockField;
    private JComboBox<String> categoryCombo;
    private JCheckBox activeCheckBox; // Added active status checkbox
    
    private Product product;
    private boolean isNewProduct;
    private boolean confirmed = false;
    
    /**
     * Create a dialog for adding or editing a product
     * @param parent The parent frame
     * @param product The product to edit, or null for a new product
     */
    public ProductDialog(JFrame parent, Product product) {
        super(parent, product == null ? "Add New Product" : "Edit Product", true);
        
        this.product = product;
        this.isNewProduct = (product == null);
        
        initializeUI();
        loadCategories();
        
        if (!isNewProduct) {
            populateFields();
        }
        
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(400, 500); // Increased size to accommodate the active checkbox
        setResizable(false);
        
        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Barcode
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Barcode:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        barcodeField = new JTextField(20);
        formPanel.add(barcodeField, gbc);
        
        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        formPanel.add(scrollPane, gbc);
        
        // Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Price:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        priceField = new JTextField(20);
        formPanel.add(priceField, gbc);
        
        // Stock
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Stock Quantity:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        stockField = new JTextField(20);
        formPanel.add(stockField, gbc);
        
        // Category
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Category:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        categoryCombo = new JComboBox<>();
        categoryCombo.setEditable(true);
        formPanel.add(categoryCombo, gbc);
        
        // Active Status
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        activeCheckBox = new JCheckBox("Active");
        activeCheckBox.setSelected(true); // Default to active for new products
        formPanel.add(activeCheckBox, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Action listeners
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    confirmed = true;
                    dispose();
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadCategories() {
        // Get all categories from the database
        java.util.List<String> categories = ProductService.getAllCategories();
        
        // Add to combo box
        for (String category : categories) {
            categoryCombo.addItem(category);
        }
    }
    
    private void populateFields() {
        barcodeField.setText(product.getBarcode());
        nameField.setText(product.getName());
        descriptionArea.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStockQuantity()));
        activeCheckBox.setSelected(product.isActive());
        
        String category = product.getCategory();
        if (category != null && !category.isEmpty()) {
            categoryCombo.setSelectedItem(category);
        }
    }
    
    private boolean validateInput() {
        // Check barcode
        if (barcodeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Barcode is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            barcodeField.requestFocus();
            return false;
        }
        
        // Check name
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        // Check price
        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price must be a positive number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price must be a valid number.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            priceField.requestFocus();
            return false;
        }
        
        // Check stock
        try {
            int stock = Integer.parseInt(stockField.getText().trim());
            if (stock < 0) {
                JOptionPane.showMessageDialog(this, "Stock quantity must be a positive integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                stockField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock quantity must be a valid integer.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            stockField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    /**
     * Get the product with data from the form
     * @return The product object
     */
    public Product getProduct() {
        if (!confirmed) {
            return null;
        }
        
        String barcode = barcodeField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        double price = Double.parseDouble(priceField.getText().trim());
        int stock = Integer.parseInt(stockField.getText().trim());
        String category = categoryCombo.getSelectedItem().toString().trim();
        boolean active = activeCheckBox.isSelected();
        
        if (isNewProduct) {
            // For new products, product_id will be assigned by the database
            return new Product(0, barcode, name, description, price, stock, category, active);
        } else {
            // For existing products, keep the original product_id
            return new Product(product.getProductId(), barcode, name, description, price, stock, category, active);
        }
    }
    
    /**
     * Check if user confirmed the dialog
     * @return true if user clicked Save, false otherwise
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}