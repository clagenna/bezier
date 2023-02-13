package sm.clagenna.bezier.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Beans;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import sm.clagenna.bezier.data.AppProperties;
import sm.clagenna.bezier.sys.IGestFile;

public class MenuFiles {
  private static int       s_maxfiles = 15;
  private static MenuFiles s_inst;
  private IGestFile        m_gfile;

  private List<File>       m_queue;

  public MenuFiles() {
    if (s_inst != null && !Beans.isDesignTime())
      throw new UnsupportedOperationException("MenuFile è un Singleton !!");
    s_inst = this;
    m_queue = new ArrayList<>();
    popolati();
  }

  private void popolati() {
    AppProperties prop = AppProperties.getInst();
    for (int i = 0; i < s_maxfiles; i++) {
      String szKey = String.format(AppProperties.CSZ_PROP_MENUFILE, i);
      String szFi = prop.getPropVal(szKey);
      if (szFi == null)
        break;
      File fi = new File(szFi);
      m_queue.add(fi);
    }

  }

  public static MenuFiles getInst() {
    return s_inst;
  }

  public void add(File p_fi) {
    if (m_queue.contains(p_fi))
      m_queue.remove(p_fi);
    // se troppi elimino l'ultimo
    while (m_queue.size() >= s_maxfiles)
      m_queue.remove(m_queue.size() - 1);
    m_queue.add(0, p_fi);
    saveProperties();
  }

  private void saveProperties() {
    AppProperties prop = AppProperties.getInst();
    int k = 0;
    for (File fi : m_queue) {
      String szKey = String.format(AppProperties.CSZ_PROP_MENUFILE, k++);
      String szFi = fi.getAbsolutePath();
      prop.setPropVal(szKey, szFi);
    }
  }

  public File poll(int p_i) {
    File ret = null;
    if (m_queue.size() < p_i)
      ret = m_queue.get(p_i);
    return ret;
  }

  @Override
  public String toString() {
    StringBuilder ret = new StringBuilder();
    for (File fi : m_queue)
      ret.append(fi.getName()).append("\n");
    return ret.toString();
  }

  public void creaElenco(IGestFile p_gfile, JMenu p_mnFile) {
    m_gfile = p_gfile;
    p_mnFile.removeAll();
    int k = 1;
    for (File fi : m_queue) {
      char cc = (char) ('0' + k);
      String actCmd = String.valueOf(k);
      String szFi = String.format("%d %s", k++, fi.getName());
      JMenuItem mni = new JMenuItem(szFi);
      // nel act cmd ci metto l'indice nel m_queue
      mni.setActionCommand(actCmd);
      KeyStroke shK = KeyStroke.getKeyStroke(cc, java.awt.Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());
      mni.setAccelerator(shK);
      mni.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          locApriFile(e);
        }
      });
      p_mnFile.add(mni);
    }
  }

  protected void locApriFile(ActionEvent p_e) {
    String sz = p_e.getActionCommand();
    Integer ii = Integer.parseInt(sz) - 1;
    File fi = m_queue.get(ii);
    m_gfile.leggiFile(fi);
  }

}
