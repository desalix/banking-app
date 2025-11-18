package com.bankapp.util;

import com.bankapp.MainApp;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

  private final Stage primaryStage;

  // Defines the views available in the application
  public enum ViewType {
    LOGIN("LoginView.fxml"),
    REGISTER("RegisterView.fxml"),
    DASHBOARD("DashboardView.fxml"),
    TRANSACTIONS("TransactionsView.fxml");

    private final String fxmlName;

    ViewType(String fxmlName) {
      this.fxmlName = fxmlName;
    }

    public String getFxmlName() {
      return fxmlName;
    }
  }

  // We initialize the factory with the primary stage from MainApp
  public ViewFactory(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * Loads and displays a view.
   *
   * @param viewType The enum representing the view to show.
   */
  public void show(ViewType viewType) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankapp/controller/" + viewType.getFxmlName()));

    try {
      Parent root = loader.load();
      Scene scene = new Scene(root);

      // Here you could load a CSS file if you wanted:
      // scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());

      primaryStage.setScene(scene);
      primaryStage.setTitle("Bank App - " + viewType.name());
      primaryStage.show();
      primaryStage.centerOnScreen();

    } catch (IOException e) {
      e.printStackTrace();
      AlertUtils.showError("UI Error", "Could not load view: " + viewType.getFxmlName());
    }
  }

  public void closeStage(Stage stage) {
    stage.close();
  }
}
