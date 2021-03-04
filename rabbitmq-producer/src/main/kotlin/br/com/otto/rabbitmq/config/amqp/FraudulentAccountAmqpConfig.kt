package br.com.otto.rabbitmq.config.amqp

import br.com.otto.rabbitmq.producer.QueueDefinition
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class FraudulentAccountAmqpConfig(private val connectionFactory: ConnectionFactory) {

    @PostConstruct
    fun createRabbitElements() {
        val rabbitAdmin = RabbitAdmin(connectionFactory)
        createExchange(rabbitAdmin)
        createQueue(rabbitAdmin)
        createDLQ(rabbitAdmin)
    }

    private fun createExchange(rabbitAdmin: RabbitAdmin) {
        val exchange = ExchangeBuilder
                .directExchange(QueueDefinition.FRAUDULENT_ACCOUNT_DIRECT_EXCHANGE)
                .durable(true)
                .build<Exchange>()
        rabbitAdmin.declareExchange(exchange)
    }

    private fun createQueue(rabbitAdmin: RabbitAdmin) {
        val queue = QueueBuilder.durable(QueueDefinition.FRAUDULENT_ACCOUNT_QUEUE)
                .deadLetterExchange(QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_EXCHANGE)
                .deadLetterRoutingKey(QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_BINDING_KEY)
                .build()
        val binding = Binding(
                QueueDefinition.FRAUDULENT_ACCOUNT_QUEUE,
                Binding.DestinationType.QUEUE,
                QueueDefinition.FRAUDULENT_ACCOUNT_DIRECT_EXCHANGE,
                QueueDefinition.FRAUDULENT_ACCOUNT_BINDING_KEY,
                null
        )
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareBinding(binding)
    }

    private fun createDLQ(rabbitAdmin: RabbitAdmin) {
        val queue = QueueBuilder.durable(QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_QUEUE)
                .build()
        val exchange = ExchangeBuilder
                .directExchange(QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_EXCHANGE)
                .durable(true)
                .build<Exchange>()
        val binding = Binding(
                QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_QUEUE,
                Binding.DestinationType.QUEUE,
                QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_EXCHANGE,
                QueueDefinition.FRAUDULENT_ACCOUNT_DLQ_BINDING_KEY,
                null
        )
        rabbitAdmin.declareQueue(queue)
        rabbitAdmin.declareExchange(exchange)
        rabbitAdmin.declareBinding(binding)
    }

}