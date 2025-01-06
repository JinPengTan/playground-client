package my.paynet.jposclient.config;

import java.util.concurrent.Executors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

@Configuration
@ConditionalOnProperty(
    value = "spring.thread-executor",
    havingValue = "virtual"
)
public class ThreadConfiguration {

    @Bean
    public AsyncTaskExecutor applicationTaskExecutor() {

        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());

    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {

        return protocolHandler -> {

            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        };

    }

}
