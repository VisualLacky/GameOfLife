package controller;

import config.ConfigurationVault;
import javafx.beans.property.BooleanProperty;
import model.AppServiceModel;
import model.BacteriaColonyModel;

public class MainProgramController {

  private ConfigurationVault configurationVault;
  private BacteriaColonyModel bacteriaColonyModel;
  private AppServiceModel appServiceModel;


  public MainProgramController(ConfigurationVault config) {
    configurationVault = config;
    initializeModels();
  }


  /**
   * @return game pause state
   */
  public boolean isPaused() {
    return appServiceModel.isControllerPaused();
  }


  /**
   * Pause game
   */
  public void pause() {
    appServiceModel.pause();
  }


  /**
   * Continue game
   */
  public void unpause() {
    if (bacteriaColonyModel.getLiveBacteriesCount() == 0) {
      bacteriaColonyModel.resetToNewColonyWithRandomFill();
    }
    appServiceModel.unpause();
  }


  /**
   * Re-initialize all colony (include colonyModel and all colony threads)
   */
  public void createNewColony() {
    bacteriaColonyModel.resetToNewEmptyColony();
    appServiceModel.resetThreads();
  }


  /**
   * Return current game field array from the colonyModel
   */
  public BooleanProperty[][] getColonyField() {
    return bacteriaColonyModel.getColonyField();
  }


  /**
   * @return current program configuration
   */
  public ConfigurationVault getConfig() {
    return configurationVault;
  }


  /**
   * Order serviceModel to stop all program tasks
   */
  public void stopExecution() {
    appServiceModel.stopExecution();
  }


  private void initializeModels() {
    bacteriaColonyModel = initializeBacteriaColonyModel();
    appServiceModel = initializeAppServiceModel();
  }


  private BacteriaColonyModel initializeBacteriaColonyModel() {
    return new BacteriaColonyModel(configurationVault.colonyWidth, configurationVault.colonyHeight);
  }


  private AppServiceModel initializeAppServiceModel() {
    AppServiceModel serviceModel = new AppServiceModel(this);
    serviceModel.startThreads();
    return serviceModel;
  }


  public AppServiceModel getAppServiceModel() {
    return appServiceModel;
  }


  public BacteriaColonyModel getBacteriaColonyModel() {
    return bacteriaColonyModel;
  }


  public void setBacteriaColonyModel(BacteriaColonyModel bacteriaColonyModel) {
    this.bacteriaColonyModel = bacteriaColonyModel;
  }


  public void setAppServiceModel(AppServiceModel appServiceModel) {
    this.appServiceModel = appServiceModel;
  }
}


