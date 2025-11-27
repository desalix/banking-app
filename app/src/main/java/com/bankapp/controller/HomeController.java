package com.bankapp.controller;

import com.bankapp.util.UserSession;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HomeController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label totalBalanceLabel;
    @FXML
    private Label incomeLabel;
    @FXML
    private Label expenseLabel;
    @FXML
    private LineChart<String, Number> financialChart;
    @FXML
    private ListView<String> recentTransactionsList;

    @FXML
    public void initialize() {
        setupWelcomeSection();
        setupSummaryCards();
        setupFinancialChart();
        setupRecentTransactions();
    }

    private void setupWelcomeSection() {
        String username = "User";
        if (UserSession.getInstance().isUserLoggedIn()) {
            username = UserSession.getInstance().getCustomer().getFirstName();
        }
        welcomeLabel.setText("Welcome back, " + username + "!");
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy")));
    }

    private void setupSummaryCards() {
        // Dummy data for now
        totalBalanceLabel.setText("$12,450.00");
        incomeLabel.setText("$4,200.00");
        expenseLabel.setText("$1,850.00");
    }

    private void setupFinancialChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Balance History");
        series.getData().add(new XYChart.Data<>("Jan", 10000));
        series.getData().add(new XYChart.Data<>("Feb", 11200));
        series.getData().add(new XYChart.Data<>("Mar", 10800));
        series.getData().add(new XYChart.Data<>("Apr", 12450));

        financialChart.getData().add(series);
    }

    private void setupRecentTransactions() {
        // Dummy data
        recentTransactionsList.getItems().addAll(
                "Netflix Subscription - $15.99",
                "Grocery Store - $124.50",
                "Salary Deposit - $4,200.00",
                "Electric Bill - $85.00");
    }
}
