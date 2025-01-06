package my.paynet.jposclient.config.channel;

import java.io.IOException;
import org.jpos.iso.ISOException;
import org.jpos.iso.channel.BASE24TCPChannel;
import org.jpos.util.LogEvent;
import org.jpos.util.Logger;

public class Base24TCPISOChannel extends BASE24TCPChannel {

  @Override
  protected int getMessageLength() throws IOException, ISOException {
    int l = 0;
    byte[] b = new byte[2];
    Logger.log(new LogEvent(this, "get-message-length"));

    while(l == 0) {
      this.serverIn.readFully(b, 0, 2);
      l = (b[0] & 255) << 8 | b[1] & 255;
      if (l == 0) {
        this.serverOut.write(b);
        this.serverOut.flush();
      }
    }

    Logger.log(new LogEvent(this, "got-message-length", Integer.toString(l)));
    return l;
  }

  @Override
  protected void getMessageTrailler() throws IOException {
    // override to remove unnecessary right shift.
  }
}
