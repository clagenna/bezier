package sm.clagenna.bezier.swing;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class MainApp {

  public MainApp() {
    init();
  }

  private void init() {
    new PropertyChangeBroadcaster();

  }

  public static void main(String[] args) {
    try {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception l_e) {
      System.err.println("Set Look and Feel:" + l_e);
    }

    MainJFrame app = new MainJFrame("Prove con le Curve");
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        app.pack();
        app.setVisible(true);
      }
    });

  }

}
