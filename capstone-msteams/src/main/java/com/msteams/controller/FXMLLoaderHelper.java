package com.msteams.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Small helper so every controller doesn't repeat the same
 * "load FXML and swap the stage's scene" boilerplate.
 */
public class FXMLLoaderHelper {

    public static void switchScene(Stage stage, String fxmlFileName) throws IOException {
        FXMLLoader loader = new FXMLLoader(FXMLLoaderHelper.class.getResource("/com/msteams/" + fxmlFileName));
        Parent root = loader.load();
        Scene scene = new Scene(root, 720, 520);
        stage.setScene(scene);
        stage.show();
    }
}
