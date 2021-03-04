package br.com.otto.rabbitmq.domain

data class Account(
        val id: Int,
        var email: String? = null
)