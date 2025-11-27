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

        animateSidebarButton(homeButton);
        animateSidebarButton(transactionsButton);
        animateSidebarButton(investmentsButton);
        animateSidebarButton(accountsButton);

        animateProfileButton(profileButton);
    }

    private void animateSidebarButton(Button button) {
        final javafx.scene.paint.Color HOVER_BG_COLOR = javafx.scene.paint.Color.web("#F4F4F4");
        final javafx.scene.paint.Color BASE_BG_COLOR = javafx.scene.paint.Color.WHITE;

        final javafx.scene.paint.Color HOVER_TEXT_COLOR = javafx.scene.paint.Color.BLACK;
        final javafx.scene.paint.Color BASE_TEXT_COLOR = javafx.scene.paint.Color.web("#666666");

        button.setOnMouseEntered(e -> {
            if (!button.getStyleClass().contains("active")) {
                playBackgroundAnimation(button, BASE_BG_COLOR, HOVER_BG_COLOR);
                playTextFillAnimation(button, BASE_TEXT_COLOR, HOVER_TEXT_COLOR);
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.getStyleClass().contains("active")) {
                playBackgroundAnimation(button, HOVER_BG_COLOR, BASE_BG_COLOR);
                playTextFillAnimation(button, HOVER_TEXT_COLOR, BASE_TEXT_COLOR);
            }
        });
    }

    private void animateProfileButton(Button button) {
        final javafx.scene.paint.Color HOVER_COLOR = javafx.scene.paint.Color.BLACK;
        final javafx.scene.paint.Color NORMAL_COLOR = javafx.scene.paint.Color.web("#555555");

        button.setOnMouseEntered(e -> {
            playTextFillAnimation(button, NORMAL_COLOR, HOVER_COLOR);
        });

        button.setOnMouseExited(e -> {
            playTextFillAnimation(button, HOVER_COLOR, NORMAL_COLOR);
        });
    }

    private void playBackgroundAnimation(Button button, javafx.scene.paint.Color start, javafx.scene.paint.Color end) {
        javafx.animation.Transition transition = new javafx.animation.Transition() {
            {
                setCycleDuration(javafx.util.Duration.millis(300));
            }

            @Override
            protected void interpolate(double frac) {
                button.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundFill(
                        start.interpolate(end, frac),
                        new javafx.scene.layout.CornerRadii(8),
                        javafx.geometry.Insets.EMPTY)));
            }
        };
        transition.play();
    }

    private void playTextFillAnimation(Button button, javafx.scene.paint.Color start, javafx.scene.paint.Color end) {
        javafx.animation.Transition transition = new javafx.animation.Transition() {
            {
                setCycleDuration(javafx.util.Duration.millis(300));
            }

            @Override
            protected void interpolate(double frac) {
                button.setTextFill(start.interpolate(end, frac));
            }
        };
        transition.play();
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
        if (homeButton != activeButton)
            resetButton(homeButton);
        if (transactionsButton != activeButton)
            resetButton(transactionsButton);
        if (investmentsButton != activeButton)
            resetButton(investmentsButton);
        if (accountsButton != activeButton)
            resetButton(accountsButton);

        // Animate to Active
        if (!activeButton.getStyleClass().contains("active")) {
            activeButton.getStyleClass().add("active");
            activeButton.setBackground(null); // Clear bg

            // Animate Text to Blue (Accent)
            // Assuming accent color is #2962ff (from styles.css) or similar.
            // Let's use the one from styles: #0070D2 (based on previous edits) or #2962ff.
            // Checking styles.css, I used -color-accent-fg.
            // I'll use a hardcoded blue that matches the theme for now to ensure smooth
            // transition.
            final javafx.scene.paint.Color ACCENT_COLOR = javafx.scene.paint.Color.web("#2962ff");
            final javafx.scene.paint.Color NORMAL_COLOR = javafx.scene.paint.Color.web("#666666");

            playTextFillAnimation(activeButton, NORMAL_COLOR, ACCENT_COLOR);
        }
    }

    private void resetButton(Button button) {
        if (button.getStyleClass().contains("active")) {
            button.getStyleClass().remove("active");
            button.setBackground(null);

            // Animate back to Normal
            final javafx.scene.paint.Color ACCENT_COLOR = javafx.scene.paint.Color.web("#2962ff");
            final javafx.scene.paint.Color NORMAL_COLOR = javafx.scene.paint.Color.web("#666666");

            playTextFillAnimation(button, ACCENT_COLOR, NORMAL_COLOR);
        }
    }
}
