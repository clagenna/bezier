package sm.clagenna.bezier.sys;

import sm.clagenna.bezier.enumerati.EPropChange;

public interface IBroadcast {
  void broadc(EPropChange evt, Object newVal);
}
