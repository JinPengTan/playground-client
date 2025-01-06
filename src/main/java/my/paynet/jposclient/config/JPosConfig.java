package my.paynet.jposclient.config;

import org.jpos.iso.ISOUtil;
import org.jpos.iso.MUX;
import org.jpos.q2.Q2;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JPosConfig {

    @Value("${q2.deploy.path:}")
    private String deployPath;

    @Bean
    public Q2 externalQ2Server(ApplicationContext applicationContext) {
        Q2 q2 = new Q2(deployPath);
        q2.start();

        NameRegistrar.register("applicationContext", applicationContext);
        return q2;
    }

    @Bean
    public MUX externalQ2Mux(Q2 q2) {
        try {
            while(!q2.ready()) {
                ISOUtil.sleep(10);
            }
            return QMUX.getMUX("internal-mux");
        } catch (NotFoundException e) {
            throw new RuntimeException("MUX jPOS_Client_Mux not found", e);
        }
    }


}
