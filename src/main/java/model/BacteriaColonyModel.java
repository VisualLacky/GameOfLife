package model;

import config.ConfigurationVault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import utils.Coordinates;

public class BacteriaColonyModel {

  public static final Random RANDOM = new Random();
  private BooleanProperty[][] colonyField;
  private int width;
  private int height;
  private int changesCountOnCurrentIteration;

  public BacteriaColonyModel(int width, int height) {
    this.width = width;
    this.height = height;
    initializeColonyModel();
  }


  /**
   * Add given changes count to current count of all changes in colony on current iteration
   */
  public synchronized void addChangesCountToCurrentChangesCount(int numberToAdd) {
    changesCountOnCurrentIteration += numberToAdd;
  }


  /**
   * Create a new game field array instead of the old one
   */
  public void resetToNewEmptyColony() {
    for (int i = 0; i < colonyField.length; i++) {
      for (int j = 0; j < colonyField[0].length; j++) {
        colonyField[i][j].set(false);
      }
    }
  }


  /**
   * Create a new game field array instead of the old one and fill it with RANDOM true/false values
   */
  public void resetToNewColonyWithRandomFill() {
    for (int i = 0; i < colonyField.length; i++) {
      for (int j = 0; j < colonyField[0].length; j++) {
        boolean value = calculateProbabilityOfSpawn();
        colonyField[i][j].set(value);
      }
    }
  }


  /**
   * Return number of live bacteria on the game field
   */
  public int getLiveBacteriesCount() {
    int bacteriaCounter = 0;

    for (int i = 0; i < colonyField.length; i++) {
      for (int j = 0; j < colonyField[0].length; j++) {
        if (colonyField[i][j].get()) {
          bacteriaCounter++;
        }
      }
    }
    return bacteriaCounter;
  }


  /**
   * Delete all given bacteria from the game field
   */
  public synchronized void deleteBacteriesAtAllIncludedCoords(
      List<Coordinates> bacteriesCoordinatesForDelete) {

    for (Coordinates coords : bacteriesCoordinatesForDelete) {
      colonyField[coords.getX()][coords.getY()].set(false);
    }
  }


  /**
   * Create new bacteria at all given coordinates on the game field
   */
  public synchronized void createBacteriesAtAllIncludedCoords(
      List<Coordinates> bacteriesCoordinatesForCreate) {

    for (Coordinates coords : bacteriesCoordinatesForCreate) {
      colonyField[coords.getX()][coords.getY()].set(true);
    }
  }


  /**
   * Goes for each game field array cell and check condition for die of live bacteria
   *
   * @return list of coordinates, where already live bacteria should die
   */
  public List<Coordinates> doKillingIteration() {
    ArrayList<Coordinates> coordinatesForKill = new ArrayList<>();

    for (int i = 0; i < colonyField.length; i++) {
      for (int j = 0; j < colonyField[0].length; j++) {

        Coordinates coordsForCheck = new Coordinates(i, j);
        if (!isGivenCellAlive(coordsForCheck)) {
          continue;
        }
        int neighbors = countNeigborsOfBacteria(coordsForCheck);
        if (neighbors < 2 || neighbors > 3) {
          coordinatesForKill.add(coordsForCheck);
        }

      }
    }
    return coordinatesForKill;
  }


  /**
   * Goes for each game field array cell and check condition for birth of new bacteria
   *
   * @return list of coordinates, where new bacteria should spawn
   */
  public List<Coordinates> doBirthIteration() {
    ArrayList<Coordinates> coordinatesForBirth = new ArrayList<>();

    for (int i = 0; i < colonyField.length; i++) {
      for (int j = 0; j < colonyField[0].length; j++) {

        Coordinates coordsForCheck = new Coordinates(i, j);
        if (isGivenCellAlive(coordsForCheck)) {
          continue;
        }
        if (countNeigborsOfBacteria(coordsForCheck) == 3) {
          coordinatesForBirth.add(coordsForCheck);
        }

      }
    }
    return coordinatesForBirth;
  }


  /**
   * Return number of neighboring live bacteria for a given coordinates
   */
  public int countNeigborsOfBacteria(Coordinates coords) {
    int neigbors = 0;

    int leftNeighborsBorder = coords.getX() - 1;
    int rightNeighborsBorder = coords.getX() + 1;
    int topNeighborsBorder = coords.getY() - 1;
    int bottomNeighborsBorder = coords.getY() + 1;

    for (int i = leftNeighborsBorder; i <= rightNeighborsBorder; i++) {
      for (int j = topNeighborsBorder; j <= bottomNeighborsBorder; j++) {

        Coordinates currentCell = new Coordinates(i, j);
        if (isOutOfBounds(currentCell)) {
          continue;
        } else if (coords.equals(currentCell)) {
          continue;
        } else if (isGivenCellAlive(currentCell)) {
          neigbors++;
        }

      }
    }
    return neigbors;
  }


  /**
   * @return true if the given coordinates is outside the colony field
   */
  private boolean isOutOfBounds(Coordinates coords) {
    boolean result = false;

    boolean isXLessThanMin = coords.getX() < 0;
    boolean isXGreaterThanMax = coords.getX() > width - 1;
    boolean isYLessThanMin = coords.getY() < 0;
    boolean isYGreaterThanMax = coords.getY() > height - 1;

    Stream<Boolean> checkBordersStream = Stream.of(
        isXGreaterThanMax, isXLessThanMin, isYGreaterThanMax, isYLessThanMin);

    if (checkBordersStream.anyMatch(Boolean.TRUE::equals)) {
      result = true;
    }
    return result;
  }


  /**
   * Initialize colony field and fill it with empty bacteria
   */
  public void initializeColonyModel() {
    colonyField = new BooleanProperty[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        colonyField[i][j] = new SimpleBooleanProperty(false);
      }
    }
  }


  /**
   * @return true if LIVE bacteria is placed on the colony field at the given coordinates
   */
  private boolean isGivenCellAlive(Coordinates coords) {
    return colonyField[coords.getX()][coords.getY()].get();
  }


  /**
   * @return probability of bacteria occurence; probability is set as a percentage in a config file
   */
  private boolean calculateProbabilityOfSpawn() {
    int probability = ConfigurationVault.CHANCE_OF_BACTERIA_APPEAR_IN_PERCENT;
    int dice = RANDOM.nextInt(100);
    return (dice < probability);
  }


  /**
   * Return game field array
   */
  public BooleanProperty[][] getColonyField() {
    return colonyField;
  }


  public int getChangesCountOnCurrentIteration() {
    return changesCountOnCurrentIteration;
  }


  public void setChangesCountOnCurrentIteration(int changesCountOnCurrentIteration) {
    this.changesCountOnCurrentIteration = changesCountOnCurrentIteration;
  }
}
