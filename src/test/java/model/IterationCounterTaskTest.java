package model;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import config.ConfigurationVault;
import controller.MainProgramController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class IterationCounterTaskTest {

  @InjectMocks
  IterationCounterTask iterationCounterTask;


  MainProgramController mainController;

  @Mock
  private BacteriaColonyModel bacteriaColonyModel;

  @Mock
  private AppServiceModel appServiceModel;

  @Mock
  private ConfigurationVault configurationVault;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    configurationVault = Mockito.mock(ConfigurationVault.class);
    bacteriaColonyModel = Mockito.mock(BacteriaColonyModel.class);
    appServiceModel = Mockito.mock(AppServiceModel.class);
    MainProgramController controller = new MainProgramController(configurationVault);
    mainController = spy(controller);

    iterationCounterTask = new IterationCounterTask(mainController);

    mainController.setAppServiceModel(appServiceModel);
    mainController.setBacteriaColonyModel(bacteriaColonyModel);
  }

  @Test
  void run_Should_CreateNewColonyAndPause_IfLiveBacteries_BelowZero() {
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(-5);
    given(appServiceModel.getTargetIterationsCount()).willReturn(1);
    given(appServiceModel.isControllerPaused()).willReturn(true);
    iterationCounterTask.run();
    verify(mainController).createNewColony();
    verify(mainController).pause();
  }


  @Test
  void run_Should_CreateNewColonyAndPause_IfLiveBacteries_Zero() {
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(0);
    given(appServiceModel.getTargetIterationsCount()).willReturn(1);
    given(appServiceModel.isControllerPaused()).willReturn(true);
    iterationCounterTask.run();
    verify(mainController).createNewColony();
    verify(mainController).pause();
  }


  @Test
  void run_ShouldNot_DoingAnything_IfLiveBacteriesGreater_ThanZero() {
    given(appServiceModel.getTargetIterationsCount()).willReturn(1);
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(5);
    given(appServiceModel.isControllerPaused()).willReturn(true);
    iterationCounterTask.run();
    verify(mainController, Mockito.times(0)).createNewColony();
    verify(mainController, Mockito.times(0)).pause();
  }


  @Test
  void run_Should_CallAppPuseAndResetThreads_If_TargetIterations_EqualsCurrent() {
    given(appServiceModel.getTargetIterationsCount()).willReturn(1);
    given(appServiceModel.getCurrentIterationsCount()).willReturn(1);
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(5);
    given(appServiceModel.isControllerPaused()).willReturn(true);
    iterationCounterTask.run();
    verify(appServiceModel).pause();
    verify(appServiceModel).resetThreads();
  }


  @Test
  void run_ShouldNot_CallAppPauseAndResetThreads_If_TargetIterations_NotEqualsCurrent() {
    given(appServiceModel.getTargetIterationsCount()).willReturn(1);
    given(appServiceModel.getCurrentIterationsCount()).willReturn(2);
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(5);
    given(appServiceModel.isControllerPaused()).willReturn(true);
    iterationCounterTask.run();
    verify(appServiceModel, Mockito.times(0)).pause();
    verify(appServiceModel, Mockito.times(0)).resetThreads();
  }
}