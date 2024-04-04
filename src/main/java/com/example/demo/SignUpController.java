package com.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class SignUpController {
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button signUpButton;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        // Initialize the ComboBox with role options
        roleComboBox.setItems(FXCollections.observableArrayList("Customer", "Admin"));
    }

    @FXML
    private void onSignUpButtonClick() {
        // Retrieve user input
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String selectedRole = roleComboBox.getValue();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedRole == null) {
            statusLabel.setText("Sign up failed. Fill in Fields.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        // Check if the password length is at least 6 characters
        if (password.length() < 6) {
            statusLabel.setText("Password must be at least 6 characters long.");
            return;
        }

        try {
            // Connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/management", "root", "muhammad");

            // Determine RoleID based on the selected role
            int roleID;
            String roleName;

            if (selectedRole.equals("Customer")) {
                roleID = 2;  // Assuming Customer role has RoleID 2
                roleName = "Customer";
            } else if (selectedRole.equals("Admin")) {
                roleID = 1;  // Assuming Admin role has RoleID 1
                roleName = "Admin";
            } else {
                statusLabel.setText("Invalid role selected.");
                return;
            }

            // Insert user data into the Users table
            String insertUserQuery = "INSERT INTO Users (Username, Password, RoleID) VALUES (?, ?, ?)";
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, username);
                userStatement.setString(2, password);
                userStatement.setInt(3, roleID);
                userStatement.executeUpdate();

                // Get the generated UserID
                ResultSet userResultSet = userStatement.getGeneratedKeys();
                int userID;
                if (userResultSet.next()) {
                    userID = userResultSet.getInt(1);

                    // Insert data into role-specific table
                    if (selectedRole.equals("Customer")) {
                        String insertCustomerQuery = "INSERT INTO Customers (UserID, Username) VALUES (?, ?)";
                        try (PreparedStatement customerStatement = connection.prepareStatement(insertCustomerQuery)) {
                            customerStatement.setInt(1, userID);
                            customerStatement.setString(2, username);
                            customerStatement.executeUpdate();
                        }
                    } else if (selectedRole.equals("Admin")) {
                        String insertAdminQuery = "INSERT INTO Admins (UserID, Username) VALUES (?, ?)";
                        try (PreparedStatement adminStatement = connection.prepareStatement(insertAdminQuery)) {
                            adminStatement.setInt(1, userID);
                            adminStatement.setString(2, username);
                            adminStatement.executeUpdate();
                        }
                    }
                }
            }

            // Display success message
            statusLabel.setText("Sign up successful. You can now login.");
            Stage stage = (Stage) signUpButton.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            // Handle database errors
            e.printStackTrace();
            statusLabel.setText("Sign up failed. Check your input.");
        }
    }
}
