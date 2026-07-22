package com.msteams.controller;

import com.msteams.model.Assignment;
import com.msteams.model.Submission;
import com.msteams.model.User;
import com.msteams.service.ClassroomFacade;
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

// Handles the Submit Assignment screen; validation/save logic now lives in ClassroomFacade.
public class AssignmentDetailController {

    @FXML private Label titleLabel;
    @FXML private Label dueDateLabel;
    @FXML private TextArea descriptionArea;
    @FXML private TextField fileField;
    @FXML private Label statusLabel;

    private Assignment assignment;
    private User currentUser;
    private final SubmissionDAO submissionDAO = new SubmissionDAO();
    private final ClassroomFacade classroomFacade = new ClassroomFacade();
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
        ClassroomFacade.Result result = classroomFacade.submitAssignment(currentUser.getUserId(), assignment, fileField.getText());
        statusLabel.setText(result.message);
        if (result.success) {
            fileField.clear();
        }
    }

    @FXML
    private void goBack() throws IOException {
        FXMLLoaderHelper.switchScene((Stage) titleLabel.getScene().getWindow(), "assignment_list.fxml");
    }
}
