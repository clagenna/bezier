package sm.clagenna.bezier.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.data.SplineInterp;
import sm.clagenna.bezier.sys.IDisegnabile;

public class PlotSpline implements IDisegnabile {

  @SuppressWarnings("unused")
  private static final Logger s_log = LogManager.getLogger(PlotSpline.class);
  private List<PlotPunto>             m_ppunti;
  private SplineInterp m_spl;
  
  public PlotSpline() {
    // 
  }

  @Override
  public Punto getPunto() {
    throw new UnsupportedOperationException("getPunto Unsupported!");
  }

  @Override
  public void paintComponent(Graphics2D p_g) {
    Graphics2D g2 = (Graphics2D) p_g.create();
    disegnaSpline(g2);
    g2.dispose();
  }

  private void disegnaSpline(Graphics2D g2) {
    g2.setColor(Color.pink);
    double dblMin = m_spl.minX();
    double dblMax = m_spl.maxX();
    
  }

  public void setPunti(List<PlotPunto> p_ppunti) {
    m_ppunti=p_ppunti;
    m_spl = new SplineInterp();
    m_spl.creaInterpolazionePP(m_ppunti);

  }

}
