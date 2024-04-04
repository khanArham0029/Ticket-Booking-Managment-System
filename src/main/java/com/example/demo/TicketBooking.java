package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketBooking {

    @FXML
    private ListView<String> tripListView;

    @FXML
    private void initialize() {
        // Load trip information from the database on initialization
        loadTripsFromDatabase();
    }

    public void onBackToMenuClick(ActionEvent actionEvent) {
        try {
            // Load the Customer.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer.fxml"));
            Parent customerParent = loader.load();

            // Create a new scene with the loaded FXML file
            Scene customerScene = new Scene(customerParent);

            // Get the stage information
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene onto the stage
            window.setScene(customerScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @FXML
    private void onViewTripDetailsClick() {
        // Get the selected trip from the list view
        String selectedTrip = tripListView.getSelectionModel().getSelectedItem();

        if (selectedTrip != null) {
            try {
                // Load the ViewTripDetails.fxml file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewTripDetails.fxml"));
                Parent viewTripDetailsParent = loader.load();

                // Get the controller from the loader
                ViewTripDetails controller = loader.getController();

                // Pass the selected trip to the controller
                controller.loadTripDetailsFromDatabase(selectedTrip);

                // Create a new scene with the loaded FXML file
                Scene viewTripDetailsScene = new Scene(viewTripDetailsParent);

                // Get the stage information
                Stage window = new Stage();

                // Set the new scene onto the stage
                window.setScene(viewTripDetailsScene);
                window.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } else {
            showAlert("Error", "Please select a trip to view details.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadTripsFromDatabase() {
        try {
            // Connection to the database (replace with your actual database connection details)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/management", "root", "muhammad");

            // Query to retrieve trip names from the database
            String selectTripsQuery = "SELECT TripName FROM Trips";

            try (PreparedStatement statement = connection.prepareStatement(selectTripsQuery);
                 ResultSet resultSet = statement.executeQuery()) {

                // Populate the list view with trip names
                ObservableList<String> tripNames = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    tripNames.add(resultSet.getString("TripName"));
                }

                // Set the trip names in the list view
                tripListView.setItems(tripNames);
            }
        } catch (SQLException e) {
            // Handle exceptions (log or display an error message)
            e.printStackTrace();
        }
    }
}
