package com.msteams.controller;

import com.msteams.service.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleComboBox;
    @FXML private Label statusLabel;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("STUDENT", "TEACHER");
    }

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (name.isBlank() || email.isBlank() || password.isBlank() || role == null) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        // The professor's requested condition: same email already registered -> error.
        if (userDAO.emailExists(email)) {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("This email is already registered.");
            return;
        }

        boolean success = userDAO.registerUser(name, email, password, role);
        if (success) {
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setText("Registration successful! You can now log in.");
            nameField.clear();
            emailField.clear();
            passwordField.clear();
            roleComboBox.setValue(null);
        } else {
            statusLabel.setStyle("-fx-text-fill: red;");
            statusLabel.setText("Something went wrong. Please try again.");
        }
    }

    @FXML
    private void goToLogin() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) nameField.getScene().getWindow(), "login.fxml");
    }
}
