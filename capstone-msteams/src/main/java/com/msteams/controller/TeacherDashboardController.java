package com.msteams.controller;

import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class TeacherDashboardController {

    @FXML private Label welcomeLabel;

    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    public void initialize() {
        var user = sessionManager.loadSession();
        if (user == null) {
            redirectToLogin();
            return;
        }
        welcomeLabel.setText("Welcome, " + user.getName());
    }

    @FXML
    private void goToManageAssignments() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "manage_assignments.fxml");
    }

    @FXML
    private void goToChannel() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "channel.fxml");
    }

    @FXML
    private void logOut() throws IOException {
        sessionManager.clearSession();
        FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "login.fxml");
    }

    private void redirectToLogin() {
        try {
            FXMLLoaderHelper.switchScene((Stage) welcomeLabel.getScene().getWindow(), "login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
