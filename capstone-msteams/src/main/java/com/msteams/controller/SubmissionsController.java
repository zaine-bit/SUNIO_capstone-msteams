package com.msteams.controller;

import com.msteams.model.Assignment;
import com.msteams.model.Submission;
import com.msteams.service.DataStore;
import com.msteams.service.SubmissionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Implements grading -- the UPDATE half of submissions CRUD, matching the
 * "gradeSubmission()" step from the combined sequence diagram.
 */
public class SubmissionsController {

    @FXML private Label assignmentTitleLabel;
    @FXML private ListView<Submission> submissionListView;
    @FXML private TextField gradeField;
    @FXML private Label statusLabel;

    private Assignment assignment;
    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    @FXML
    public void initialize() {
        assignment = DataStore.getInstance().getSelectedAssignment();
        assignmentTitleLabel.setText("Submissions for: " + assignment.getTitle());
        refreshList();

        submissionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.isGraded()) {
                gradeField.setText(String.valueOf(newVal.getGrade()));
            } else {
                gradeField.clear();
            }
        });
    }

    private void refreshList() {
        submissionListView.getItems().setAll(submissionDAO.getByAssignment(assignment.getAssignmentId()));
    }

    @FXML
    private void handleSaveGrade() {
        Submission selected = submissionListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Select a submission first.");
            return;
        }

        double grade;
        try {
            grade = Double.parseDouble(gradeField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Grade must be a number.");
            return;
        }

        boolean success = submissionDAO.updateGrade(selected.getSubmissionId(), grade);
        statusLabel.setText(success ? "Grade saved." : "Failed to save grade.");
        if (success) {
            refreshList();
        }
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) assignmentTitleLabel.getScene().getWindow(), "manage_assignments.fxml");
    }
}
