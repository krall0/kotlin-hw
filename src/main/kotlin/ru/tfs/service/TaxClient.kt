package ru.tfs.service

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import ru.tfs.config.properties.TaxApiConfigurationProps
import ru.tfs.dto.UserRequest
import ru.tfs.dto.UserDetails

@Service
class TaxClient(
    private val webClient: WebClient,
    private val taxApiConfigurationProps: TaxApiConfigurationProps
) {

    suspend fun getUserDetails(request: UserRequest): UserDetails {
        return webClient.post()
            .uri(taxApiConfigurationProps.getUserDetailsURL)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .body(fromValue(request))
            .retrieve()
            .awaitBody()
    }
}
