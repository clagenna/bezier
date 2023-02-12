package sm.clagenna.bezier.sys;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Closeable;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;
import lombok.Setter;
import sm.clagenna.bezier.data.ModelloDati;

public class ViewDati implements PropertyChangeListener, Closeable {

  private static final Logger s_log = LogManager.getLogger(ViewDati.class);
  @Getter @Setter
  private ModelloDati         modello;

  public ViewDati(ModelloDati dt) {
    setModello(dt);
    initData();
  }

  private void initData() {
    PropertyChangeBroadcaster broa = PropertyChangeBroadcaster.getInst();
    broa.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent p_evt) {
    s_log.debug("change {} val={}", p_evt.getPropertyName(), p_evt.getNewValue());
  }

  @Override
  public void close() throws IOException {
    PropertyChangeBroadcaster broa = PropertyChangeBroadcaster.getInst();
    broa.removePropertyChangeListener(this);
  }
}
