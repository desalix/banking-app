package com.bankapp;

import com.bankapp.util.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import atlantafx.base.theme.PrimerDark;

public class MainApp extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Load Atlantafx for nice UI/UX
    Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

    // 1. Create the ViewFactory, passing the main "Stage" (window)
    ViewFactory viewFactory = new ViewFactory(primaryStage);

    // 2. Ask the factory to show the Main Layout
    viewFactory.showMainLayout();
  }

  public static void main(String[] args) {
    // 3. Launch the JavaFX application
    launch(args);
  }
}
