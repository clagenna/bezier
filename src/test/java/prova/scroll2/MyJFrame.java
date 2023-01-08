package prova.scroll2;

import javax.swing.JFrame;
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
    setContentPane(pan);
  }

}
