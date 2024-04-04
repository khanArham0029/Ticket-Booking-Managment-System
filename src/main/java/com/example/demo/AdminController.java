package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Optional;

public class AdminController {

    @FXML
    private VBox sidebar;

    @FXML
    private HBox contentArea;

    @FXML
    private void onToggleSidebar() {
        sidebar.setVisible(!sidebar.isVisible());
        sidebar.setManaged(!sidebar.isManaged());
    }


    @FXML
    private void onAddTripClick(ActionEvent actionEvent) {
        try {
            // Load the Add_trip.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Addtrip.fxml"));
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
    private void onSeeUsersClick() {
        System.out.println("See Users clicked!");
        // Implement See Users logic here
    }

    @FXML
    private void onReportsAndAnalyticsClick() {
        System.out.println("Reports and Analytics clicked!");
        // Implement Reports and Analytics logic here
    }

    @FXML
    private void onPromotionsClick() {
        System.out.println("Promotions clicked!");
        // Implement Promotions logic here
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

