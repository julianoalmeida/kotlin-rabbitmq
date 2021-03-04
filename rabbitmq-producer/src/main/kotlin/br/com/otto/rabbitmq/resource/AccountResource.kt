package br.com.otto.rabbitmq.resource

import br.com.otto.rabbitmq.domain.Account
import br.com.otto.rabbitmq.service.AccountService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("account")
class AccountResource(private val accountService: AccountService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("report-due-fraud")
    fun reportDueFraud(@RequestBody account: Account): HttpEntity<Any?> {
        log.info("sending fraudulent account $account")
        accountService.reportAsFraudulent(account)
        return ResponseEntity.ok().build()
    }
}