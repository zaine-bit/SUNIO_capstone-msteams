package com.msteams;

import com.msteams.model.Teacher;
import com.msteams.model.User;
import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the capstone project.
 *
 * On startup, checks whether session.dat already exists (from a previous
 * login that never logged out). If so, the user is taken straight to their
 * dashboard instead of the login screen -- this is the "maintain the user's
 * session while navigating the system" requirement demonstrated concretely:
 * the serialized file is what's actually consulted, not just in-memory state
 * that resets when the app restarts.
 */
public class Main extends Application {

    private final ISessionManager sessionManager = new FileSessionManager();

    @Override
    public void start(Stage primaryStage) throws Exception {
        String startingScreen = determineStartingScreen();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/msteams/" + startingScreen));
        Parent root = loader.load();

        primaryStage.setTitle("MS Teams for Education - Capstone");
        primaryStage.setScene(new Scene(root, 720, 520));
        primaryStage.show();
    }

    private String determineStartingScreen() {
        if (!sessionManager.hasActiveSession()) {
            return "login.fxml";
        }
        User user = sessionManager.loadSession();
        if (user == null) {
            return "login.fxml";
        }
        return (user instanceof Teacher) ? "teacher_dashboard.fxml" : "student_dashboard.fxml";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
