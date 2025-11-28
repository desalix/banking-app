package com.bankapp.controller;

import com.bankapp.util.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

  @FXML
  private Label pageTitle;
  @FXML
  private Button homeButton;
  @FXML
  private Button transactionsButton;
  @FXML
  private Button investmentsButton;
  @FXML
  private Button accountsButton;
  @FXML
  private Button profileButton;
  @FXML
  private StackPane contentArea;

  private ViewFactory viewFactory;

  public void setViewFactory(ViewFactory viewFactory) {
    this.viewFactory = viewFactory;
  }

  @FXML
  public void initialize() {
    homeButton.setOnAction(e -> showHome());
    transactionsButton.setOnAction(e -> showTransactions());
    investmentsButton.setOnAction(e -> showInvestments());
    accountsButton.setOnAction(e -> showAccounts());
    profileButton.setOnAction(e -> System.out.println("Profile clicked"));
  }

  public void showHome() {
    replaceContent("Home", "Home.fxml");
    setActiveButton(homeButton);
  }

  public void showTransactions() {
    replaceContent("Transactions", "Transactions.fxml");
    setActiveButton(transactionsButton);
  }

  public void showInvestments() {
    replaceContent("Investments", "Investments.fxml");
    setActiveButton(investmentsButton);
  }

  public void showAccounts() {
    replaceContent("Accounts", "Accounts.fxml");
    setActiveButton(accountsButton);
  }

  private void replaceContent(String title, String fxmlFile) {
    pageTitle.setText(title);
    if (viewFactory != null) {
      Node node = viewFactory.loadView(fxmlFile);
      contentArea.getChildren().setAll(node);
    }
  }

  private void setActiveButton(Button activeButton) {
    // Reset others
    resetButton(homeButton);
    resetButton(transactionsButton);
    resetButton(investmentsButton);
    resetButton(accountsButton);

    // Set Active
    if (!activeButton.getStyleClass().contains("active")) {
      activeButton.getStyleClass().add("active");
    }
  }

  private void resetButton(Button button) {
    button.getStyleClass().remove("active");
  }
}
