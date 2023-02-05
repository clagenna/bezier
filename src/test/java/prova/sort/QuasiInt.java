package prova.sort;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class QuasiInt implements Comparable<QuasiInt> {
  @Getter @Setter
  Integer key;
  @Getter @Setter
  String  val;

  public QuasiInt(Integer kk, String vv) {
    setKey(kk);
    setVal(vv);
  }

  @Override
  public int compareTo(QuasiInt p_o) {
    if ( (p_o == null) || ! (p_o instanceof QuasiInt other))
      return -1;
    return key.compareTo(other.getKey());
  }

}
