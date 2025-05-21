package model;

import java.util.Map;

/**
 * Model for sales report insights.
 */
public class SalesInsights {
	private double totalSales;
	private int totalOrders;
	private double averageOrderValue;
	private Map<String, Integer> topProducts; // Product name -> Quantity sold
	private Map<String, Double> topCategories; // Category name -> Total sales

	// Getters and Setters
	public double getTotalSales() {
		return totalSales;
	}

	public void setTotalSales(double totalSales) {
		this.totalSales = totalSales;
	}

	public int getTotalOrders() {
		return totalOrders;
	}

	public void setTotalOrders(int totalOrders) {
		this.totalOrders = totalOrders;
	}

	public double getAverageOrderValue() {
		return averageOrderValue;
	}

	public void setAverageOrderValue(double averageOrderValue) {
		this.averageOrderValue = averageOrderValue;
	}

	public Map<String, Integer> getTopProducts() {
		return topProducts;
	}

	public void setTopProducts(Map<String, Integer> topProducts) {
		this.topProducts = topProducts;
	}

	public Map<String, Double> getTopCategories() {
		return topCategories;
	}

	public void setTopCategories(Map<String, Double> topCategories) {
		this.topCategories = topCategories;
	}
}