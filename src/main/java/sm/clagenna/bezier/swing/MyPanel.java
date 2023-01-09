package sm.clagenna.bezier.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.enumerati.EMouseGesture;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MyPanel extends JPanel {
  private static final long         serialVersionUID = 6248852778512372166L;

  private static final Logger       s_log            = LogManager.getLogger(MyPanel.class);
  private ModelloDati               m_model;
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
    Dimension dim = getSize();
    g2.setColor(Color.gray);
    g2.drawLine(0, 0, dim.width, dim.height);
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
      default:
        break;
    }
    if (bRepaint)
      repaint();
  }

  private boolean checkSelezionePunto(Point p_point) {
    System.out.println("MyPanel.checkSelezionePunto():" + p_point);
    boolean bRet = m_model.checkSelezionePunto(p_point);
    if (bRet)
      PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.selectVertice, m_model.getBersaglio());
    return bRet;
  }

}
