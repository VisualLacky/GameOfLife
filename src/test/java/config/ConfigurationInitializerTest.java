package config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConfigurationInitializerTest {

  @Test
  void initializeConfiguration_Should_ReturnNewCondigurationVault() {
    String[] args = new String[] {"1","2","3"};

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.MIN_FIELD_SIDE_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.MIN_FIELD_SIDE_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.MIN_ITERATIONS, result.targetIterationsCount);
  }

  @Test
  void initializeConfiguration_Should_ReturnNewCondigurationVault_If_ArgsNegative() {
    String[] args = new String[] {"-1","-2","-3"};

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.MIN_FIELD_SIDE_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.MIN_FIELD_SIDE_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.MIN_ITERATIONS, result.targetIterationsCount);
  }


  @Test
  void initializeConfiguration_Should_ReturnNewCondigurationVault_If_ArgsTooBig() {
    String[] args = new String[] {"1000","2000","3000"};

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.MAX_FIELD_SIDE_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.MAX_FIELD_SIDE_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.MAX_ITERATIONS, result.targetIterationsCount);
  }


  @Test
  void initializeConfiguration_Should_ReturnDefaultConfig_If_ArgsEmpty() {
    String[] args = new String[0];

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.DEFAULT_ITERATIONS_COUNT, result.targetIterationsCount);
  }

  @Test
  void initializeConfiguration_Should_ReturnDefaultConfig_If_TooManyArgs() {
    String[] args = new String[]{"1","2","3","4"};

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.DEFAULT_ITERATIONS_COUNT, result.targetIterationsCount);
  }

  @Test
  void initializeConfiguration_Should_ReturnDefaultConfig_If_ArgsAreNotInts() {
    String[] args = new String[3];
    args[0] = "1";
    args[1] = "STRING";
    args[2] = "3";

    ConfigurationVault result = ConfigurationInitializer.initializeConfiguration(args);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyWidth);
    assertEquals(ConfigurationVault.DEFAULT_FIELD_SIZE, result.colonyHeight);
    assertEquals(ConfigurationVault.DEFAULT_ITERATIONS_COUNT, result.targetIterationsCount);
  }
}