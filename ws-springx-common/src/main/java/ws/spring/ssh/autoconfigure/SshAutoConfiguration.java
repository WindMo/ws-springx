package ws.spring.ssh.autoconfigure;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.session.ClientSessionCreator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ws.spring.ssh.SshService;
import ws.spring.ssh.support.SshServiceBean;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
@EnableScheduling
@AutoConfiguration
@ConditionalOnClass(org.apache.sshd.client.SshClient.class)
@ConditionalOnProperty(prefix = "spring.ext.ssh", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(SshConfigProperties.class)
public class SshAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public static SshService sshService(SshConfigProperties properties, SshServiceFactory factory) throws Exception {
        return new SshServiceBean(factory.buildSshService(properties));
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(SshClient.class)
    @ConditionalOnMissingBean(SshServiceFactory.class)
    protected static class MinaSshServiceConfiguration {

        @Bean
        public static SshServiceFactory sshServiceFactory(ClientSessionCreator client) {
            return new MinaSshServiceFactory(client);
        }

        @Bean(initMethod = "start", destroyMethod = "close")
        @ConditionalOnMissingBean(ClientSessionCreator.class)
        public static SshClient defaultClient() {

            SshClient client = SshClient.setUpDefaultClient();
            client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
            return client;
        }
    }
}
