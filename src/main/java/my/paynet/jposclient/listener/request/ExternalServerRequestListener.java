package my.paynet.jposclient.listener.request;

import java.io.IOException;
import my.paynet.jposclient.listener.response.ExternalServerResponseListener;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.jpos.iso.MUX;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.context.ApplicationContext;

public class ExternalServerRequestListener implements ISORequestListener, Configurable {
    private ApplicationContext applicationContext;

    @Override
    public void setConfiguration(Configuration configuration) throws ConfigurationException {
        try {
            applicationContext = NameRegistrar.get("applicationContext");
        } catch (NotFoundException e) {
            throw new RuntimeException("Application Context not found", e);
        }
    }

    @Override
    public boolean process(ISOSource isoSource, ISOMsg isoMsg) {
        try {
            MUX mux = applicationContext.getBean("externalQ2Mux", MUX.class);
            if (isoMsg.getMTI().charAt(2) == '0') {
//                isoMsg.setMTI("0110");
//                isoMsg.set(15, "0705");
//                isoMsg.set(38, "000098");
//                isoMsg.set(39, "00");
                isoSource.send(isoMsg);
            } else {
                throw new RuntimeException("Unable to process XX1X MTI Message");
            }
        } catch (ISOException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
