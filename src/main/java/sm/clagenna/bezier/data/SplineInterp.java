package sm.clagenna.bezier.data;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.swing.PlotPunto;

/**
 * Crea i coefficienti per l'interpolazione di cubica di Hermite (<a>vedi
 * wikipedia<href="https://en.wikipedia.org/wiki/Cubic_Hermite_spline" >}
 */
public class SplineInterp {
  // punti da interpolare
  private List<Punto>  m_liPunti;
  // tangenti di Hermite
  private List<Double> m_her;
  @Getter
  @Setter
  private Double       minX;
  @Getter
  @Setter
  private Double       maxX;

  public SplineInterp() {
    //
  }

  public void creaInterpolazionePP(List<PlotPunto> p_ppunti) {
    List<Punto> li = p_ppunti.stream().map(s -> s.getPuntoW()).toList();
    creaInterpolazione(li);
  }

  public void creaInterpolazione(List<Punto> p_li) {
    if (p_li == null || p_li.size() < 2)
      throw new IllegalArgumentException("Non ci sono punti a sufficienza");
    m_liPunti = p_li;
    minX = m_liPunti.stream().map(s -> s.getX()).min(Double::compare).get();
    maxX = m_liPunti.stream().map(s -> s.getX()).max(Double::compare).get();
    int n = p_li.size();
    Double zero = Double.valueOf(0);
    double[] arrDeriv = new double[n - 1];
    Double[] arrSecan = new Double[n];

    // calcolo pendenza fra punti successivi
    for (int i = 0; i < n - 1; i++) {
      Punto p1 = m_liPunti.get(i);
      Punto p2 = m_liPunti.get(i + 1);
      double h = p2.getX() - p1.getX();
      if (h <= 0)
        throw new IllegalArgumentException("I punti devono essere successivi");
      // calcolo della pendenza fra i due punti
      arrDeriv[i] = (p2.getY() - p1.getY()) / h;
    }
    //    System.out.println("--- derivative --");
    //    for (int i = 0; i < n - 1; i++)
    //      System.out.printf("d(%d)=%8.3f\n", i, arrDeriv[i]);

    // inizializzo le secanti come media delle pendenze
    arrSecan[0] = arrDeriv[0];
    for (int i = 1; i < n - 1; i++) {
      arrSecan[i] = (arrDeriv[i - 1] + arrDeriv[i]) / 2f;
    }
    arrSecan[n - 1] = arrDeriv[n - 2];
    //    System.out.println("--- tangents(1) --");
    //    for (int i = 0; i < n; i++)
    //      System.out.printf("m1(%d)=%8.3f\n", i, arrSecan[i]);

    // aggiorno le tangenti di Hermite per mantenere la continuita
    for (int i = 0; i < n - 1; i++) {
      if (arrDeriv[i] == 0) {
        arrSecan[i] = zero;
        arrSecan[i + 1] = zero;
        continue;
      }
      double a, b, h;
      a = arrSecan[i] / arrDeriv[i];
      b = arrSecan[i + 1] / arrDeriv[i];
      h = Math.hypot(a, b);
      if (h > 3.) {
        double t = 3. / h;
        arrSecan[i] = t * a * arrDeriv[i];
        arrSecan[i + 1] = t * b * arrDeriv[i];
      }
    }
    //    System.out.println("--- tangents(2) --");
    //    for (int i = 0; i < n; i++)
    //      System.out.printf("m1(%d)=%8.3f\n", i, arrSecan[i]);

    m_her = Arrays.asList(arrSecan);
  }

  public double interpola(double p_x) {
    double dRet = p_x;
    final int n = m_liPunti.size();
    if (Double.isNaN(p_x))
      return dRet;
    // se sono fuori i limiti torno i limiti
    Punto p0 = m_liPunti.get(0);
    if (p_x <= p0.getX())
      return p0.getY();
    Punto pn = m_liPunti.get(n - 1);
    if (p_x >= pn.getX())
      return pn.getY();

    // cerco il punto range di 2 punti entro i quali mi trovo
    int i = 0;
    for (Punto p : m_liPunti) {
      if (p_x == p.getX())
        return p.getY();
      if (p_x < p.getX()) {
        i--;
        break;
      }
      i++;
    }

    // eseguo la Interpolazione Cubica
    Punto p1 = m_liPunti.get(i);
    Punto p2 = m_liPunti.get(i + 1);
    Double her1 = m_her.get(i);
    Double her2 = m_her.get(i + 1);

    double h = p2.getX() - p1.getX();
    double t = (p_x - p1.getX()) / h;

    // System.out.printf("i=%d, x=%1.3f, h=%1.3f, t=%1.3f\n", i, p_x, h, t);

    dRet = (p1.getY() * (1 + 2 * t) //
        + h * her1 * t) * (1 - t) * (1 - t) //
        + (p2.getY() * (3 - 2 * t) //
            + h * her2 * (t - 1)) * t * t;
    return dRet;
  }
}
