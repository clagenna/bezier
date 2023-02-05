package prova.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ProvaSortGen {

  public abstract void doTheJob();

  protected static final int     CSZ_QTA_ELEM = 10_000;
  protected static final boolean S_PRINT      = false;
  protected List<QuasiInt>       m_list;

  public ProvaSortGen() {
    //
  }

  protected void initArray() {
    // Date now = new Date();
    // now.getTime()
    Random rnd = new Random(0);
    m_list = new ArrayList<>();
    int k = 0;
    for (int i = 0; i < CSZ_QTA_ELEM; i++)
      m_list.add(new QuasiInt(rnd.nextInt(0, CSZ_QTA_ELEM), String.valueOf(k++)));
    stampa("Unsorted", m_list);
  }

  protected void stampa(String p_msg, List<QuasiInt> liRet) {
    if (S_PRINT) {
      System.out.printf("\n%s\n-------------\n", p_msg);
      liRet.forEach(x -> System.out.println(x));
    }

  }
}
