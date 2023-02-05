package prova.sort;

import java.util.ArrayList;
import java.util.List;

import sm.clagenna.bezier.sys.TimeThis;

public class ProvaMergeSort extends ProvaSortGen {

  public static void main(String[] args) {
    var app = new ProvaMergeSort();
    app.initArray();
    app.doTheJob();
  }

  @Override
  public void doTheJob() {

    TimeThis tm = new TimeThis("MergeSort");
    List<QuasiInt> liRet = mergeSort(m_list);
    tm.stop(true);

    stampa("Sorted merge", liRet);
  }

  private List<QuasiInt> mergeSort(List<QuasiInt> p_li) {
    List<QuasiInt> liRet = p_li;
    if (p_li == null || p_li.size() <= 1)
      return liRet;
    int nSiz = p_li.size();
    int nMid = nSiz / 2;
    List<QuasiInt> sinis = p_li.subList(0, nMid);
    List<QuasiInt> destr = p_li.subList(nMid, nSiz);
    List<QuasiInt> liSin = mergeSort(sinis);
    List<QuasiInt> liDes = mergeSort(destr);
    liRet = merge(liSin, liDes);
    return liRet;
  }

  private List<QuasiInt> merge(List<QuasiInt> p_liSin, List<QuasiInt> p_liDes) {
    List<QuasiInt> liRet = new ArrayList<>();
    int j = 0, k = 0;
    while (j < p_liSin.size() && k < p_liDes.size()) {
      if (p_liSin.get(j).compareTo(p_liDes.get(k)) <= 0)
        liRet.add(p_liSin.get(j++));
      else
        liRet.add(p_liDes.get(k++));
    }
    while (j < p_liSin.size())
      liRet.add(p_liSin.get(j++));
    while (k < p_liDes.size())
      liRet.add(p_liDes.get(k++));
    return liRet;
  }
}
