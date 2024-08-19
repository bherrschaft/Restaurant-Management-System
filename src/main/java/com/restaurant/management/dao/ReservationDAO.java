package com.restaurant.management.dao;

import com.restaurant.management.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public void addReservation(Reservation reservation) throws SQLException {
        String query = "INSERT INTO Reservations (customer_name, reservation_time, table_id) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, reservation.getCustomerName());
            pstmt.setString(2, reservation.getReservationTime());
            pstmt.setInt(3, reservation.getTableId());
            pstmt.executeUpdate();
        }
    }

    public void updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE Reservations SET customer_name = ?, reservation_time = ?, table_id = ? WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, reservation.getCustomerName());
            pstmt.setString(2, reservation.getReservationTime());
            pstmt.setInt(3, reservation.getTableId());
            pstmt.setInt(4, reservation.getReservationId());
            pstmt.executeUpdate();
        }
    }

    public void deleteReservation(int reservationId) throws SQLException {
        String query = "DELETE FROM Reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        }
    }

    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();
        String query = "SELECT * FROM Reservations";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(rs.getInt("reservation_id"));
                reservation.setCustomerName(rs.getString("customer_name"));
                reservation.setReservationTime(rs.getString("reservation_time"));
                reservation.setTableId(rs.getInt("table_id"));
                reservationList.add(reservation);
            }
        }
        return reservationList;
    }
}
