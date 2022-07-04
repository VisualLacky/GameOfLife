package model;

import config.ConfigurationVault;
import controller.MainProgramController;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import javafx.application.Platform;
import view.ViewController;

public abstract class BacteriaBaseLifePhaseTask implements Runnable {

  protected MainProgramController mainController;
  protected AppServiceModel appServiceModel;
  protected CyclicBarrier endOfWorkIterationBarrier;
  protected CyclicBarrier stopBeforeWriteBarrier;
  protected boolean iterationIsOver;
  protected boolean shutdown;


  public BacteriaBaseLifePhaseTask(
      MainProgramController controller,
      AppServiceModel appServiceModel,
      CyclicBarrier endOfWorkIterationBarrier,
      CyclicBarrier stopBeforeWriteBarrier
  ) {
    this.mainController = controller;
    this.appServiceModel = appServiceModel;
    this.iterationIsOver = true;
    this.shutdown = false;
    this.endOfWorkIterationBarrier = endOfWorkIterationBarrier;
    this.stopBeforeWriteBarrier = stopBeforeWriteBarrier;
  }


  public void run() throws ThreadsInteractionException {
    while (!shutdown) {
      for (int i = 0; i < appServiceModel.getTargetIterationsCount(); i++) {
        try {
          while (mainController.isPaused()) {
            Thread.sleep(ConfigurationVault.CONTROLLER_PAUSE_WAIT_TIME);
          }
          if (isIterationIsOver()) {
            endOfWorkIterationBarrier.await();
            if (shutdown) {
              break;
            } else {
              startNewWorkIteration();
            }
          }
          doTaskWork();
          Thread.sleep(ConfigurationVault.CONTROLLER_PAUSE_WAIT_TIME);
          iterationIsOver = true;

        } catch (InterruptedException | BrokenBarrierException e) {
          mainController.stopExecution();
          shutdown = true;
          iterationIsOver = true;
          Platform.runLater(ViewController::callErrorWindow);
        }
      }

    }
  }


  /**
   * Do base work of the current thread
   */
  protected abstract void doTaskWork() throws InterruptedException, BrokenBarrierException;


  /**
   * @return status of current iteration (done or still in progress)
   */
  public boolean isIterationIsOver() {
    return iterationIsOver;
  }


  /**
   * Start new iteration of work
   */
  public void startNewWorkIteration() {
    iterationIsOver = false;
  }


  /**
   * Terminate this thread.
   */
  public void shutdown() {
    shutdown = true;
  }

}


