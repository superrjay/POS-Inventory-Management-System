package com.pos.all;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;

public class PaymentDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean paymentSuccessful = false;
    private String paymentMethod = "cash";
    private double amountTendered = 0;
    private JComboBox<String> paymentMethodCombo;
    private JTextField amountField;
    
    public PaymentDialog(JFrame parent, double totalAmount) {
        super(parent, "Payment Processing", true);
        setSize(400, 250);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Total amount display
        panel.add(new JLabel("Total Amount:"));
        panel.add(new JLabel(String.format("₱ %.2f", totalAmount)));
        
        // Payment method selection
        panel.add(new JLabel("Payment Method:"));
        paymentMethodCombo = new JComboBox<>(new String[]{"Cash", "Card", "Mobile"});
        panel.add(paymentMethodCombo);
        
        // Amount tendered (for cash payments)
        panel.add(new JLabel("Amount Tendered:"));
        amountField = new JTextField();
        panel.add(amountField);
        
        // Buttons
        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(this::processPayment);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add listener to show/hide amount field based on payment method
        paymentMethodCombo.addActionListener(e -> {
            if (paymentMethodCombo.getSelectedItem().equals("Cash")) {
                amountField.setEnabled(true);
            } else {
                amountField.setEnabled(false);
            }
        });
    }
    
    private void processPayment(ActionEvent e) {
        paymentMethod = paymentMethodCombo.getSelectedItem().toString().toLowerCase();
        
        if (paymentMethod.equals("cash")) {
            try {
                amountTendered = Double.parseDouble(amountField.getText());
                double totalAmount = getTotalAmountFromDisplay();
                
                if (amountTendered < totalAmount) {
                    JOptionPane.showMessageDialog(this, 
                        "Amount tendered is less than total amount", 
                        "Payment Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter a valid amount", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        paymentSuccessful = true;
        dispose();
    }
    
    private double getTotalAmountFromDisplay() {
        // Extract total amount from the label (this is a simplified approach)
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] subComps = ((JPanel)comp).getComponents();
                for (Component subComp : subComps) {
                    if (subComp instanceof JLabel) {
                        JLabel label = (JLabel)subComp;
                        if (label.getText().startsWith("$")) {
                            return Double.parseDouble(label.getText().substring(1));
                        }
                    }
                }
            }
        }
        return 0;
    }
    
    // Getters
    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public double getAmountTendered() {
        return amountTendered;
    }
}