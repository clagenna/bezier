package prova.scroll3;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class MyPanel extends JPanel implements MouseListener {
  private static final long serialVersionUID = 6248852778512372166L;

  public MyPanel() {
    initComponents();
  }

  private void initComponents() {
    setBackground(Color.white);
    setOpaque(true);
    Dimension area = new Dimension(600, 400);
    setPreferredSize(area);

    addMouseListener(this);
  }

  @Override
  protected void paintComponent(Graphics p_g) {
    super.paintComponent(p_g);
    Graphics2D g2 = (Graphics2D) p_g.create();
    Dimension dim = getSize();
    g2.setColor(Color.gray);
    g2.drawLine(0, 0, dim.width, dim.height);
  }

  @Override
  public void mouseClicked(MouseEvent p_e) {

  }

  @Override
  public void mousePressed(MouseEvent p_e) {

  }

  @Override
  public void mouseReleased(MouseEvent p_e) {

  }

  @Override
  public void mouseEntered(MouseEvent p_e) {

  }

  @Override
  public void mouseExited(MouseEvent p_e) {

  }

}
