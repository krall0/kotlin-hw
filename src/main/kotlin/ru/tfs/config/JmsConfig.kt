package ru.tfs.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import javax.jms.ConnectionFactory


@Configuration
@EnableJms
class JmsConfig {

    @Bean
    fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory) =
        DefaultJmsListenerContainerFactory().apply {
            setConnectionFactory(connectionFactory)
        }
}
