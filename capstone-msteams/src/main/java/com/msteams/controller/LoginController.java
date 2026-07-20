package com.msteams.controller;

import com.msteams.model.Student;
import com.msteams.model.Teacher;
import com.msteams.model.User;
import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import com.msteams.service.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserDAO userDAO = new UserDAO();
    // Depends on the interface, not FileSessionManager directly (Dependency Inversion Principle).
    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            errorLabel.setText("Please enter your email and password.");
            return;
        }

        User user = userDAO.authenticate(email, password);
        if (user == null) {
            errorLabel.setText("Incorrect email or password.");
            return;
        }

        // Creates session.dat containing the logged-in user's info.
        sessionManager.saveSession(user);
        navigateToDashboard(user);
    }

    private void navigateToDashboard(User user) {
        try {
            Stage stage = (Stage) emailField.getScene().getWindow();
            if (user instanceof Teacher) {
                FXMLLoaderHelper.switchScene(stage, "teacher_dashboard.fxml");
            } else if (user instanceof Student) {
                FXMLLoaderHelper.switchScene(stage, "student_dashboard.fxml");
            }
        } catch (IOException ex) {
            errorLabel.setText("Could not load dashboard: " + ex.getMessage());
        }
    }

    @FXML
    private void goToRegister() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) emailField.getScene().getWindow(), "register.fxml");
    }
}
