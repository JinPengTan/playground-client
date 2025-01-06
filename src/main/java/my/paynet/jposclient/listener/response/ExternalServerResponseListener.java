package my.paynet.jposclient.listener.response;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOResponseListener;
import org.jpos.iso.MUX;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.context.ApplicationContext;

public class ExternalServerResponseListener implements ISOResponseListener {

    private static final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void responseReceived(ISOMsg isoMsg, Object o) {
        int current = counter.incrementAndGet();
        System.out.println("Response received. Total responses: " + current);

    }

    public static int getCurrentResponse() {
        return counter.get();
    }
}
