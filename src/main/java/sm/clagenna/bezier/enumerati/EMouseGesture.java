package sm.clagenna.bezier.enumerati;

import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public enum EMouseGesture {
  SingClickSinistro(MouseEvent.BUTTON1 * 10 + 1), //
  DoppClickSinistro(MouseEvent.BUTTON1 * 10 + 2), //
  SingClickCentrale(MouseEvent.BUTTON3 * 10 + 1), //
  DoppClickCentrale(MouseEvent.BUTTON3 * 10 + 2), //
  SingClickDestro(MouseEvent.BUTTON2 * 10 + 1), //
  DoppClickDestro(MouseEvent.BUTTON2 * 10 + 2),;

  private int                                m_click;
  private static Map<Integer, EMouseGesture> s_map;

  static {
    s_map = new HashMap<Integer, EMouseGesture>();
    for (EMouseGesture en : EMouseGesture.values())
      s_map.put(en.click(), en);
  }

  private EMouseGesture(int no) {
    m_click = no;
  }

  public int click() {
    return m_click;
  }

  public static EMouseGesture valueOf(int v) {
    EMouseGesture ret = null;
    if (s_map.containsKey(v))
      ret = s_map.get(v);
    return ret;
  }
}
