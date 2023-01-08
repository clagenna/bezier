package prova.scroll3;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public class MyJFrame extends JFrame {

  /** long */
  private static final long serialVersionUID = -2772804427885900779L;

  public MyJFrame() {
    initComponents();
  }

  public MyJFrame(String p_string) {
    super(p_string);
    initComponents();
  }

  private void initComponents() {
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    MyPanel pan = new MyPanel();
    JScrollPane scroll = new JScrollPane(pan);
    Dimension area = new Dimension(400, 300);
    scroll.setPreferredSize(area);
    setContentPane(scroll);
  }

}
