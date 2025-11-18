package com.bankapp.controller;

import com.bankapp.model.Customer;
import com.bankapp.repository_impl.mock.MockCustomerRepository;
import com.bankapp.service.AuthService;
import com.bankapp.service.exception.AuthException;
import com.bankapp.service.impl.AuthServiceImpl;
import com.bankapp.util.AlertUtils;
import com.bankapp.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.bankapp.util.ViewFactory;

public class LoginController {

  // These fields link to the fx:id in your FXML file
  @FXML
  private TextField usernameField;
  @FXML
  private PasswordField passwordField;
  @FXML
  private Button loginButton;
  @FXML
  private Button registerButton;
  @FXML
  private Label errorLabel;

  private final AuthService authService;

  public LoginController() {
    // Manual Dependency Injection:
    // We create the service using the Mock Repository.
    // In a bigger app, a framework (like Spring) would do this for us.
    this.authService = new AuthServiceImpl(new MockCustomerRepository());
  }

  @FXML
  public void initialize() {
    loginButton.setOnAction(event -> handleLogin());

    registerButton.setOnAction(event -> {
      Stage stage = (Stage) registerButton.getScene().getWindow();
      ViewFactory vf = new ViewFactory(stage);
      vf.show(ViewFactory.ViewType.REGISTER);
    });
  }

  private void handleLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    try {
      // 1. Attempt to login
      Customer customer = authService.login(username, password);

      // 2. Store the user in the session
      UserSession.getInstance().login(customer);

      // 3. Success!
      System.out.println("Login Successful for: " + customer.getUsername());

      // TODO: Navigate to Dashboard (We will do this later)
      AlertUtils.showInformation("Success", "Logged in as " + customer.getUsername());

    } catch (AuthException e) {
      // 4. Failure - Show error in the label
      errorLabel.setText(e.getMessage());
      errorLabel.setVisible(true);
    }
  }
}
