package com.pos.all;
 
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
 
@SuppressWarnings("unused")
public class AdminDashboard extends JFrame {
    private static final long serialVersionUID = 1L;
 
    // Colors - matching the cashier dashboard for consistency
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color SUCCESS_COLOR = new Color(46, 125, 50);     // Green
    private static final Color DANGER_COLOR = new Color(211, 47, 47);      // Red
    private static final Color WARNING_COLOR = new Color(255, 152, 0);     // Orange
    private static final Color INFO_COLOR = new Color(2, 136, 209);        // Light Blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Color LIGHT_TEXT = new Color(250, 250, 250);      // Off White
 
    // Fonts
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font SMALL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
 
    // Dashboard components
    private JTabbedPane tabbedPane;
    private JTable productsTable;
    private JTable usersTable;
    private JTable salesTable;
    private ProductTableModel productTableModel;
    private UserTableModel userTableModel;
    private SalesTableModel salesTableModel;
 
    // Sales report components
    private JComboBox<String> periodComboBox;
    private JLabel totalSalesLabel;
    private JLabel avgSalesLabel;
    private JLabel transactionCountLabel;
    private JLabel userNameLabel;
    private JLabel statusLabel;
 
    public AdminDashboard() {
        setTitle("Supermarket POS - Admin Dashboard");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        initializeUI();
        loadInitialData();
    }
 
    private void initializeUI() {
        // Main container with padding
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainContainer.setBackground(SECONDARY_COLOR);
 
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
 
        // Create tabbed pane with modern styling
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(TEXT_COLOR);
 
        // Add tabs
        tabbedPane.addTab("Products", null, createProductsPanel(), "Manage Products");
        tabbedPane.addTab("Users", null, createUsersPanel(), "Manage Users");
        tabbedPane.addTab("Sales Reports", null, createSalesPanel(), "View Sales Reports");
        tabbedPane.addTab("Inventory Alerts", null, createInventoryAlertsPanel(), "View Low Stock Items");
        tabbedPane.addTab("Categories", null, createCategoriesPanel(), "Manage Product Categories");
 
        // Add icons to tabs
        // Note: In a real app, you'd use proper icons instead of null
 
        // Create a panel to hold the tabbed pane with some padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
 
        mainContainer.add(contentPanel, BorderLayout.CENTER);
 
        // Create status bar
        JPanel statusPanel = createStatusPanel();
        mainContainer.add(statusPanel, BorderLayout.SOUTH);
 
        // Add main container to frame
        add(mainContainer);
 
        // Add menu bar
        setJMenuBar(createMenuBar());
    }
 
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Supermarket POS Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(LIGHT_TEXT);

        // User info and logout panel (right side)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        userPanel.setBackground(PRIMARY_COLOR);
        
        userNameLabel = new JLabel("Admin: " + "Administrator"); // You may want to make this dynamic
        userNameLabel.setFont(REGULAR_FONT);
        userNameLabel.setForeground(LIGHT_TEXT);
        
        // Add logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setForeground(PRIMARY_COLOR);
        logoutButton.setBackground(LIGHT_TEXT);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(this::handleLogout);
        
        userPanel.add(userNameLabel);
        userPanel.add(logoutButton);

        panel.add(titleLabel, BorderLayout.WEST);
        panel.add(userPanel, BorderLayout.EAST);

