package view;

import config.ConfigurationVault;
import controller.MainProgramController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class ViewController {

  private MainProgramController mainProgramController;
  private Rectangle[][] rectangles;
  private final int colonyWidth;
  private final int colonyHeight;

  public ViewController(MainProgramController mainProgramController) {
    this.mainProgramController = mainProgramController;
    colonyWidth = mainProgramController.getConfig().colonyWidth;
    colonyHeight = mainProgramController.getConfig().colonyHeight;
  }

  @FXML
  private AnchorPane commonBackground;

  @FXML
  private Pane imagePane;

  @FXML
  private AnchorPane buttonsAnchorPane;

  @FXML
  private AnchorPane fieldForColony;

  @FXML
  private AnchorPane fieldContentAnchor;

  @FXML
  private Button startButton;

  @FXML
  private Button stopButton;

  @FXML
  private Button clearButton;


  @FXML
  private void stopButtonClick(ActionEvent event) {
    mainProgramController.getAppServiceModel().pause();
  }


  @FXML
  private void startButtonClick(ActionEvent event) {
    mainProgramController.unpause();
  }


  @FXML
  private void clearButtonClick(ActionEvent event) {
    mainProgramController.createNewColony();
  }


  @FXML
  void initialize() {
  }


  /**
   * Bind buttons state to a controller pause property
   */
  public void initializeButtonsBindings() {
    startButton.disableProperty().bind(
        mainProgramController.getAppServiceModel().getControllerPausedProperty().not());

    stopButton.disableProperty().bindBidirectional(
        mainProgramController.getAppServiceModel().getControllerPausedProperty());

    clearButton.disableProperty().bind(
        mainProgramController.getAppServiceModel().getControllerPausedProperty().not());

    startButton.textProperty().bind(
        Bindings.when(
            mainProgramController.getAppServiceModel()
                .getControllerPausedProperty())
            .then("Start")
            .otherwise("In run"));
  }


  /**
   * Create colony field boxes on form
   */
  public void createColonyBoxesFieldOnForm() {
    resizeAndMoveColonyFieldSocket();
    rectangles = new Rectangle[colonyWidth][colonyHeight];

    for (int i = 0; i < colonyWidth; i++) {
      for (int j = 0; j < colonyHeight; j++) {
        Rectangle rectangle = createRectangleWithCoordinates(j, i);

        BooleanProperty isCurrentCellLive = mainProgramController.getColonyField()[i][j];
        initializeRectangle(rectangle, isCurrentCellLive);

        rectangles[i][j] = rectangle;
        fieldForColony.getChildren().add(rectangle);
      }
    }
  }


  /**
   * Move game field to right place (depends on field size)
   */
  public void resizeAndMoveColonyFieldSocket() {
    final int boxWidth = ConfigurationVault.BACTERIA_BOX_WIDTH;
    final int boxSpacing = ConfigurationVault.BACTERIA_BOX_SPACING;
    final int width = (boxWidth * (colonyWidth + 3)) + (boxSpacing * (colonyWidth + 1));
    final int height = (boxWidth * (colonyHeight + 3)) + (boxSpacing * (colonyHeight + 1));

    final int borderImageWidth = width - ConfigurationVault.COLONY_BORDER_IMAGE_WIDTH_AMPLIFIER;
    final int borderImageHeight = height - ConfigurationVault.COLONY_BORDER_IMAGE_WIDTH_AMPLIFIER;
    imagePane.setMaxWidth(borderImageWidth);
    imagePane.setMinWidth(borderImageWidth);
    imagePane.setMaxHeight(borderImageHeight);
    imagePane.setMinHeight(borderImageHeight);

    final int colonyFieldWidth = width - ConfigurationVault.COLONY_FIELD_WIDTH_AMPLIFIER;
    final int colonyFieldHeight = height - ConfigurationVault.COLONY_FIELD_WIDTH_AMPLIFIER;
    fieldForColony.setMaxWidth(colonyFieldWidth);
    fieldForColony.setMinWidth(colonyFieldWidth);
    fieldForColony.setMaxHeight(colonyFieldHeight);
    fieldForColony.setMinHeight(colonyFieldHeight);

    imagePane.setTranslateX(-width / 2);
    imagePane.setTranslateY(-height / 2);

    double currentAnchorHeight = fieldContentAnchor.getHeight();
    fieldContentAnchor.setMinHeight(currentAnchorHeight - (boxWidth * (colonyHeight / 2)));
  }


  /**
   * Show error window with exit button
   */
  public static void callErrorWindow() {
    ButtonType exit = new ButtonType("Exit", ButtonBar.ButtonData.OK_DONE);
    String errorText = "Something went wrong...\nPlease, restart the application";

    Alert alert = new Alert(AlertType.WARNING, errorText, exit);
    alert.setTitle("Warning!");

    DialogPane dialogPane = alert.getDialogPane();
    dialogPane.getScene().getStylesheets().add("background.css");

    alert.showAndWait();
    Platform.exit();
  }


  private void initializeActionHandlerForRegularBox(Rectangle rectangle,
      BooleanProperty currentCell) {
    rectangle.setOnMouseClicked(event -> {
      if (!mainProgramController.isPaused()) {
        return;
      }
      currentCell.set((currentCell.get() == true) ? false : true);
    });
  }

  private Rectangle createRectangleWithCoordinates(int x, int y){
    int boxWidth = ConfigurationVault.BACTERIA_BOX_WIDTH;
    int boxSpacing = ConfigurationVault.BACTERIA_BOX_SPACING;

    return new Rectangle((boxSpacing * y) + (boxWidth * y) + 1,
        (boxSpacing * x) + (boxWidth * x) + 1, boxWidth, boxWidth);
  }

  private void initializeRectangle(Rectangle rectangle, BooleanProperty isCurrentCellLive){
    rectangle.fillProperty().bind(
        Bindings.when(isCurrentCellLive)
            .then(ConfigurationVault.BACTERIA_LIVE_COLOR)
            .otherwise(ConfigurationVault.BACTERIA_DEAD_COLOR));

    initializeActionHandlerForRegularBox(rectangle, isCurrentCellLive);
    rectangle.getStyleClass().clear();
    rectangle.getStyleClass().add("boxes");
  }
}

