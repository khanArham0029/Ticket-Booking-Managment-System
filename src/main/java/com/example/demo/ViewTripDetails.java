package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewTripDetails {

    @FXML
    private Label tripNameLabel;

    @FXML
    private Label tripDescriptionLabel;

    @FXML
    private Label ticketPriceLabel;

    @FXML
    private Label tripLocationLabel;

    @FXML
    private Label departureTimeLabel;

    @FXML
    private Label arrivalTimeLabel;


    public void initialize(String selectedTripName) {
        // Load trip details from the database on initialization
        loadTripDetailsFromDatabase(selectedTripName);
    }

    void loadTripDetailsFromDatabase(String selectedTripName) {
        try {
            // Connection to the database (replace with your actual database connection details)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/management", "root", "muhammad");

            // Query to retrieve trip details from the database
            String selectTripDetailsQuery = "SELECT * FROM Trips WHERE TripName = ?";

            try (PreparedStatement statement = connection.prepareStatement(selectTripDetailsQuery)) {
                statement.setString(1, selectedTripName);

                try (ResultSet resultSet = statement.executeQuery()) {
                    // If there is a matching trip, display its details
                    if (resultSet.next()) {
                        tripNameLabel.setText("Trip Name: " + resultSet.getString("TripName"));
                        tripDescriptionLabel.setText("Description: " + resultSet.getString("TripDescription"));
                        ticketPriceLabel.setText("Ticket Price: " + resultSet.getInt("TicketPrice"));
                        tripLocationLabel.setText("Location: " + resultSet.getString("TripLocation"));
                        departureTimeLabel.setText("Departure Time: " + resultSet.getString("DepartureTime"));
                        arrivalTimeLabel.setText("Arrival Time: " + resultSet.getString("ArrivalTime"));
                    } else {
                        // Handle case where no matching trip is found
                        tripNameLabel.setText("Trip not found");
                    }
                }
            }
        } catch (SQLException e) {
            // Handle exceptions (log or display an error message)
            e.printStackTrace();
        }
    }
}
