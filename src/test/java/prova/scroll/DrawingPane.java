package prova.scroll;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** The component inside the scroll pane. */
public class DrawingPane extends JPanel implements MouseListener {
  private static final long                            serialVersionUID = 8793990458664477071L;

  @SuppressWarnings("unused") private final ScrollDemo m_scrollDemo2;

  final Color                                          colors[]         = {                    //
      Color.red,                                                                               //
      Color.blue,                                                                              //
      Color.green,                                                                             //
      Color.orange,                                                                            //
      Color.cyan,                                                                              //
      Color.magenta,                                                                           //
      Color.darkGray,                                                                          //
      Color.yellow };

  /** coordinates used to draw graphics */
  private List<Rectangle>                              circles;
  private Dimension                                    area;

  DrawingPane(ScrollDemo p_scrollDemo2) {
    m_scrollDemo2 = p_scrollDemo2;
    circles = new ArrayList<>();
    addMouseListener(this);
    setBackground(Color.white);
    area = new Dimension();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    int i = 0;
    for (Rectangle rect : circles) {
      g.setColor(colors[ (i++ % colors.length)]);
      g.fillOval(rect.x, rect.y, rect.width, rect.height);
    }
  }

  //Handle mouse events.
  @Override
  public void mouseReleased(MouseEvent e) {
    final int W = 100;
    final int H = 100;
    boolean changed = false;
    if (SwingUtilities.isRightMouseButton(e)) {
      //This will clear the graphic objects.
      circles.clear();
      area.width = 0;
      area.height = 0;
      changed = true;
    } else {
      int x = e.getX() - W / 2;
      int y = e.getY() - H / 2;
      if (x < 0)
        x = 0;
      if (y < 0)
        y = 0;
      Rectangle rect = new Rectangle(x, y, W, H);
      circles.add(rect);
      scrollRectToVisible(rect);

      int this_width = (x + W + 2);
      if (this_width > area.width) {
        area.width = this_width;
        changed = true;
      }

      int this_height = (y + H + 2);
      if (this_height > area.height) {
        area.height = this_height;
        changed = true;
      }
    }
    if (changed) {
      //Update client's preferred size because
      //the area taken up by the graphics has
      //gotten larger or smaller (if cleared).
      setPreferredSize(area);

      //Let the scroll pane know to update itself
      //and its scrollbars.
      revalidate();
    }
    repaint();
  }

  @Override
  public void mousePressed(MouseEvent p_e) {
    // 

  }

  @Override
  public void mouseEntered(MouseEvent p_e) {
    // 

  }

  @Override
  public void mouseExited(MouseEvent p_e) {
    // 

  }

  @Override
  public void mouseClicked(MouseEvent p_e) {
    // 

  }
}
