package model;

import controller.MainProgramController;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

public class AppServiceModel {

  private ExecutorService bacteriaLifePhasesExecutor;
  private BacteriaBirthTask bacteriaBirthTask;
  private BacteriaKillerTask bacteriaKillerTask;
  private SimpleBooleanProperty controllerPaused;
  private int targetIterationsCount;
  private int currentIterationsCount;


  public AppServiceModel(MainProgramController mainController) {
    this.targetIterationsCount = mainController.getConfig().targetIterationsCount;
    this.currentIterationsCount = 0;
    controllerPaused = new SimpleBooleanProperty(true);
    bacteriaLifePhasesExecutor = Executors.newFixedThreadPool(2);

    CyclicBarrier endOfWorkIterationBarrier =
        new CyclicBarrier(2);
    CyclicBarrier stopBeforeWriteBarrier =
        new CyclicBarrier(2, new IterationCounterTask(mainController));

    bacteriaBirthTask = new BacteriaBirthTask(
        mainController, this, endOfWorkIterationBarrier, stopBeforeWriteBarrier);
    bacteriaKillerTask = new BacteriaKillerTask(
        mainController, this, endOfWorkIterationBarrier, stopBeforeWriteBarrier);
  }


  /**
   * Start birth and killing threads
   */
  public void startThreads() {
    bacteriaLifePhasesExecutor.execute(bacteriaBirthTask);
    bacteriaLifePhasesExecutor.execute(bacteriaKillerTask);
  }


  /**
   * Pause game
   */
  public void pause() {
    Platform.runLater(() ->
        controllerPaused.set(true)
    );
  }

  /**
   * Continue game
   */
  public void unpause() {
    Platform.runLater(() ->
        controllerPaused.set(false)
    );
  }


  /**
   * Reset data in threads for start from an blank state
   */
  public void resetThreads() {
    setCurrentIterationsCount(0);
  }


  /**
   * Stop all program tasks
   */
  public void stopExecution() {
    bacteriaLifePhasesExecutor.shutdown();
    bacteriaKillerTask.shutdown();
    bacteriaBirthTask.shutdown();
  }


  /**
   * @return game pause state
   */
  public boolean isControllerPaused() {
    return controllerPaused.get();
  }


  /**
   * @return game pause boolean property
   */
  public SimpleBooleanProperty getControllerPausedProperty() {
    return controllerPaused;
  }


  public int getCurrentIterationsCount() {
    return currentIterationsCount;
  }


  public int getTargetIterationsCount() {
    return targetIterationsCount;
  }


  public void setCurrentIterationsCount(int currentIterationsCount) {
    this.currentIterationsCount = currentIterationsCount;
  }
}
