package br.com.otto.rabbitmq.config.amqp

import br.com.otto.rabbitmq.consumer.FraudulentAccountConsumer
import br.com.otto.rabbitmq.consumer.QueueDefinition
import org.aopalliance.aop.Advice
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FraudulentAccountConfig(
    private val connectionFactory: ConnectionFactory,
    private val fraudulentAccountConsumer: FraudulentAccountConsumer,
    private val simpleRabbitListenerContainerFactory: SimpleRabbitListenerContainerFactory
) {

    @Bean
    fun listenerContainer(): MessageListenerContainer {
        val container = SimpleMessageListenerContainer()
        container.connectionFactory = connectionFactory
        container.setQueueNames(QueueDefinition.FRAUDULENT_ACCOUNT_QUEUE)
        container.setMessageListener(fraudulentAccountConsumer)
        simpleRabbitListenerContainerFactory.adviceChain?.let {
            container.setAdviceChain(*it, retryPolicy())
        }
        return container
    }

    private fun retryPolicy(): Advice {
        return RetryInterceptorBuilder
                .stateless()
                .maxAttempts(5)
                .backOffOptions(
                        1000, // Initial interval
                        2.0, // Multiplier
                        6000 // Max interval
                )
                .recoverer(RejectAndDontRequeueRecoverer())
                .build()
    }
}