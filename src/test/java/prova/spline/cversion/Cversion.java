package prova.spline.cversion;

import java.util.Random;

/**
 * <pre>
    Numerical Analysis 9th ed - Burden, Faires (Ch. 3 Natural Cubic Spline, Pg. 149)
    https://gist.github.com/svdamani/1015c5c4b673c3297309


  int main() {
       Step 0
      int n, i, j;
      scanf("%d", &n);
      n--;
      float x[n + 1], a[n + 1], h[n], A[n], l[n + 1],
          u[n + 1], z[n + 1], c[n + 1], b[n], d[n];
      for (i = 0; i < n + 1; ++i) scanf("%f", &x[i]);
      for (i = 0; i < n + 1; ++i) scanf("%f", &a[i]);

       Step 1
      for (i = 0; i <= n - 1; ++i) h[i] = x[i + 1] - x[i];

       Step 2
      for (i = 1; i <= n - 1; ++i)
          A[i] = 3 * (a[i + 1] - a[i]) / h[i] - 3 * (a[i] - a[i - 1]) / h[i - 1];

       Step 3
      l[0] = 1;
      u[0] = 0;
      z[0] = 0;

       Step 4
      for (i = 1; i <= n - 1; ++i) {
          l[i] = 2 * (x[i + 1] - x[i - 1]) - h[i - 1] * u[i - 1];
          u[i] = h[i] / l[i];
          z[i] = (A[i] - h[i - 1] * z[i - 1]) / l[i];
      }

       Step 5
      l[n] = 1;
      z[n] = 0;
      c[n] = 0;

       Step 6
      for (j = n - 1; j >= 0; --j) {
          c[j] = z[j] - u[j] * c[j + 1];
          b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2 * c[j]) / 3;
          d[j] = (c[j + 1] - c[j]) / (3 * h[j]);
      }

       Step 7
      printf("%2s %8s %8s %8s %8s\n", "i", "ai", "bi", "ci", "di");
      for (i = 0; i < n; ++i)
          printf("%2d %8.2f %8.2f %8.2f %8.2f\n", i, a[i], b[i], c[i], d[i]);
      return 0;
  }
 * </pre>
 *
 * @author svdamani
 *
 */
public class Cversion {

  private int m_qtap;
  double      x[];
  double      y[];
  double      b[];
  double      c[];
  double      d[];
  double      h[];
  double      A[];
  double      l[];
  double      u[];
  double      z[];

  public Cversion() {
    // 
  }

  public static void main(String[] args) {
    var app = new Cversion();
    app.doTheJob();
  }

  private void doTheJob() {
    m_qtap = 16;
    initData(m_qtap);

  }

  private void initData(int p_qtap) {
    Random rnd = new Random();
    int i, j, nMin = 1, nMax = 20;
    // instanciation of data
    x = new double[p_qtap + 1];
    y = new double[p_qtap + 1];
    
    A = new double[p_qtap];
    b = new double[p_qtap];
    c = new double[p_qtap + 1];
    d = new double[p_qtap];

    h = new double[p_qtap];
    l = new double[p_qtap + 1];
    u = new double[p_qtap + 1];
    z = new double[p_qtap + 1];
    for (i = 0; i <= p_qtap; i++) {
      x[i] = 0;
      y[i] = 0;
      l[i] = 0;
      u[i] = 0;
      z[i] = 0;
      c[i] = 0;
      if (i < p_qtap) {
        h[i] = 0;
        A[i] = 0;
        b[i] = 0;
        d[i] = 0;
      }
    }
    // init of x and y
    for (i = 0; i <= p_qtap; ++i) {
      x[i] = i;
      y[i] = rnd.nextDouble(nMax - nMin) + nMin;
    }
    // init of h = delta xi, i.e. x[i+1]-x[i]

    // for (i = 0; i < n - 1; ++i) h[i] = x[i + 1] - x[i];

    for (i = 0; i < p_qtap; ++i)
      h[i] = x[i + 1] - x[i];

    // init coeff A
    for (i = 1; i < p_qtap - 1; ++i)
      A[i] = 3 * (y[i + 1] - y[i]) / h[i] - 3 * (y[i] - y[i - 1]) / h[i - 1];

    //
    l[0] = 1;
    u[0] = 0;
    z[0] = 0;

    /** Step 4 */
    for (i = 1; i < p_qtap; ++i) {
      l[i] = 2 * (x[i + 1] - x[i - 1]) - h[i - 1] * u[i - 1];
      u[i] = h[i] / l[i];
      z[i] = (A[i] - h[i - 1] * z[i - 1]) / l[i];
    }

    /** Step 5 */
    l[p_qtap] = 1;
    z[p_qtap] = 0;
    c[p_qtap] = 0;

    /** Step 6 */
    for (j = p_qtap - 1; j >= 0; --j) {
      c[j] = z[j] - u[j] * c[j + 1];
      b[j] = (y[j + 1] - y[j]) / h[j] - h[j] * (c[j + 1] + 2 * c[j]) / 3;
      d[j] = (c[j + 1] - c[j]) / (3 * h[j]);
    }

    /** Step 7 */
    System.out.printf("%2s %8s %8s %8s %8s %8s\n", "x", "y", "ai", "bi", "ci", "di");
    for (i = 0; i < p_qtap; ++i)
      System.out.printf("%2d %8.2f %8.2f %8.2f %8.2f %8.2f\n", i, y[i], A[i], b[i], c[i], d[i]);
  }

}
