package sm.clagenna.bezier.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.ModelloDati.TipoCurva;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MainJFrame extends JFrame {
  private static final long         serialVersionUID = -2772804427885900779L;
  @SuppressWarnings("unused")
  private PropertyChangeBroadcaster m_broadc;
  private JCheckBoxMenuItem         m_mnuOptSpline;
  private JCheckBoxMenuItem         m_mnuOptBezier;
  private MyPanel                   m_pan;

  public MainJFrame() {
    initComponents();
  }

  public MainJFrame(String p_string) {
    super(p_string);
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    m_broadc = new PropertyChangeBroadcaster();
    m_pan = new MyPanel();
    JScrollPane scroll = new JScrollPane(m_pan);
    Dimension area = new Dimension(400, 300);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnuMenuFile = new JMenu("File");
    menuBar.add(mnuMenuFile);

    JMenuItem mnuSalva = new JMenuItem("Salva");
    mnuSalva.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuSalvaClick();
      }
    });
    mnuMenuFile.add(mnuSalva);

    JMenuItem mnuLeggi = new JMenuItem("Leggi");
    mnuLeggi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuLeggiClick();
      }
    });
    mnuMenuFile.add(mnuLeggi);
    mnuMenuFile.addSeparator();

    JMenuItem mnuExit = new JMenuItem("Esci");
    mnuExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuEsciClick();
      }
    });
    mnuMenuFile.add(mnuExit);

    JMenu mnuOpzioni = new JMenu("Opzioni");
    menuBar.add(mnuOpzioni);

    m_mnuOptBezier = new JCheckBoxMenuItem("Bezier");
    m_mnuOptBezier.setSelected(true);
    m_pan.getModello().setTipoCurva(ModelloDati.TipoCurva.Bezier);
    m_mnuOptBezier.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOptClick(e);
      }
    });
    mnuOpzioni.add(m_mnuOptBezier);

    m_mnuOptSpline = new JCheckBoxMenuItem("Spline");
    m_mnuOptSpline.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOptClick(e);
      }
    });
    mnuOpzioni.add(m_mnuOptSpline);
    scroll.setPreferredSize(area);
    setContentPane(scroll);
  }

  protected void mnuLeggiClick() {
    System.out.println("MainJFrame.mnuLeggiClick()");
  }

  protected void mnuSalvaClick() {
    System.out.println("MainJFrame.mnuSalvaClick()");
  }

  protected void mnuOptClick(ActionEvent p_e) {
    JCheckBoxMenuItem mnu = (JCheckBoxMenuItem) p_e.getSource();
    String sz = mnu.getText();
    System.out.printf("mnuOptClick(%s)\n", sz);
    ModelloDati.TipoCurva tip = null;
    switch (sz) {
      case "Bezier":
        m_mnuOptBezier.setSelected(true);
        m_mnuOptSpline.setSelected(false);
        tip = TipoCurva.Bezier;
        break;
      case "Spline":
        m_mnuOptBezier.setSelected(false);
        m_mnuOptSpline.setSelected(true);
        tip = TipoCurva.Spline;
        break;
    }
    if (Beans.isDesignTime())
      return;
    m_pan.getModello().setTipoCurva(tip);
  }

  protected void mnuEsciClick() {
    System.out.println("MainJFrame.mnuEsciClick()");
    this.dispose();
    // System.exit(0);
  }

}
