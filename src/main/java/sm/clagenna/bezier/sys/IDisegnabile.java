package sm.clagenna.bezier.sys;

import java.awt.Graphics2D;

import sm.clagenna.bezier.data.Punto;

public interface IDisegnabile {
  Punto getPunto();

  void paintComponent(Graphics2D p_g2);

}
