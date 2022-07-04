package utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoordinatesTest {

  Coordinates coordinates;

  @BeforeEach
  void setUp() {
    coordinates = new Coordinates(5, 10);
  }

  @Test
  void equals1_Should_ReturnFalse_IfObjectNotACoordinatesClass() {
    Object other = new Object();
    assertFalse(coordinates.equals(other));
  }

  @Test
  void equals1_Should_ReturnFalse_IfObjectHasDifferentCoordinates() {
    Coordinates other = new Coordinates(1, 8);
    assertFalse(coordinates.equals(other));
  }

  @Test
  void equals1_Should_ReturnTrue_IfObjectHasSameCoordinates() {
    Coordinates other = new Coordinates(5, 10);
    assertTrue(coordinates.equals(other));
  }

  @Test
  void hashCode1() {
    int expected = 220;
    assertEquals(expected, coordinates.hashCode());
  }
}