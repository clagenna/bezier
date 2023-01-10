package sm.clagenna.bezier.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.enumerati.EMouseGesture;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.IBroadcast;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MyPanel extends JPanel implements IBroadcast {
  private static final long         serialVersionUID = 6248852778512372166L;

  private static final Logger       s_log            = LogManager.getLogger(MyPanel.class);
  private ModelloDati               m_model;
  private List<PlotPunto>           m_ppunti;
  private PropertyChangeBroadcaster m_broadc;

  public MyPanel() {
    initComponents();
  }

  private void initComponents() {
    setBackground(Color.white);
    setOpaque(true);
    Dimension area = new Dimension(800, 600);
    setPreferredSize(area);
    m_broadc = PropertyChangeBroadcaster.getInst();
    m_model = new ModelloDati();
    m_model.setDimensione(area);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent p_e) {
        mouseClickRel(p_e);
      }
    });
  }

  @Override
  protected void paintComponent(Graphics p_g) {
    super.paintComponent(p_g);
    Graphics2D g2 = (Graphics2D) p_g.create();
    Dimension dim = getPreferredSize();
    g2.setColor(Color.lightGray);
    g2.drawLine(0, 0, dim.width, dim.height);
    g2.drawLine(dim.width, 0, dim.width, dim.height);
    g2.drawLine(0, dim.height, dim.width, dim.height);
    disegnaBordi(g2);
    disegnaPunti(g2);
    disegnaBezier(g2);
    g2.dispose();
  }

  private void disegnaBordi(Graphics2D p_g2) {
    if (m_ppunti == null || m_ppunti.size() <= 1)
      return;
    Graphics2D g2 = (Graphics2D) p_g2.create();
    PlotPunto prev = null;
    g2.setColor(Color.cyan);
    Stroke stk = new BasicStroke(2);
    g2.setColor(Color.green);
    g2.setStroke(stk);
    for (PlotPunto p : m_ppunti) {
      if (prev != null) {
        Punto p1 = prev.getPuntoW();
        Punto p2 = p.getPuntoW();
        var li = new Line2D.Double(p1.getX(), p1.getY(), //
            p2.getX(), p2.getY());
        g2.draw(li);
      }
      prev = p;
    }
    g2.dispose();
  }

  private void disegnaPunti(Graphics2D p_g2) {
    if (m_ppunti == null || m_ppunti.size() == 0)
      return;
    Graphics2D g2 = (Graphics2D) p_g2.create();
    g2.setColor(Color.cyan);
    for (PlotPunto p : m_ppunti)
      p.paintComponent(g2);
    g2.dispose();
  }

  private void disegnaBezier(Graphics2D p_g2) {
    if (m_ppunti == null || m_ppunti.size() < 4)
      return;
    for (int i = 3; i < m_ppunti.size(); i++) {
      PlotBezier bez = new PlotBezier(m_ppunti.get(i - 3), m_ppunti.get(i - 2), m_ppunti.get(i - 1), m_ppunti.get(i));
      bez.paintComponent(p_g2);
    }
  }

  public void mouseClickRel(MouseEvent p_e) {
    // s_log.debug("mouseRelease(butt={}, qta={})", p_e.getButton(), p_e.getClickCount());
    boolean bRepaint = false;
    int buttNo = p_e.getButton() * 10 + p_e.getClickCount();
    EMouseGesture mogest = EMouseGesture.valueOf(buttNo);
    if (mogest == null) {
      s_log.error("Non interpreto bene il mouse: butt={}, qta={}", p_e.getButton(), p_e.getClickCount());
      return;
    }

    switch (mogest) {
      case SingClickSinistro:
        bRepaint = checkSelezionePunto(p_e.getPoint());
        break;
      case DoppClickSinistro:
        bRepaint = creaPunto(p_e.getPoint());
        break;

      default:
        break;
    }
    if (bRepaint)
      repaint();
  }

  private boolean checkSelezionePunto(Point p_point) {
    // System.out.println("MyPanel.checkSelezionePunto():" + p_point);
    boolean bRet = m_model.checkSelezionePunto(p_point);
    if (bRet) {
      Punto bersaglioX = m_model.getBersaglioX();
      Punto bersaglioW = m_model.getTraspondiFinestra().convertiW(bersaglioX);
      resettaSelezionati();
      PlotPunto ppBersW = trovaPlotBersaglio(bersaglioW);
      if (ppBersW != null)
        ppBersW.setSelected(true);
      broadc(EPropChange.selectVertice, bersaglioX);
    }
    return bRet;
  }

  private void resettaSelezionati() {
    for (PlotPunto pp : m_ppunti)
      pp.setSelected(false);
  }

  private PlotPunto trovaPlotBersaglio(Punto p_puntoW) {
    PlotPunto pRet = null;
    for (PlotPunto pp : m_ppunti)
      if (pp.checkBersaglio(p_puntoW)) {
        pRet = pp;
        break;
      }
    return pRet;
  }

  @Override
  public void broadc(EPropChange evt, Object newVal) {
    m_broadc.broadCast(this, evt, newVal);
  }

  private boolean creaPunto(Point p_pointW) {
    boolean bRet = true;
    broadc(EPropChange.nuovoPunto, p_pointW);
    Punto lastPX = m_model.getLastAddedPunto();
    Punto lastPW = m_model.getTraspondiFinestra().convertiW(lastPX);
    PlotPunto plp = new PlotPunto(m_model, lastPW);
    if (m_ppunti == null)
      m_ppunti = new ArrayList<>();
    m_ppunti.add(plp);
    return bRet;
  }

}
