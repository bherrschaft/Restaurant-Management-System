package com.restaurant.management.models;

public class SpecialOffer {
    private int offerId;
    private String description;
    private double discountAmount;
    private String dealDetails;

    // Getters and Setters
    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDealDetails() {
        return dealDetails;
    }

    public void setDealDetails(String dealDetails) {
        this.dealDetails = dealDetails;
    }
}
