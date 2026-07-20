package com.msteams.controller;

import com.msteams.model.Assignment;
import com.msteams.model.Submission;
import com.msteams.model.User;
import com.msteams.service.DataStore;
import com.msteams.service.FileSessionManager;
import com.msteams.service.ISessionManager;
import com.msteams.service.SubmissionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Implements the "Submit Assignment" flow from the sequence/activity diagrams:
 * display details -> check deadline -> attach file -> validate file -> save submission.
 */
public class AssignmentDetailController {

    @FXML private Label titleLabel;
    @FXML private Label dueDateLabel;
    @FXML private TextArea descriptionArea;
    @FXML private TextField fileField;
    @FXML private Label statusLabel;

    private Assignment assignment;
    private User currentUser;
    private final SubmissionDAO submissionDAO = new SubmissionDAO();
    private final ISessionManager sessionManager = new FileSessionManager();

    @FXML
    public void initialize() {
        currentUser = sessionManager.loadSession();
        assignment = DataStore.getInstance().getSelectedAssignment();
        titleLabel.setText(assignment.getTitle());
        dueDateLabel.setText("Due: " + assignment.getDueDate());
        descriptionArea.setText(assignment.getDescription());

        Submission existing = submissionDAO.getByStudentAndAssignment(currentUser.getUserId(), assignment.getAssignmentId());
        if (existing != null) {
            String gradeText = existing.isGraded() ? ("Graded: " + existing.getGrade()) : "Not yet graded";
            statusLabel.setText("Already submitted: " + existing.getFileUrl() + " (" + gradeText + ")");
        }
    }

    @FXML
    private void handleSubmit() {
        // Decision point 1: has the deadline passed?
        if (LocalDate.now().isAfter(assignment.getDueDate())) {
            statusLabel.setText("Submission closed: the deadline for this assignment has passed.");
            return;
        }

        // Decision point 2: is the attached file valid (not blank)?
        String fileName = fileField.getText();
        if (fileName == null || fileName.isBlank()) {
            statusLabel.setText("Please attach a file before submitting.");
            return;
        }

        boolean success = submissionDAO.create(currentUser.getUserId(), assignment.getAssignmentId(), fileName);

        if (success) {
            statusLabel.setText("Submission successful! Your file \"" + fileName + "\" was submitted.");
            fileField.clear();
        } else {
            statusLabel.setText("Something went wrong saving your submission. Please try again.");
        }
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) titleLabel.getScene().getWindow(), "assignment_list.fxml");
    }
}
