package sm.clagenna.bezier.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Line2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.sys.IDisegnabile;

/**
 * vedi anche {@linkplain https://cubic-bezier.com/#.78,.05,.12,.97}
 * @author claudio
 *
 */
public class PlotBezier implements IDisegnabile {
  @SuppressWarnings("unused")
  private static final Logger s_log = LogManager.getLogger(PlotBezier.class);
  private PlotPunto           p0;
  private PlotPunto           p1;
  private PlotPunto           p2;
  private PlotPunto           p3;

  public PlotBezier() {

  }

  public PlotBezier(PlotPunto p_p0, PlotPunto p_p1, PlotPunto p_p2, PlotPunto p_p3) {
    p0 = p_p0;
    p1 = p_p1;
    p2 = p_p2;
    p3 = p_p3;
  }

  public void add(PlotPunto p) {
    if (p0 == null)
      p0 = p;
    else if (p1 == null)
      p1 = p;
    else if (p2 == null)
      p2 = p;
    else if (p3 == null)
      p3 = p;
    else {
      throw new UnsupportedOperationException("Adding more point than two!");
    }
  }

  @Override
  public Punto getPunto() {
    throw new UnsupportedOperationException("getPunto Unsupported!");
  }

  @Override
  public void paintComponent(Graphics2D p_g) {
    Graphics2D g2 = (Graphics2D) p_g.create();
    disegnaBezierConLerp(g2);
    g2.dispose();
  }

  private void disegnaBezierConLerp(Graphics2D g2) {
    g2.setColor(Color.cyan);
    Stroke stk = new BasicStroke(2);
    g2.setColor(Color.orange);
    g2.setStroke(stk);
    PlotPunto prev = p0;
    for (double t = 0; t <= 1; t += 0.05) {
      PlotPunto A = funY(p0, p1, t);
      PlotPunto B = funY(p1, p2, t);
      PlotPunto C = funY(p2, p3, t);

      PlotPunto D = funY(A, B, t);
      PlotPunto E = funY(B, C, t);

      PlotPunto P = funY(D, E, t);
      var li = new Line2D.Double(//
          prev.getPuntoW().getX(), prev.getPuntoW().getY(), //
          P.getPuntoW().getX(), P.getPuntoW().getY());
      g2.draw(li);
      prev = P;
    }
  }

  private PlotPunto funY(PlotPunto p_p0, PlotPunto p_p1, double t) {
    Punto p0 = p_p0.getPuntoW();
    Punto p1 = p_p1.getPuntoW();
    double nx = p0.getX() + t * (p1.getX() - p0.getX());
    double ny = (nx - p1.getX()) / (p0.getX() - p1.getX()) * p0.getY() //
        - (nx - p0.getX()) / (p0.getX() - p1.getX()) * p1.getY();
    return new PlotPunto(new Punto(nx, ny, 0));
  }
}
