package config;

public class ConfigurationInitializer {

  /**
   * Create, initializing and return ready-to-work program configuration
   */
  public static ConfigurationVault initializeConfiguration(String[] args) {

    if (args.length != 3) {
      return getDefaultConfig();
    }

    try {
      int min = ConfigurationVault.MIN_FIELD_SIDE_SIZE;
      int max = ConfigurationVault.MAX_FIELD_SIDE_SIZE;
      int minIterations = ConfigurationVault.MIN_ITERATIONS;
      int maxIterations = ConfigurationVault.MAX_ITERATIONS;

      int colonyWidth = clampIntoAllowedSize(Integer.parseInt(args[0]), min, max);
      int colonyHeight = clampIntoAllowedSize(Integer.parseInt(args[1]), min, max);
      int targetIterationCount =
          clampIntoAllowedSize(Integer.parseInt(args[2]), minIterations, maxIterations);

      return new ConfigurationVault(colonyWidth, colonyHeight, targetIterationCount);

    } catch (NumberFormatException e) {
      return getDefaultConfig();
    }
  }

  private static int clampIntoAllowedSize(int value, int min, int max){
    return Math.max(min, Math.min(max, value));
  }

  private static ConfigurationVault getDefaultConfig(){
    int size =  ConfigurationVault.DEFAULT_FIELD_SIZE;
    int iterations = ConfigurationVault.DEFAULT_ITERATIONS_COUNT;
    return new ConfigurationVault(size, size, iterations);
  }
}