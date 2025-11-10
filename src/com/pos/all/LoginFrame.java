package com.pos.all;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Color PRIMARY_COLOR = new Color(25, 118, 210);    // Blue
    private static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Light Gray
    private static final Color TEXT_COLOR = new Color(33, 33, 33);         // Dark Gray
    private static final Color LIGHT_TEXT = new Color(250, 250, 250);      // Off White
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    
    public LoginFrame() {
        setTitle("Sales and Inventory - Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Main container with BorderLayout
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainContainer.setBackground(SECONDARY_COLOR);
        

        JPanel headerPanel = createHeaderPanel();
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        

        JPanel contentPanel = createContentPanel();
        mainContainer.add(contentPanel, BorderLayout.CENTER);
        

        JPanel statusPanel = createStatusPanel();
        mainContainer.add(statusPanel, BorderLayout.SOUTH);
        
        add(mainContainer);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        JLabel titleLabel = new JLabel("Sales and Inventory System");
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(LIGHT_TEXT);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(createRoundedBorder("Login to Your Account", PRIMARY_COLOR));
        panel.setBackground(Color.WHITE);
        
        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Username Field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(REGULAR_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(usernameLabel);
        formPanel.add(Box.createVerticalStrut(5));
        
        usernameField = new JTextField();
        usernameField.setFont(REGULAR_FONT);
        usernameField.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(usernameField);
        formPanel.add(Box.createVerticalStrut(15));
        
        // Password Field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(REGULAR_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));
        
        passwordField = new JPasswordField();
        passwordField.setFont(REGULAR_FONT);
        passwordField.setMaximumSize(new Dimension(Short.MAX_VALUE, 35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(passwordField);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Login Button
        JButton loginButton = createStyledButton("Login", PRIMARY_COLOR, LIGHT_TEXT);
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(Short.MAX_VALUE, 40));
        loginButton.addActionListener(this::handleLogin);
        formPanel.add(loginButton);
        
        // Add key listener for Enter key in both fields
        usernameField.addActionListener(e -> loginButton.doClick());
        passwordField.addActionListener(e -> loginButton.doClick());
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(224, 224, 224));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        statusLabel = new JLabel("Please log in to continue");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(versionLabel, BorderLayout.EAST);
        
        return panel;
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
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 16));
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
        statusLabel.setForeground(isError ? new Color(211, 47, 47) : TEXT_COLOR);
        
        // Reset status after 5 seconds
        Timer timer = new Timer(5000, e -> {
            statusLabel.setText("Please log in to continue");
            statusLabel.setForeground(TEXT_COLOR);
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", true);
            return;
        }
        
        showStatus("Logging in...", false);
        
        // Simulate a small delay for authentication process
        Timer timer = new Timer(800, event -> {
            User user = AuthService.authenticate(username, password);
            
            if (user != null) {
                if (user.getRole().equals("admin")) {
                    new AdminDashboard().setVisible(true);
                } else {
                    new CashierDashboard(user).setVisible(true);
                }
                dispose();
            } else {
                showStatus("Invalid username or password", true);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
   
}