package com.msteams.controller;

import com.msteams.model.Assignment;
import com.msteams.service.AssignmentDAO;
import com.msteams.service.DataStore;
import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Full CRUD screen for a Teacher: Create, Read (list), Update, and Delete
 * assignments. Selecting an assignment and clicking "View / Grade Submissions"
 * hands off to SubmissionsController for that assignment's CRUD on grades.
 */
public class ManageAssignmentsController {

    @FXML private ListView<Assignment> assignmentListView;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField dueDateField;
    @FXML private Label statusLabel;

    private final AssignmentDAO assignmentDAO = new AssignmentDAO();
    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    public void initialize() {
        refreshList();
        assignmentListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                titleField.setText(newVal.getTitle());
                descriptionField.setText(newVal.getDescription());
                dueDateField.setText(newVal.getDueDate().toString());
            }
        });
    }

    private void refreshList() {
        Assignment previouslySelected = assignmentListView.getSelectionModel().getSelectedItem();
        assignmentListView.getItems().setAll(assignmentDAO.getAll());
        if (previouslySelected != null) {
            assignmentListView.getItems().stream()
                    .filter(a -> a.getAssignmentId() == previouslySelected.getAssignmentId())
                    .findFirst()
                    .ifPresent(a -> assignmentListView.getSelectionModel().select(a));
        }
    }

    @FXML
    private void handleCreate() {
        LocalDate dueDate = parseDate();
        if (dueDate == null) return;
        if (titleField.getText().isBlank()) {
            statusLabel.setText("Title cannot be empty.");
            return;
        }

        int teacherId = sessionManager.loadSession().getUserId();
        boolean success = assignmentDAO.create(titleField.getText(), descriptionField.getText(), dueDate, teacherId);

        statusLabel.setText(success ? "Assignment created." : "Failed to create assignment.");
        if (success) {
            handleClear();
            refreshList();
        }
    }

    @FXML
    private void handleUpdate() {
        Assignment selected = assignmentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an assignment from the list first.");
            return;
        }
        LocalDate dueDate = parseDate();
        if (dueDate == null) return;

        boolean success = assignmentDAO.update(selected.getAssignmentId(), titleField.getText(), descriptionField.getText(), dueDate);
        statusLabel.setText(success ? "Assignment updated." : "Failed to update assignment.");
        if (success) {
            refreshList();
        }
    }

    @FXML
    private void handleDelete() {
        Assignment selected = assignmentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an assignment to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + selected.getTitle() + "\"? This also deletes its submissions.");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = assignmentDAO.delete(selected.getAssignmentId());
                statusLabel.setText(success ? "Assignment deleted." : "Failed to delete assignment.");
                handleClear();
                refreshList();
            }
        });
    }

    @FXML
    private void handleClear() {
        titleField.clear();
        descriptionField.clear();
        dueDateField.clear();
        assignmentListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void viewSubmissions() throws IOException {
        Assignment selected = assignmentListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select an assignment first.");
            return;
        }
        DataStore.getInstance().setSelectedAssignment(selected);
        FXMLLoaderHelper.switchScene((Stage) assignmentListView.getScene().getWindow(), "submissions.fxml");
    }

    private LocalDate parseDate() {
        try {
            return LocalDate.parse(dueDateField.getText().trim());
        } catch (DateTimeParseException e) {
            statusLabel.setText("Due date must be in YYYY-MM-DD format.");
            return null;
        }
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) assignmentListView.getScene().getWindow(), "teacher_dashboard.fxml");
    }
}
