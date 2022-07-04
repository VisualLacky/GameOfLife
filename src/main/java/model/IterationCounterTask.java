package model;

import controller.MainProgramController;

public class IterationCounterTask implements Runnable {

  MainProgramController mainController;


  public IterationCounterTask(MainProgramController mainController) {
    this.mainController = mainController;
  }


  @Override
  public void run() {
    incrementIterationsCounter();
    stopColonyIfNeeded();
  }


  private void incrementIterationsCounter() {
    AppServiceModel appServiceModel = mainController.getAppServiceModel();
    int targetIterationsCount = appServiceModel.getTargetIterationsCount();
    appServiceModel.setCurrentIterationsCount(
        appServiceModel.getCurrentIterationsCount() + 1);

    System.out.println("Current iteration is: "+appServiceModel.getCurrentIterationsCount());

    if (appServiceModel.getCurrentIterationsCount() == targetIterationsCount) {
      appServiceModel.pause();
      appServiceModel.resetThreads();
    }
  }


  private void stopColonyIfNeeded() {
    stopControllerIfAllBacteriesDead();
    stopColonyIfNoChangesOccured();
  }


  private void stopControllerIfAllBacteriesDead() {
    if (mainController.getBacteriaColonyModel().getLiveBacteriesCount() <= 0) {
      mainController.createNewColony();
      mainController.pause();
    }
  }


  private void stopColonyIfNoChangesOccured() {
    BacteriaColonyModel colonyModel = mainController.getBacteriaColonyModel();
    if (colonyModel.getChangesCountOnCurrentIteration() == 0 && !mainController.isPaused()) {
      mainController.pause();
    }
    colonyModel.setChangesCountOnCurrentIteration(0);
  }
}
