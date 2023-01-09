package sm.clagenna.bezier.swing;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MainJFrame extends JFrame {
  private static final long serialVersionUID = -2772804427885900779L;
  PropertyChangeBroadcaster m_broadc;

  public MainJFrame() {
    initComponents();
  }

  public MainJFrame(String p_string) {
    super(p_string);
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    m_broadc = new PropertyChangeBroadcaster();
    MyPanel pan = new MyPanel();
    JScrollPane scroll = new JScrollPane(pan);
    Dimension area = new Dimension(400, 300);
    scroll.setPreferredSize(area);
    setContentPane(scroll);
  }

}
