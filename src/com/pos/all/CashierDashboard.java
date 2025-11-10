package com.pos.all;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
 
public class CashierDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50);     // Green
    private static final Color DANGER_COLOR = new Color(211, 47, 47);      // Red
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Color LIGHT_TEXT = new Color(250, 250, 250);      // Off White
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    @SuppressWarnings("unused")
	private static final int BORDER_RADIUS = 8;
 
    private User user;
    private List<CartItem> cart = new ArrayList<>();
    private JTable cartTable;
    private JLabel totalLabel;
    private JTextField barcodeField;
    private JSpinner quantitySpinner;
    private JLabel userLabel;
    private JPanel headerPanel;
    private JPanel statusPanel;
    private JLabel statusLabel;
 
    public CashierDashboard(User user) {
        this.user = user;
        initializeUI();
    }


	private void initializeUI() {
        setTitle("Supermarket POS System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainContainer.setBackground(SECONDARY_COLOR);
 
        // Create header
        headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
 
        // Create content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(SECONDARY_COLOR);
 
        // Create left panel with barcode scanner
        JPanel scannerPanel = createScannerPanel();
        contentPanel.add(scannerPanel, BorderLayout.NORTH);
 
        // Create cart panel
        JPanel cartPanel = createCartPanel();
        contentPanel.add(cartPanel, BorderLayout.CENTER);
 
        // Create bottom panel
        JPanel bottomPanel = createBottomPanel();
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
 
        // Add content panel to main container
        mainContainer.add(contentPanel, BorderLayout.CENTER);
 
        // Create status bar
        statusPanel = createStatusPanel();
        mainContainer.add(statusPanel, BorderLayout.SOUTH);
 
        // Add main container to frame
        add(mainContainer);
    }
 
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
 
        JLabel titleLabel = new JLabel("Supermarket POS");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(LIGHT_TEXT);
 
        // User info and logout panel (right side)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setBackground(PRIMARY_COLOR);
        
        userLabel = new JLabel("Cashier: " + user.getFullName());
        userLabel.setFont(REGULAR_FONT);
        userLabel.setForeground(LIGHT_TEXT);
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setForeground(PRIMARY_COLOR);
        logoutButton.setBackground(LIGHT_TEXT);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(this::handleLogout);
        
        userPanel.add(userLabel);
        userPanel.add(logoutButton);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(userPanel, BorderLayout.EAST);
 
        return panel;
    }
 
    private JPanel createScannerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(createRoundedBorder("Scan Products", PRIMARY_COLOR));
        panel.setBackground(Color.WHITE);
 
        // Input fields panel
        JPanel inputFieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        inputFieldsPanel.setBackground(Color.WHITE);
 
        // Quantity section
        JPanel quantityPanel = new JPanel(new BorderLayout(5, 0));
        quantityPanel.setBackground(Color.WHITE);
 
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(REGULAR_FONT);
        quantityLabel.setForeground(TEXT_COLOR);
 
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 9999, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        quantitySpinner.setFont(REGULAR_FONT);
        JComponent editor = quantitySpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) editor).getTextField().setColumns(3);
        }
 
        quantityPanel.add(quantityLabel, BorderLayout.WEST);
        quantityPanel.add(quantitySpinner, BorderLayout.CENTER);
        inputFieldsPanel.add(quantityPanel);
 
        // Barcode section
        JPanel barcodePanel = new JPanel(new BorderLayout(5, 0));
        barcodePanel.setBackground(Color.WHITE);
 
        JLabel barcodeLabel = new JLabel("Barcode:");
        barcodeLabel.setFont(REGULAR_FONT);
        barcodeLabel.setForeground(TEXT_COLOR);
 
        barcodeField = new JTextField(20);
        barcodeField.setFont(REGULAR_FONT);
        barcodeField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
 
        barcodePanel.add(barcodeLabel, BorderLayout.WEST);
        barcodePanel.add(barcodeField, BorderLayout.CENTER);
        inputFieldsPanel.add(barcodePanel);
 
        // Scan button
        JButton scanButton = createStyledButton("Scan/Enter", PRIMARY_COLOR, LIGHT_TEXT);
        scanButton.addActionListener(this::handleScanAction);
        inputFieldsPanel.add(scanButton);
 
        // Add key listener for Enter key
        barcodeField.addActionListener(e -> scanButton.doClick());
 
        panel.add(inputFieldsPanel);
 
        return panel;
    }
 
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createRoundedBorder("Shopping Cart", PRIMARY_COLOR));
        panel.setBackground(Color.WHITE);
 
        // Create custom table model
        CartTableModel tableModel = new CartTableModel(cart);
        cartTable = new JTable(tableModel);
        customizeTable(cartTable);
 
        // Add action to remove items from cart
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem removeItem = new JMenuItem("Remove Item");
        removeItem.setFont(REGULAR_FONT);
        removeItem.addActionListener(e -> {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow >= 0) {
                cart.remove(selectedRow);
                updateCartDisplay();
                showStatus("Item removed from cart", false);
            }
        });
        popupMenu.add(removeItem);
 
        cartTable.setComponentPopupMenu(popupMenu);
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int r = cartTable.rowAtPoint(e.getPoint());
                if (r >= 0 && r < cartTable.getRowCount()) {
                    cartTable.setRowSelectionInterval(r, r);
                } else {
                    cartTable.clearSelection();
                }
            }
        });
 
        JScrollPane scrollPane = new JScrollPane(cartTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        panel.add(scrollPane, BorderLayout.CENTER);
 
        return panel;
    }
 
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
        panel.setBackground(SECONDARY_COLOR);
 
        // Cart actions panel (left side)
        JPanel cartActionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cartActionsPanel.setBackground(SECONDARY_COLOR);
 
        JButton clearButton = createStyledButton("Cancel Transaction", DANGER_COLOR, LIGHT_TEXT);
        clearButton.addActionListener(this::handleCancelTransaction);
        cartActionsPanel.add(clearButton);
 
        // Total and checkout panel (right side)
        JPanel checkoutPanel = new JPanel(new BorderLayout(15, 0));
        checkoutPanel.setBackground(SECONDARY_COLOR);
 
        totalLabel = new JLabel("Total: ₱ 0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        totalLabel.setForeground(TEXT_COLOR);
 
        JButton checkoutButton = createStyledButton("Checkout", SUCCESS_COLOR, LIGHT_TEXT);
        checkoutButton.setPreferredSize(new Dimension(150, 50));
        checkoutButton.addActionListener(this::handleCheckout);
 
        checkoutPanel.add(totalLabel, BorderLayout.CENTER);
        checkoutPanel.add(checkoutButton, BorderLayout.EAST);
 
        panel.add(cartActionsPanel, BorderLayout.WEST);
        panel.add(checkoutPanel, BorderLayout.EAST);
 
        return panel;
    }
 
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(224, 224, 224));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
 
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
 
        JLabel timeLabel = new JLabel(new java.util.Date().toString());
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
 
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(timeLabel, BorderLayout.EAST);
 
        // Add timer to update the time
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(new java.util.Date().toString());
        });
        timer.start();
 
        return panel;
    }
 
    private void customizeTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_COLOR);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
 
        // Center align for quantity column
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
 
        // Right align for price and subtotal columns
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
 
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(300); // Product name
        columnModel.getColumn(1).setPreferredWidth(100); // Price
        columnModel.getColumn(2).setPreferredWidth(80);  // Quantity
        columnModel.getColumn(3).setPreferredWidth(120); // Subtotal
 
        // Table header customization
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
    }
 
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
 
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(darkenColor(bgColor));
            }
 
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
 
        return button;
    }
 
    private Border createRoundedBorder(String title, Color color) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
            title
        );
        titledBorder.setTitleFont(HEADER_FONT);
        titledBorder.setTitleColor(color);
        titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
 
        return BorderFactory.createCompoundBorder(
            titledBorder,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }
 
    private Color darkenColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        return Color.getHSBColor(hsb[0], hsb[1], Math.max(0.0f, hsb[2] - 0.1f));
    }
 
    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? DANGER_COLOR : TEXT_COLOR);
 
        // Reset status after 5 seconds
        Timer timer = new Timer(5000, e -> {
            statusLabel.setText("Ready");
            statusLabel.setForeground(TEXT_COLOR);
        });
        timer.setRepeats(false);
        timer.start();
    }
 
    private void handleScanAction(ActionEvent e) {
        String barcode = barcodeField.getText().trim();
        if (barcode.isEmpty()) {
            showErrorDialog("Please enter a barcode");
            return;
        }
 
        Product product = ProductService.getProductByBarcode(barcode);
        if (product != null) {
            int quantity = (Integer) quantitySpinner.getValue();
            addToCart(product, quantity);
            barcodeField.setText("");
            barcodeField.requestFocus();
            quantitySpinner.setValue(1);
            showStatus("Product added to cart: " + product.getName(), false);
        } else {
            showErrorDialog("Product not found: " + barcode);
            showStatus("Product not found: " + barcode, true);
        }
    }
 
    private void handleCancelTransaction(ActionEvent e) {
        if (cart.isEmpty()) {
            showInfoDialog("No transaction to cancel");
            return;
        }
 
        if (showConfirmDialog("Are you sure you want to cancel this transaction?", "Confirm Cancel")) {
            cancelTransaction();
        }
    }
 
    private void cancelTransaction() {
        cart.clear();
        updateCartDisplay();
        barcodeField.setText("");
        quantitySpinner.setValue(1);
        barcodeField.requestFocus();
 
        showInfoDialog("Transaction cancelled successfully");
        showStatus("Transaction cancelled", false);
    }
 
    private void handleCheckout(ActionEvent e) {
        if (cart.isEmpty()) {
            showErrorDialog("Cart is empty");
            return;
        }
 
        PaymentDialog paymentDialog = new PaymentDialog(this, calculateTotal());
        paymentDialog.setVisible(true);
 
        if (paymentDialog.isPaymentSuccessful()) {
            boolean success = TransactionService.processTransaction(
                user.getUserId(), 
                cart, 
                paymentDialog.getPaymentMethod()
            );
 
            if (success) {
                generateReceipt(paymentDialog);
                cart.clear();
                updateCartDisplay();
                showSuccessMessage();
                showStatus("Transaction completed successfully", false);
            }
        }
    }
    
    /**
     * Handle logout action
     */
    private void handleLogout(ActionEvent e) {
        // Check if there's an ongoing transaction
        if (!cart.isEmpty()) {
            if (!showConfirmDialog("You have an active transaction. Are you sure you want to logout?", "Confirm Logout")) {
                return;
            }
        }
        
        // Show confirmation dialog
        if (showConfirmDialog("Are you sure you want to logout?", "Logout Confirmation")) {
            // Display logout status
            showStatus("Logging out...", false);
            
            // Small delay for visual feedback
            Timer timer = new Timer(800, event -> {
                // Close current window
                dispose();
                
                // Return to login screen
                new LoginFrame().setVisible(true);
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
 
    private void addToCart(Product product, int quantity) {
        // Check if product already in cart
        for (CartItem item : cart) {
            if (item.getProduct().getProductId() == product.getProductId()) {
                item.setQuantity(item.getQuantity() + quantity);
                updateCartDisplay();
                return;
            }
        }
 
        // Add new item to cart with specified quantity
        cart.add(new CartItem(product, quantity));
        updateCartDisplay();
    }
 
    private void updateCartDisplay() {
        ((AbstractTableModel) cartTable.getModel()).fireTableDataChanged();
        double total = calculateTotal();
 
        // Format currency with NumberFormat
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        String formattedTotal = currencyFormat.format(total)
            .replace("$", "₱ ");
 
        totalLabel.setText("Total: " + formattedTotal);
    }
 
    private double calculateTotal() {
        return cart.stream().mapToDouble(CartItem::getSubtotal).sum();
    }
 
    private void generateReceipt(PaymentDialog paymentDialog) {
        ReceiptGenerator.generateReceipt(
            TransactionService.getLastTransactionId(),
            user.getFullName(),
            cart,
            paymentDialog.getPaymentMethod(),
            paymentDialog.getAmountTendered()
        );
    }
 
    private void showSuccessMessage() {
        showInfoDialog("Transaction completed successfully");
    }
 
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
 
    private void showInfoDialog(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
 
    private boolean showConfirmDialog(String message, String title) {
        int result = JOptionPane.showConfirmDialog(
            this,
            message,
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
 
        return result == JOptionPane.YES_OPTION;
    }
}
 
// CartItem class remains unchanged
class CartItem {
    private Product product;
    private int quantity;
 
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
 
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
 
    public Product getProduct() {
        return product;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
 
    public void incrementQuantity() {
        this.quantity++;
    }
}
 
// Enhanced CartTableModel with improved formatting
class CartTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private List<CartItem> cart;
    private final String[] columnNames = {"Product", "Price", "Quantity", "Subtotal"};
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
 
    public CartTableModel(List<CartItem> cart) {
        this.cart = cart;
    }
 
    @Override
    public int getRowCount() {
        return cart.size();
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
    public Object getValueAt(int row, int column) {
        CartItem item = cart.get(row);
        switch (column) {
            case 0: return item.getProduct().getName();
            case 1: return currencyFormat.format(item.getProduct().getPrice())
                        .replace("$", "₱ "); // Replace $ with ₱
            case 2: return item.getQuantity();
            case 3: return currencyFormat.format(item.getSubtotal())
                        .replace("$", "₱ "); // Replace $ with ₱
            default: return null;
        }
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 2 ? Integer.class : String.class;
    }
}