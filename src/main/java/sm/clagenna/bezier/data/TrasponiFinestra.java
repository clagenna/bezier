package sm.clagenna.bezier.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class TrasponiFinestra implements PropertyChangeListener {
  private static final Logger   s_log         = LogManager.getLogger(TrasponiFinestra.class);
  public static double          s_zoomDefault = 1F;

  /** zoom della trasposiz. Wx --> x */
  @Getter
  private double                zoom;
  @Getter @Setter
  private double                width;
  @Getter @Setter
  private double                eight;
  /** max dim della windows */
  @Getter
  private double                maxX;
  @Getter
  private double                maxY;

  private transient ModelloDati m_dati;

  public TrasponiFinestra(ModelloDati p_tr) {
    m_dati = p_tr;
    setZoom(s_zoomDefault);
    inizializza();
  }

  //  public TrasponiFinestra(JPanel p_pan) {
  //    setZoom(s_zoomDefault);
  //    resetGeometry(p_pan);
  //    inizializza();
  //  }

  public TrasponiFinestra(Rectangle p_r) {
    setZoom(s_zoomDefault);
    setDimensione(p_r);
    inizializza();
  }

  public TrasponiFinestra(Dimension p_size) {
    setZoom(s_zoomDefault);
    setDimensione(p_size);
    inizializza();
  }

  private void inizializza() {
    PropertyChangeBroadcaster broadc = PropertyChangeBroadcaster.getInst();
    broadc.removePropertyChangeListener(getClass());
    broadc.addPropertyChangeListener(this);
  }

  public void setDimensione(Dimension p_size) {
    setWidth(p_size.getWidth());
    setEight(p_size.getHeight());
  }

  public void setDimensione(Rectangle p_r) {
    setWidth(p_r.getWidth());
    setEight(p_r.getHeight());
  }

  public ModelloDati getDati() {
    return m_dati;
  }

  public Punto convertiX(Point p) {
    return convertiX(new Punto(p));
  }

  /**
   * Converte dalle coordinate window a quelle cartesiane
   *
   * @param p
   * @return
   */
  public Punto convertiX(Punto p) {
    Punto ret = null;
    try {
      ret = (Punto) p.clone();
    } catch (CloneNotSupportedException e) {
      s_log.error("Error In convertX", e);
    }
    double lx = p.getX() / zoom;
    double ly = (getEight() - p.getY()) / zoom;
    ret.setX(lx);
    ret.setY(ly);
    return ret;
  }

  /**
   * Converte dalle coordinate cartesiane a quelle window
   *
   * @param p
   * @return
   */
  public Punto convertiW(Punto p) {
    Punto ret = null;
    try {
      ret = (Punto) p.clone();
    } catch (CloneNotSupportedException e) {
      s_log.error("Error In convertW", e);
    }
    double lx = p.getX() * zoom;
    // double ly = height - p.getPy() * zoom;
    double ly = maxY - p.getY() * zoom;
    ret.setX((int) lx);
    ret.setY((int) ly);
    // System.out.println("convertiW=" + ret.toString());
    return ret;
  }

  public void setZoom(double pv) {
    double delta = pv - zoom;
    if ( (Math.abs(delta) > 25 || Math.abs(pv) > 25))
      System.out.println("TrasponiFinestra.setZoom():" + pv);
    zoom = pv;
  }

  /**
   * Mi arriva lo zoom sotto forma di quantità di clic della rotella. di solito
   * 1/-1
   *
   * @param pv
   */
  public void incrZoom(int pv) {
    setZoom(zoom + pv / 2.);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    if ( ! (obj instanceof EPropChange))
      return;
    EPropChange pch = (EPropChange) obj;
    switch (pch) {

      case panelRezized:
        Dimension dim = (Dimension) p_evt.getNewValue();
        System.out.println("TrasponiFinestra.propertyChange():" + dim.toString());
        setWidth(dim.getWidth());
        setEight(dim.getHeight());
        break;

      default:
        break;
    }
  }

}
