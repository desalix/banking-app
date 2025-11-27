package com.bankapp.util;

import com.bankapp.controller.MainLayoutController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

  private final Stage primaryStage;
  private MainLayoutController mainLayoutController;

  public ViewFactory(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void showMainLayout() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankapp/view/MainLayout.fxml"));
    try {
      Parent root = loader.load();
      mainLayoutController = loader.getController();
      mainLayoutController.setViewFactory(this);

      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setTitle("Finsaver");
      primaryStage.show();
      primaryStage.centerOnScreen();

      // Show Home view by default
      mainLayoutController.showHome();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Parent loadView(String fxmlName) {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bankapp/view/" + fxmlName));
    try {
      return loader.load();
    } catch (IOException e) {
      e.printStackTrace();
      return new StackPane(); // Return empty pane on error
    }
  }
}
