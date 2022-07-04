package model;

import controller.MainProgramController;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import utils.Coordinates;

public class BacteriaBirthTask extends BacteriaBaseLifePhaseTask {

  public BacteriaBirthTask(
      MainProgramController controller,
      AppServiceModel appServiceModel,
      CyclicBarrier endOfWorkIterationBarrier,
      CyclicBarrier stopBeforeWriteBarrier
  ) {
    super(controller, appServiceModel, endOfWorkIterationBarrier, stopBeforeWriteBarrier);
  }


  /**
   * Do birth iteration and update the model
   */
  @Override
  public void doTaskWork() throws BrokenBarrierException, InterruptedException {
    BacteriaColonyModel colonyModel = mainController.getBacteriaColonyModel();

    List<Coordinates> coordinatesForBirth = colonyModel.doBirthIteration();
    colonyModel.addChangesCountToCurrentChangesCount(coordinatesForBirth.size());

    stopBeforeWriteBarrier.await();
    colonyModel.createBacteriesAtAllIncludedCoords(coordinatesForBirth);
  }

}
