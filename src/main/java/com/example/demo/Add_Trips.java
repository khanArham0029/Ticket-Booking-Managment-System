package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Add_Trips {

    @FXML
    private TextField tripDescriptionField;
    @FXML
    private Stage stage;

    @FXML
    private TextField tripNameField;

    @FXML
    private TextField ticketPriceField;

    @FXML
    private TextField tripLocationField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField arrivalTimeField;

    @FXML
    private void onToggleSidebar() {
        // Implement toggle sidebar logic if needed
    }

    @FXML
    private void onSettingsClick() {
        // Implement settings button logic
        showAlert("Settings", "Settings button clicked.");
    }

    @FXML
    private void onDashboardClick(ActionEvent actionEvent) {
        // Close the Add Trip screen and open the Admin screen
        closeWindow();
        try {
            // Load the Add_trip.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Admin.fxml"));
            Parent addTripParent = loader.load();

            // Create a new scene with the loaded FXML file
            Scene addTripScene = new Scene(addTripParent);

            // Get the stage information
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene onto the stage
            window.setScene(addTripScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Close the Addtrip page
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    private void onAddTripClick() {
        // Validate input
        if (!validateInput()) {
            showAlert("Error", "Please fill in all fields with valid values.");
            return;
        }

        // Insert trip into the database
        if (insertTrip()) {
            showAlert("Success", "Trip added successfully!");
            clearFields();
        } else {
            showAlert("Error", "Failed to add trip. Check your input.");
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) tripNameField.getScene().getWindow();
        currentStage.close();
    }

    private boolean validateInput() {

        if (isEmpty(tripNameField) || isEmpty(tripDescriptionField) || isEmpty(ticketPriceField) || isEmpty(tripLocationField)
                || isEmpty(departureTimeField) || isEmpty(arrivalTimeField)) {
            showAlert("Error", "Please fill in all fields.");
            return false;
        }

        try {
            // Validate numeric fields
            Integer.parseInt(ticketPriceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Ticket Price. Please enter a valid number.");
            return false;
        }

        return true;
        // Implement validation logic (e.g., check if fields are not empty, valid numeric values, etc.)
         // Placeholder, replace with actual validation logic
    }

    private boolean isEmpty(TextField textField) {
        return textField.getText().trim().isEmpty();
    }

    private boolean insertTrip() {
        try {
            // Connection to the database (replace with your actual database connection details)
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/management", "root", "muhammad");

            // Insert data into Trips table
            String insertTripQuery = "INSERT INTO Trips (TripName, TripDescription , TicketPrice, TripLocation, DepartureTime, ArrivalTime) VALUES (?,?, ?, ?, ?, ?)";
            try (PreparedStatement tripStatement = connection.prepareStatement(insertTripQuery)) {
                tripStatement.setString(1, tripNameField.getText());
                tripStatement.setString(2, tripDescriptionField.getText());
                tripStatement.setInt(3, Integer.parseInt(ticketPriceField.getText()));
                tripStatement.setString(4, tripLocationField.getText());
                tripStatement.setString(5, departureTimeField.getText());
                tripStatement.setString(6, arrivalTimeField.getText());
                int rowsAffected = tripStatement.executeUpdate();

                // Return true if at least one row was affected (successful insertion)
                return rowsAffected > 0;
            }
        } catch (SQLException | NumberFormatException e) {
            // Handle exceptions (log or display an error message)
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearFields() {
        tripNameField.clear();
        tripDescriptionField.clear();
        ticketPriceField.clear();
        tripLocationField.clear();
        departureTimeField.clear();
        arrivalTimeField.clear();
    }
}
