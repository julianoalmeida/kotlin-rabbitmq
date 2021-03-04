package br.com.otto.rabbitmq.producer

interface AmqpProducer<T> {
    fun produce(message: T)
}