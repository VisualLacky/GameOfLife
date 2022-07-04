package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Coordinates;

class BacteriaColonyModelTest {

  private BacteriaColonyModel model;
  private int width = 10;
  private int height = 20;

  @BeforeEach
  void setUp() {
    model = new BacteriaColonyModel(width, height);
  }

  @Test
  void constructorTest() {
    model = new BacteriaColonyModel(width, height);
    assertEquals(model.getColonyField().length, width);
    assertEquals(model.getColonyField()[0].length, height);
  }


  @Test
  void resetToNewEmptyColony_Should_SetColonyFieldToEmpty() {
    model.getColonyField()[0][0].set(true);
    model.getColonyField()[1][0].set(true);
    model.getColonyField()[2][3].set(true);
    model.getColonyField()[4][4].set(true);
    model.resetToNewEmptyColony();

    BacteriaColonyModel expectedModel = new BacteriaColonyModel(width, height);

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        assertEquals(
            expectedModel.getColonyField()[i][j].get(), model.getColonyField()[i][j].get());
      }
    }
  }


  @Test
  void resetToNewColonyWithRandomFill_Should_SetColonyField_NotEmpty() {
    BacteriaColonyModel model = new BacteriaColonyModel(5, 5);
    model.resetToNewColonyWithRandomFill();

    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        if (model.getColonyField()[i][j].get()) {
          assertTrue(true);
        }
      }
    }
  }


  @Test
  void getLiveBacteriesCount_Should_ReturnExpectedCount() {
    setColonyFieldDifferentValues(model.getColonyField());
    int expected = 16;
    int result = 0;
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (model.getColonyField()[i][j].get()) {
          result++;
        }
      }
    }
    assertEquals(expected, result);
  }


  @Test
  void deleteBacteriesAtAllIncludedCoords_Should_DeleteOnlyOnGivenCoords() {
    ArrayList<Coordinates> bacteriesForDelete = new ArrayList<>();
    bacteriesForDelete.add(new Coordinates(2, 6));
    bacteriesForDelete.add(new Coordinates(3, 5));
    bacteriesForDelete.add(new Coordinates(7, 8));

    setColonyFieldDifferentValues(model.getColonyField());
    model.deleteBacteriesAtAllIncludedCoords(bacteriesForDelete);

    BacteriaColonyModel expectedModel = new BacteriaColonyModel(width, height);
    setColonyFieldDifferentValues(expectedModel.getColonyField());
    expectedModel.getColonyField()[2][6].set(false);
    expectedModel.getColonyField()[3][5].set(false);
    expectedModel.getColonyField()[7][8].set(false);

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (expectedModel.getColonyField()[i][j].get() != model.getColonyField()[i][j].get()) {
          fail();
        }
      }
    }
  }

  @Test
  void createBacteriesAtAllIncludedCoords_Should_CreateOnlyOnGivenCoords() {
    ArrayList<Coordinates> bacteriesForCreate = new ArrayList<>();
    bacteriesForCreate.add(new Coordinates(1, 8));
    bacteriesForCreate.add(new Coordinates(4, 2));
    bacteriesForCreate.add(new Coordinates(5, 9));
    bacteriesForCreate.add(new Coordinates(8, 0));

    setColonyFieldDifferentValues(model.getColonyField());
    model.createBacteriesAtAllIncludedCoords(bacteriesForCreate);

    BacteriaColonyModel expectedModel = new BacteriaColonyModel(width, height);
    setColonyFieldDifferentValues(expectedModel.getColonyField());
    expectedModel.getColonyField()[1][8].set(true);
    expectedModel.getColonyField()[4][2].set(true);
    expectedModel.getColonyField()[5][9].set(true);
    expectedModel.getColonyField()[8][0].set(true);

    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (expectedModel.getColonyField()[i][j].get() != model.getColonyField()[i][j].get()) {
          fail();
        }
      }
    }
  }

  @Test
  void countNeigborsOfBacteria_Should_ReturnRightCount() {
    setColonyFieldDifferentValues(model.getColonyField());
    assertEquals(3, model.countNeigborsOfBacteria(new Coordinates(0, 0)));
    assertEquals(3, model.countNeigborsOfBacteria(new Coordinates(0, 1)));
    assertEquals(2, model.countNeigborsOfBacteria(new Coordinates(0, 2)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(0, 3)));
    assertEquals(5, model.countNeigborsOfBacteria(new Coordinates(1, 0)));
    assertEquals(5, model.countNeigborsOfBacteria(new Coordinates(1, 1)));
    assertEquals(3, model.countNeigborsOfBacteria(new Coordinates(1, 2)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(1, 3)));
    assertEquals(3, model.countNeigborsOfBacteria(new Coordinates(2, 0)));
    assertEquals(3, model.countNeigborsOfBacteria(new Coordinates(2, 1)));
    assertEquals(2, model.countNeigborsOfBacteria(new Coordinates(2, 2)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(2, 3)));
    assertEquals(2, model.countNeigborsOfBacteria(new Coordinates(3, 0)));
    assertEquals(2, model.countNeigborsOfBacteria(new Coordinates(3, 1)));
    assertEquals(1, model.countNeigborsOfBacteria(new Coordinates(3, 2)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(3, 3)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(7, 0)));
    assertEquals(1, model.countNeigborsOfBacteria(new Coordinates(7, 1)));
    assertEquals(0, model.countNeigborsOfBacteria(new Coordinates(7, 2)));
    assertEquals(1, model.countNeigborsOfBacteria(new Coordinates(7, 3)));
  }


  @Test
  void doKillingIteration_Should_ReturnEmptyList_ifColonyIsEmpty() {
    List<Coordinates> result = model.doKillingIteration();
    List<Coordinates> expected = new ArrayList<>();
    assertEquals(expected, result);
  }


  @Test
  void doKillingIteration_Should_ReturnExpectedList() {
    BooleanProperty[][] colonyField = model.getColonyField();
    setColonyFieldDifferentValues(colonyField);
    List<Coordinates> result = model.doKillingIteration();
    List<Coordinates> expected = new ArrayList<>();
    expected.add(new Coordinates(1, 0));
    expected.add(new Coordinates(1, 1));
    expected.add(new Coordinates(7, 2));
    expected.add(new Coordinates(7, 7));
    expected.add(new Coordinates(7, 8));
    assertEquals(expected, result);
  }


  @Test
  void doBirthIteration_Should_ReturnEmptyList_ifColonyIsEmpty() {
    List<Coordinates> result = model.doBirthIteration();
    List<Coordinates> expected = new ArrayList<>();
    assertEquals(expected, result);
  }

  @Test
  void doBirthIteration_Should_ReturnExpectedList() {
    BooleanProperty[][] colonyField = model.getColonyField();
    setColonyFieldDifferentValues(colonyField);
    List<Coordinates> result = model.doBirthIteration();
    List<Coordinates> expected = new ArrayList<>();
    expected.add(new Coordinates(1, 2));
    expected.add(new Coordinates(2, 5));
    expected.add(new Coordinates(3, 7));
    expected.add(new Coordinates(4, 5));
    expected.add(new Coordinates(6, 7));
    expected.add(new Coordinates(7, 9));
    assertEquals(expected, result);
  }


  @Test
  void initializeColonyModel_Should_SetEmptyColonyField() {
    model.initializeColonyModel();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        if (model.getColonyField()[i][j].get() == true) {
          fail();
        }
      }
    }
  }


  private void setColonyFieldDifferentValues(BooleanProperty[][] colonyField) {
    colonyField[0][0].set(true);
    colonyField[0][1].set(true);
    colonyField[1][0].set(true);
    colonyField[1][1].set(true);
    colonyField[2][0].set(true);
    colonyField[2][1].set(true);
    colonyField[7][2].set(true);
    colonyField[2][6].set(true);
    colonyField[3][5].set(true);
    colonyField[3][6].set(true);
    colonyField[4][6].set(true);
    colonyField[6][8].set(true);
    colonyField[7][7].set(true);
    colonyField[7][8].set(true);
    colonyField[8][7].set(true);
    colonyField[8][8].set(true);
  }

}