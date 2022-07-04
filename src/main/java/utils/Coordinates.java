package utils;

public class Coordinates {

  private int x;
  private int y;

  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }


  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Coordinates)) {
      return false;
    }
    Coordinates other = (Coordinates) obj;

    if (this.x == other.x && this.y == other.y) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return (x + x)*20 + (y + y);
  }
}
