package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertTrue;

import config.ConfigurationInitializer;
import config.ConfigurationVault;
import model.AppServiceModel;
import model.BacteriaColonyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MainProgramControllerTest {

  MainProgramController mainController;

  @Mock
  private BacteriaColonyModel bacteriaColonyModel;

  @Mock
  private AppServiceModel appServiceModel;

  @Mock
  private ConfigurationVault configurationVault;

  @BeforeEach
  void setUp() {
    try {
      MockitoAnnotations.initMocks(this);
      appServiceModel = Mockito.mock(AppServiceModel.class);
      bacteriaColonyModel = Mockito.mock(BacteriaColonyModel.class);
      mainController = new MainProgramController(configurationVault);

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
  }


  @Test
  void MainProgramController_Should_InitializeFields() {
    mainController = new MainProgramController(configurationVault);
    assertNotNull(mainController.getBacteriaColonyModel());
    assertNotNull(mainController.getAppServiceModel());
  }


  @Test
  void isPaused_Should_ReturnPauseFromAppModel() {
    given(appServiceModel.isControllerPaused()).willReturn(true);
    assertTrue(mainController.isPaused());
  }

  @Test
  void pause() {
    mainController.pause();
    verify(mainController.getAppServiceModel()).pause();
  }


  @Test
  void unpause_Should_callReset_IfBacteriesCountZero() {
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(0);

    mainController.unpause();

    verify(mainController.getBacteriaColonyModel()).getLiveBacteriesCount();
    verify(mainController.getBacteriaColonyModel()).resetToNewColonyWithRandomFill();
    verify(mainController.getAppServiceModel()).unpause();
  }

  @Test
  void unpause_Should_callReset_IfBacteriesCountPositive() {
    given(bacteriaColonyModel.getLiveBacteriesCount()).willReturn(0);

    mainController.unpause();

    verify(mainController.getBacteriaColonyModel()).getLiveBacteriesCount();
    verify(mainController.getAppServiceModel()).unpause();
  }


  @Test
  void createNewColony_Should_CallModelsMethods() {
    mainController.createNewColony();
    verify(mainController.getBacteriaColonyModel()).resetToNewEmptyColony();
    verify(mainController.getAppServiceModel()).resetThreads();
  }

  @Test
  void getColonyField_Should_ReturnColonyField_FromModel() {
    mainController.getColonyField();
    verify(mainController.getBacteriaColonyModel()).getColonyField();
  }

  @Test
  void getConfig_Should_ReturnProgramConfiguration() {
    String[] args = new String[]{"2", "3", "5"};
    configurationVault = ConfigurationInitializer.initializeConfiguration(args);
    mainController = new MainProgramController(configurationVault);
    assertEquals(configurationVault, mainController.getConfig());
  }


  @Test
  void stopExecution_Should_CallStop_OnAppModel() {
    mainController.stopExecution();
    verify(mainController.getAppServiceModel()).stopExecution();
  }

}