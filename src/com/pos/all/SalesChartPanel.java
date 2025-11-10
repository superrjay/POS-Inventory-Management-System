package com.pos.all;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class SalesChartPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private List<SalesService.DailySalesSummary> salesData;
    private String chartTitle = "Sales Chart";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");
    private DecimalFormat decimalFormat = new DecimalFormat("#,##0");
    
    public SalesChartPanel() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setPreferredSize(new Dimension(800, 400));
    }
    
    public void setSalesData(List<SalesService.DailySalesSummary> salesData, String title) {
        this.salesData = salesData;
        this.chartTitle = title;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (salesData == null || salesData.isEmpty()) {
            g.setColor(Color.GRAY);
            g.drawString("No data available", getWidth() / 2 - 40, getHeight() / 2);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Chart dimensions
        int padding = 60;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        int chartX = padding;
        int chartY = padding / 2;
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(chartTitle);
        g2d.drawString(chartTitle, (getWidth() - titleWidth) / 2, 25);
        
        // Find max value for scaling
        double maxValue = 0;
        for (SalesService.DailySalesSummary data : salesData) {
            maxValue = Math.max(maxValue, data.getTotalAmount());
        }
        
        // Add some padding to max value
        maxValue = maxValue * 1.1;
        
        // Draw axes
        g2d.setColor(Color.BLACK);
        g2d.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight); // X-axis
        g2d.drawLine(chartX, chartY, chartX, chartY + chartHeight); // Y-axis
        
        // Draw Y-axis labels
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int numYLabels = 5;
        for (int i = 0; i <= numYLabels; i++) {
            int y = chartY + chartHeight - (i * chartHeight / numYLabels);
            double value = (maxValue * i) / numYLabels;
            String label = "₱" + decimalFormat.format(value);
            g2d.drawString(label, chartX - fm.stringWidth(label) - 10, y + 5);
            
            // Draw grid lines
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(chartX, y, chartX + chartWidth, y);
            g2d.setColor(Color.BLACK);
        }
        
        // Draw bars
        if (!salesData.isEmpty()) {
            int barWidth = Math.max(chartWidth / salesData.size() - 10, 20);
            int spacing = (chartWidth - (barWidth * salesData.size())) / (salesData.size() + 1);
            
            for (int i = 0; i < salesData.size(); i++) {
                SalesService.DailySalesSummary data = salesData.get(i);
                
                int barHeight = (int) ((data.getTotalAmount() / maxValue) * chartHeight);
                int x = chartX + spacing + i * (barWidth + spacing);
                int y = chartY + chartHeight - barHeight;
                
                // Draw bar
                g2d.setColor(new Color(70, 130, 180)); // Steel blue
                g2d.fillRect(x, y, barWidth, barHeight);
                
                // Draw bar outline
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, barWidth, barHeight);
                
                // Draw X-axis labels (dates)
                String dateLabel = dateFormat.format(data.getDate());
                int labelX = x + (barWidth - fm.stringWidth(dateLabel)) / 2;
                g2d.drawString(dateLabel, labelX, chartY + chartHeight + 20);
                
                // Draw value on top of bar
                String valueLabel = "₱" + decimalFormat.format(data.getTotalAmount());
                int valueLabelX = x + (barWidth - fm.stringWidth(valueLabel)) / 2;
                g2d.drawString(valueLabel, valueLabelX, y - 5);
            }
        }
        
        // Draw chart border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(chartX, chartY, chartWidth, chartHeight);
    }
}
