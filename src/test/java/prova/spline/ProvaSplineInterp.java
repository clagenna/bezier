package prova.spline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ProvaSplineInterp {

  private static List<Float> s_arrx;
  private static List<Float> s_arry;

  public static void main(String[] args) {
    float delta;
    ProvaSplineInterp.creaLista2();
    SplineInterpolator spl = SplineInterpolator.createMonotoneCubicSpline(s_arrx, s_arry);
    final String fmt1 = "%2.2f\t%2.3f\t%2.3f\n";
    @SuppressWarnings("unused") 
    final String fmt2 = "%6.2f %8.3f %8.3f\n";
    String fmt = fmt1;

    float seg = 5f;
    float xa = -1;
    float yi = -1;
    float yp = 0;
    int i = 0;
    for (float xi : s_arrx) {
      float x = xi;

      if (xa >= 0) {
        delta = (xi - xa) / seg;
        x = xa;
        for (x += delta; x < xi; x += delta) {
          yi = spl.interpolate(x);
          System.out.printf(fmt, x, yi, 0f);
        }
      }
      yp = s_arry.get(i++);
      yi = spl.interpolate(xi);
      System.out.printf(fmt, xi, yi, yp);
      xa = xi;
    }
  }

  @SuppressWarnings("unused")
  private static void creaLista() {
    final int qta = 20;
    final float nMin = 2;
    final float nMax = 20;
    s_arrx = new ArrayList<>();
    s_arry = new ArrayList<>();
    Random rnd = new Random( (new Date()).getTime());
    float delta = (nMax - nMin) / qta;
    float inix = nMin;
    for (int i = 0; i < qta; i++) {
      s_arrx.add(inix);
      float tmp = rnd.nextFloat(nMax - nMin) + nMin;
      s_arry.add(tmp);
      inix += delta;
    }
  }

  private static void creaLista2() {
    s_arrx = new ArrayList<>();
    s_arry = new ArrayList<>();
    
    s_arrx.add(0f);    s_arry.add(15f);
    s_arrx.add(1f);    s_arry.add(18f);
    s_arrx.add(2f);    s_arry.add(28f);
    s_arrx.add(3f);    s_arry.add(5f);
    s_arrx.add(4f);    s_arry.add(5f);
    s_arrx.add(5f);    s_arry.add(14f);
    s_arrx.add(6f);    s_arry.add(9f);
    s_arrx.add(7f);    s_arry.add(16f);
    s_arrx.add(8f);    s_arry.add(20f);
    s_arrx.add(9f);    s_arry.add(3f);
    s_arrx.add(10f);   s_arry.add(6f);
    s_arrx.add(11f);   s_arry.add(3f);
    s_arrx.add(12f);   s_arry.add(25f);
    s_arrx.add(13f);   s_arry.add(10f);
    s_arrx.add(14f);   s_arry.add(8f);
    s_arrx.add(15f);   s_arry.add(2f);
    s_arrx.add(16f);   s_arry.add(6f);
    s_arrx.add(17f);   s_arry.add(25f);
    s_arrx.add(18f);   s_arry.add(24f);
    s_arrx.add(19f);   s_arry.add(14f);
  }

}
