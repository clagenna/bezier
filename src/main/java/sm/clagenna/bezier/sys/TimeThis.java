package sm.clagenna.bezier.sys;

public class TimeThis {
  private String m_tit;
  private long   m_init;
  private long   m_fine;

  public TimeThis() {
    m_tit = "--";
    start();
  }

  public TimeThis(String t) {
    m_tit = t;
    start();
  }

  public void start() {
    m_init = System.nanoTime();
  }

  public long stop(boolean p_b) {
    m_fine = System.nanoTime();
    if (p_b)
      System.out.println(toString());
    return elapsed();
  }

  private long elapsed() {
    if (m_fine == 0 || m_init >= m_fine)
      return 0;
    return ( (m_fine - m_init) / 1000000);
  }

  @Override
  public String toString() {
    String sz = String.format("%s:%.4f", m_tit, elapsed() / 1000F);
    return sz;
  }

}
