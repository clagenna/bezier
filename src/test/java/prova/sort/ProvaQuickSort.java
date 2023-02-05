package prova.sort;

import java.util.ArrayList;
import java.util.List;

import sm.clagenna.bezier.sys.TimeThis;

public class ProvaQuickSort extends ProvaSortGen {

  public static void main(String[] args) {
    var app = new ProvaQuickSort();
    app.initArray();
    app.doTheJob();
  }

  @Override
  public void doTheJob() {

    TimeThis tm = new TimeThis("QuickSort");
    List<QuasiInt> liRet = quickSort(m_list);
    tm.stop(true);
    stampa("Quick Sort", liRet);
  }

  private List<QuasiInt> quickSort(List<QuasiInt> p_arr) {
    List<QuasiInt> liRet = p_arr;
    if (p_arr == null || p_arr.size() <= 1)
      return liRet;
    int mid = p_arr.size() / 2;
    QuasiInt pivot = p_arr.get(mid);
    // System.out.printf("Pivot-->%s\tdim=%d\n", pivot.toString(), p_arr.size());
    List<QuasiInt> infe = new ArrayList<>();
    List<QuasiInt> supe = new ArrayList<>();
    int k = 0;
    for (QuasiInt el : p_arr) {
      // scarto il pivot
      if (k++ == mid)
        continue;
      int co = el.compareTo(pivot);
      if (co < 0)
        infe.add(el);
      else if (co > 0)
        supe.add(el);
      else if (k < mid)
        infe.add(el);
      else
        supe.add(el);
    }
    List<QuasiInt> sortInfe = quickSort(infe);
    List<QuasiInt> sortSupe = quickSort(supe);

    liRet = new ArrayList<>();
    liRet.addAll(sortInfe);
    liRet.add(pivot);
    liRet.addAll(sortSupe);

    return liRet;
  }
}
