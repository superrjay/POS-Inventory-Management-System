package com.pos.all;

import java.util.Date;

public class Sale {
    private int transactionId;
    private Date date;
    private String cashierName;
    private double totalAmount;
    private int itemCount;
    private String paymentMethod;

    public Sale(int transactionId, Date date, String cashierName, 
               double totalAmount, int itemCount, String paymentMethod) {
        this.transactionId = transactionId;
        this.date = date;
        this.cashierName = cashierName;
        this.totalAmount = totalAmount;
        this.itemCount = itemCount;
        this.paymentMethod = paymentMethod;
    }

    // Getters
    public int getTransactionId() { return transactionId; }
    public Date getDate() { return date; }
    public String getCashierName() { return cashierName; }
    public double getTotalAmount() { return totalAmount; }
    public int getItemCount() { return itemCount; }
    public String getPaymentMethod() { return paymentMethod; }
}
