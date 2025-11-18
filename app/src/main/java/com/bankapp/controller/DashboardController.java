package com.bankapp.controller;

import com.bankapp.model.Account;
import com.bankapp.model.AccountType;
import com.bankapp.model.Customer;
import com.bankapp.service.BankingService;
import com.bankapp.util.AlertUtils;
import com.bankapp.util.UserSession;
import com.bankapp.util.ViewFactory;
import com.bankapp.util.DependencyFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.Set;

public class DashboardController {

  @FXML
  private Label welcomeLabel;
  @FXML
  private Label balanceLabel;
  @FXML
  private Label accountNumLabel;
  @FXML
  private Button logoutButton;
  @FXML
  private Button transferButton;
  @FXML
  private Button transactionsButton;
  @FXML
  private Button addAccountButton;

  private final BankingService bankingService;

  public DashboardController() {
    // ⚠️ WARNING: For now, we are creating NEW mock repositories.
    // This means data created here won't be seen by other screens yet.
    // We will fix this "Shared State" issue in the very next step.
    this.bankingService = DependencyFactory.getBankingService();
  }

  @FXML
  public void initialize() {
    Customer currentUser = UserSession.getInstance().getCustomer();

    if (currentUser != null) {
      welcomeLabel.setText("Welcome, " + currentUser.getFirstName());
      updateAccountInfo(currentUser);
    }

    // Setup Buttons
    logoutButton.setOnAction(e -> handleLogout());
    addAccountButton.setOnAction(e -> handleCreateAccount());

    // Placeholder actions for now
    transferButton.setOnAction(e -> AlertUtils.showInformation("Coming Soon", "Transfer screen is next!"));
    transactionsButton.setOnAction(e -> AlertUtils.showInformation("Coming Soon", "Transaction history is next!"));
  }

  private void updateAccountInfo(Customer user) {
    // Fetch accounts for the user
    Set<Account> accounts = bankingService.getAccountsForCustomer(user.getId());

    if (accounts.isEmpty()) {
      balanceLabel.setText("$ 0.00");
      accountNumLabel.setText("No Active Accounts");
      // Disable buttons if no account
      transferButton.setDisable(true);
      transactionsButton.setDisable(true);
    } else {
      // For simplicity, just grab the first account found
      Account primaryAccount = accounts.iterator().next();
      balanceLabel.setText("$ " + primaryAccount.getBalance().toString());
      accountNumLabel.setText(primaryAccount.getAccountType() + " - " + primaryAccount.getAccountNumber());

      transferButton.setDisable(false);
      transactionsButton.setDisable(false);
      addAccountButton.setDisable(true); // Hide create button if they have one
    }
  }

  private void handleCreateAccount() {
    try {
      // Create a default checking account for the current user
      Customer user = UserSession.getInstance().getCustomer();
      // We generate a random account number for now
      String randomAccNum = "CH-" + (int) (Math.random() * 10000);

      bankingService.createAccount(user, AccountType.CHECKING, randomAccNum);

      AlertUtils.showInformation("Success", "New Checking Account Opened!");
      updateAccountInfo(user); // Refresh the UI

    } catch (Exception e) {
      AlertUtils.showError("Error", "Could not create account: " + e.getMessage());
    }
  }

  private void handleLogout() {
    UserSession.getInstance().logout();
    Stage stage = (Stage) logoutButton.getScene().getWindow();
    new ViewFactory(stage).show(ViewFactory.ViewType.LOGIN);
  }
}