        return panel;
    }
 
    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Top panel with search and buttons
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(createRoundedBorder("Products Management", PRIMARY_COLOR));
 
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
 
        JTextField searchField = new JTextField(25);
        searchField.setFont(REGULAR_FONT);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
 
        JButton searchButton = createStyledButton("Search", INFO_COLOR, LIGHT_TEXT);
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));
 
        // Enter key to search
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchProducts(searchField.getText());
                }
            }
        });
 
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
 
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
 
        JButton addButton = createStyledButton("Add Product", SUCCESS_COLOR, LIGHT_TEXT);
        JButton editButton = createStyledButton("Edit Product", PRIMARY_COLOR, LIGHT_TEXT);
        JButton deleteButton = createStyledButton("Delete Product", DANGER_COLOR, LIGHT_TEXT);
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR, LIGHT_TEXT);
 
        addButton.addActionListener(e -> showProductDialog(null));
        editButton.addActionListener(e -> editSelectedProduct());
        deleteButton.addActionListener(e -> deleteSelectedProduct());
        refreshButton.addActionListener(e -> refreshProducts());
 
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(refreshButton);
 
        JPanel operationsPanel = new JPanel(new BorderLayout());
        operationsPanel.setBackground(Color.WHITE);
        operationsPanel.add(searchPanel, BorderLayout.WEST);
        operationsPanel.add(buttonsPanel, BorderLayout.EAST);
 
        topPanel.add(operationsPanel, BorderLayout.CENTER);
 
        // Products table
        productTableModel = new ProductTableModel();
        productsTable = new JTable(productTableModel);
        customizeTable(productsTable);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productsTable.setAutoCreateRowSorter(true);
 
        // Adjust column widths
        productsTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        productsTable.getColumnModel().getColumn(1).setPreferredWidth(120); // Barcode
        productsTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Name
        productsTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Price
        productsTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Stock
        productsTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Category
 
        // Double-click to edit
        productsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedProduct();
                }
            }
        });
 
        JScrollPane scrollPane = new JScrollPane(productsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        // Main panel layout
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
 
        return panel;
    }
 
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Top panel with buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(createRoundedBorder("User Management", PRIMARY_COLOR));
 
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
 
        JButton addButton = createStyledButton("Add User", SUCCESS_COLOR, LIGHT_TEXT);
        JButton editButton = createStyledButton("Edit User", PRIMARY_COLOR, LIGHT_TEXT);
        JButton deleteButton = createStyledButton("Delete User", DANGER_COLOR, LIGHT_TEXT);
 
        addButton.addActionListener(e -> showUserDialog(null));
        editButton.addActionListener(e -> editSelectedUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
 
        buttonsPanel.add(addButton);
        buttonsPanel.add(editButton);
        buttonsPanel.add(deleteButton);
 
        topPanel.add(buttonsPanel, BorderLayout.EAST);
 
        // Users table
        userTableModel = new UserTableModel();
        usersTable = new JTable(userTableModel);
        customizeTable(usersTable);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
 
        return panel;
    }
 
    private JPanel createSalesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Top panel with period selector
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(createRoundedBorder("Sales Reports", PRIMARY_COLOR));
 
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        periodPanel.setBackground(Color.WHITE);
 
        JLabel periodLabel = new JLabel("Select Period:");
        periodLabel.setFont(REGULAR_FONT);
 
        periodComboBox = new JComboBox<>(new String[]{"Today", "This Week", "This Month", "This Year"});
        periodComboBox.setFont(REGULAR_FONT);
        periodComboBox.setBackground(Color.WHITE);
        periodComboBox.setPreferredSize(new Dimension(150, 30));
 
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR, LIGHT_TEXT);
 
        refreshButton.addActionListener(e -> loadSalesReport());
        periodComboBox.addActionListener(e -> loadSalesReport());
 
        periodPanel.add(periodLabel);
        periodPanel.add(periodComboBox);
        periodPanel.add(refreshButton);
 
        JButton exportButton = createStyledButton("Export to CSV", PRIMARY_COLOR, LIGHT_TEXT);
        exportButton.addActionListener(e -> exportSalesReport());
 
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exportPanel.setBackground(Color.WHITE);
        exportPanel.add(exportButton);
 
        topPanel.add(periodPanel, BorderLayout.WEST);
        topPanel.add(exportPanel, BorderLayout.EAST);
 
        // Sales table
        salesTableModel = new SalesTableModel();
        salesTable = new JTable(salesTableModel);
        customizeTable(salesTable);
        salesTable.setAutoCreateRowSorter(true);
 
        // Right-align the amount column
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        salesTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
 
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        // Summary cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        summaryPanel.setBackground(SECONDARY_COLOR);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
 
        totalSalesLabel = new JLabel("₱0.00", JLabel.CENTER);
        avgSalesLabel = new JLabel("₱0.00", JLabel.CENTER);
        transactionCountLabel = new JLabel("0", JLabel.CENTER);
 
        summaryPanel.add(createSummaryCard("Total Sales", totalSalesLabel, PRIMARY_COLOR));
        summaryPanel.add(createSummaryCard("Average Transaction", avgSalesLabel, SUCCESS_COLOR));
        summaryPanel.add(createSummaryCard("Transaction Count", transactionCountLabel, INFO_COLOR));
 
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
 
        return panel;
    }
 
    private JPanel createInventoryAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(createRoundedBorder("Inventory Alerts", WARNING_COLOR));
 
        JLabel alertsLabel = new JLabel("Low Stock Items (Quantity < 10)");
        alertsLabel.setFont(REGULAR_FONT);
        alertsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));
 
        JButton exportButton = createStyledButton("Export to CSV", PRIMARY_COLOR, LIGHT_TEXT);
        exportButton.addActionListener(e -> exportLowStockReport());
 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(exportButton);
 
        topPanel.add(alertsLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
 
        // Create a dummy table for low stock items
        // In a real application, you would populate this with actual data
        String[] columnNames = {"ID", "Product Name", "Current Stock", "Min Stock", "Action Required"};
        Object[][] data = {
            {1, "Example Low Stock Item", 5, 10, "Order Now"},
            {2, "Another Low Stock Item", 3, 10, "Order Now"}
        };
 
        JTable lowStockTable = new JTable(data, columnNames);
        customizeTable(lowStockTable);
 
        JScrollPane scrollPane = new JScrollPane(lowStockTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
 
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
 
        return panel;
    }
 
    private JPanel createCategoriesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(createRoundedBorder("Product Categories", PRIMARY_COLOR));

        JLabel titleLabel = new JLabel("Manage Product Categories");
        titleLabel.setFont(REGULAR_FONT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton addButton = createStyledButton("Add Category", SUCCESS_COLOR, LIGHT_TEXT);
        JButton removeButton = createStyledButton("Remove Category", DANGER_COLOR, LIGHT_TEXT);
        JButton refreshButton = createStyledButton("Refresh", INFO_COLOR, LIGHT_TEXT);

        // Categories list with modern styling
        DefaultListModel<String> categoriesListModel = new DefaultListModel<>();
        JList<String> categoriesList = new JList<>(categoriesListModel);
        categoriesList.setFont(REGULAR_FONT);
        categoriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoriesList.setFixedCellHeight(30);
        categoriesList.setBackground(Color.WHITE);
        categoriesList.setBorder(BorderFactory.createEmptyBorder());

        // Products table for selected category
        ProductTableModel categoryProductsModel = new ProductTableModel();
        JTable categoryProductsTable = new JTable(categoryProductsModel);
        customizeTable(categoryProductsTable);
        JScrollPane productsScrollPane = new JScrollPane(categoryProductsTable);

        // Load categories initially
        refreshCategories(categoriesListModel);

        addButton.addActionListener(e -> {
            String newCategory = JOptionPane.showInputDialog(this, "Enter new category name:");
            if (newCategory != null && !newCategory.trim().isEmpty()) {
                // Show product selection dialog
                List<Product> allProducts = ProductService.getAllProducts();
                JDialog productSelectDialog = new JDialog(this, "Select Product for Category", true);
                productSelectDialog.setSize(500, 400);
                productSelectDialog.setLocationRelativeTo(this);

                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                ProductTableModel selectModel = new ProductTableModel();
                selectModel.setProducts(allProducts);
                JTable productSelectTable = new JTable(selectModel);
                customizeTable(productSelectTable);

                JButton selectButton = createStyledButton("Select", PRIMARY_COLOR, LIGHT_TEXT);
                selectButton.addActionListener(ev -> {
                    int selectedRow = productSelectTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Product selectedProduct = selectModel.getProductAt(productSelectTable.convertRowIndexToModel(selectedRow));
                        if (CategoryService.addCategory(newCategory, selectedProduct.getProductId())) {
                            refreshCategories(categoriesListModel);
                            JOptionPane.showMessageDialog(this, 
                                "Category added successfully to product: " + selectedProduct.getName(),
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                            productSelectDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, 
                                "Failed to add category", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Please select a product", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                });

                contentPanel.add(new JScrollPane(productSelectTable), BorderLayout.CENTER);
                contentPanel.add(selectButton, BorderLayout.SOUTH);
                productSelectDialog.add(contentPanel);
                productSelectDialog.setVisible(true);
            }
        });

        removeButton.addActionListener(e -> {
            String selected = categoriesList.getSelectedValue();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove category '" + selected + "'?\n" +
                    "This will remove the category from all products.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (CategoryService.removeCategory(selected)) {
                        refreshCategories(categoriesListModel);
                        categoryProductsModel.setProducts(new ArrayList<>());
                        JOptionPane.showMessageDialog(this, 
                            "Category removed successfully", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to remove category", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category to remove.");
            }
        });

        refreshButton.addActionListener(e -> refreshCategories(categoriesListModel));

        categoriesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = categoriesList.getSelectedValue();
                if (selectedCategory != null) {
                    List<Product> products = CategoryService.getProductsByCategory(selectedCategory);
                    categoryProductsModel.setProducts(products);
                }
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Split pane for categories and products
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(new JScrollPane(categoriesList));
        splitPane.setRightComponent(productsScrollPane);
        splitPane.setDividerLocation(200);
        splitPane.setResizeWeight(0.3);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private void refreshCategories(DefaultListModel<String> listModel) {
        listModel.clear();
        List<String> categories = CategoryService.getAllCategories();
        for (String category : categories) {
            listModel.addElement(category);
        }
    }
 
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(240, 240, 240));
 
        // File menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(REGULAR_FONT);
 
        JMenuItem backupItem = new JMenuItem("Backup Database");
        JMenuItem restoreItem = new JMenuItem("Restore Database");
        JMenuItem exitItem = new JMenuItem("Exit");
 
        styleMenuItem(backupItem);
        styleMenuItem(restoreItem);
        styleMenuItem(exitItem);
 
        exitItem.addActionListener(e -> System.exit(0));
 
        fileMenu.add(backupItem);
        fileMenu.add(restoreItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
 
        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.setFont(REGULAR_FONT);
 
        JMenuItem dailySalesItem = new JMenuItem("Daily Sales Report");
        JMenuItem inventoryItem = new JMenuItem("Inventory Report");
        JMenuItem userActivityItem = new JMenuItem("User Activity Report");
 
        styleMenuItem(dailySalesItem);
        styleMenuItem(inventoryItem);
        styleMenuItem(userActivityItem);
 
        reportsMenu.add(dailySalesItem);
        reportsMenu.add(inventoryItem);
        reportsMenu.add(userActivityItem);
 
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setFont(REGULAR_FONT);
 
        JMenuItem aboutItem = new JMenuItem("About");
        styleMenuItem(aboutItem);
 
        aboutItem.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, 
                "Supermarket POS System\nVersion 1.0\n© 2025 Your Company", 
                "About", JOptionPane.INFORMATION_MESSAGE)
        );
 
        helpMenu.add(aboutItem);
 
        menuBar.add(fileMenu);
        menuBar.add(reportsMenu);
        menuBar.add(helpMenu);
 
        return menuBar;
    }
 
    private void styleMenuItem(JMenuItem item) {
        item.setFont(REGULAR_FONT);
        item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
 
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
 
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(SMALL_FONT);
 
        JLabel timeLabel = new JLabel(new java.util.Date().toString());
        timeLabel.setFont(SMALL_FONT);
 
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(timeLabel, BorderLayout.EAST);
 
        // Add timer to update the time
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(new java.util.Date().toString());
        });
        timer.start();
 
        return panel;
    }
 
    private JPanel createSummaryCard(String title, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(color, 1, true));
 
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(REGULAR_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
 
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
 
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
 
        return card;
    }
 
    private void customizeTable(JTable table) {
        table.setFont(REGULAR_FONT);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_COLOR);
        table.setFocusable(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
 
        // Table header customization
        JTableHeader header = table.getTableHeader();
        header.setFont(HEADER_FONT);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(TEXT_COLOR);
        header.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
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
 
    private void loadInitialData() {
        // Load products
        List<Product> products = ProductService.getAllProducts();
        productTableModel.setProducts(products);
 
        // Load users
        List<User> users = UserService.getAllUsers();
        userTableModel.setUsers(users);
 
        // Load sales for today initially
        loadSalesReport();
 
        // Set status message
        showStatus("System ready. Data loaded successfully.", false);
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
 
    private void loadSalesReport() {
        String selectedPeriod = (String) periodComboBox.getSelectedItem();
        List<Sale> sales = null;
 
        switch (selectedPeriod) {
            case "Today":
                sales = SalesService.getTodaySales();
                break;
            case "This Week":
                sales = SalesService.getWeeklySales();
                break;
            case "This Month":
                sales = SalesService.getMonthlySales();
                break;
            case "This Year":
                sales = SalesService.getYearlySales();
                break;
            default:
                sales = SalesService.getTodaySales();
        }
 
        salesTableModel.setSales(sales);
        updateSummary(sales);
        showStatus("Sales report updated for " + selectedPeriod.toLowerCase(), false);
    }
 
    private void updateSummary(List<Sale> sales) {
        double totalSales = 0;
        int transactionCount = sales.size();
 
        for (Sale sale : sales) {
            totalSales += sale.getTotalAmount();
        }
 
        double avgSales = transactionCount > 0 ? totalSales / transactionCount : 0;
 
        DecimalFormat df = new DecimalFormat("#,##0.00");
        totalSalesLabel.setText("₱" + df.format(totalSales));
        avgSalesLabel.setText("₱" + df.format(avgSales));
        transactionCountLabel.setText(String.format("%d", transactionCount));
    }
 
    private void exportSalesReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Sales Report");
        fileChooser.setSelectedFile(new File("sales_report_" + 
            new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
 
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                SalesService.exportSalesReport(
                    salesTableModel.getSales(), 
                    fileChooser.getSelectedFile().getAbsolutePath()
                );
                JOptionPane.showMessageDialog(this, 
                    "Sales report exported successfully!", 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
 
                showStatus("Sales report exported to " + fileChooser.getSelectedFile().getName(), false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting sales report: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
 
                showStatus("Error exporting sales report", true);
            }
        }
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

    
    private void handleLogout(ActionEvent e) {
        // Check if there's any unsaved work (you can add checks here if needed)
        
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
 
	/**
     * Show dialog to add a new product or edit an existing one
     * @param product The product to edit, or null for a new product
     */
    private void showProductDialog(Product product) {
        JDialog dialog = new JDialog(this, product == null ? "Add New Product" : "Edit Product", true);
        dialog.setSize(500, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
 
        // Main content panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
 
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;
 
        // Product information fields
        JTextField barcodeField = createStyledTextField();
        JTextField nameField = createStyledTextField();
        JTextField priceField = createStyledTextField();
        JTextField stockField = createStyledTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(ProductService.getAllCategories().toArray(new String[0]));
        categoryCombo.setFont(REGULAR_FONT);
        categoryCombo.setBackground(Color.WHITE);
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(REGULAR_FONT);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
 
        // If editing an existing product, populate fields
        if (product != null) {
            barcodeField.setText(product.getBarcode());
            nameField.setText(product.getName());
            priceField.setText(String.format("%.2f", product.getPrice()));
            stockField.setText(String.valueOf(product.getStockQuantity()));
            categoryCombo.setSelectedItem(product.getCategory());
            descriptionArea.setText(product.getDescription());
        }
 
        // Add form components with labels
        addFormField(formPanel, "Barcode:", barcodeField, gbc, 0);
        addFormField(formPanel, "Product Name:", nameField, gbc, 1);
        addFormField(formPanel, "Price:", priceField, gbc, 2);
        addFormField(formPanel, "Stock Quantity:", stockField, gbc, 3);
        addFormField(formPanel, "Category:", categoryCombo, gbc, 4);
 
        // Description field with its own panel for better styling
        JLabel descLabel = new JLabel("Description:");
        descLabel.setFont(REGULAR_FONT);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(descLabel, gbc);
 
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formPanel.add(descScrollPane, gbc);
 
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
 
        JButton cancelButton = createStyledButton("Cancel", Color.LIGHT_GRAY, TEXT_COLOR);
        JButton saveButton = createStyledButton("Save", SUCCESS_COLOR, LIGHT_TEXT);
 
        cancelButton.addActionListener(e -> dialog.dispose());
 
        saveButton.addActionListener(e -> {
            try {
                // Validate input
                String barcode = barcodeField.getText().trim();
                String name = nameField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                String category = (String) categoryCombo.getSelectedItem();
                String description = descriptionArea.getText().trim();
                
                if (barcode.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please fill in all required fields.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Create or update product
                Product updatedProduct;
                if (product == null) {
                    // New product - use the correct constructor
                    updatedProduct = new Product(
                        0, barcode, name, description, 
                        price, stock, category, true
                    );
                } else {
                    // Update existing product - use the correct constructor
                    updatedProduct = new Product(
                        product.getProductId(), barcode, name, description,
                        price, stock, category, product.isActive()
                    );
                }
 
                boolean success;
                if (product == null) {
                    success = ProductService.addProduct(updatedProduct);
                    if (success) {
                        refreshProducts();
                        showStatus("Product added successfully", false);
                    }
                } else {
                    success = ProductService.updateProduct(updatedProduct);
                    if (success) {
                        productTableModel.updateProduct(updatedProduct);
                        showStatus("Product updated successfully", false);
                    }
                }
 
                if (success) {
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "An error occurred while saving the product.", 
                        "Save Error", 
                        JOptionPane.ERROR_MESSAGE);
                    showStatus("Error saving product", true);
                }
 
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Invalid number format. Please check price and stock values.", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
 
        buttonsPanel.add(cancelButton);
        buttonsPanel.add(saveButton);
 
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
 
        JLabel headerLabel = new JLabel(product == null ? "Add New Product" : "Edit Product");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(LIGHT_TEXT);
        headerPanel.add(headerLabel, BorderLayout.CENTER);
 
        // Assemble dialog
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);
 
        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(headerPanel, BorderLayout.NORTH);
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
 
        dialog.setVisible(true);
    }
 
    /**
     * Helper method to add a form field with label to GridBagLayout
     */
    private void addFormField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(REGULAR_FONT);
 
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);
 
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(field, gbc);
    }
 
    /**
     * Create a styled text field for forms
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(REGULAR_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }
 
    /**
     * Edit the currently selected product
     */
    private void editSelectedProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow != -1) {
            // Convert view index to model index in case table is sorted
            int modelRow = productsTable.convertRowIndexToModel(selectedRow);
            Product product = productTableModel.getProductAt(modelRow);
 
            if (product != null) {
                showProductDialog(product);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
        }
    }
 
    /**
     * Delete the currently selected product
     */
    private void deleteSelectedProduct() {
        int selectedRow = productsTable.getSelectedRow();
        if (selectedRow != -1) {
            // Convert view index to model index in case table is sorted
            int modelRow = productsTable.convertRowIndexToModel(selectedRow);
            Product product = productTableModel.getProductAt(modelRow);
 
            if (product != null) {
                // Create custom confirm dialog
                JDialog confirmDialog = new JDialog(this, "Confirm Deletion", true);
                confirmDialog.setSize(400, 200);
                confirmDialog.setLocationRelativeTo(this);
                confirmDialog.setResizable(false);
                confirmDialog.setLayout(new BorderLayout());
 
                // Dialog content
                JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
                contentPanel.setBackground(Color.WHITE);
                contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
 
                // Warning icon and message
                JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
                JPanel messagePanel = new JPanel(new BorderLayout());
                messagePanel.setBackground(Color.WHITE);
 
                JLabel titleLabel = new JLabel("Delete Product?");
                titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
 
                JLabel messageLabel = new JLabel("<html>Are you sure you want to delete the product:<br><b>" + 
                    product.getName() + "</b>?<br>This action cannot be undone.</html>");
                messageLabel.setFont(REGULAR_FONT);
 
                messagePanel.add(titleLabel, BorderLayout.NORTH);
                messagePanel.add(messageLabel, BorderLayout.CENTER);
 
                JPanel topPanel = new JPanel(new BorderLayout(15, 0));
                topPanel.setBackground(Color.WHITE);
                topPanel.add(iconLabel, BorderLayout.WEST);
                topPanel.add(messagePanel, BorderLayout.CENTER);
 
                // Buttons
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
                buttonPanel.setBackground(Color.WHITE);
 
                JButton cancelButton = createStyledButton("Cancel", Color.LIGHT_GRAY, TEXT_COLOR);
                JButton deleteButton = createStyledButton("Delete", DANGER_COLOR, LIGHT_TEXT);
 
                cancelButton.addActionListener(e -> confirmDialog.dispose());
 
                deleteButton.addActionListener(e -> {
                    confirmDialog.dispose();
 
                    if (ProductService.deleteProduct(product.getProductId())) {
                        productTableModel.removeProduct(product.getProductId());
                        showStatus("Product '" + product.getName() + "' deleted successfully", false);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to delete product.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        showStatus("Error deleting product", true);
                    }
                });
 
                buttonPanel.add(cancelButton);
                buttonPanel.add(deleteButton);
 
                contentPanel.add(topPanel, BorderLayout.CENTER);
                contentPanel.add(buttonPanel, BorderLayout.SOUTH);
 
                confirmDialog.add(contentPanel);
                confirmDialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
        }
    }
 
    /**
     * Refresh the products table by reloading data from the database
     */
    private void refreshProducts() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
        List<Product> products = ProductService.getAllProducts();
        productTableModel.setProducts(products);
 
        setCursor(Cursor.getDefaultCursor());
        showStatus("Products refreshed successfully", false);
    }
 
    /**
     * Search for products by name or barcode
     * @param query The search query
     */
    private void searchProducts(String query) {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
 
        if (query == null || query.trim().isEmpty()) {
            refreshProducts(); // Show all products if query is empty
        } else {
            List<Product> results = ProductService.searchProducts(query.trim());
            productTableModel.setProducts(results);
            showStatus("Found " + results.size() + " products matching '" + query + "'", false);
        }
 
        setCursor(Cursor.getDefaultCursor());
    }
 
    // User Management Methods
 
    /**
     * Show dialog to add or edit a user
     */
    private void showUserDialog(User user) {
    	JDialog dialog = new JDialog(this, user == null ? "Add New User" : "Edit User", true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;

        // User fields
        JTextField usernameField = createStyledTextField();
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        passwordField.setFont(REGULAR_FONT);

        JTextField fullNameField = createStyledTextField();
        String[] roles = {"admin", "cashier"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setFont(REGULAR_FONT);
        roleCombo.setBackground(Color.WHITE);

        if (user != null) {
            JButton changePasswordBtn = createStyledButton("Change Password", WARNING_COLOR, LIGHT_TEXT);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            formPanel.add(changePasswordBtn, gbc);

            changePasswordBtn.addActionListener(e -> {
                JPasswordField newPasswordField = new JPasswordField();
                JPasswordField confirmPasswordField = new JPasswordField();
                
                JPanel passwordPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                passwordPanel.add(new JLabel("New Password:"));
                passwordPanel.add(newPasswordField);
                passwordPanel.add(new JLabel("Confirm Password:"));
                passwordPanel.add(confirmPasswordField);

                int result = JOptionPane.showConfirmDialog(
                    dialog,
                    passwordPanel,
                    "Change Password",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
                );

                if (result == JOptionPane.OK_OPTION) {
                    String newPassword = new String(newPasswordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());

                    if (!newPassword.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Passwords do not match!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (newPassword.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Password cannot be empty!", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (UserService.changePassword(user.getUserId(), newPassword)) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Password changed successfully!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Failed to change password", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        // Add form components
        addFormField(formPanel, "Username:", usernameField, gbc, 0);
        if (user == null) {
            addFormField(formPanel, "Password:", passwordField, gbc, 1);
        }
        addFormField(formPanel, "Full Name:", fullNameField, gbc, 2);
        addFormField(formPanel, "Role:", roleCombo, gbc, 3);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton cancelButton = createStyledButton("Cancel", Color.LIGHT_GRAY, TEXT_COLOR);
        JButton saveButton = createStyledButton("Save", SUCCESS_COLOR, LIGHT_TEXT);

        cancelButton.addActionListener(e -> dialog.dispose());
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String fullName = fullNameField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();

            if (username.isEmpty() || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please fill in all required fields.", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success;
            if (user == null) {
                // New user
                String password = new String(passwordField.getPassword());
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Please enter a password.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User newUser = new User(0, username, role, fullName);
                success = UserService.addUser(newUser, password);
                if (success) {
                    userTableModel.setUsers(UserService.getAllUsers());
                    showStatus("User added successfully", false);
                }
            } else {
                // Update existing user
                User updatedUser = new User(user.getUserId(), username, role, fullName);
                success = UserService.updateUser(updatedUser);
                if (success) {
                    userTableModel.updateUser(updatedUser);
                    showStatus("User updated successfully", false);
                }
            }

            if (success) {
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "An error occurred while saving the user.", 
                    "Save Error", 
                    JOptionPane.ERROR_MESSAGE);
                showStatus("Error saving user", true);
            }
        });

        buttonsPanel.add(cancelButton);
        buttonsPanel.add(saveButton);

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel headerLabel = new JLabel(user == null ? "Add New User" : "Edit User");
        headerLabel.setFont(HEADER_FONT);
        headerLabel.setForeground(LIGHT_TEXT);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        // Assemble dialog
        contentPanel.add(formPanel, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.getContentPane().setLayout(new BorderLayout());
        dialog.getContentPane().add(headerPanel, BorderLayout.NORTH);
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    
    }
 
    /**
     * Edit selected user
     */
    private void editSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = userTableModel.getUserAt(selectedRow);
            if (user != null) {
                showUserDialog(user);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.");
        }
    }
 
    /**
     * Delete selected user
     */
    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow != -1) {
            User user = userTableModel.getUserAt(selectedRow);

            if (user != null) {
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete user '" + user.getUsername() + "'?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    if (UserService.deleteUser(user.getUserId())) {
                        userTableModel.removeUser(user.getUserId());
                        showStatus("User deleted successfully", false);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Failed to delete user.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                        showStatus("Error deleting user", true);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.");
        }
    }
 
    /**
     * Export low stock report to CSV
     */
    private void exportLowStockReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Low Stock Report");
        fileChooser.setSelectedFile(new File("low_stock_report_" + 
            new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv"));
 
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                // In a real app, you'd call the service to export the report
                // InventoryService.exportLowStockReport(fileChooser.getSelectedFile().getAbsolutePath());
 
                // Just a placeholder message for now
                JOptionPane.showMessageDialog(this, 
                    "Low stock report exported successfully!", 
                    "Export Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
 
                showStatus("Low stock report exported to " + fileChooser.getSelectedFile().getName(), false);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error exporting low stock report: " + e.getMessage(), 
                    "Export Error", 
                    JOptionPane.ERROR_MESSAGE);
 
                showStatus("Error exporting low stock report", true);
            }
        }
    }

}