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
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

  private int                       m_mouButt;
  private PlotPunto                 m_ppSelez;

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
        mouseRelease(p_e);
      }

      @Override
      public void mouseClicked(MouseEvent p_e) {
        mouseClick(p_e);
      }
    });
    addMouseMotionListener(new MouseMotionAdapter() {

      @Override
      public void mouseDragged(MouseEvent p_e) {
        boolean bRepaint = mouseTrascinato(p_e);
        if (bRepaint)
          repaint();
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

  protected void mouseClick(MouseEvent p_e) {
    // s_log.debug("mouseRelease(butt={}, qta={})", p_e.getButton(), p_e.getClickCount());
    boolean bRepaint = false;
    m_mouButt = p_e.getButton() * 10 + p_e.getClickCount();
    EMouseGesture mogest = EMouseGesture.valueOf(m_mouButt);
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

  protected void mouseRelease(MouseEvent p_e) {
    boolean bRepaint = deSelectPunto();
    if (bRepaint)
      repaint();
  }

  private boolean deSelectPunto() {
    boolean bSelez = m_ppSelez != null;
    if (bSelez)
      m_ppSelez.setSelected(false);
    m_ppSelez = null;
    return bSelez;
  }

  private boolean mouseTrascinato(MouseEvent p_e) {
    boolean bRepaint = false;
    if (m_ppSelez == null)
      return bRepaint;
    m_mouButt = 0;
    if (SwingUtilities.isLeftMouseButton(p_e))
      m_mouButt = MouseEvent.BUTTON1;
    else if (SwingUtilities.isRightMouseButton(p_e))
      m_mouButt = MouseEvent.BUTTON2;
    else if (SwingUtilities.isMiddleMouseButton(p_e))
      m_mouButt = MouseEvent.BUTTON3;
    m_mouButt = m_mouButt * 10 + 1;
    EMouseGesture mogest = EMouseGesture.valueOf(m_mouButt);
    if (mogest == null) {
      System.err.printf("Non interpreto mouse Trasc: butt=%d, qta=%d\n", m_mouButt, p_e.getClickCount());
      return bRepaint;
    } // else
    //      System.out.printf("mouseTrasc(butt=%d, xy=(%d,%d) mgest=%s )\n", //
    //          m_mouButt, p_e.getX(), p_e.getY(), mogest.toString());
    int nx = p_e.getX();
    int ny = p_e.getY();
    switch (mogest) {
      case SingClickSinistro:
        m_ppSelez.setPuntoDrag(nx, ny);
        // System.out.printf("MyPanel.mouseTrascinato(%s)\n", m_ppSelez.toString());
        m_model.setPuntoDrag(m_ppSelez);
        bRepaint = true;
        break;
      default:
        break;
    }
    return bRepaint;
  }

  private boolean checkSelezionePunto(Point p_point) {
    // System.out.println("MyPanel.checkSelezionePunto():" + p_point);
    boolean bSelez = deSelectPunto();
    boolean bRet = m_model.checkSelezionePunto(p_point);
    if (bRet) {
      Punto bersaglioX = m_model.getBersaglioX();
      Punto bersaglioW = m_model.getTraspondiFinestra().convertiW(bersaglioX);
      resettaSelezionati();
      m_ppSelez = trovaPlotBersaglio(bersaglioW);
      if (m_ppSelez != null)
        m_ppSelez.setSelected(true);
      broadc(EPropChange.selectVertice, bersaglioX);
    }
    return bRet | bSelez;
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
