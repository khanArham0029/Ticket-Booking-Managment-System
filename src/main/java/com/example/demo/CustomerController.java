package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class CustomerController {
    @FXML
    private VBox sidebar;

    @FXML
    private void onTicketBookingClick(ActionEvent actionEvent) {
        try {
            // Load the Add_trip.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TicketBooking.fxml"));
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
    private void onCancelBookingClick() {
        System.out.println("Cancel Booking clicked!");
        // Implement Cancel Booking logic here
    }

    @FXML
    private void onViewBookingHistoryClick() {
        System.out.println("View Booking History clicked!");
        // Implement View Booking History logic here
    }

    @FXML
    private void onFeedbackAndRatingsClick() {
        System.out.println("Feedback and Ratings clicked!");
        // Implement Feedback and Ratings logic here
    }

    @FXML
    private void onToggleSidebar() {
        sidebar.setVisible(!sidebar.isVisible());
        sidebar.setManaged(!sidebar.isManaged());
    }

    @FXML
    private void onLogoutClick(ActionEvent event) {
        // Ask for confirmation before logging out
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.setContentText("Are you sure you want to logout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Close the Admin page
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
