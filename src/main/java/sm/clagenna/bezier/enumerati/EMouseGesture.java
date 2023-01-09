package sm.clagenna.bezier.enumerati;

import java.util.HashMap;
import java.util.Map;

public enum EMouseGesture {
  SingClickSinistro(11), //
  DoppClickSinistro(12), //
  SingClickCentrale(21), //
  DoppClickCentrale(22), //
  SingClickDestro(31), //
  DoppClickDestro(32),;

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
