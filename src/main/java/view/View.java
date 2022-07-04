package view;

import config.ConfigurationInitializer;
import config.ConfigurationVault;
import controller.MainProgramController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application {

  public static MainProgramController mainProgramController;

  public static void main(String[] args) {
    ConfigurationVault config = ConfigurationInitializer.initializeConfiguration(args);
    mainProgramController = new MainProgramController(config);

    launch(args);
  }


  /**
   * Initialize javafx stage
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    ViewController viewController = new ViewController(mainProgramController);

    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Form.fxml"));
    loader.setController(viewController);
    primaryStage.setScene(new Scene(loader.load()));
    primaryStage.setResizable(false);
    setCloseAction(primaryStage);
    primaryStage.setTitle(ConfigurationVault.APP_NAME);
    primaryStage.show();

    viewController.createColonyBoxesFieldOnForm();
    viewController.initializeButtonsBindings();
  }


  private void setCloseAction(Stage primaryStage) {
    primaryStage.setOnCloseRequest(evt -> {
      mainProgramController.getAppServiceModel().unpause();
      mainProgramController.stopExecution();
      Platform.exit();
    });
  }


}
