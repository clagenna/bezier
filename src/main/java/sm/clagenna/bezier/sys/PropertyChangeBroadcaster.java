package sm.clagenna.bezier.sys;

import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sm.clagenna.bezier.enumerati.EPropChange;

public class PropertyChangeBroadcaster {

  private static final Logger              s_log = LogManager.getLogger(PropertyChangeBroadcaster.class);

  private static PropertyChangeBroadcaster s_inst;

  private List<PropertyChangeListener>     m_liBCstPropList;
  private boolean                          m_bBroadcastPropEventChange;

  public PropertyChangeBroadcaster() {
    if (s_inst != null && !Beans.isDesignTime())
      throw new UnsupportedOperationException("PropertyChangeBroadcaster gia istanziato");
    s_inst = this;
  }

  public static PropertyChangeBroadcaster getInst() {
    return s_inst;
  }

  public void addPropertyChangeListener(PropertyChangeListener p_listen) {
    if (m_liBCstPropList == null)
      m_liBCstPropList = new ArrayList<>();
    @SuppressWarnings("unused") String szName = p_listen.getClass().getSimpleName();

    if ( !m_liBCstPropList.contains(p_listen))
      m_liBCstPropList.add(p_listen);
  }

  public void removePropertyChangeListener(PropertyChangeListener p_listen) {
    if (m_liBCstPropList == null)
      return;
    if (m_liBCstPropList.contains(p_listen))
      m_liBCstPropList.remove(p_listen);
    if (m_liBCstPropList.size() == 0)
      m_liBCstPropList = null;
  }

  public void removePropertyChangeListener(Class<?> p_class) {
    if (m_liBCstPropList == null)
      return;
    // for ( PropertyChangeListener p : m_liBCstPropList)
    m_liBCstPropList.removeIf(p -> p.getClass() == p_class);
  }

  public void setBroadcastPropEventChange(boolean p_bv) {
    m_bBroadcastPropEventChange = p_bv;
  }

  public boolean isBroadcastPropEventChange() {
    return m_bBroadcastPropEventChange;
  }

  /**
   *
   * @param p_cls
   *          L'istanza della classe che sta emettendo il broadcast
   * @param p_prop
   *          quale tipo di broadcast si sta emettendo
   */
  public void broadCast(Object p_cls, EPropChange p_prop) {
    broadCast(p_cls, p_prop, null);
  }

  /**
   *
   * @param p_cls
   *          L'istanza della classe che sta emettendo il broadcast
   * @param p_prop
   *          quale tipo di broadcast si sta emettendo
   * @param newValue
   *          quale tipo di broadcast si sta emettendo
   */
  public void broadCast(Object p_cls, EPropChange p_prop, Object newValue) {
    PropertyChangeEvent p_evt = new PropertyChangeEvent(p_cls, null, p_prop, newValue);
    if (isBroadcastPropEventChange())
      return;

    if (m_liBCstPropList == null || m_liBCstPropList.size() == 0) {
      s_log.debug("Nessun listener per property:" + p_evt.getPropertyName());
      return;
    }
    setBroadcastPropEventChange(true);
    try {
      for (PropertyChangeListener p : m_liBCstPropList) {
        try {
          p.propertyChange(p_evt);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } finally {
      setBroadcastPropEventChange(false);
    }
  }

}
