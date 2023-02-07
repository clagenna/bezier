package sm.clagenna.bezier.sys;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MioFileFilter extends FileFilter {

  private String m_szExt;
  private String m_szDes;

  public MioFileFilter(String p_desc, String p_ext) {
    m_szDes = p_desc;
    m_szExt = p_ext.toLowerCase();
  }

  @Override
  public boolean accept(File p_f) {
    if (p_f.isDirectory())
      return true;
    return (p_f.getName().toLowerCase().endsWith(m_szExt));
  }

  @Override
  public String getDescription() {
    return String.format("%s (%s)", m_szDes, m_szExt);
  }

}
