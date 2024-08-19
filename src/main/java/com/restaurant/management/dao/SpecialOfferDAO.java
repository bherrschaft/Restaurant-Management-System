package com.restaurant.management.dao;
import com.restaurant.management.models.SpecialOffer;  // Ensure this import is present
import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpecialOfferDAO {

    public void addSpecialOffer(SpecialOffer offer) throws SQLException {
        String query = "INSERT INTO SpecialOffers (description, discount_amount, deal_details) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, offer.getDescription());
            pstmt.setDouble(2, offer.getDiscountAmount());
            pstmt.setString(3, offer.getDealDetails());
            pstmt.executeUpdate();
        }
    }

    public void updateSpecialOffer(SpecialOffer offer) throws SQLException {
        String query = "UPDATE SpecialOffers SET description = ?, discount_amount = ?, deal_details = ? WHERE offer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, offer.getDescription());
            pstmt.setDouble(2, offer.getDiscountAmount());
            pstmt.setString(3, offer.getDealDetails());
            pstmt.setInt(4, offer.getOfferId());
            pstmt.executeUpdate();
        }
    }

    public void deleteSpecialOffer(int offerId) throws SQLException {
        String query = "DELETE FROM SpecialOffers WHERE offer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, offerId);
            pstmt.executeUpdate();
        }
    }

    public List<SpecialOffer> getAllSpecialOffers() throws SQLException {
        List<SpecialOffer> offerList = new ArrayList<>();
        String query = "SELECT * FROM SpecialOffers";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                SpecialOffer offer = new SpecialOffer();
                offer.setOfferId(rs.getInt("offer_id"));
                offer.setDescription(rs.getString("description"));
                offer.setDiscountAmount(rs.getDouble("discount_amount"));
                offer.setDealDetails(rs.getString("deal_details"));
                offerList.add(offer);
            }
        }
        return offerList;
    }
}
