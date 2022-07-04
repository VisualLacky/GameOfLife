package config;

import javafx.scene.paint.Color;

public class ConfigurationVault {

  public static final String APP_NAME = "Lacky's Colony app";
  public static final int CONTROLLER_PAUSE_WAIT_TIME = 100;
  public static final int DEFAULT_FIELD_SIZE = 50;
  public static final int MAX_FIELD_SIDE_SIZE = 55;
  public static final int MIN_FIELD_SIDE_SIZE = 5;
  public static final int DEFAULT_ITERATIONS_COUNT = 50;
  public static final int MIN_ITERATIONS = 3;
  public static final int MAX_ITERATIONS = 100;
  public static final int BACTERIA_BOX_WIDTH = 10;
  public static final int BACTERIA_BOX_SPACING = 1;
  public static final int COLONY_BORDER_IMAGE_WIDTH_AMPLIFIER = 5;
  public static final int COLONY_FIELD_WIDTH_AMPLIFIER = 30;

  public static final int CHANCE_OF_BACTERIA_APPEAR_IN_PERCENT = 30;
  public static final Color BACTERIA_LIVE_COLOR = Color.rgb(68, 106, 189);
  public static final Color BACTERIA_DEAD_COLOR = Color.LIGHTGRAY;

  public final int colonyWidth;
  public final int colonyHeight;
  public final int targetIterationsCount;


  public ConfigurationVault(int colonyWidth, int colonyHeight, int targetIterationsCount) {
    this.colonyWidth = colonyWidth;
    this.colonyHeight = colonyHeight;
    this.targetIterationsCount = targetIterationsCount;
  }
}
