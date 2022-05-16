package ru.tfs.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class TaxClient(
    private val restTemplate: RestTemplate,
    @Value("\${tax-service.address}") private val taxServiceAddress: String
) {

    fun getInn(docNumber: String): String? = try {
        restTemplate.getForObject("$taxServiceAddress$GET_INN_BY_DOCNUM", docNumber)
    } catch (e: HttpClientErrorException.NotFound) {
        null
    }
}

private const val GET_INN_BY_DOCNUM = "/inn?docnum={docnum}"
