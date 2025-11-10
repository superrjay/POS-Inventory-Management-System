package com.pos.all;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static final String GET_ALL_CATEGORIES = "SELECT DISTINCT category FROM products WHERE category IS NOT NULL";
    private static final String ADD_CATEGORY = "UPDATE products SET category = ? WHERE product_id = ?";
    private static final String REMOVE_CATEGORY = "UPDATE products SET category = NULL WHERE category = ?";
    private static final String GET_PRODUCTS_BY_CATEGORY = "SELECT * FROM products WHERE category = ?";

    public static List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_CATEGORIES)) {
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public static boolean addCategory(String category, int productId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(ADD_CATEGORY)) {
            
            pstmt.setString(1, category);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeCategory(String category) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(REMOVE_CATEGORY)) {
            
            pstmt.setString(1, category);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(GET_PRODUCTS_BY_CATEGORY)) {
            
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("product_id"),
                    rs.getString("barcode"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("category"),
                    rs.getString("description")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}