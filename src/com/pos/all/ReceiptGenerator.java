package com.pos.all;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptGenerator {
    public static void generateReceipt(int transactionId, String cashierName, 
                                     List<CartItem> items, String paymentMethod, 
                                     double amountTendered) {
        // Create a printable component
        JTextArea receiptArea = new JTextArea();
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Build receipt content
        StringBuilder receipt = new StringBuilder();
        receipt.append("SUPERMARKET RECEIPT\n");
        receipt.append("-------------------\n");
        receipt.append("Transaction #: ").append(transactionId).append("\n");
        receipt.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        receipt.append("Cashier: ").append(cashierName).append("\n\n");
        receipt.append(String.format("%-20s %6s %4s %8s\n", "ITEM", "PRICE", "QTY", "TOTAL"));
        
        for (CartItem item : items) {
            receipt.append(String.format("%-20s %6.2f %4d %8.2f\n", 
                item.getProduct().getName(),
                item.getProduct().getPrice(),
                item.getQuantity(),
                item.getSubtotal()));
        }
        
        double subtotal = items.stream().mapToDouble(CartItem::getSubtotal).sum();
        receipt.append("\n");
        receipt.append(String.format("%34s: %8.2f\n", "SUBTOTAL", subtotal));
        receipt.append(String.format("%34s: %8s\n", "PAYMENT METHOD", paymentMethod));
        
        if (paymentMethod.equals("cash")) {
            receipt.append(String.format("%34s: %8.2f\n", "AMOUNT TENDERED", amountTendered));
            receipt.append(String.format("%34s: %8.2f\n", "CHANGE", amountTendered - subtotal));
        }
        
        receipt.append("\nTHANK YOU FOR SHOPPING WITH US!");
        
        receiptArea.setText(receipt.toString());
        
        // Print the receipt
        try {
            receiptArea.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(null, 
                "Error printing receipt: " + e.getMessage(), 
                "Print Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Optionally also show on screen
        JOptionPane.showMessageDialog(null, new JScrollPane(receiptArea), 
            "Receipt", JOptionPane.PLAIN_MESSAGE);
    }
}