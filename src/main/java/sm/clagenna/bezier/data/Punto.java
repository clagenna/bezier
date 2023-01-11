package sm.clagenna.bezier.data;

import java.awt.Point;

import lombok.Getter;
import lombok.Setter;

public class Punto implements Cloneable {
  private static int    s_id = 1;
  private static double s_raggioMaxBersaglio;

  @Getter @Setter
  private double        x;
  @Getter @Setter
  private double        y;
  @Getter @Setter
  private String        id;

  static {
    s_raggioMaxBersaglio = 5;
  }

  public Punto(double x, double y) {
    setX(x);
    setY(y);
    id = String.valueOf(Punto.s_id++);
  }
  public Punto(double x, double y, int i) {
    setX(x);
    setY(y);
  }

  public Punto(Point p_point) {
    setX(p_point.getX());
    setY(p_point.getY());
    id = String.valueOf(s_id++);
  }

  public boolean isBersaglio(Punto pp) {
    double dx = Math.abs(pp.getX() - x);
    if (dx > s_raggioMaxBersaglio)
      return false;
    double dy = Math.abs(pp.getY() - y);
    return dy < s_raggioMaxBersaglio;
  }

  public void setPunto(Punto p_pu) {
    this.x = p_pu.getX();
    this.y = p_pu.getY();
  }

  public void setPunto(int p_x, int p_y) {
    this.x = p_x;
    this.y = p_y;
  }
  @Override
  public String toString() {
    return String.format("p(%d,%d)", (int)x, (int)y);
  }

  public String toStringEx() {
    return String.format("%s\t%.3f\t%.3f", id, x, y);
  }

  @Override
  public boolean equals(Object p_obj) {
    if ( (p_obj == null) || ! (p_obj instanceof Punto))
      return false;
    Punto altro = (Punto) p_obj;
    return getId().equals(altro.getId());
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    Punto lp = (Punto) super.clone();
    return lp;
  }
}
