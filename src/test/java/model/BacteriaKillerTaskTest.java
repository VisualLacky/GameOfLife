package model;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import config.ConfigurationVault;
import controller.MainProgramController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import utils.Coordinates;

class BacteriaKillerTaskTest {

  @InjectMocks
  BacteriaKillerTask bacteriaKillerTask;


  MainProgramController mainController;

  @Mock
  BacteriaColonyModel bacteriaColonyModel;

  @Mock
  AppServiceModel appServiceModel;

  @Mock
  private ConfigurationVault configurationVault;

  @Mock
  CyclicBarrier endOfWorkBarrier;

  @Mock
  CyclicBarrier stopBeforeWriteBarrier;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    mainController = new MainProgramController(configurationVault);
    mainController.stopExecution();

    bacteriaColonyModel = Mockito.mock(BacteriaColonyModel.class);
    appServiceModel = Mockito.mock(AppServiceModel.class);
    endOfWorkBarrier = Mockito.mock(CyclicBarrier.class);
    stopBeforeWriteBarrier = Mockito.mock(CyclicBarrier.class);

    try {

      FieldSetter.setField(
          mainController,
          mainController.getClass().getDeclaredField("bacteriaColonyModel"),
          bacteriaColonyModel);
      FieldSetter.setField(
          mainController,
          mainController.getClass().getDeclaredField("appServiceModel"),
          appServiceModel);

    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }

    bacteriaKillerTask = new BacteriaKillerTask(
        mainController,
        appServiceModel,
        endOfWorkBarrier,
        stopBeforeWriteBarrier);


  }

  @Test
  void doTaskWork_Should_CallDeleteMethod_WithExpectedArgs() {
    ArrayList<Coordinates> coordinatesForKill = new ArrayList<>();
    coordinatesForKill.add(new Coordinates(1, 1));
    coordinatesForKill.add(new Coordinates(2, 3));

    given(mainController.getBacteriaColonyModel().doKillingIteration())
        .willReturn(coordinatesForKill);

    try {
      bacteriaKillerTask.doTaskWork();
    } catch (BrokenBarrierException | InterruptedException e) {
      e.printStackTrace();
    }

    verify(bacteriaColonyModel).doKillingIteration();

    ArgumentCaptor<List<Coordinates>> acList = ArgumentCaptor.forClass(List.class);
    verify(bacteriaColonyModel).deleteBacteriesAtAllIncludedCoords(acList.capture());
  }


  @Test
  @Timeout(value = 300)
  void run_Should_End_IfShutdown_True() {
    bacteriaKillerTask.shutdown = true;
    bacteriaKillerTask.run();
  }


  @Test
  void run_Should_CallStartNewWorIteration() {
    bacteriaKillerTask.shutdown = false;
    given(appServiceModel.getTargetIterationsCount()).willReturn(5);
    BacteriaKillerTask spyTask = spy(bacteriaKillerTask);
    Thread killerThread = new Thread(spyTask);
    Thread spyKillerTread = spy(killerThread);

    spyKillerTread.start();

    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    verify(spyTask, Mockito.atLeast(1)).startNewWorkIteration();
    spyTask.shutdown = true;
  }


  @Test
  void run_Should_CallDoTaskWork() {
    bacteriaKillerTask.shutdown = false;
    bacteriaKillerTask.iterationIsOver = false;

    given(appServiceModel.getTargetIterationsCount()).willReturn(5);
    BacteriaKillerTask spyTask = spy(bacteriaKillerTask);
    given(spyTask.isIterationIsOver()).willReturn(false);
    Thread killerThread = new Thread(spyTask);
    Thread spyKillerTread = spy(killerThread);

    spyKillerTread.start();

    try {
      Thread.sleep(100);
      verify(spyTask, Mockito.atLeast(1)).doTaskWork();
      spyTask.shutdown = true;
    } catch (BrokenBarrierException | InterruptedException e) {
      e.printStackTrace();
    }
  }


  @Test
  void run_Should_ThrowException() {
    bacteriaKillerTask.shutdown = false;
    given(appServiceModel.getTargetIterationsCount()).willReturn(2);
    BacteriaKillerTask spyTask = spy(bacteriaKillerTask);
    Thread killerThread = new Thread(spyTask);

    Thread spyKillerTread = spy(killerThread);
    spyKillerTread.start();

    doThrow(new ThreadsInteractionException(new BrokenBarrierException()))
        .when(spyKillerTread).interrupt();
    assertThrows(ThreadsInteractionException.class, () -> {
      spyKillerTread.interrupt();
    });
  }


}