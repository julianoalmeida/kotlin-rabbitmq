package br.com.otto.rabbitmq.producer

import br.com.otto.rabbitmq.domain.Account
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import java.lang.Exception

@Component
class FraudulentAccountProducer(private val rabbitTemplate: RabbitTemplate) : AmqpProducer<Account> {

    override fun produce(message: Account) = try {
        rabbitTemplate.convertAndSend(
            QueueDefinition.FRAUDULENT_ACCOUNT_DIRECT_EXCHANGE,
            QueueDefinition.FRAUDULENT_ACCOUNT_BINDING_KEY,
                message
        )
    } catch (exception: Exception) {
        throw AmqpRejectAndDontRequeueException(exception)
    }
}