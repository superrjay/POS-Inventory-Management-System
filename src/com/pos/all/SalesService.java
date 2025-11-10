package com.pos.all;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class SalesService {
    private static final String GET_RECENT_SALES = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE t.transaction_date >= DATE_SUB(NOW(), INTERVAL ? DAY) " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";
    
    private static final String GET_SALES_BY_DATE_RANGE = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE t.transaction_date >= ? AND t.transaction_date <= ? " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";
    
    private static final String GET_TODAY_SALES = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE DATE(t.transaction_date) = CURDATE() " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";
    
    private static final String GET_THIS_WEEK_SALES = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE YEARWEEK(t.transaction_date) = YEARWEEK(CURDATE()) " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";
    
    private static final String GET_THIS_MONTH_SALES = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE YEAR(t.transaction_date) = YEAR(CURDATE()) " +
        "AND MONTH(t.transaction_date) = MONTH(CURDATE()) " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";
    
    private static final String GET_THIS_YEAR_SALES = 
        "SELECT t.transaction_id, t.transaction_date as date, u.full_name as cashier, " +
        "t.total_amount, COUNT(ti.item_id) as item_count, t.payment_method " +
        "FROM transactions t " +
        "JOIN users u ON t.user_id = u.user_id " +
        "JOIN transaction_items ti ON t.transaction_id = ti.transaction_id " +
        "WHERE YEAR(t.transaction_date) = YEAR(CURDATE()) " +
        "GROUP BY t.transaction_id " +
        "ORDER BY t.transaction_date DESC";

    public static List<Sale> getRecentSales(int days) {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_RECENT_SALES)) {
            
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sales", e);
        }
        return sales;
    }
    
    public static List<Sale> getTodaySales() {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_TODAY_SALES)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load today's sales", e);
        }
        return sales;
    }
    
    public static List<Sale> getWeeklySales() {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_THIS_WEEK_SALES)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load weekly sales", e);
        }
        return sales;
    }
    
    public static List<Sale> getMonthlySales() {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_THIS_MONTH_SALES)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load monthly sales", e);
        }
        return sales;
    }
    
    public static List<Sale> getYearlySales() {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_THIS_YEAR_SALES)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load yearly sales", e);
        }
        return sales;
    }
    
    public static List<Sale> getSalesByDateRange(Timestamp startDate, Timestamp endDate) {
        List<Sale> sales = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_SALES_BY_DATE_RANGE)) {
            
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sales.add(new Sale(
                    rs.getInt("transaction_id"),
                    rs.getTimestamp("date"),
                    rs.getString("cashier"),
                    rs.getDouble("total_amount"),
                    rs.getInt("item_count"),
                    rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load sales by date range", e);
        }
        return sales;
    }
    
    public static void exportSalesReport(List<Sale> sales, String filename) throws Exception {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            
            // Write header
            writer.println("Transaction ID,Date & Time,Cashier,Total Amount,Items,Payment Method");
            
            // Write sales data
            for (Sale sale : sales) {
                writer.printf("%d,%s,%s,₱%s,%d,%s%n",
                    sale.getTransactionId(),
                    dateFormat.format(sale.getDate()),
                    sale.getCashierName(),
                    decimalFormat.format(sale.getTotalAmount()),
                    sale.getItemCount(),
                    sale.getPaymentMethod().toUpperCase()
                );
            }
        }
    }
    
    // Get total sales amount for a specific period
    public static double getTotalSalesAmount(List<Sale> sales) {
        double total = 0;
        for (Sale sale : sales) {
            total += sale.getTotalAmount();
        }
        return total;
    }
    
    // Get daily sales summary for charting
    public static List<DailySalesSummary> getDailySalesSummary(int days) {
        List<DailySalesSummary> summary = new ArrayList<>();
        String query = "SELECT DATE(transaction_date) as sale_date, " +
                      "COUNT(DISTINCT transaction_id) as transaction_count, " +
                      "SUM(total_amount) as daily_total " +
                      "FROM transactions " +
                      "WHERE transaction_date >= DATE_SUB(CURDATE(), INTERVAL ? DAY) " +
                      "GROUP BY DATE(transaction_date) " +
                      "ORDER BY sale_date";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                summary.add(new DailySalesSummary(
                    rs.getDate("sale_date"),
                    rs.getInt("transaction_count"),
                    rs.getDouble("daily_total")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load daily sales summary", e);
        }
        return summary;
    }
    
    // Inner class for daily sales summary
    public static class DailySalesSummary {
        private Date date;
        private int transactionCount;
        private double totalAmount;
        
        public DailySalesSummary(Date date, int transactionCount, double totalAmount) {
            this.date = date;
            this.transactionCount = transactionCount;
            this.totalAmount = totalAmount;
        }
        
        public Date getDate() { return date; }
        public int getTransactionCount() { return transactionCount; }
        public double getTotalAmount() { return totalAmount; }
    }
}