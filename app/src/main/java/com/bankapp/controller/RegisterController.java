package com.bankapp.controller;

import com.bankapp.service.AuthService;
import com.bankapp.service.exception.UserAlreadyExistsException;
import com.bankapp.util.AlertUtils;
import com.bankapp.util.ViewFactory;
import com.bankapp.util.DependencyFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController {

  @FXML
  private TextField firstNameField;
  @FXML
  private TextField lastNameField;
  @FXML
  private TextField emailField;
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button registerButton;
  @FXML
  private Button backButton;
  @FXML
  private Label errorLabel;

  private final AuthService authService;

  public RegisterController() {
    this.authService = DependencyFactory.getAuthService();
  }

  // We need a way to inject the current stage (window) to close it/switch scenes.
  // A cleaner way in JavaFX without a framework is to just get the stage from a
  // button.

  @FXML
  public void initialize() {
    registerButton.setOnAction(event -> handleRegister());
    backButton.setOnAction(event -> goBackToLogin());
  }

  private void handleRegister() {
    try {
      authService.register(
          usernameField.getText(),
          passwordField.getText(),
          firstNameField.getText(),
          lastNameField.getText(),
          emailField.getText());

      AlertUtils.showInformation("Success", "Account created! Please log in.");
      goBackToLogin();

    } catch (UserAlreadyExistsException e) {
      errorLabel.setText(e.getMessage());
      errorLabel.setVisible(true);
    } catch (Exception e) {
      errorLabel.setText("Error: " + e.getMessage());
      errorLabel.setVisible(true);
    }
  }

  private void goBackToLogin() {
    // Get the current window (Stage)
    Stage stage = (Stage) backButton.getScene().getWindow();
    ViewFactory vf = new ViewFactory(stage);
    vf.show(ViewFactory.ViewType.LOGIN);
  }
}
