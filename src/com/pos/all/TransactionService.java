package com.pos.all;
import java.sql.*;
import java.util.List;

public class TransactionService {
    public static boolean processTransaction(int userId, List<CartItem> cart, String paymentMethod) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Calculate total
            double total = cart.stream().mapToDouble(CartItem::getSubtotal).sum();
            
            // Create transaction
            String transSql = "INSERT INTO transactions (user_id, transaction_date, total_amount, payment_method) " +
                             "VALUES (?, NOW(), ?, ?)";
            PreparedStatement transStmt = conn.prepareStatement(transSql, Statement.RETURN_GENERATED_KEYS);
            transStmt.setInt(1, userId);
            transStmt.setDouble(2, total);
            transStmt.setString(3, paymentMethod);
            transStmt.executeUpdate();
            
            // Get generated transaction ID
            ResultSet rs = transStmt.getGeneratedKeys();
            int transactionId = rs.next() ? rs.getInt(1) : 0;
            
            // Add transaction items
            String itemSql = "INSERT INTO transaction_items (transaction_id, product_id, quantity, unit_price, subtotal) " +
                            "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement itemStmt = conn.prepareStatement(itemSql);
            
            for (CartItem item : cart) {
                itemStmt.setInt(1, transactionId);
                itemStmt.setInt(2, item.getProduct().getProductId());
                itemStmt.setInt(3, item.getQuantity());
                itemStmt.setDouble(4, item.getProduct().getPrice());
                itemStmt.setDouble(5, item.getSubtotal());
                itemStmt.addBatch();
                
                // Update stock
                updateProductStock(conn, item.getProduct().getProductId(), item.getQuantity());
            }
            
            itemStmt.executeBatch();
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void updateProductStock(Connection conn, int productId, int quantitySold) throws SQLException {
        String sql = "UPDATE products SET stock_quantity = stock_quantity - ? WHERE product_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, quantitySold);
        stmt.setInt(2, productId);
        stmt.executeUpdate();
    }
    
    public static int getLastTransactionId() {
        String sql = "SELECT LAST_INSERT_ID()";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}