package sm.clagenna.bezier.swing;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import sm.clagenna.bezier.data.AppProperties;
import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.ModelloDati.TipoCurva;
import sm.clagenna.bezier.sys.IProperty;

public class JDialogOpts extends JDialog implements IProperty {
  private static final long serialVersionUID = -8474695387606471867L;
  private MainJFrame        m_padre;
  private JTextField        m_txLastDir;
  private JTextField        m_txLastFile;
  private JCheckBox         m_ckDisBordi;
  private JCheckBox         m_ckDisGriglia;
  private JRadioButton      m_rdbGrafBezier;
  private JRadioButton      m_rdbGrafSpline;
  private Dimension         m_OptsSize;
  protected Point           m_OptsPos;

  public JDialogOpts(JFrame p_padre) {
    super(p_padre, "Opzioni del grafo", false);
    m_padre = (MainJFrame) p_padre;
    initComponents();
    initValues();
  }

  private void initComponents() {
    addComponentListener(new ComponentAdapter() {

      @Override
      public void componentResized(ComponentEvent p_e) {
        m_OptsSize = getSize();
        super.componentResized(p_e);
      }

      @Override
      public void componentMoved(ComponentEvent p_e) {
        m_OptsPos = getLocation();
        super.componentMoved(p_e);
      }
    });

    setSize(new Dimension(400, 300));

    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.NORTH);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[] { 32, 32, 32, 10, 32, 0 };
    gbl_panel.rowHeights = new int[] { 0, 0, 0, 10, 0, 0 };
    gbl_panel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0 };
    gbl_panel.rowWeights = new double[] { 0.0, 1.0, 0.0, 0.0 };
    panel.setLayout(gbl_panel);

    JLabel lbDisBordi = new JLabel("Disegna Bordi");
    lbDisBordi.setHorizontalAlignment(SwingConstants.RIGHT);
    lbDisBordi.setToolTipText("Disegna le congiunzioni fra un punto e l'altro");
    GridBagConstraints gbc_lbDisBordi = new GridBagConstraints();
    gbc_lbDisBordi.ipadx = 5;
    gbc_lbDisBordi.insets = new Insets(0, 0, 5, 5);
    gbc_lbDisBordi.gridx = 0;
    gbc_lbDisBordi.gridy = 0;
    panel.add(lbDisBordi, gbc_lbDisBordi);

    m_ckDisBordi = new JCheckBox("");
    m_ckDisBordi.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ckDisBordiClick(e);
      }
    });
    m_ckDisBordi.setSelected(true);
    lbDisBordi.setLabelFor(m_ckDisBordi);
    m_ckDisBordi.setHorizontalAlignment(SwingConstants.CENTER);
    GridBagConstraints gbc_ckDisBordi = new GridBagConstraints();
    gbc_ckDisBordi.anchor = GridBagConstraints.WEST;
    gbc_ckDisBordi.insets = new Insets(0, 0, 5, 5);
    gbc_ckDisBordi.gridx = 1;
    gbc_ckDisBordi.gridy = 0;
    panel.add(m_ckDisBordi, gbc_ckDisBordi);

    JLabel lbDisGriglia = new JLabel("Disegna Griglia");
    lbDisGriglia.setToolTipText("Crea una griglia di sottofondo");
    GridBagConstraints gbc_lbDisGriglia = new GridBagConstraints();
    gbc_lbDisGriglia.ipadx = 5;
    gbc_lbDisGriglia.insets = new Insets(0, 0, 5, 5);
    gbc_lbDisGriglia.gridx = 2;
    gbc_lbDisGriglia.gridy = 0;
    panel.add(lbDisGriglia, gbc_lbDisGriglia);

    m_ckDisGriglia = new JCheckBox("");
    m_ckDisGriglia.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ckDisGrigliaClick(e);
      }
    });
    m_ckDisGriglia.setHorizontalAlignment(SwingConstants.RIGHT);
    GridBagConstraints gbc_ckDisGriglia = new GridBagConstraints();
    gbc_ckDisGriglia.insets = new Insets(0, 0, 5, 5);
    gbc_ckDisGriglia.gridx = 3;
    gbc_ckDisGriglia.gridy = 0;
    panel.add(m_ckDisGriglia, gbc_ckDisGriglia);

    JLabel lbTipoGrafico = new JLabel("Tipo Grafico");
    GridBagConstraints gbc_lbTipoGrafico = new GridBagConstraints();
    gbc_lbTipoGrafico.insets = new Insets(0, 0, 5, 5);
    gbc_lbTipoGrafico.gridx = 0;
    gbc_lbTipoGrafico.gridy = 1;
    panel.add(lbTipoGrafico, gbc_lbTipoGrafico);

    JPanel panButton = new JPanel();
    panButton.setToolTipText("Tipo di grafico");
    FlowLayout fl_panButton = (FlowLayout) panButton.getLayout();
    fl_panButton.setAlignment(FlowLayout.LEFT);
    GridBagConstraints gbc_panButton = new GridBagConstraints();
    gbc_panButton.gridwidth = 2;
    gbc_panButton.insets = new Insets(0, 0, 5, 5);
    gbc_panButton.fill = GridBagConstraints.BOTH;
    gbc_panButton.gridx = 1;
    gbc_panButton.gridy = 1;
    panel.add(panButton, gbc_panButton);

    m_rdbGrafBezier = new JRadioButton("Bezier");
    m_rdbGrafBezier.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        rdbGrafBezierClick(e);
      }
    });
    panButton.add(m_rdbGrafBezier);

    m_rdbGrafSpline = new JRadioButton("Spline");
    m_rdbGrafSpline.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        rdbGrafBezierClick(e);
      }
    });
    panButton.add(m_rdbGrafSpline);

    ButtonGroup grpTipo = new ButtonGroup();
    grpTipo.add(m_rdbGrafBezier);
    grpTipo.add(m_rdbGrafSpline);

    JLabel lbLastDir = new JLabel("Last Dir");
    lbLastDir.setHorizontalAlignment(SwingConstants.RIGHT);
    GridBagConstraints gbc_lbLastDir = new GridBagConstraints();
    gbc_lbLastDir.ipadx = 5;
    gbc_lbLastDir.anchor = GridBagConstraints.EAST;
    gbc_lbLastDir.insets = new Insets(0, 0, 5, 5);
    gbc_lbLastDir.gridx = 0;
    gbc_lbLastDir.gridy = 2;
    panel.add(lbLastDir, gbc_lbLastDir);

    m_txLastDir = new JTextField();
    GridBagConstraints gbc_txLastDir = new GridBagConstraints();
    gbc_txLastDir.gridwidth = 4;
    gbc_txLastDir.insets = new Insets(0, 0, 5, 5);
    gbc_txLastDir.fill = GridBagConstraints.HORIZONTAL;
    gbc_txLastDir.gridx = 1;
    gbc_txLastDir.gridy = 2;
    panel.add(m_txLastDir, gbc_txLastDir);
    m_txLastDir.setColumns(10);

    JLabel lbLastFile = new JLabel("Last File");
    GridBagConstraints gbc_lbLastFile = new GridBagConstraints();
    gbc_lbLastFile.anchor = GridBagConstraints.EAST;
    gbc_lbLastFile.insets = new Insets(0, 0, 5, 5);
    gbc_lbLastFile.gridx = 0;
    gbc_lbLastFile.gridy = 3;
    panel.add(lbLastFile, gbc_lbLastFile);

    m_txLastFile = new JTextField();
    m_txLastFile.setColumns(10);
    GridBagConstraints gbc_txLastFile = new GridBagConstraints();
    gbc_txLastFile.gridwidth = 4;
    gbc_txLastFile.insets = new Insets(0, 0, 5, 5);
    gbc_txLastFile.fill = GridBagConstraints.HORIZONTAL;
    gbc_txLastFile.gridx = 1;
    gbc_txLastFile.gridy = 3;
    panel.add(m_txLastFile, gbc_txLastFile);

    JButton btEsci = new JButton("Esci");
    btEsci.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        chiudiDialog();
      }
    });
    GridBagConstraints gbc_btEsci = new GridBagConstraints();
    gbc_btEsci.insets = new Insets(0, 0, 0, 5);
    gbc_btEsci.gridx = 4;
    gbc_btEsci.gridy = 5;
    panel.add(btEsci, gbc_btEsci);

    if (Beans.isDesignTime())
      m_padre.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent p_e) {
        System.out.println("JDialogOpts.{...}.windowClosing()");
        chiudiDialog();
        super.windowClosing(p_e);
      }

      @Override
      public void windowClosed(WindowEvent p_e) {
        System.out.println("JDialogOpts.{...}.windowClosed()");
        chiudiDialog();
        super.windowClosed(p_e);
      }
    });
  }

  protected void rdbGrafBezierClick(ActionEvent p_e) {
    TipoCurva tp = null;
    if (m_rdbGrafBezier.isSelected())
      tp = TipoCurva.Bezier;
    else
      tp = TipoCurva.Spline;
    m_padre.setTipoCurva(tp);
  }

  public void initValues() {
    leggiProperties();
    ModelloDati mod = m_padre.getModello();
    boolean bv = mod.isDisegnabordi();
    m_ckDisBordi.setSelected(bv);
    bv = mod.isDisegnaGriglia();
    m_ckDisGriglia.setSelected(bv);
    switch (mod.getTipoCurva()) {

      case Bezier:
        m_rdbGrafBezier.setSelected(true);
        break;

      case Spline:
        m_rdbGrafSpline.setSelected(true);
        break;

    }
  }

  protected void ckDisGrigliaClick(ActionEvent p_e) {
    var src = p_e.getSource();
    if ( ! (src instanceof JCheckBox))
      return;
    JCheckBox ck = (JCheckBox) src;
    boolean bSel = ck.isSelected();
    m_padre.setDisegnaGriglia(bSel);
  }

  protected void ckDisBordiClick(ActionEvent p_e) {
    var src = p_e.getSource();
    if ( ! (src instanceof JCheckBox))
      return;
    JCheckBox ck = (JCheckBox) src;
    boolean bSel = ck.isSelected();
    m_padre.setDisegnaBordi(bSel);
  }

  private void chiudiDialog() {
    salvaProperties();
    if (m_padre != null)
      m_padre.optWinClosed();
    m_padre = null;
  }

  @Override
  public void salvaProperties() {
    AppProperties props = AppProperties.getInst();
    if (m_OptsPos != null)
      props.setOptsPos(m_OptsPos);
    if (m_OptsSize != null)
      props.setOptsDim(m_OptsSize);
  }

  @Override
  public void leggiProperties() {
    AppProperties props = AppProperties.getInst();
    Point op = props.getOptsPos();
    Dimension loc = props.getOptsDim();
    if (op != null) {
      m_OptsPos = op;
      setLocation(op);
    }
    if (loc != null && (loc.height * loc.width) > 0) {
      m_OptsSize = loc;
      setSize(loc);
    }
    String sz = props.getLastDir();
    if (sz != null)
      m_txLastDir.setText(sz);
    sz = props.getLastFile();

    if (sz != null) {
      File fi = new File(sz);
      m_txLastFile.setText(fi.getName());
    }

  }

}
