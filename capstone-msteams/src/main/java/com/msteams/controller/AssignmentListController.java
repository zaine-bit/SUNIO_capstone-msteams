package com.msteams.controller;

import com.msteams.model.Assignment;
import com.msteams.service.AssignmentDAO;
import com.msteams.service.DataStore;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;

public class AssignmentListController {

    @FXML private ListView<Assignment> assignmentListView;

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();

    @FXML
    public void initialize() {
        assignmentListView.getItems().setAll(assignmentDAO.getAll());
    }

    @FXML
    private void openSelected() throws IOException {
        Assignment selected = assignmentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Select an assignment first.").showAndWait();
            return;
        }
        DataStore.getInstance().setSelectedAssignment(selected);
        FXMLLoaderHelper.switchScene((Stage) assignmentListView.getScene().getWindow(), "assignment_detail.fxml");
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) assignmentListView.getScene().getWindow(), "student_dashboard.fxml");
    }
}
