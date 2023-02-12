package sm.clagenna.bezier.swing;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.data.AppProperties;
import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.ModelloDati.TipoCurva;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.MioFileFilter;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MainJFrame extends JFrame implements PropertyChangeListener {
  private static final long         serialVersionUID = -2772804427885900779L;
  private static final Logger       s_log            = LogManager.getLogger(MainJFrame.class);

  private PropertyChangeBroadcaster m_broadc;
  private JCheckBoxMenuItem         m_mnuOptSpline;
  private JCheckBoxMenuItem         m_mnuOptBezier;
  private JMenuItem                 m_mnuOpenDialOpts;
  private MyPanel                   m_pan;
  private JScrollPane               m_scroll;
  private JDialogOpts               m_dialOpts;
  @Getter @Setter
  private AppProperties             props;
  private JMenuItem                 m_mnuOptDisegnaPunti;

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
    m_broadc.addPropertyChangeListener(this);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent p_e) {
        // System.out.println("JDialogOpts.{...}.windowClosing()");
        salvaProperties();
        super.windowClosing(p_e);
      }

      @Override
      public void windowClosed(WindowEvent p_e) {
        // System.out.println("JDialogOpts.{...}.windowClosed()");
        super.windowClosed(p_e);
      }
    });

    if ( !Beans.isDesignTime()) {
      props = new AppProperties();
      props.openProperties();
      Dimension dim = props.getFrameDim();
      Point pos = props.getFramePos();
      if ( (dim.height * dim.width) > 0)
        setPreferredSize(dim);
      setLocation(pos);
    }

    m_pan = new MyPanel();
    m_pan.leggiProperties();
    m_scroll = new JScrollPane(m_pan);
    Dimension area = new Dimension(400, 300);

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnuMenuFile = new JMenu("File");
    menuBar.add(mnuMenuFile);

    JMenuItem mnuNuovo = new JMenuItem("Nuovo");
    mnuNuovo.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuNuovoDisegno();
      }
    });
    mnuMenuFile.add(mnuNuovo);

    mnuMenuFile.addSeparator();

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

    m_mnuOptDisegnaPunti = new JCheckBoxMenuItem("Dis. bordi");
    m_mnuOptDisegnaPunti.setSelected(true);
    m_mnuOptDisegnaPunti.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOptDisegnaBordo(e);
      }
    });
    mnuOpzioni.add(m_mnuOptDisegnaPunti);

    mnuOpzioni.addSeparator();

    m_mnuOpenDialOpts = new JMenuItem("Dialog");
    m_mnuOpenDialOpts.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuDialOptsClick();
      }
    });
    mnuOpzioni.add(m_mnuOpenDialOpts);

    m_scroll.setPreferredSize(area);
    setContentPane(m_scroll);
  }

  protected void mnuNuovoDisegno() {
    m_pan.nuovoDisegno();
  }

  protected void mnuLeggiClick() {
    // System.out.println("MainJFrame.mnuLeggiClick()");
    File fiIn = apriFileChooser("Leggi il file salvataggio", true);
    if (fiIn != null)
      m_pan.leggiFile(fiIn);
    aggiornaDatiInterfaccia();
  }

  private File apriFileChooser(String p_titolo, boolean p_b) {
    File fiRet = null;
    ModelloDati modello = m_pan.getModello();
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle(p_titolo);
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
  
    MioFileFilter json = new MioFileFilter("json file", ".json");
    jfc.addChoosableFileFilter(json);
    String sz = props.getLastDir();
    if (sz != null)
      jfc.setCurrentDirectory(new File(sz));
    int returnValue = 0;
    if (p_b)
      returnValue = jfc.showOpenDialog(this);
    else
      returnValue = jfc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      fiRet = jfc.getSelectedFile();
      if (fiRet != null)
        if ( !fiRet.getAbsolutePath().toLowerCase().endsWith(".json"))
          fiRet = new File(fiRet.getAbsolutePath() + ".json");
      modello.setFileDati(fiRet);
      File fi2 = jfc.getCurrentDirectory();
      props.setLastDir(fi2.getAbsolutePath());
      s_log.info("Scelto file: {} dal dir {}", fiRet.getAbsolutePath(), fi2.getAbsolutePath());
    } else
      s_log.info("Scartato lettura file");
    return fiRet;
  }

  private void aggiornaDatiInterfaccia() {
    //    AppProperties props = AppProperties.getInst();
    ModelloDati mod = getModello();
    boolean bv = mod.isDisegnabordi();
    m_mnuOptDisegnaPunti.setSelected(bv);
    //    bv = mod.isDisegnaGriglia();
    //    m_ckDisGriglia.setSelected(bv);

    switch (mod.getTipoCurva()) {

      case Bezier:
        m_mnuOptBezier.setSelected(true);
        m_mnuOptSpline.setSelected(false);
        break;

      case Spline:
        m_mnuOptBezier.setSelected(false);
        m_mnuOptSpline.setSelected(true);
        break;

    }
    if (m_dialOpts != null) {
      m_dialOpts.initValues();
    }
  }

  protected void mnuSalvaClick() {
    // System.out.println("MainJFrame.mnuSalvaClick()");
    File fiOut = apriFileChooser("Salva il contenuto su file", false);
    if (fiOut != null)
      m_pan.salvaFile(fiOut);

  }

  protected void mnuOptClick(ActionEvent p_e) {
    JCheckBoxMenuItem mnu = (JCheckBoxMenuItem) p_e.getSource();
    String sz = mnu.getText();
    // System.out.printf("mnuOptClick(%s)\n", sz);
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
    setTipoCurva(tip);
  }

  protected void setTipoCurva(ModelloDati.TipoCurva p_tip) {
    m_pan.getModello().setTipoCurva(p_tip);
    aggiornaDatiInterfaccia();
  }

  protected void mnuEsciClick() {
    // System.out.println("MainJFrame.mnuEsciClick()");
    salvaProperties();
    this.dispose();
    // System.exit(0);
  }

  protected void mnuOptDisegnaBordo(ActionEvent p_e) {
    JCheckBoxMenuItem ck = (JCheckBoxMenuItem) p_e.getSource();
    boolean disPunti = ck.isSelected();
    setDisegnaBordi(disPunti);
  }

  protected void mnuDialOptsClick() {
    if (m_dialOpts != null) {
      JOptionPane.showMessageDialog(m_dialOpts, "Hai già la dialog aperta !");
      return;
    }
    m_dialOpts = new JDialogOpts(this);
    m_mnuOpenDialOpts.setEnabled(false);
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        m_dialOpts.setVisible(true);
      }
    });
  }

  public void optWinClosed() {
    // System.out.println("MainJFrame.optWinClosed()");
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    // m_dialOpts.salvaProperties();
    m_dialOpts.dispose();
    m_dialOpts = null;
    m_mnuOpenDialOpts.setEnabled(true);
  }

  protected void salvaProperties() {
    if (props == null)
      return;
    // System.out.println("MainJFrame.salvaProperties()");
    try {
      m_pan.salvaProperties();
      if ( m_dialOpts != null)
        m_dialOpts.salvaProperties();
      props.setFramePos(getLocation());
      props.setFrameDim(getSize());
      props.saveProperties();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    Object nuova = p_evt.getNewValue();
    if ( ! (obj instanceof EPropChange pch))
      return;

    switch (pch) {

      case ridisegna:
      case tipoGrafico:
        getContentPane().repaint();
        break;

      case nuovoPunto:
        if ( ! (nuova instanceof Point))
          break;
        Dimension d = m_scroll.getPreferredSize();
        Point p = (Point) nuova;
        Dimension nd = new Dimension(Math.max(d.width, (int) p.getX()) + 10, Math.max(d.height, (int) p.getY()) + 10);
        // System.out.println("MainJFrame.new dim():" + nd.toString());
        m_pan.setPreferredSize(nd);
        break;

      default:
        break;
    }

  }

  public void setDisegnaGriglia(boolean p_bSel) {
    m_pan.setDisegnaGriglia(p_bSel);

  }

  public void setDisegnaBordi(boolean p_bSel) {
    m_pan.setDisegnaPunti(p_bSel);
  }

  public ModelloDati getModello() {
    return m_pan.getModello();
  }

}
