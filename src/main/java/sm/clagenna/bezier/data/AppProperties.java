package sm.clagenna.bezier.data;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

public class AppProperties {

  private static final Logger  s_log                   = LogManager.getLogger(AppProperties.class);

  private static final String  CSZ_PROP_FILE           = "bezier.properties";
  public static final String   CSZ_PROP_LASTDIR        = "last.dir";
  public static final String   CSZ_PROP_LASTFIL        = "last.fil";
  public static final String   CSZ_PROP_DIMFRAME_X     = "frame.dimx";
  public static final String   CSZ_PROP_DIMFRAME_Y     = "frame.dimy";
  public static final String   CSZ_PROP_POSFRAME_X     = "frame.posx";
  public static final String   CSZ_PROP_POSFRAME_Y     = "frame.posy";
  public static final String   CSZ_PROP_DIMOPTS_X      = "opts.dimx";
  public static final String   CSZ_PROP_DIMOPTS_Y      = "opts.dimy";
  public static final String   CSZ_PROP_POSOPTS_X      = "opts.posx";
  public static final String   CSZ_PROP_POSOPTS_Y      = "opts.posy";
  public static final String   CSZ_PROP_MENUFILE       = "menu.%d";
  public static final String   CSZ_PROP_BO_DISEGNA     = "bordo.disegna";
  public static final String   CSZ_PROP_BO_TOLLER      = "bordo.tolleranza";
  public static final String   CSZ_PROP_DISEGNAGRIGLIA = "disegna.griglia";
  public static final String   CSZ_PROP_TIPOCURVA      = "tipo.curva";

  @Getter
  private static AppProperties inst;
  @Getter @Setter
  private Properties           properties;
  @Getter @Setter
  private Path                 propertyFile;

  public AppProperties() {
    if (inst != null)
      throw new UnsupportedOperationException("Properties gia' istanziate !");
    inst = this;
  }

  public void openProperties() {
    var fi = new File(CSZ_PROP_FILE);
    properties = new Properties();
    propertyFile = Paths.get(fi.getAbsolutePath());

    s_log.info("Apro il file properties {}", propertyFile);
    properties = new Properties();
    if ( !Files.isRegularFile(propertyFile, LinkOption.NOFOLLOW_LINKS)) {
      s_log.error("Il file di properties {} non esiste", CSZ_PROP_FILE);
      return;
    }
    try (InputStream is = new FileInputStream(propertyFile.toFile())) {
      properties.load(is);
      setPropertyFile(propertyFile);
    } catch (IOException e) {
      e.printStackTrace();
      s_log.error("Errore apertura property file: {}", propertyFile, e);
    }

  }

  public void saveProperties() {
    try (OutputStream output = new FileOutputStream(propertyFile.toFile())) {
      properties.store(output, null);
      s_log.info("Salvo property file {}", propertyFile);
    } catch (IOException e) {
      e.printStackTrace();
      s_log.error("Errore scrittura property file: {}", propertyFile, e);
    }
  }

  public String getLastDir() {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(CSZ_PROP_LASTDIR);
    return szRet;
  }

  public void setLastDir(String p_lastDir) {
    if (properties != null)
      if (p_lastDir != null)
        properties.setProperty(CSZ_PROP_LASTDIR, p_lastDir);
  }

  public String getLastFile() {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(CSZ_PROP_LASTFIL);
    return szRet;
  }

  public void setLastFile(String p_lastFile) {
    if (properties != null)
      if (p_lastFile != null)
        properties.setProperty(CSZ_PROP_LASTFIL, p_lastFile);
  }

  public String getPropVal(String p_key) {
    String szRet = null;
    if (properties != null)
      szRet = properties.getProperty(p_key);
    return szRet;
  }

  public void setPropVal(String p_key, String p_val) {
    if (properties != null)
      if (p_val != null)
        properties.setProperty(p_key, p_val);
  }

  public void setPropVal(String p_key, int p_val) {
    setPropVal(p_key, String.valueOf(p_val));
  }

  public int getPropIntVal(String p_key) {
    Integer ii = Integer.valueOf(0);
    String sz = getPropVal(p_key);
    if (sz != null)
      ii = Integer.decode(sz);
    return ii.intValue();
  }

  public boolean getBooleanPropVal(String p_key) {
    return getBooleanPropVal(p_key, false);
  }

  public boolean getBooleanPropVal(String p_key, boolean b_def) {
    boolean bRet = b_def;
    String sz = getPropVal(p_key);
    if (sz == null)
      return bRet;
    sz = sz.toLowerCase();
    switch (sz) {
      case "vero":
      case "true":
      case "yes":
      case "y":
      case "t":
      case "1":
        bRet = true;
        break;
    }
    return bRet;
  }

  public void setBooleanPropVal(String p_key, boolean bVal) {
    setPropVal(p_key, Boolean.valueOf(bVal).toString());
  }

  public Point getFramePos() {
    int posX = getPropIntVal(AppProperties.CSZ_PROP_POSFRAME_X);
    int posY = getPropIntVal(AppProperties.CSZ_PROP_POSFRAME_Y);
    return new Point(posX, posY);
  }

  public void setFramePos(Point p_pos) {
    setPropVal(CSZ_PROP_POSFRAME_X, p_pos.x);
    setPropVal(CSZ_PROP_POSFRAME_Y, p_pos.y);
  }

  public Dimension getFrameDim() {
    int dimX = getPropIntVal(AppProperties.CSZ_PROP_DIMFRAME_X);
    int dimY = getPropIntVal(AppProperties.CSZ_PROP_DIMFRAME_Y);
    return new Dimension(dimX, dimY);
  }

  public void setFrameDim(Dimension p_pos) {
    setPropVal(CSZ_PROP_DIMFRAME_X, p_pos.width);
    setPropVal(CSZ_PROP_DIMFRAME_Y, p_pos.height);
  }

  public Point getOptsPos() {
    int posX = getPropIntVal(AppProperties.CSZ_PROP_POSOPTS_X);
    int posY = getPropIntVal(AppProperties.CSZ_PROP_POSOPTS_Y);
    return new Point(posX, posY);
  }

  public void setOptsPos(Point p_pos) {
    setPropVal(CSZ_PROP_POSOPTS_X, p_pos.x);
    setPropVal(CSZ_PROP_POSOPTS_Y, p_pos.y);
  }

  public Dimension getOptsDim() {
    int dimX = getPropIntVal(AppProperties.CSZ_PROP_DIMOPTS_X);
    int dimY = getPropIntVal(AppProperties.CSZ_PROP_DIMOPTS_Y);
    return new Dimension(dimX, dimY);
  }

  public void setOptsDim(Dimension p_pos) {
    setPropVal(CSZ_PROP_DIMOPTS_X, p_pos.width);
    setPropVal(CSZ_PROP_DIMOPTS_Y, p_pos.height);
  }

}
