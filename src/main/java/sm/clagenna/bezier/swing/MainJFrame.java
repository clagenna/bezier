package sm.clagenna.bezier.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.ModelloDati.TipoCurva;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.MioFileFilter;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MainJFrame extends JFrame implements PropertyChangeListener {
  private static final long         serialVersionUID = -2772804427885900779L;
  private static final Logger       s_log            = LogManager.getLogger(MainJFrame.class);
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
    m_broadc.addPropertyChangeListener(this);
    m_pan = new MyPanel();
    JScrollPane scroll = new JScrollPane(m_pan);
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
    
    
    JMenuItem mnuOptDisegnaPunti = new JCheckBoxMenuItem("Dis. bordi");
    mnuOptDisegnaPunti.setSelected(true);
    mnuOptDisegnaPunti.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mnuOptDisegnaBordo(e);
      }
    });
    mnuOpzioni.add(mnuOptDisegnaPunti);
    
    
    
    scroll.setPreferredSize(area);
    setContentPane(scroll);
  }

  protected void mnuNuovoDisegno() {
    m_pan.nuovoDisegno();
  }

  protected void mnuLeggiClick() {
    System.out.println("MainJFrame.mnuLeggiClick()");
    File fiIn = apriFileChooser("Leggi il file salvataggio", true);
    if (fiIn != null)
      m_pan.leggiFile(fiIn);
  }

  private File apriFileChooser(String p_titolo, boolean p_b) {
    File fiRet = null;
    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
    jfc.setDialogTitle(p_titolo);
    jfc.setMultiSelectionEnabled(false);
    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

    MioFileFilter json = new MioFileFilter("json file", ".json");
    jfc.addChoosableFileFilter(json);
    ModelloDati props = m_pan.getModello();
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
      props.setFileDati(fiRet);
      File fi2 = jfc.getCurrentDirectory();
      props.setLastDir(fi2.getAbsolutePath());
      s_log.info("Scelto file: {}", fiRet.getAbsolutePath());
    } else
      s_log.info("Scartato lettura file");
    return fiRet;
  }

  protected void mnuSalvaClick() {
    System.out.println("MainJFrame.mnuSalvaClick()");
    File fiOut = apriFileChooser("Salva il contenuto su file", false);
    if (fiOut != null)
      m_pan.salvaFile(fiOut);

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

  protected void mnuOptDisegnaBordo(ActionEvent p_e) {
    JCheckBoxMenuItem ck = (JCheckBoxMenuItem) p_e.getSource();
    boolean disPunti = ck.isSelected();
    m_pan.setDisegnaPunti(disPunti);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    Object obj = p_evt.getOldValue();
    if ( ! (obj instanceof EPropChange pch))
      return;

    switch (pch) {
      case ridisegna:
      case tipoGrafico:
        getContentPane().repaint();
        break;
      default:
        break;
    }

  }

}
