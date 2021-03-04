package br.com.otto.rabbitmq.service

import br.com.otto.rabbitmq.producer.FraudulentAccountProducer
import br.com.otto.rabbitmq.domain.Account
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccountService(private val producer: FraudulentAccountProducer) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun reportAsFraudulent(account: Account) = try {
        producer.produce(account)
    } catch (exception: Exception) {
        log.error("An error occurred during sending fraudulent account $account, exception=$exception")
    }
}