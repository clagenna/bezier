package prova.spline;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.data.SplineInterp;

public class ProvaMiaInterp {

  private List<Punto> m_li;

  @Test
  public void provalo() {
    SplineInterp spl = new SplineInterp();
    int qta = creaLista1();

    spl.creaInterpolazione(m_li);
    System.out.println("X\tY\tYp");
    for (double x = 0; x < qta; x += 0.1) {
      double yp = trovaPunto(x);
      //      int id = (int) (x * 100d);
      //      int p = m_li.indexOf(new Punto(x, 0, id));
      //      if (p >= 0)
      //        yp = m_li.get(p).getY();
      double y = spl.interpola(x);
      System.out.printf("%1.2f\t%8.3f\t%8.3f\n", x, y, yp);
    }
  }

  private double trovaPunto(double p_x) {
    NumberFormat fmt = NumberFormat.getInstance();
    String sz2 = fmt.format(p_x);
    for (Punto p : m_li) {
      String sz1 = fmt.format(p.getX());
      // if (p.getX() == p_x)
      if (sz2.equals(sz1))
        return p.getY();
    }
    return 0;
  }

  @SuppressWarnings("unused")
  private int creaLista1() {
    m_li = new ArrayList<>();
    int nMin = 2;
    int nMax = 30;
    Random rnd = new Random( (new Date()).getTime());

    int qta = 20;
    for (int i = 0; i < qta; i++) {
      Punto p = new Punto(i, rnd.nextInt(nMin, nMax), i * 100);
      m_li.add(p);
      // System.out.println(p);
    }
    return qta;
  }

  @SuppressWarnings("unused")
  private int creaLista2() {
    m_li = new ArrayList<>();
    m_li.add(new Punto(0, 15));
    m_li.add(new Punto(1, 18));
    m_li.add(new Punto(2, 28));
    m_li.add(new Punto(3, 5));
    m_li.add(new Punto(4, 20));
    m_li.add(new Punto(5, 14));
    m_li.add(new Punto(6, 9));
    m_li.add(new Punto(7, 16));
    m_li.add(new Punto(8, 20));
    m_li.add(new Punto(9, 3));
    m_li.add(new Punto(10, 6));
    m_li.add(new Punto(11, 3));
    m_li.add(new Punto(12, 25));
    m_li.add(new Punto(13, 10));
    m_li.add(new Punto(14, 8));
    m_li.add(new Punto(15, 2));
    m_li.add(new Punto(16, 6));
    m_li.add(new Punto(17, 25));
    m_li.add(new Punto(18, 24));
    m_li.add(new Punto(19, 14));
    return m_li.size();
  }

}
