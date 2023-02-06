package sm.clagenna.bezier.data;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.swing.PlotPunto;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class ModelloDati implements Serializable, PropertyChangeListener, Closeable {
  private static final long   serialVersionUID = 375605770128849415L;
  private static final Logger s_log            = LogManager.getLogger(ModelloDati.class);
  private List<Punto>         liPunti;
  @Getter @Setter
  private Punto               bersaglioX;
  @Getter @Setter
  private Punto               lastAddedPunto;
  @Getter @Setter
  private boolean             disegnaGriglia;
  private TrasponiFinestra    m_trasp;

  public enum TipoCurva {
    Bezier, Spline
  }

  @Getter
  private TipoCurva tipoCurva = TipoCurva.Bezier;

  public ModelloDati() {
    initData();
  }

  private void initData() {
    m_trasp = new TrasponiFinestra(this);
    if ( !Beans.isDesignTime())
      PropertyChangeBroadcaster.getInst().addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    s_log.debug("changeOf({}) val={}", p_evt.getPropertyName(), p_evt.getNewValue());
    Object obj = p_evt.getOldValue();
    if ( ! (obj instanceof EPropChange pch))
      return;

    switch (pch) {

      case nuovoPunto:
        evtNuovoPunto(p_evt);
        break;

      default:
        break;
    }

  }

  private void evtNuovoPunto(PropertyChangeEvent p_evt) {
    Punto p = null;
    Object obj = p_evt.getNewValue();
    if (obj instanceof Point po)
      p = new Punto(po);
    if (p == null && obj instanceof Punto)
      p = (Punto) obj;
    if (p == null) {
      s_log.error("non interpreto evt \"{}\" val={}", p_evt.getPropertyName(), p_evt.getNewValue());
      return;
    }
    var px = m_trasp.convertiX(p);
    addPunto(px);
  }

  public List<Punto> getPunti() {
    return liPunti;
  }

  public void setTipoCurva(TipoCurva p) {
    if (p == tipoCurva)
      return;
    tipoCurva = p;
    PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.tipoGrafico, p);
  }

  public void setPuntoDrag(PlotPunto p_ppSelez) {
    for (Punto p : liPunti) {
      if (p.getId().equals(p_ppSelez.getId())) {
        p.setPunto(p_ppSelez.getPuntoX());
        break;
      }
    }
  }

  public TrasponiFinestra getTraspondiFinestra() {
    return m_trasp;
  }

  public boolean checkSelezionePunto(Point p_pointW) {
    Punto punto = m_trasp.convertiX(p_pointW);
    bersaglioX = null;
    if (liPunti == null || liPunti.size() == 0)
      return false;
    for (Punto p : liPunti) {
      if (p.isBersaglio(punto)) {
        bersaglioX = p;
        break;
      }
    }
    return bersaglioX != null;
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

  public void addPunto(Punto p_px) {
    if (liPunti == null)
      liPunti = new ArrayList<>();
    if ( !liPunti.contains(p_px))
      liPunti.add(p_px);
    setLastAddedPunto(p_px);
  }

  @Override
  public void close() throws IOException {
    if (liPunti != null)
      liPunti.clear();
    liPunti = null;
    PropertyChangeBroadcaster.getInst().removePropertyChangeListener(this);
  }

}
