package prova.scroll3;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainApp {

  public MainApp() {
    init();
  }

  private void init() {

  }

  public static void main(String[] args) {
    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception l_e) {
      System.err.println("Set Look and Feel:" + l_e);
    }

    MyJFrame app = new MyJFrame("Titolo della finestra");
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        app.pack();
        app.setVisible(true);
      }
    });

  }

}
