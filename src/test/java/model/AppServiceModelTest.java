package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import config.ConfigurationInitializer;
import config.ConfigurationVault;
import controller.MainProgramController;
import java.util.concurrent.ExecutorService;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class AppServiceModelTest {

  MainProgramController mainController;
  ConfigurationVault config;

  @InjectMocks
  AppServiceModel appServiceModel;

  @Mock
  BacteriaBirthTask bacteriaBirthTask;

  @Mock
  BacteriaKillerTask bacteriaKillerTask;

  @Mock
  ExecutorService bacteriaLifePhasesExecutor;

  @Mock
  SimpleBooleanProperty controllerePaused;


  @BeforeEach
  void setUp() {
    String[] args = new String[]{"1", "2", "3"};
    config = ConfigurationInitializer.initializeConfiguration(args);
    mainController = new MainProgramController(config);
    appServiceModel = new AppServiceModel(mainController);
    bacteriaLifePhasesExecutor = Mockito.mock(ExecutorService.class);
    controllerePaused = Mockito.mock(SimpleBooleanProperty.class);
    MockitoAnnotations.initMocks(this);
  }


  @Test
  void getCurrentIterationsCount_Should_ReturnCurrentCount() {
    int expected = 10;
    appServiceModel.setCurrentIterationsCount(10);
    int actual = appServiceModel.getCurrentIterationsCount();
    assertEquals(expected, actual);
  }


  @Test
  void startThreads_Should_ExecuteTasks() {
    appServiceModel.startThreads();
    verify(bacteriaLifePhasesExecutor).execute(bacteriaBirthTask);
    verify(bacteriaLifePhasesExecutor).execute(bacteriaKillerTask);
  }


  @Test
  void getControllerPausedProperty_Should_ReturnSimpleBoolean() {
    assertInstanceOf(SimpleBooleanProperty.class, appServiceModel.getControllerPausedProperty());
  }

  @Test
  void resetThreads() {
    AppServiceModel localModel = spy(appServiceModel);
    localModel.resetThreads();
    verify(localModel).setCurrentIterationsCount(0);
  }


  @Test
  void stopExecution_Should_CallAllShutdowns() {
    appServiceModel.stopExecution();
    verify(bacteriaLifePhasesExecutor).shutdown();
    verify(bacteriaKillerTask).shutdown();
    verify(bacteriaBirthTask).shutdown();
  }

}