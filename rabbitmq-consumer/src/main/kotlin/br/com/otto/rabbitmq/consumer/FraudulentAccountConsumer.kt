package br.com.otto.rabbitmq.consumer

import br.com.otto.rabbitmq.domain.Account
import br.com.otto.rabbitmq.utils.JsonUtils
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.stereotype.Component

@Component
class FraudulentAccountConsumer(
        private val jsonUtils: JsonUtils
) : MessageListener {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message?) {
        log.info("receive message from ${message?.messageProperties?.consumerQueue}")
        message?.let {
            val account: Account = jsonUtils.fromJson(message.body, Account::class.java)
            log.info("account $account")
            if (account.email == null) throw RuntimeException("Wrong data for account class")
        } ?: log.warn("No message found")
    }
}
