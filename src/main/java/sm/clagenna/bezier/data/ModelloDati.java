package sm.clagenna.bezier.data;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class ModelloDati implements Serializable, PropertyChangeListener {
  private static final long   serialVersionUID = 375605770128849415L;
  private static final Logger s_log            = LogManager.getLogger(ModelloDati.class);
  private List<Punto>         liPunti;
  @Getter @Setter
  private Punto               bersaglio;
  @Getter @Setter
  private boolean             disegnaGriglia;
  private TrasponiFinestra    m_trasp;

  public ModelloDati() {
    initData();
  }

  private void initData() {
    m_trasp = new TrasponiFinestra(this);

  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    s_log.debug("change {} val={}", p_evt.getPropertyName(), p_evt.getNewValue());
  }

  public List<Punto> getPunti() {
    return liPunti;
  }

  public TrasponiFinestra getTraspondiFinestra() {
    return m_trasp;
  }

  public boolean checkSelezionePunto(Point p_point) {
    Punto punto = m_trasp.convertiX(p_point);
    bersaglio = null;
    if (liPunti == null || liPunti.size() == 0)
      return false;
    for (Punto p : liPunti) {
      if (p.isBersaglio(punto)) {
        bersaglio = p;
        break;
      }
    }
    return bersaglio != null;
  }

  /**
   * Imposta la dimensione del foglio su cui si andra a disegnare
   * 
   * @param p_dim
   *          larghezza e altezza del foglio
   */
  public void setDimensione(Dimension p_dim) {
    m_trasp.setDimensione(p_dim);
  }
}
