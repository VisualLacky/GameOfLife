package model;

import controller.MainProgramController;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import utils.Coordinates;

public class BacteriaKillerTask extends BacteriaBaseLifePhaseTask {

  public BacteriaKillerTask(
      MainProgramController controller,
      AppServiceModel appServiceModel,
      CyclicBarrier endOfWorkIterationBarrier,
      CyclicBarrier stopBeforeWriteBarrier
  ) {
    super(controller, appServiceModel, endOfWorkIterationBarrier, stopBeforeWriteBarrier);
  }


  /**
   * Do killing iteration and update the model
   */
  @Override
  public void doTaskWork() throws BrokenBarrierException, InterruptedException {
    BacteriaColonyModel colonyModel = mainController.getBacteriaColonyModel();

    List<Coordinates> coordinatesForKill = colonyModel.doKillingIteration();
    colonyModel.addChangesCountToCurrentChangesCount(coordinatesForKill.size());

    stopBeforeWriteBarrier.await();
    mainController.getBacteriaColonyModel().deleteBacteriesAtAllIncludedCoords(coordinatesForKill);
  }

}
