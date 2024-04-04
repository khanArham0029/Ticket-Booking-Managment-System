package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    // JDBC connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "muhammad";

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Query to retrieve user information including RoleID
            String query = "SELECT * FROM Users WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, enteredUsername);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // User found, check password
                    String storedPassword = resultSet.getString("Password");
                    int roleID = resultSet.getInt("RoleID");

                    if (enteredPassword.equals(storedPassword)) {
                        // Successful login
                        statusLabel.setText("Login successful!");

                        // Redirect based on RoleID
                        if (roleID == 1) {
                            // Admin
                            loadAdminPage();
                        } else if (roleID == 2) {
                            // Customer
                            loadCustomerPage();
                        }
                        // You may navigate to another scene or perform additional actions here
                    } else {
                        // Incorrect password
                        statusLabel.setText("Invalid password. Please try again.");
                    }
                } else {
                    // User not found
                    statusLabel.setText("User not found. Please check your username.");
                }
            }
        } catch (SQLException e) {
            // Handle database connection or query errors
            e.printStackTrace();
            statusLabel.setText("Login failed. Please try again.");
        }
    }

    private void loadAdminPage() {
        try {
            // Load the Admin.fxml file
            Parent adminRoot = FXMLLoader.load(getClass().getResource("Admin.fxml"));

            // Create a new stage for the admin page
            Stage adminStage = new Stage();
            adminStage.setTitle("Admin Page");
            adminStage.setScene(new Scene(adminRoot, 600, 400));

            // Show the admin stage
            adminStage.show();

            // Close the current (login) stage if needed
            // ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerPage() {
        try {
            // Load the Customer.fxml file
            Parent customerRoot = FXMLLoader.load(getClass().getResource("Customer.fxml"));

            // Create a new stage for the customer page
            Stage customerStage = new Stage();
            customerStage.setTitle("Customer Page");
            customerStage.setScene(new Scene(customerRoot, 600, 400));

            // Show the customer stage
            customerStage.show();

            // Close the current (login) stage if needed
            // ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSignUpButtonClick(ActionEvent actionEvent) {
        loadSignUpPage();
    }

    private void loadSignUpPage() {
        try {
            // Load the SignUp.fxml file
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("SignUp.fxml"));

            // Create a new stage for the sign-up page
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(new Scene(signUpRoot, 400, 340));

            // Show the sign-up stage
            signUpStage.show();

            // Close the current (login) stage if needed
            // ((Stage) usernameField.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
