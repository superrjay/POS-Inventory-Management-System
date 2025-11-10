package com.pos.all;

public class Product {
    private int productId;
    private String barcode;
    private String name;
    private String description;
    private double price;   
    private int stockQuantity;
    private String category;
    private boolean active;
    
    // Existing constructors
    public Product(int productId, String barcode, String name, String description, 
                 double price, int stockQuantity, String category) {
        this(productId, barcode, name, description, price, stockQuantity, category, true);
    }
    
    public Product(int productId, String barcode, String name, String description, 
                 double price, int stockQuantity, String category, boolean active) {
        this.productId = productId;
        this.barcode = barcode;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.active = active;
    }
    
    // Fix this constructor that was empty
    public Product(int productId, String barcode, String name, double price, 
                  int stockQuantity, String category, String description) {
        this(productId, barcode, name, description, price, stockQuantity, category, true);
    }

	// Getters
    public int getProductId() {
        return productId;
    }
    
    public String getBarcode() {
        return barcode;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getStockQuantity() {
        return stockQuantity;
    }
    
    public String getCategory() {
        return category;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public boolean isInStock() {
        return stockQuantity > 0;
    }
    
    // Setters (if needed)
    public void setPrice(double price) {
        this.price = price;
    }
    
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setCategory(String category) {
    	this.category = category;
    }
    
    // Other methods as needed
}