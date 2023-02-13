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
import java.beans.Beans;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.data.TrasponiFinestra;
import sm.clagenna.bezier.enumerati.EMouseGesture;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.IBroadcast;
import sm.clagenna.bezier.sys.IGestFile;
import sm.clagenna.bezier.sys.IProperty;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MyPanel extends JPanel implements IBroadcast, IProperty, IGestFile {
  private static final long         serialVersionUID = 6248852778512372166L;

  private static final Logger       s_log            = LogManager.getLogger(MyPanel.class);
  @Getter
  private ModelloDati               modello;
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
    if ( !Beans.isDesignTime())
      m_broadc = PropertyChangeBroadcaster.getInst();
    modello = new ModelloDati();
    modello.setDimensione(area);
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
    ModelloDati.TipoCurva tip = modello.getTipoCurva();
    if (modello.isDisegnaGriglia())
      disegnaGriglia(g2);
    boolean bDisPunti = getModello().isDisegnabordi();
    if (bDisPunti)
      disegnaBordi(g2);
    try {
      switch (tip) {
        case Bezier:
          disegnaBezier(g2);
          break;
        case Spline:
          disegnaSpline(g2);
          break;
      }
    } catch (Exception l_e) {
      System.err.println("Errore:" + l_e.getMessage());
    }
    if (bDisPunti)
      disegnaPunti(g2);
    g2.dispose();
  }

  private void disegnaGriglia(Graphics2D g2) {
    Dimension dim = getSize();
    Color co1 = new Color(240, 240, 240);
    Color co2 = new Color(200, 200, 200);

    int k = 0;
    for (int col = 0; col < dim.width; col += 10) {
      g2.setColor(co1);
      if ( (k++ % 5) == 0)
        g2.setColor(co2);
      g2.drawLine(col, 0, col, dim.height);
    }
    k = 0;
    for (int row = 0; row < dim.height; row += 10) {
      g2.setColor(co1);
      if ( (k++ % 5) == 0)
        g2.setColor(co2);
      g2.drawLine(0, row, dim.width, row);
    }
  }

  private void disegnaBordi(Graphics2D p_g2) {
    if (m_ppunti == null || m_ppunti.size() <= 1)
      return;
    Graphics2D g2 = (Graphics2D) p_g2.create();
    PlotPunto prev = null;
    //    g2.setColor(Color.cyan);
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

  private void disegnaSpline(Graphics2D p_g2) {
    if (m_ppunti == null || m_ppunti.size() <= 2) {
      s_log.warn("Non ho punti da disegnare");
      return;
    }
    PlotSpline spl = new PlotSpline();
    spl.setPunti(m_ppunti);
    spl.paintComponent(p_g2);
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
        modello.setPuntoDrag(m_ppSelez);
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
    boolean bRet = modello.checkSelezionePunto(p_point);
    if (bRet) {
      Punto bersaglioX = modello.getBersaglioX();
      Punto bersaglioW = modello.getTraspondiFinestra().convertiW(bersaglioX);
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
    Punto lastPX = modello.getLastAddedPunto();
    Punto lastPW = modello.getTraspondiFinestra().convertiW(lastPX);
    PlotPunto plp = new PlotPunto(modello, lastPW);
    if (m_ppunti == null)
      m_ppunti = new ArrayList<>();
    m_ppunti.add(plp);
    return bRet;
  }

  @Override
  public void leggiFile(File p_fiIn) {
    ModelloDati newMod = new ModelloDati();
    newMod.leggiFile(p_fiIn);
    if (m_ppunti != null)
      m_ppunti.clear();
    m_ppunti = new ArrayList<>();
    for (Punto p : newMod.getPunti()) {
      Punto px = newMod.getTraspondiFinestra().convertiX(p);
      m_ppunti.add(new PlotPunto(newMod, px));
    }
    try {
      modello.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    modello = null;
    modello = newMod;
    repaint();
  }

  public void salvaFile(File p_fiOut) {
    modello.salvaFile(p_fiOut);
  }

  public void nuovoDisegno() {
    m_broadc.removePropertyChangeListener(ModelloDati.class);
    m_broadc.removePropertyChangeListener(TrasponiFinestra.class);
    if (m_ppunti != null)
      m_ppunti.clear();
    m_ppunti = new ArrayList<>();
    try {
      modello.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    modello = null;
    modello = new ModelloDati();
    repaint();
  }

  public void setDisegnaPunti(boolean p_disPunti) {
    if (modello.isDisegnabordi() ^ p_disPunti) {
      modello.setDisegnabordi(p_disPunti);
      repaint();
    }
  }

  public void setDisegnaGriglia(boolean p_bSel) {
    if (modello.isDisegnaGriglia() ^ p_bSel) {
      modello.setDisegnaGriglia(p_bSel);
      repaint();
    }
  }

  @Override
  public void salvaProperties() {
    modello.salvaProperties();
  }

  @Override
  public void leggiProperties() {
    modello.leggiProperties();
  }

}
