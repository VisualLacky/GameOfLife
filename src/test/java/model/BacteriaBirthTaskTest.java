package model;

import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;

import config.ConfigurationVault;
import controller.MainProgramController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.Coordinates;

@ExtendWith(MockitoExtension.class)
class BacteriaBirthTaskTest {

  @InjectMocks
  BacteriaBirthTask bacteriaBirthTask;


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

    bacteriaBirthTask = new BacteriaBirthTask(
        mainController,
        appServiceModel,
        endOfWorkBarrier,
        stopBeforeWriteBarrier);
  }

  @Test
  void doTaskWork_Should_CallCreateMethod_WithExpectedArgs() {
    ArrayList<Coordinates> coordinatesForBirth = new ArrayList<>();
    coordinatesForBirth.add(new Coordinates(1, 1));
    coordinatesForBirth.add(new Coordinates(2, 3));

    given(mainController.getBacteriaColonyModel().doBirthIteration())
        .willReturn(coordinatesForBirth);

    try {
      bacteriaBirthTask.doTaskWork();
    } catch (BrokenBarrierException | InterruptedException e) {
      e.printStackTrace();
    }

    verify(bacteriaColonyModel).doBirthIteration();

    ArgumentCaptor<List<Coordinates>> acList = ArgumentCaptor.forClass(List.class);
    verify(bacteriaColonyModel).createBacteriesAtAllIncludedCoords(acList.capture());
  }

}