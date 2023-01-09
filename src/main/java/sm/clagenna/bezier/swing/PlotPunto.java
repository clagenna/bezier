package sm.clagenna.bezier.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.data.ModelloDati;
import sm.clagenna.bezier.data.Punto;
import sm.clagenna.bezier.enumerati.EPropChange;
import sm.clagenna.bezier.sys.IDisegnabile;
import sm.clagenna.bezier.sys.PropertyChangeBroadcaster;

public class PlotPunto implements Comparable<PlotPunto>, IDisegnabile {

  private static Color      s_Vert          = new Color(0, 0, 0);
  private static Color      s_Vert_Selected = new Color(92, 128, 92);
  private static int        s_raggioDefault = 1;

  @Getter @Setter
  private int               raggio;

  /** il punto windows da convertire in x */
  private ModelloDati       m_dati;
  @Getter
  private Punto             puntoW;
  @Getter
  private Punto             puntoX;
  private Shape             m_cerchio;
  private transient Color   m_colore;
  @Getter @Setter
  private transient boolean selected;

  public PlotPunto() {
    m_colore = s_Vert;
    setRaggio(s_raggioDefault);
  }

  /**
   * Un punto disegnabile partendo da una locazione di Windows
   *
   * @param p_md
   *          il modello da cui il trasponder
   * @param p_w
   *          il punto in Windows
   */
  public PlotPunto(ModelloDati p_md, Punto p_w) {
    m_dati = p_md;
    puntoW = p_w;
    puntoX = m_dati.getTraspondiFinestra().convertiX(p_w);
    puntoX.setId(p_w.getId());
    setRaggio(s_raggioDefault);
    m_colore = s_Vert;
  }

  @Override
  public void paintComponent(Graphics2D p_g2) {
    // double zoo = p_trasp.getZoom();
    // int ragW = (int) (getRaggio() * 10.); // * zoo);
    Graphics2D g2 = (Graphics2D) p_g2.create();

    double px = puntoW.getX();
    double py = puntoW.getY();

    Color bkg = g2.getBackground();
    // il piu stretto
    var l_rag = (int) (getRaggio() * 10.); // * zoo);
    m_cerchio = new Ellipse2D.Double(px - l_rag, py - l_rag, l_rag * 2.0, l_rag * 2.0);

    g2.setColor(bkg);
    g2.fill(m_cerchio);
    if (isSelected()) {
      g2.setColor(s_Vert_Selected);
      g2.setStroke(new BasicStroke(3));
    } else
      g2.setColor(m_colore);
    g2.draw(m_cerchio);
    stampaNome(g2);
    g2.dispose();
  }

  private void stampaNome(Graphics2D g2) {
    double nFsize = 12.; // p_trasp.getZoom();
    String sz = puntoW.getId();
    Font font = new Font("SanSerif", Font.PLAIN, (int) (nFsize));
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, sz);
    Rectangle2D rec = gv.getVisualBounds();

    int px = (int) puntoW.getX();
    int py = (int) puntoW.getY();
    g2.setColor(s_Vert);
    // float px = (pu.x - (nFsize / +5F) * (sz.length() - 1));
    // float pfx = (float) (px - (nFsize * 0.6F));
    float pfx = (float) (px - (rec.getWidth() / 2));
    // float pfy = (float) (py + nFsize / 2.5F);
    float pfy = (float) (py + (rec.getHeight() / 2));
    // System.out.printf("PlotVertice.stampaNome(\"%s\", %.2f, %.2f) zo=%.2f\n", sz, px, py, nFsize);
    g2.drawGlyphVector(gv, pfx, pfy);
  }

  public String getId() {
    String ret = "?";
    if (puntoW != null)
      ret = puntoW.getId();
    return ret;
  }

  public void setPunto(Punto p_pu) {
    if (puntoW == null)
      return;
    puntoW.setPunto(p_pu);
    PropertyChangeBroadcaster.getInst().broadCast(this, EPropChange.modificaGeomtria);
  }

  @Override
  public Punto getPunto() {
    return puntoW;
  }

  public boolean checkBersaglio(Punto p_pu) {
    if (p_pu.equals(puntoW))
      return true;
    double dx = Math.abs(p_pu.getX() - puntoW.getX());
    if (dx > raggio)
      return false;
    double dy = Math.abs(p_pu.getY() - puntoW.getY());
    return dy <= raggio;
  }

  @Override
  public String toString() {
    String sz = String.format("%s,Pu={%s}", //
        puntoW.getId(), //
        puntoW.toString());
    sz += "\n\t" + puntoW.toString();
    return sz;
  }

  @Override
  public boolean equals(Object p_obj) {
    if ( (puntoW == null) || (p_obj == null) || ! (p_obj instanceof PlotPunto))
      return false;
    PlotPunto altro = (PlotPunto) p_obj;
    if (altro.puntoW == null)
      return false;
    return puntoW.getId().equals(altro.puntoW.getId());
  }

  @Override
  public int compareTo(PlotPunto p_o) {
    // TODO Auto-generated method stub
    return 0;
  }

}
